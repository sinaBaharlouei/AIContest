package game;

import java.util.ArrayList;

/**
 * stores agent data.
 * 
 * @author farzad
 * 
 */
public class Agents
{
	private ArrayList<Building.HeadQuarters>	headQuarters;
	private ArrayList<Unit>		meleeUnits;
	private ArrayList<Unit>		rangedUnits;

	public Agents()
	{
		headQuarters = new ArrayList<Building.HeadQuarters>();
		meleeUnits = new ArrayList<Unit>();
		rangedUnits = new ArrayList<Unit>();
	}

	/**
	 * 
	 * @return a list of all headquarters
	 */
	public ArrayList<Building.HeadQuarters> getHQsList()
	{
		return headQuarters;
	}

	/**
	 * 
	 * @return a list of all melee units
	 */
	public ArrayList<Unit> getMeleeList()
	{
		return meleeUnits;
	}
	
	/**
	 * 
	 * @return a list of all units
	 */
	public ArrayList<Unit> getAllUnits()
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		
		for(Unit unit : meleeUnits) {
			units.add(unit);
		}
		
		for(Unit unit : rangedUnits) {
			units.add(unit);
		}
		
		return units;
	}
	
	/**
	 * 
	 * @param id
	 * @return the unit with the given id, or null if that unit does not exist
	 */
	public Unit getUnitByID(int id)
	{
		for (Unit unit : meleeUnits) {
			if (unit.getID() == id) {
				return unit;
			}
		}
		
		for (Unit unit : rangedUnits) {
			if (unit.getID() == id) {
				return unit;
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param id
	 * @return the HQ with the given id, or null if that unit does not exist
	 */
	public Building.HeadQuarters getHQByID(int id)
	{
		for (Building.HeadQuarters hq : headQuarters) {
			if (hq.getID() == id) {
				return hq;
			}
		}
		
		return null;
	}

	/**
	 * 
	 * @return a list of all ranged units
	 */
	public ArrayList<Unit> getRangedList()
	{
		return rangedUnits;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return the unit in the given position. null if there is no unit there
	 */
	public Unit getUnitAt(int x, int y)
	{
		for (Unit unit : meleeUnits) {
			if (unit.getX() == x && unit.getY() == y) {
				return unit;
			}
		}

		for (Unit unit : rangedUnits) {
			if (unit.getX() == x && unit.getY() == y) {
				return unit;
			}
		}

		return null;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return the building in the given position. null if there is no unit
	 *         there
	 */
	public Building getBuildingAt(int x, int y)
	{
		for (Building building : headQuarters) {
			if (building.getX() == x && building.getY() == y) {
				return building;
			}
		}

		return null;
	}

	/**
	 * adds a ranged unit to the agents list
	 * 
	 * @param unit
	 */
	public void addRangedUnit(Agent unit)
	{
		rangedUnits.add((Unit.Ranged) unit);
	}

	/**
	 * adds a melee unit to the agents list
	 * 
	 * @param unit
	 */
	public void addMeleeUnit(Agent unit)
	{
		meleeUnits.add((Unit.Melee) unit);
	}

	/**
	 * adds a headquarters building to the agents list
	 * 
	 * @param building
	 */
	public void addHQ(Agent building)
	{
		headQuarters.add((Building.HeadQuarters) building);
	}

	/**
	 * removes every agent from the list
	 */
	public void clearAll()
	{
		headQuarters.clear();
		meleeUnits.clear();
		rangedUnits.clear();
	}
	
	/**
	 * copies everything from the given agents into this one
	 * @param agents
	 */
	public void copyAgents(Agents agents)
	{
		for (Unit unit : agents.getMeleeList()) {
			this.meleeUnits.add(unit);
		}
		
		for (Unit unit : agents.getRangedList()) {
			this.rangedUnits.add(unit);
		}
		
		for (Building.HeadQuarters building : agents.getHQsList()) {
			this.headQuarters.add(building);
		}
	}
	
	public void removeDeadAgents()
	{
		for (int i = headQuarters.size() - 1 ; i >= 0 ; i--) {
			if (headQuarters.get(i).isDead()) {
				headQuarters.remove(i);
			}
		}
		
		for (int i = meleeUnits.size() - 1 ; i >= 0 ; i--) {
			if (meleeUnits.get(i).isDead()) {
				meleeUnits.remove(i);
			}
		}
		
		for (int i = rangedUnits.size() - 1 ; i >= 0 ; i--) {
			if (rangedUnits.get(i).isDead()) {
				rangedUnits.remove(i);
			}
		}
	}
}
