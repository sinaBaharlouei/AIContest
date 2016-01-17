package io;

import game.Unit.UnitType;

import java.awt.Point;

/**
 * represents a command for an agent to run
 * 
 * @author farzad
 * 
 */
public abstract class Command
{

	private CommandType	comType;
	private int[]		args;

	/**
	 * the command constructor
	 * @param comType determines what this command will do. use CommandType.[type]
	 */
	public Command(CommandType comType)
	{
		this.comType = comType;
	}
	
	/**
	 * sets the command arguments 
	 * @param args command arguments. syntax:
	 * 		move/attack:	{fromX, fromY, toX, toY}
	 * 		spawn:			{ownerX, ownerY, unitType}

	 */
	protected void setArgs(int[] args)
	{
		this.args = args;
	}
	
	public String toString()
	{
		String tempString = comType.toString();
		
		for(int i = 0 ; i < comType.getArgsCount() ; i++)
		{
			tempString += " ";
			tempString += args[i];
		}
		
		return tempString;
	}
	
	
	/**
	 * represents a geographical direction
	 * @author farzad
	 *
	 */
	public static enum Direction
	{
		EAST(1, 0), NORTH(0, -1), WEST(-1, 0), SOUTH(0, 1);
		
		public final int deltaX, deltaY;
		
		private Direction(int deltaX, int deltaY)
		{
			this.deltaX = deltaX;
			this.deltaY = deltaY;
		}
		
		/**
		 * 
		 * @param dirNum can be considered an angle
		 * @return the direction according to the given number.
		 */
		public static Direction getDirByNum(int dirNum)
		{
			dirNum %= 4;
			dirNum += 4;
			dirNum %= 4;
			
			return Direction.values()[dirNum];
		}
	}
	
	
	/**
	 * determines what a command will do.
	 * 
	 * @author farzad
	 * 
	 */
	private enum CommandType
	{
		MOVE("mov", 4), ATTACK("atk", 4), SPAWN("spw", 3), WAIT("n/a", 0);

		private final String	commandString;
		private final int		argsCount;

		private CommandType(String commandString, int argCount)
		{
			this.commandString = commandString;
			this.argsCount = argCount;
		}

		public String toString()
		{
			return this.commandString;
		}
		
		/**
		 * 
		 * @return the number of arguments needed for this kind of command
		 */
		public int getArgsCount()
		{
			return argsCount;
		}
	}
	
	/**
	 * represents a move command
	 * @author farzad
	 *
	 */
	public static class Move extends Command
	{
		public Move(Point from, Point to)
		{
			super(CommandType.MOVE);
			int [] args = {from.x, from.y, to.x, to.y};
			super.setArgs(args);
		}
	}

	/**
	 * represents an attack command
	 * @author farzad
	 *
	 */
	public static class Attack extends Command
	{
		public Attack(Point from, Point to)
		{
			super(CommandType.ATTACK);
			int [] args = {from.x, from.y, to.x, to.y};
			super.setArgs(args);
		}
	}

	/**
	 * represents a spawn command
	 * @author farzad
	 *
	 */
	public static class Spawn extends Command
	{
		public Spawn(Point position, UnitType unitType)
		{
			super(CommandType.SPAWN);
			int [] args = {position.x, position.y, unitType.ordinal()};
			super.setArgs(args);
		}
	}
	
//	/**
//	 * represents a spawn command
//	 * @author farzad
//	 *
//	 */
//	public static class Charge extends Command
//	{
//		public Charge(Point position, UnitType unitType)
//		{
//			super(CommandType.SPAWN);
//			int [] args = {position.x, position.y};
//			super.setArgs(args);
//		}
//	}
	
	/**
	 * represents a wait command (do nothing)
	 * @author farzad
	 *
	 */
	public static class Wait extends Command
	{
		public Wait()
		{
			super(CommandType.WAIT);
		}
	}
}
