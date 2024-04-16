abstract class Quest implements Comparable<Quest> {

	protected double distance;
	protected int questId;
	protected Assignment assignment = null;
	protected int minimumPerformTime = Constants.INF;
	protected int minimumStartTime = Constants.INF;
	protected int createTime = Constants.INF;

	public abstract int getTime(double walkingSpeed, double drivingSkill, double accuracy, double experience,
			double intelligence, Car car);

	public int getTime(Assignment assignment) {
		double walkingSpeed = assignment.getFirstAgent().getWalkingSpeed();
		double drivingSkill = assignment.getFirstAgent().getDrivingSkill();
		double accuracy = assignment.getFirstAgent().getAccuracy();
		double experience = assignment.getFirstAgent().getExperience();
		double intelligence = assignment.getFirstAgent().getIntelligence();

		if (assignment.getSecondAgent() != null) {
			walkingSpeed = Math.min(walkingSpeed, assignment.getSecondAgent().getWalkingSpeed());
			drivingSkill = Math.max(drivingSkill, assignment.getSecondAgent().getDrivingSkill());
			accuracy = (accuracy + assignment.getSecondAgent().getAccuracy()) / 2.0;
			experience = 1 - (1 - assignment.getFirstAgent().getExperience()) * (1 - assignment.getSecondAgent().getExperience());
			intelligence = 1 - (1 - assignment.getFirstAgent().getIntelligence()) * (1 - assignment.getSecondAgent().getIntelligence());
		}

		if (assignment.getCar() != null) {
			if (assignment.getCar().getThreshold() > drivingSkill + 1e-6) return Constants.INF;
		}

		return getTime(walkingSpeed, drivingSkill, accuracy, experience, intelligence, assignment.getCar());
	}

	public int getHalfDistanceTime(double walkingSpeed, double drivingSkill, int leftDistanceForCar, boolean isToQuest) {
		int distancePassTime = 0;
		if (leftDistanceForCar == 0) {
			distancePassTime += MenInBlack.toInt(distance / walkingSpeed);
		} else {
			if (leftDistanceForCar > distance) {
				leftDistanceForCar -= distance;
				distancePassTime += MenInBlack.toInt(distance / drivingSkill);
			} else {
				distancePassTime += MenInBlack.toInt(leftDistanceForCar / drivingSkill);
				distancePassTime += MenInBlack.toInt((distance - leftDistanceForCar) / walkingSpeed);
				leftDistanceForCar = 0;
			}
		}
		return distancePassTime + (isToQuest ? getHalfDistanceTime(walkingSpeed, drivingSkill, leftDistanceForCar, false) : 0);
	}

	public int getDistancePassTime(double walkingSpeed, double drivingSkill, Car car) {
		int leftDistanceForCar = 0;
		if (car != null) leftDistanceForCar = car.getDistanceLeft();
		return getHalfDistanceTime(walkingSpeed, drivingSkill, leftDistanceForCar, true);
	}
	
	public int performQuest(Journal journal, Constants constants, int timeToSolveMainTask) {
		journal.addStartQuestEvent(minimumStartTime, assignment.getFirstAgent(), assignment.getSecondAgent(), assignment.getCar(), questId + "");
		
		double walkingSpeed = assignment.getFirstAgent().getWalkingSpeed();
		double drivingSkill = assignment.getFirstAgent().getDrivingSkill();
		if(assignment.getSecondAgent() != null) {
			walkingSpeed = Math.min(walkingSpeed, assignment.getSecondAgent().getWalkingSpeed());
			drivingSkill = Math.max(drivingSkill, assignment.getSecondAgent().getDrivingSkill());
		}
		
		int timeToPassFirstHalf = 0;
		int timeToPassSecondHalf = 0;
		double walkingDistance = 0;
		double drivingDistance = 0;
		
		if(assignment.getCar() == null) {
			walkingDistance = getDistance() + getDistance();
			timeToPassFirstHalf = getHalfDistanceTime(walkingSpeed, drivingSkill, 0, false);
			timeToPassSecondHalf = getHalfDistanceTime(walkingSpeed, drivingSkill, 0, false);
		} else {
			//Get to main quest
			if(getDistance() >= assignment.getCar().getDistanceLeft()) {
				drivingDistance += assignment.getCar().getDistanceLeft();
				walkingDistance += getDistance() - assignment.getCar().getDistanceLeft();
				int timeOnCar = MenInBlack.toInt(assignment.getCar().getDistanceLeft() / drivingSkill);
				journal.addCarBrokenEvent(minimumStartTime + timeOnCar, assignment.getCar().getCarId());
				timeToPassFirstHalf = timeOnCar + MenInBlack.toInt((getDistance() - assignment.getCar().getDistanceLeft()) / walkingSpeed);
				assignment.getCar().setDistanceLeft(0);
			} else {
				int timeOnCar = MenInBlack.toInt(getDistance() / drivingSkill);
				drivingDistance += getDistance();
				assignment.getCar().setDistanceLeft(MenInBlack.toInt(assignment.getCar().getDistanceLeft() - getDistance()));
				timeToPassFirstHalf = timeOnCar;
			}
			//Return back
			if(getDistance() >= assignment.getCar().getDistanceLeft()) {
				drivingDistance += assignment.getCar().getDistanceLeft();
				walkingDistance += getDistance() - assignment.getCar().getDistanceLeft();
				int timeOnCar = MenInBlack.toInt(assignment.getCar().getDistanceLeft() / drivingSkill);
				if(assignment.getCar().getDistanceLeft() > 0) journal.addCarBrokenEvent(minimumStartTime + timeToPassFirstHalf + timeToSolveMainTask + timeOnCar, assignment.getCar().getCarId());
				timeToPassSecondHalf = timeOnCar + MenInBlack.toInt((getDistance() - assignment.getCar().getDistanceLeft()) / walkingSpeed);
				assignment.getCar().setDistanceLeft(0);
			} else {
				int timeOnCar = MenInBlack.toInt(getDistance() / drivingSkill);
				drivingDistance += getDistance();
				assignment.getCar().setDistanceLeft(MenInBlack.toInt(assignment.getCar().getDistanceLeft() - getDistance()));
				timeToPassSecondHalf = timeOnCar;
			}
		}
		
		journal.addFinishedQuestEvent(minimumStartTime + timeToPassFirstHalf + timeToPassSecondHalf + timeToSolveMainTask, assignment.getFirstAgent(), assignment.getSecondAgent(), questId + "");
		
		assignment.getFirstAgent().setAvailableTime(minimumStartTime + timeToPassFirstHalf + timeToPassSecondHalf + timeToSolveMainTask);
		if(assignment.getSecondAgent() != null) assignment.getSecondAgent().setAvailableTime(minimumStartTime + timeToPassFirstHalf + timeToPassSecondHalf + timeToSolveMainTask);
		if(assignment.getCar() != null) assignment.getCar().setAvailableTime(minimumStartTime + timeToPassFirstHalf + timeToPassSecondHalf + timeToSolveMainTask);
		
		assignment.getFirstAgent().updateWalkingSpeed(walkingDistance, constants.getMaximalWalkingDistance());
		assignment.getFirstAgent().updateDrivingSkill(drivingDistance, constants.getMaximalWalkingDistance());
		
		if(assignment.getSecondAgent() != null) {
			assignment.getSecondAgent().updateWalkingSpeed(walkingDistance, constants.getMaximalWalkingDistance());
			assignment.getSecondAgent().updateDrivingSkill(drivingDistance, constants.getMaximalWalkingDistance());
		}
		
		return timeToPassFirstHalf;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public int getMinimumPerformTime() {
		return minimumPerformTime;
	}

	public void setMinimumPerformTime(int minimumPerformTime) {
		this.minimumPerformTime = minimumPerformTime;
	}

	public int getMinimumStartTime() {
		return minimumStartTime;
	}

	public void setMinimumStartTime(int minimumStartTime) {
		this.minimumStartTime = minimumStartTime;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	@Override
	public int compareTo(Quest q) {
		if(minimumStartTime == q.minimumStartTime) return questId - q.questId;
		return minimumStartTime - q.minimumStartTime;
	}
	
}

class DeliveryQuest extends Quest {

	@Override
	public int getTime(double walkingSpeed, double drivingSkill, double accuracy, double experience,
			double intelligence, Car car) {
		return getDistancePassTime(walkingSpeed, drivingSkill, car);
	}

	@Override
	public int performQuest(Journal journal, Constants constants, int timeToSolveMainTask) {
		return super.performQuest(journal, constants, timeToSolveMainTask);
	}
	
}

class InvestigationQuest extends Quest {

	protected double minimalIntelligence;
	protected double minimalInvestigationTime;

	public double getMinimalInvestigationTime() {
		return minimalInvestigationTime;
	}

	public void setMinimalInvestigationTime(double minimalInvestigationTime) {
		this.minimalInvestigationTime = minimalInvestigationTime;
	}

	public double getMinimalIntelligence() {
		return minimalIntelligence;
	}

	public void setMinimalIntelligence(double minimalIntelligence) {
		this.minimalIntelligence = minimalIntelligence;
	}

	@Override
	public int getTime(double walkingSpeed, double drivingSkill, double accuracy, double experience,
			double intelligence, Car car) {
		if (minimalIntelligence > intelligence + 1e-6) return Constants.INF;
		double investigationTime = minimalInvestigationTime / intelligence;
		return getDistancePassTime(walkingSpeed, drivingSkill, car) + MenInBlack.toInt(investigationTime);
	}

	@Override
	public int performQuest(Journal journal, Constants constants, int timeToSolveMainTask) {
		double intelligence = assignment.getFirstAgent().getIntelligence();
		if(assignment.getSecondAgent() != null) {
			intelligence = 1 - (1 - assignment.getFirstAgent().getIntelligence()) * (1 - assignment.getSecondAgent().getIntelligence());
		}
		int timeToPassMainQuest = MenInBlack.toInt(minimalInvestigationTime / intelligence);
		super.performQuest(journal, constants, timeToPassMainQuest);

		assignment.getFirstAgent().updateExperience(assignment.getFirstAgent().getIntelligence(), minimalInvestigationTime);
		assignment.getFirstAgent().updateIntelligence(assignment.getFirstAgent().getIntelligence(), minimalInvestigationTime);
		
		if(assignment.getSecondAgent() != null) {
			assignment.getSecondAgent().updateExperience(assignment.getSecondAgent().getIntelligence(), minimalInvestigationTime);
			assignment.getSecondAgent().updateIntelligence(assignment.getSecondAgent().getIntelligence(), minimalInvestigationTime);
		}

		return timeToPassMainQuest;
	}

}

class NegotiationQuest extends Quest {

	protected double minimalExperience;
	protected double minimalNegotiationTime;

	public double getMinimalNegotiationTime() {
		return minimalNegotiationTime;
	}

	public void setMinimalNegotiationTime(double minimalNegotiationTime) {
		this.minimalNegotiationTime = minimalNegotiationTime;
	}

	public double getMinimalExperience() {
		return minimalExperience;
	}

	public void setMinimalExperience(double minimalExperience) {
		this.minimalExperience = minimalExperience;
	}

	@Override
	public int getTime(double walkingSpeed, double drivingSkill, double accuracy, double experience,
			double intelligence, Car car) {
		if (minimalExperience > experience + 1e-6) return Constants.INF;
		double negotiationTime = minimalNegotiationTime / experience;
		return getDistancePassTime(walkingSpeed, drivingSkill, car) + MenInBlack.toInt(negotiationTime);
	}

	@Override
	public int performQuest(Journal journal, Constants constants, int timeToSolveMainTask) {
		double experience = assignment.getFirstAgent().getExperience();
		if(assignment.getSecondAgent() != null) {
			experience = 1 - (1 - assignment.getFirstAgent().getExperience()) * (1 - assignment.getSecondAgent().getExperience());
		}
		int timeToPassMainQuest = MenInBlack.toInt(minimalNegotiationTime / experience);
		super.performQuest(journal, constants, timeToPassMainQuest);
		
		assignment.getFirstAgent().updateExperience(assignment.getFirstAgent().getExperience(), minimalNegotiationTime);
		if(assignment.getSecondAgent() != null) {
			assignment.getSecondAgent().updateExperience(assignment.getSecondAgent().getExperience(), minimalNegotiationTime);
		}
		
		return timeToPassMainQuest;
	}

}

class KillQuest extends Quest {

	protected Monster monsterType;

	public Monster getMonsterType() {
		return monsterType;
	}

	public void setMonsterType(Monster monsterType) {
		this.monsterType = monsterType;
	}

	@Override
	public int getTime(double walkingSpeed, double drivingSkill, double accuracy, double experience,
			double intelligence, Car car) {
		if(monsterType.getExperienceThreshold() > experience + 1e-6 || monsterType.getIntelligenceThreshold() > intelligence + 1e-6) return Constants.INF;
		double killTime = monsterType.getEvasiveness() / accuracy;
		return getDistancePassTime(walkingSpeed, drivingSkill, car) + MenInBlack.toInt(killTime);
	}

	@Override
	public int performQuest(Journal journal, Constants constants, int timeToSolveMainTask) {
		double accuracy = assignment.getFirstAgent().getAccuracy();
		if (assignment.getSecondAgent() != null) {
			accuracy = (assignment.getFirstAgent().getAccuracy() + assignment.getSecondAgent().getAccuracy()) / 2.0;
		}

		int timeToPassMainQuest = MenInBlack.toInt(getMonsterType().getEvasiveness() / accuracy);
		int timeToGetToTarget = super.performQuest(journal, constants, timeToPassMainQuest);
		
		journal.addKillMonsterEvent(minimumStartTime + timeToGetToTarget + timeToPassMainQuest, assignment.getFirstAgent(), assignment.getSecondAgent(), getMonsterType().getName());

		assignment.getFirstAgent().updateAccuracy(getMonsterType().getEvasiveness(), constants.getMaximalMonstersEvasiveness());
		assignment.getFirstAgent().updateExperience(getMonsterType().getExperience(), constants.getMaximalExperienceForMonsterKilling());
		if (assignment.getSecondAgent() != null) {
			assignment.getSecondAgent().updateAccuracy(getMonsterType().getEvasiveness(), constants.getMaximalMonstersEvasiveness());
			assignment.getSecondAgent().updateExperience(getMonsterType().getExperience(), constants.getMaximalExperienceForMonsterKilling());
		}
		
		return timeToPassMainQuest;
	}

}

class QuestReader {
	
	public static Quest readQuest(java.util.Scanner scanner, int questId, java.util.HashMap<String, Monster> monsterTypes, int timestamp) {
		String questType = scanner.next();
		if (questType.equals("run")) {
			DeliveryQuest dq = new DeliveryQuest();
			dq.setDistance(scanner.nextInt());
			dq.setQuestId(questId);
			dq.setCreateTime(timestamp);
			return dq;
		}
		if (questType.equals("kill")) {
			KillQuest kq = new KillQuest();
			kq.setDistance(scanner.nextInt());
			kq.setMonsterType(monsterTypes.get(scanner.next()));
			kq.setQuestId(questId);
			kq.setCreateTime(timestamp);
			return kq;
		}
		if (questType.equals("findout")) {
			InvestigationQuest iq = new InvestigationQuest();
			iq.setDistance(scanner.nextInt());
			iq.setMinimalIntelligence(Double.parseDouble(scanner.next()));
			iq.setMinimalInvestigationTime(scanner.nextInt());
			iq.setQuestId(questId);
			iq.setCreateTime(timestamp);
			return iq;
		}
		if (questType.equals("talk")) {
			NegotiationQuest nq = new NegotiationQuest();
			nq.setDistance(scanner.nextInt());
			nq.setMinimalExperience(Double.parseDouble(scanner.next()));
			nq.setMinimalNegotiationTime(scanner.nextInt());
			nq.setQuestId(questId);
			nq.setCreateTime(timestamp);
			return nq;
		}
		return null;
	}
	
}

class Agent implements Comparable<Agent> {

	protected String name;
	
	protected char codeLetter;

	protected double accuracy;
	protected double intelligence;
	protected double walkingSpeed;
	protected double experience;
	protected double drivingSkill;
	protected int availableTime;

	public Agent(String name, char codeLetter, double accuracy, double intelligence, double walkingSpeed, double experience,
			double drivingSkill, int availableTime) {
		this.name = name;
		this.codeLetter = codeLetter;
		this.accuracy = accuracy;
		this.intelligence = intelligence;
		this.walkingSpeed = walkingSpeed;
		this.experience = experience;
		this.drivingSkill = drivingSkill;
		this.availableTime = availableTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAvailableTime() {
		return availableTime;
	}

	public void setAvailableTime(int availableTime) {
		this.availableTime = availableTime;
	}

	public char getCodeLetter() {
		return codeLetter;
	}

	public void setCodeLetter(char codeLetter) {
		this.codeLetter = codeLetter;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public double getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(double intelligence) {
		this.intelligence = intelligence;
	}

	public double getWalkingSpeed() {
		return walkingSpeed;
	}

	public void setWalkingSpeed(double walkingSpeed) {
		this.walkingSpeed = walkingSpeed;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}

	public double getDrivingSkill() {
		return drivingSkill;
	}

	public void setDrivingSkill(double drivingSkill) {
		this.drivingSkill = drivingSkill;
	}
	
	public void updateExperience(double nom, double denom) {
		experience = experience + nom / denom - experience * nom / denom;
	}

	public void updateIntelligence(double nom, double denom) {
		intelligence = intelligence + nom / denom - intelligence * nom / denom;
	}

	public void updateAccuracy(double nom, double denom) {
		accuracy = accuracy + nom / denom - accuracy * nom / denom;
	}

	public void updateWalkingSpeed(double nom, double denom) {
		walkingSpeed = walkingSpeed + nom / denom - walkingSpeed * nom / denom;
	}

	public void updateDrivingSkill(double nom, double denom) {
		drivingSkill = drivingSkill + nom / denom - drivingSkill * nom / denom;
	}

	@Override
	public int compareTo(Agent o) {
		return codeLetter - o.codeLetter;
	}

}

class Assignment {

	protected Agent firstAgent;
	protected Agent secondAgent;
	protected Car car;

	public Assignment(Agent firstAgent, Agent secondAgent, Car car) {
		this.firstAgent = firstAgent;
		this.secondAgent = secondAgent;
		this.car = car;
	}

	public Agent getFirstAgent() {
		return firstAgent;
	}

	public void setFirstAgent(Agent firstAgent) {
		this.firstAgent = firstAgent;
	}

	public Agent getSecondAgent() {
		return secondAgent;
	}

	public void setSecondAgent(Agent secondAgent) {
		this.secondAgent = secondAgent;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
	
	public int getAvailableTime(int questTime) {
		int agentOneAvailableTime = firstAgent.getAvailableTime();
		int agentTwoAvailableTime = secondAgent != null ? secondAgent.getAvailableTime() : 0;
		int carAvailableTime = car != null ? car.getAvailableTime() : 0;
		return Math.max(Math.max(carAvailableTime, questTime), Math.max(agentOneAvailableTime, agentTwoAvailableTime));
	}
	
}

class Car implements Comparable<Car> {

	protected String carId;
	protected double threshold;
	protected int distanceLeft;
	protected int availableTime;

	public Car(String carId, double threshold, int distanceLeft, int availableTime) {
		this.carId = carId;
		this.threshold = threshold;
		this.distanceLeft = distanceLeft;
		this.availableTime = availableTime;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public int getDistanceLeft() {
		return distanceLeft;
	}

	public void setDistanceLeft(int distanceLeft) {
		this.distanceLeft = distanceLeft;
	}

	public int getAvailableTime() {
		return availableTime;
	}

	public void setAvailableTime(int availableTime) {
		this.availableTime = availableTime;
	}

	@Override
	public int compareTo(Car o) {
		return carId.compareTo(o.getCarId());
	}

}

class Monster {

	protected String name;
	protected double experienceThreshold;
	protected double intelligenceThreshold;
	protected double evasiveness;
	protected double experience;

	public Monster(String name, double experienceThreshold, double intelligenceThreshold, double evasiveness, double experience) {
		this.name = name;
		this.experienceThreshold = experienceThreshold;
		this.intelligenceThreshold = intelligenceThreshold;
		this.evasiveness = evasiveness;
		this.experience = experience;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getEvasiveness() {
		return evasiveness;
	}

	public double getExperienceThreshold() {
		return experienceThreshold;
	}

	public void setExperienceThreshold(double experienceThreshold) {
		this.experienceThreshold = experienceThreshold;
	}

	public double getIntelligenceThreshold() {
		return intelligenceThreshold;
	}

	public void setIntelligenceThreshold(double intelligenceThreshold) {
		this.intelligenceThreshold = intelligenceThreshold;
	}

}

class CarType {

	protected double threshold;
	protected int leftDistance;
	
	public CarType(int leftDistance, double threshold) {
		this.leftDistance = leftDistance;
		this.threshold = threshold;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	public int getLeftDistance() {
		return leftDistance;
	}
	
	public void setLeftDistance(int leftDistance) {
		this.leftDistance = leftDistance;
	}
	
}

class Constants {

	public static final int INF = 1000000000;

	private double maximalWalkingDistance;
	private double maximalMonstersEvasiveness;
	private double maximalExperienceForMonsterKilling;
	private double retirementExperience;

	public double getMaximalWalkingDistance() {
		return maximalWalkingDistance;
	}

	public void setMaximalWalkingDistance(double maximalWalkingDistance) {
		this.maximalWalkingDistance = maximalWalkingDistance;
	}

	public double getMaximalMonstersEvasiveness() {
		return maximalMonstersEvasiveness;
	}

	public void setMaximalMonstersEvasiveness(double maximalMonstersEvasiveness) {
		this.maximalMonstersEvasiveness = maximalMonstersEvasiveness;
	}

	public double getMaximalExperienceForMonsterKilling() {
		return maximalExperienceForMonsterKilling;
	}

	public void setMaximalExperienceForMonsterKilling(double maximalExperienceForMonsterKilling) {
		this.maximalExperienceForMonsterKilling = maximalExperienceForMonsterKilling;
	}

	public double getRetirementExperience() {
		return retirementExperience;
	}

	public void setRetirementExperience(double retirementExperience) {
		this.retirementExperience = retirementExperience;
	}

}

class Log implements Comparable<Log> {

	private int timestamp;
	private int type;
	private String message;

	public Log(int timestamp, String message, int type) {
		this.timestamp = timestamp;
		this.message = message;
		this.type = type;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void append(String suffix) {
		this.message = this.message + suffix;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public int compareTo(Log o) {
		if (timestamp != o.timestamp) {
			return timestamp - o.timestamp;
		}
		if( type != o.type) {
			return type - o.type;
		}
		return message.compareTo(o.getMessage());
	}

	@Override
	public String toString() {
		//int day = timestamp / 60 / 24;
		//int hour = (timestamp - day * 24 * 60) / 60;
		//int minute = timestamp - day * 24 * 60 - hour * 60;
		int minute = timestamp % 60;
		int hour = (timestamp / 60) % 24;
		int day = timestamp / 60 / 24;
		return String.format("%04d:%02d:%02d", day, hour, minute) + "    " + message;
	}

}

class Journal {

	Log[] logs = new Log[10024];
	int logsCount = 0;

	public void addLog(Log message) {
		logs[logsCount++] = message;
	}

	public void print() {
		java.util.Arrays.sort(logs, 0, logsCount);
		for (int i = 0; i < logsCount; i++) {
			System.out.println(logs[i].toString());
		}
	}

	public void addBuyCarEvent(int timestamp, String carType) {
		logs[logsCount++] = new Log(timestamp, "MIB bought a car of class " + carType + ".", 1);
	}

	public void addCarBrokenEvent(int timestamp, String carId) {
		logs[logsCount++] = new Log(timestamp, "Car " + carId + " was broken.", 2);
	}

	public void addKillMonsterEvent(int timestamp, Agent a, Agent b, String monsterId) {
		if (b == null)
			logs[logsCount++] = new Log(timestamp, "Agent " + a.getCodeLetter() + " killed monster " + monsterId + ".", 3);
		else
			logs[logsCount++] = new Log(timestamp, "Agent " + a.getCodeLetter() + " and agent " + b.getCodeLetter()
					+ " killed monster " + monsterId + ".", 3);
	}

	public void addFinishedQuestEvent(int timestamp, Agent a, Agent b, String questId) {
		if (b == null)
			logs[logsCount++] = new Log(timestamp, "Agent " + a.getCodeLetter() + " finished quest " + questId + ".", 4);
		else
			logs[logsCount++] = new Log(timestamp, "Agent " + a.getCodeLetter() + " and agent " + b.getCodeLetter()
					+ " finished quest " + questId + ".", 4);
	}

	public void addAgentRetiredEvent(int timestamp, Agent a) {
		logs[logsCount++] = new Log(timestamp, "Agent " + a.getCodeLetter() + " has tired.", 5);
	}

	public void addAgentEvent(int timestamp, String agentName, String agentId) {
		logs[logsCount++] = new Log(timestamp, "New agent " + agentName + " got a letter " + agentId + ".", 6);
	}

	public void addStartQuestEvent(int timestamp, Agent a, Agent b, Car c, String questId) {
		if (b == null)
			logs[logsCount] = new Log(timestamp, "Agent " + a.getCodeLetter() + " started quest " + questId, 7);
		else
			logs[logsCount] = new Log(timestamp,
					"Agent " + a.getCodeLetter() + " and agent " + b.getCodeLetter() + " started quest " + questId, 7);

		if (c == null) {
			logs[logsCount].append(".");
		} else {
			logs[logsCount].append(" using car " + c.getCarId() + ".");
		}
		logsCount++;
	}

}

public class MenInBlack {

	private Agent[] agents = new Agent[2048];
	private int agentsCount = 0;
	private Car[] cars = new Car[2048];
	private int carsCount = 0;
	private Constants constants = new Constants();
	private Journal journal = new Journal();
	private Quest[] quests = new Quest[2048];
	private int currentQuest = 0, questsCount = 0;

	private java.util.HashMap<String, Monster> monsterTypes = new java.util.HashMap<String, Monster>();
	private java.util.HashMap<String, CarType> carTypes = new java.util.HashMap<String, CarType>();

	private int[] availableLetter = new int[256];
	
	public static int toInt(double a) {
		return (int)(a + 0.5 + 1e-6);
	}
	
	private char getLetter(String s, int timestamp) {
		char bestCharacter = 'A';
		int bestValue = Constants.INF;
		for (char i = 'A'; i <= 'Z'; i++) {
			if (bestValue > Math.abs(s.charAt(0) - i) && availableLetter[i] <= timestamp) {
				bestValue = Math.abs(s.charAt(0) - i);
				bestCharacter = i;
			}
		}
		return bestCharacter;
	}

	private void addQuest(Quest quest) {
		quests[questsCount++] = quest;
	}

	private Agent addAgent(String name, double accuracy, double walkingSpeed, double intelligence, double experience,
			double drivingSkill, int currentTime) {
		char agentLetter = getLetter(name, currentTime);

		Agent agent = new Agent(name, agentLetter, accuracy, intelligence, walkingSpeed, experience, drivingSkill,
				currentTime);
		availableLetter[agentLetter] = Constants.INF;
		agents[agentsCount++] = agent;
		journal.addAgentEvent(currentTime, name, agentLetter + "");
		return agent;
	}

	private Agent addAgent(char letter, double accuracy, double walkingSpeed, double intelligence, double experience,
			double drivingSkill, int currentTime, String name) {
		Agent agent = new Agent(name, letter, accuracy, intelligence, walkingSpeed, experience, drivingSkill, currentTime);
		availableLetter[letter] = Constants.INF;
		agents[agentsCount++] = agent;
		return agent;
	}

	private Car addCar(String carId, double threshold, int distanceLeft, String carType, int timestamp) {
		if(distanceLeft < 0) distanceLeft = 0;
		Car car = new Car(carId, threshold, distanceLeft, timestamp);
		cars[carsCount++] = car;
		if(carType != null) journal.addBuyCarEvent(timestamp, carType);
		if(distanceLeft == 0) {
			cars[carsCount - 1].setAvailableTime(Constants.INF);
		}
		return car;
	}

	private Monster addMonster(String name, double minimalExperienceNeeded, double minimalIntelligence,
			double evasiveness, double experience) {
		Monster monster = new Monster(name, minimalExperienceNeeded, minimalIntelligence, evasiveness, experience);
		monsterTypes.put(name, monster);
		return monster;
	}

	//Simulation
	public void findAcceptableAssignment(Quest q, int timestamp) {
		int bestTime = Constants.INF;
		int bestStartTime = Constants.INF;
		Assignment best = new Assignment(null, null, null);
		Assignment local = new Assignment(null, null, null);

		for (int i = 0; i < agentsCount; i++) {
			local.setFirstAgent(agents[i]);
			local.setSecondAgent(null);
			local.setCar(null);
			
			int time = q.getTime(local);
			int startTime = local.getAvailableTime(q.getCreateTime());

			if(startTime == Constants.INF || time == Constants.INF) continue;
			if (bestStartTime > startTime || (bestStartTime == startTime && bestTime > time)) {
				bestStartTime = startTime;
				bestTime = time;
				best.setCar(null);
				best.setFirstAgent(agents[i]);
				best.setSecondAgent(null);
			}
			for (int k = 0; k < carsCount; k++) {
				local.setCar(cars[k]);
				startTime = local.getAvailableTime(q.getCreateTime());
				time = q.getTime(local);

				if(startTime == Constants.INF || time == Constants.INF) continue;
				if (bestStartTime > startTime || (bestStartTime == startTime && bestTime > time)) {
					bestStartTime = startTime;
					bestTime = time;
					best.setCar(cars[k]);
					best.setFirstAgent(agents[i]);
					best.setSecondAgent(null);
				}
			}
		}

		for (int i = 0; i < agentsCount; i++) {
			for (int j = i + 1; j < agentsCount; j++) {
				local.setFirstAgent(agents[i]);
				local.setSecondAgent(agents[j]);
				local.setCar(null);
				int time = q.getTime(local);
				int startTime = local.getAvailableTime(q.getCreateTime());
				
				if(startTime == Constants.INF || time == Constants.INF) continue;
				if (bestStartTime > startTime || (bestStartTime == startTime && bestTime > time)) {
					bestStartTime = startTime;
					bestTime = time;
					best.setCar(null);
					best.setFirstAgent(agents[i]);
					best.setSecondAgent(agents[j]);
				}
				for (int k = 0; k < carsCount; k++) {
					local.setCar(cars[k]);
					time = q.getTime(local);
					startTime = local.getAvailableTime(q.getCreateTime());

					if(startTime == Constants.INF || time == Constants.INF) continue;
					if (bestStartTime > startTime || (bestStartTime == startTime && bestTime > time)) {
						bestStartTime = startTime;
						bestTime = time;
						best.setCar(cars[k]);
						best.setFirstAgent(agents[i]);
						best.setSecondAgent(agents[j]);
					}
				}
			}
		}
		q.setAssignment(best);
		q.setMinimumPerformTime(bestTime);
		q.setMinimumStartTime(bestStartTime);
	}
	
	private void updateAllQuests(int timestamp) {
		java.util.Arrays.sort(cars, 0, carsCount);
		java.util.Arrays.sort(agents, 0, agentsCount);
		java.util.Arrays.sort(quests, currentQuest, questsCount);
		for(int i = currentQuest; i < questsCount; i++) {
			findAcceptableAssignment(quests[i], timestamp);
		}
		java.util.Arrays.sort(quests, currentQuest, questsCount);
	}
	
	private void performAllQuestsBefore(int timestamp) {
		while(true) {
			if(currentQuest >= questsCount) break;
			updateAllQuests(timestamp);
			if(quests[currentQuest].minimumStartTime <= timestamp) {
				performQuest(quests[currentQuest]);
				currentQuest++;
			} else {
				break;
			}
		}
	}

	private void removeRetired(Assignment assignment) {
		checkAgentRetirement(assignment.getFirstAgent());
		checkAgentRetirement(assignment.getSecondAgent());
		checkCarUnavailable(assignment.getCar());
		
		for(int i = 0; i < agentsCount; i++) {
			if(agents[i].getAvailableTime() >= Constants.INF) {
				Agent h = agents[i];
				agents[i] = agents[agentsCount - 1];
				agents[agentsCount - 1] = h;
				agentsCount--;
			}
		}
		
		for(int i = 0; i < carsCount; i++) {
			if(cars[i].getAvailableTime() >= Constants.INF) {
				Car h = cars[i];
				cars[i] = cars[carsCount - 1];
				cars[carsCount - 1] = h;
				carsCount--;
			}
		}
		
		java.util.Arrays.sort(cars, 0, carsCount);
		java.util.Arrays.sort(agents, 0, agentsCount);
	}	

	private void performQuest(Quest quest) {
		if(quest instanceof DeliveryQuest) ((DeliveryQuest)quest).performQuest(journal, constants, 0);
		if(quest instanceof KillQuest) ((KillQuest)quest).performQuest(journal, constants, 0);
		if(quest instanceof InvestigationQuest) ((InvestigationQuest)quest).performQuest(journal, constants, 0);
		if(quest instanceof NegotiationQuest) ((NegotiationQuest)quest).performQuest(journal, constants, 0);
		removeRetired(quest.getAssignment());
	}

	private void checkAgentRetirement(Agent a) {
		if(a == null) return;
		if(a.getExperience() >= constants.getRetirementExperience()) {
			journal.addAgentRetiredEvent(a.getAvailableTime(), a);
			availableLetter[a.getCodeLetter()] = a.getAvailableTime();
			a.setAvailableTime(Constants.INF);
		}
	}
	
	private void checkCarUnavailable(Car c) {
		if(c == null) return;
		if(c.getDistanceLeft() <= 0) {
			c.setAvailableTime(Constants.INF);
		}
	}
	
	public void solve() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);

		int initialAgentsCount = scanner.nextInt();
		for (int i = 0; i < initialAgentsCount; i++) {
			String name = scanner.next();
			double accuracy = Double.parseDouble(scanner.next());
			double walkingSpeed = Double.parseDouble(scanner.next());
			double intelligence = Double.parseDouble(scanner.next());
			double experience = Double.parseDouble(scanner.next());
			double drivingSkill = Double.parseDouble(scanner.next());
			char agentLetter = scanner.next().charAt(0);
			addAgent(agentLetter, accuracy, walkingSpeed, intelligence, experience, drivingSkill, 0, name);
		}

		int initialCarTypesCount = scanner.nextInt();
		for (int i = 0; i < initialCarTypesCount; i++) {
			double threshold = Double.parseDouble(scanner.next());
			int leftDistance = scanner.nextInt();
			String carType = scanner.next();
			carTypes.put(carType, new CarType(leftDistance, threshold));
		}

		int initialCarsCount = scanner.nextInt();
		for (int i = 0; i < initialCarsCount; i++) {
			String type = scanner.next();
			int distance = scanner.nextInt();
			String carId = scanner.next();
			addCar(carId, carTypes.get(type).getThreshold(), carTypes.get(type).getLeftDistance() - distance, null, 0);
		}

		int monstersKindCount = scanner.nextInt();
		for (int i = 0; i < monstersKindCount; i++) {
			double minimalExperienceNeeded = Double.parseDouble(scanner.next());
			double minimalIntelligence = Double.parseDouble(scanner.next());
			double evasiveness = Double.parseDouble(scanner.next());
			double experience = Double.parseDouble(scanner.next());
			String name = scanner.next();
			addMonster(name, minimalExperienceNeeded, minimalIntelligence, evasiveness, experience);
		}

		constants.setMaximalWalkingDistance(Double.parseDouble(scanner.next()));
		constants.setMaximalMonstersEvasiveness(Double.parseDouble(scanner.next()));
		constants.setMaximalExperienceForMonsterKilling(Double.parseDouble(scanner.next()));
		constants.setRetirementExperience(Double.parseDouble(scanner.next()));

		int eventsCount = scanner.nextInt();
		for (int i = 0; i < eventsCount; i++) {
			int timestamp = scanner.nextInt();
			String type = scanner.next();
			if (type.equals("newagent")) {
				performAllQuestsBefore(timestamp - 1);
				addAgent(
					scanner.next(), 
					Double.parseDouble(scanner.next()), 
					Double.parseDouble(scanner.next()), 
					Double.parseDouble(scanner.next()),
					Double.parseDouble(scanner.next()), 
					Double.parseDouble(scanner.next()), 
					timestamp)
				;
			}
			if (type.equals("newcar")) {
				performAllQuestsBefore(timestamp - 1);
				String carType = scanner.next();
				int leftDistance = carTypes.get(carType).getLeftDistance() - scanner.nextInt();
				addCar(
					scanner.next(), 
					carTypes.get(carType).getThreshold(), 
					leftDistance,
					carType, 
					timestamp
				);
			}
			if (type.equals("quest")) {
				addQuest(QuestReader.readQuest(scanner, questsCount + 1, monsterTypes, timestamp));
			}
			performAllQuestsBefore(timestamp);
		}

		performAllQuestsBefore(Constants.INF - 1);

		scanner.close();
		
		journal.print();
	}
	
	public static void main(String[] args) {
		MenInBlack mib = new MenInBlack();
		mib.solve();
	}

}