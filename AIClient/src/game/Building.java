package game;

import game.Unit.UnitType;

import io.Command;

import java.awt.Point;

import config.Config;

/**
 * represents a building in the game
 * 
 * @author farzad
 * 
 */
public abstract class Building extends Agent
{
	public Building(Point position, int health, int viewRadius2)
	{
		super(position, health, viewRadius2);
	}

	/**
	 * represents the HeadQuarters (main building)
	 * 
	 * @author farzad
	 * 
	 */
	public static final class HeadQuarters extends Building
	{
		public HeadQuarters(int x, int y, int health)
		{
			super(new Point(x, y), health,
					Config.Building.HeadQuarters.viewRadius2);
		}
		
		/**
		 * orders this headquarters building to spawn a unit in its current position.
		 * @param unitType
		 */
		public void spawnUnit(UnitType unitType)
		{
			this.setCommand(new Command.Spawn(new Point(this.getX(), this.getY()), unitType));
		}
		
//		/**
//		 * orders this headquarters building to spawn a unit in its current position.
//		 * @param unitType
//		 */
//		public void chargeUnit()
//		{
//			this.setCommand(new Command.Spawn(new Point(this.getX(), this.getY())));
//		}
	}
}
