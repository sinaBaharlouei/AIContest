package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import config.Config;
import game.*;
import game.Unit.*;
import io.Command.Direction;

public class MyBot extends Bot
{
	@Override
	public void think()
	{
		// place your code here.
		// here is a very basic sample of what you can do:
		Random rand = new Random();

		// spawn some random units, if you have enough supplies
		for(Building.HeadQuarters hq : getSelfAgents().getHQsList()) {
			if (new Random().nextBoolean()) {
				if(getSuppliesAmount() >= Config.Unit.Ranged.creationCost) {
					hq.spawnUnit(UnitType.RANGED);
				}
			} else {
				if(getSuppliesAmount() >= Config.Unit.Melee.creationCost) {
					hq.spawnUnit(UnitType.MELEE);
				}
			}
		}

		// list all friendly units
		ArrayList<Unit> units = getSelfAgents().getAllUnits();

		// list friendly ranged units:
		ArrayList<Unit> rangedUnits = getSelfAgents().getRangedList();
		
		// list friendly melee units:
		ArrayList<Unit> meleeUnits = getSelfAgents().getMeleeList();
		
		//list all supplies' positions
		ArrayList<Point> supplies = new ArrayList<Point>();
		for (int i = 0 ; i < getGameField().getWidth() ; i++) {
			for (int j = 0 ; j < getGameField().getHeight() ; j++) {
				if (getGameField().isSuppliesAt(i, j)) {
					supplies.add((new Point(i, j)));
				}
			}
		}
		
		// gather nearby supplies, or move randomly if there is none
		for (Unit unit : units) {
			if(getGameField().isSuppliesAt(unit.getX() + 1, unit.getY())) {
				unit.move(Direction.EAST);
			} else if(getGameField().isSuppliesAt(unit.getX() - 1, unit.getY())) {
				unit.move(Direction.WEST);
			} else if(getGameField().isSuppliesAt(unit.getX(), unit.getY() + 1)) {
				unit.move(Direction.SOUTH);
			} else if(getGameField().isSuppliesAt(unit.getX(), unit.getY() - 1)) {
				unit.move(Direction.NORTH);
			} else {
				// pick a random direction and make sure it is walkable(there is no
				// wall there) and no friendly unit is already there

				Direction dir;
				dir = Direction.getDirByNum(rand.nextInt());

				// check walkable
				if(getGameField().isWalkable(unit.getX() + dir.deltaX, unit.getY() + dir.deltaY)) {
					// check if there is no unit
					if(getSelfAgents().getUnitAt(unit.getX() + dir.deltaX, unit.getY() + dir.deltaY) == null) {
						unit.move(dir);
					}
				}
			}
		}

		// attack everything you can see!!
		// (note that each agent will only run the LAST command given to it,
		// meaning they will ignore the move command and run the attack command
		// if possible)
		ArrayList<Unit> enemyUnits = getEnemyAgents().getAllUnits();
		ArrayList<Building.HeadQuarters> enemyBuildings = getEnemyAgents().getHQsList();

		for(Unit unit : units) {
			for(Unit enemyUnit : enemyUnits) {
				if(unit.isInAttackRange(enemyUnit)) {
					unit.attack(enemyUnit);
				}
			}

			for(Building enemyBuilding : enemyBuildings) {
				if(unit.isInAttackRange(enemyBuilding)) {
					unit.attack(enemyBuilding);
				}
			}
		}
	}
}
