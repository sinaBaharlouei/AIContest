package ai;

import config.Config;
import game.*;

/**
 * the command center for a team's bot's AI. this class is declared abstract and
 * should not be modified, edit the inherited class "ai.MyBot" instead.
 * 
 * @author farzad
 * 
 */
public abstract class Bot
{
	private int		teamNumber;

	private Agents	selfAgents;
	private Agents	enemyAgents;

	private Field	gameField;

	private int		suppliesStock;
	
	private int cycleNumber;
	private int lastProcessedCycle;

	public Bot()
	{
		this.teamNumber = Config.Bot.teamNumber;

		this.selfAgents = new Agents();
		this.enemyAgents = new Agents();

		this.gameField = new Field(Config.Game.mapWidth, Config.Game.mapHeight);

		this.suppliesStock = 10;
		this.cycleNumber = 0;
		this.lastProcessedCycle = -1;
		
		System.out.println(String.format("Map created: (%d x %d)", Config.Game.mapWidth, Config.Game.mapHeight));
		System.out.println(String.format("This is bot #%d", teamNumber));
	}
	
	/**
	 * 
	 * @return current cycle's number
	 */
	public int getCycleNumber()
	{
		return this.cycleNumber;
	}
	
	
	/**
	 * sets the current cycle's number
	 * @param cycleNumber
	 */
	public void setCycleNumber(int cycleNumber)
	{
		this.cycleNumber = cycleNumber;
	}

	/**
	 * sets the amount of supplies currently available. WARNING: never use this
	 * or you might corrupt your information
	 * 
	 * @param suppliesAmount
	 */
	public void setSuppliesAmount(int suppliesAmount)
	{
		this.suppliesStock = suppliesAmount;
	}

	/**
	 * 
	 * @return the amount of supplies currently available
	 */
	public int getSuppliesAmount()
	{
		return this.suppliesStock;
	}

	/**
	 * 
	 * @return the game field this bot is currently playing on
	 */
	public Field getGameField()
	{
		return gameField;
	}

	/**
	 * 
	 * @return the agents owned by this Bot
	 */
	public Agents getSelfAgents()
	{
		return this.selfAgents;
	}

	/**
	 * 
	 * @return the agents not owned by this bot
	 */
	public Agents getEnemyAgents()
	{
		return this.enemyAgents;
	}

	/**
	 * 
	 * @return this bot's team number
	 */
	public int getTeamNumber()
	{
		return this.teamNumber;
	}

	/**
	 * this method runs in every game cycle. you should not edit this method, as
	 * it implements and uses the protocols used by the server
	 */
	public String tick()
	{
		if(cycleNumber <= lastProcessedCycle) {
			return "n/a";
		}
		
		lastProcessedCycle = cycleNumber;
		
		System.out.println(String.format("Bot #%d - Cycle #%d... Processing the cycle", teamNumber, cycleNumber));
		System.out.println(String.format("Available Supplies: %5d", getSuppliesAmount()));
		System.out.println("-----Units count-----");
		System.out.println("\tFriendly Units\t\tHostile Units (visible)");
		System.out.println(String.format("HQ:\t%3d\t\t\t%3d\n", getSelfAgents().getHQsList().size(), getEnemyAgents().getHQsList().size()));
		System.out.println(String.format("Melee:\t%3d\t\t\t%3d", getSelfAgents().getMeleeList().size(), getEnemyAgents().getMeleeList().size()));
		System.out.println(String.format("Ranged:\t%3d\t\t\t%3d", getSelfAgents().getRangedList().size(), getEnemyAgents().getRangedList().size()));
		System.out.println("---------------------");
		
		String agentCommands = "";
		
		think();

		for (Agent agent : selfAgents.getHQsList()) {
			agentCommands += agent.getCommand() + "\n";
		}

		for (Agent agent : selfAgents.getMeleeList()) {
			agentCommands += agent.getCommand() + "\n";
		}

		for (Agent agent : selfAgents.getRangedList()) {
			agentCommands += agent.getCommand() + "\n";
		}
		
		System.out.println(String.format("Done processing cycle #%d.", cycleNumber));
		System.out.println("-------------------------------------------------------------------\n\n\n\n");
		return agentCommands.trim();
	}

	/**
	 * this method will run in each cycle. place your code here.
	 */
	public abstract void think();
}
