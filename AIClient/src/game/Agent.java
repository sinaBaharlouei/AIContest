package game;

import io.*;

import java.awt.Point;

/**
 * represents an agent in the game
 * @author farzad
 *
 */
public abstract class Agent
{
	private int		viewRadius2;
	private int		health;
	private Point	position;
	private int		teamNumber;
	private Command	command;
	private int id;
	private boolean dead;

	public Agent(Point position, int health, int viewRadius2)
	{
		this.id = -1;
		this.dead = false;
		this.position = position;
		this.health = health;
		this.viewRadius2 = viewRadius2;
		command = new Command.Wait();
	}
	
	/**
	 * 
	 * @return true if this agent is no more alive
	 */
	public boolean isDead()
	{
		return this.dead;
	}
	
	/**
	 * sets this agent's life status
	 * @param dead
	 */
	public void setDead(boolean dead)
	{
		this.dead = dead;
	}
	
	/**
	 * sets this agents id
	 * @param id
	 */
	public void setID(int id)
	{
		this.id = id;
	}
	
	/**
	 * 
	 * @return this agents id
	 */
	public int getID()
	{
		return this.id;
	}

	/**
	 * checks if this agent can see the specified point
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public final boolean isInViewRange(int x, int y)
	{
		int dx2 = (this.position.x - x) * (this.position.x - x);
		int dy2 = (this.position.y - y) * (this.position.y - y);

		int d2 = dx2 + dy2;

		return (d2 <= viewRadius2);
	}
	
	/**
	 * sets the position of this agent
	 * @param position
	 */
	public void setPosition(Point position)
	{
		this.position.x = position.x;
		this.position.y = position.y;
	}
	
	/**
	 * 
	 * @return the agents position.x
	 */
	public int getX()
	{
		return position.x;
	}
	
	/**
	 * 
	 * @return the agents position.y
	 */
	public int getY()
	{
		return position.y;
	}
	
	/**
	 * 
	 * @return this agent's team number
	 */
	public int getTeamNumber()
	{
		return teamNumber;
	}
	
	/**
	 * sets this agent's team number
	 * @param teamNumber
	 */
	public void setTeamNumber(int teamNumber)
	{
		this.teamNumber = teamNumber;
	}
	
	/**
	 * 
	 * @return the remaining health of this agent
	 */
	public int getHealth()
	{
		return health;
	}
	
	/**
	 * sets the health for this agent
	 * @param health
	 */
	public void setHealth(int health)
	{
		this.health = health;
	}
	
	/**
	 * 
	 * @return the string that will be passed to the server as this agents command
	 */
	public String getCommand()
	{
		return command.toString();
	}
	
	protected void setCommand(Command command)
	{
		this.command = command;
	}
}
