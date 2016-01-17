package game;

import io.*;
import io.Command.Direction;

import java.awt.Point;

import config.Config;


public abstract class Unit extends Agent
{
	private int			maxAttackRange2;
	private int			maxDamage;
	
	public Unit(Point position, int health, int viewRadius2, int maxAttackRange2, int maxDamage)
	{
		super(position, health, viewRadius2);
		this.maxAttackRange2 = maxAttackRange2;
		this.maxDamage = maxDamage;
	}
	
	/**
	 * 
	 * @return the maximum damage for each of this unit's attacks
	 */
	public int getMaxDamage()
	{
		return this.maxDamage;
	}

	/**
	 * checks if this agent can attack the specified point
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isInAttackRange(int x, int y)
	{
		int dx2 = (this.getX() - x) * (this.getX() - x);
		int dy2 = (this.getY() - y) * (this.getY() - y);

		int d2 = dx2 + dy2;

		return (d2 <= maxAttackRange2);
	}
	
	/**
	 * checks if this agent can attack the specified enemy
	 * @param enemy
	 * @return
	 */
	public boolean isInAttackRange(Agent enemy)
	{
		return isInAttackRange(enemy.getX(), enemy.getY());
	}
	
	/**
	 * moves this agent in the specified direction by one cell
	 * @param direction use "Direction.[dir]"
	 */
	public void move(Direction direction)
	{
		Point from = new Point(getX(), getY());
		Point to = new Point(from);
		
		switch (direction)
		{
			case EAST:
				to.x++;
				break;
			case NORTH:
				to.y--;
				break;
			case SOUTH:
				to.y++;
				break;
			case WEST:
				to.x--;
				break;
		}
		
		setCommand(new Command.Move(from, to));
	}
	
	/**
	 * orders this unit to attack the specified point
	 * @param to
	 */
	public void attack(Point to)
	{
		Point from = new Point(getX(), getY());
		
		setCommand(new Command.Attack(from, to));
	}
	
	/**
	 * orders this unit to attack the specified point
	 * @param x
	 * @param y
	 */
	public void attack(int x, int y)
	{
		attack(new Point(x, y));
	}
	
	/**
	 * orders this unit to attack the specified enemy agent
	 * @param targetEnemy
	 */
	public void attack(Agent targetEnemy)
	{
		attack(targetEnemy.getX(), targetEnemy.getY());
	}
	
	/**
	 * represents a melee unit
	 * @author farzad
	 *
	 */
	public static final class Melee extends Unit
	{
		public Melee(int x, int y, int health)
		{
			super(new Point(x, y), health, Config.Unit.Melee.viewRadius2, Config.Unit.Melee.maxAttackRange2, Config.Unit.Melee.maxDamage);
		}
	}
	
	/**
	 * represents a ranged unit
	 * @author farzad
	 *
	 */
	public static final class Ranged extends Unit
	{
		public Ranged(int x, int y, int health)
		{
			super(new Point(x, y), health, Config.Unit.Ranged.viewRadius2, Config.Unit.Ranged.maxAttackRange2, Config.Unit.Ranged.maxDamage);
		}
	}
	
	/**
	 * represents a unit's type. do not change the order of the values, as it matters while spawning new units.
	 * @author farzad
	 *
	 */
	public enum UnitType
	{
		MELEE, RANGED;
	}
}