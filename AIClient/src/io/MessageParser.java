package io;

import java.awt.Point;

import config.Config;
import ai.Bot;
import game.Agent;
import game.Agents;
import game.Building;
import game.Field;
import game.Field.TileType;
import game.Unit;

/**
 * receives and parses messages, and changes the game field and configurations.
 * 
 * @author farzad
 * 
 */
public class MessageParser
{
	private Field	gameField;

	private Agents	selfAgents;
	private Agents	enemyAgents;

	private int		teamNumber;

	private Bot		ownerBot;

	public MessageParser(Bot bot)
	{
		this.ownerBot = bot;

		this.gameField = bot.getGameField();

		this.selfAgents = bot.getSelfAgents();
		this.enemyAgents = bot.getEnemyAgents();

		this.teamNumber = bot.getTeamNumber();
	}

	/**
	 * 
	 * @param startTag
	 * @param endTag
	 * @return the part of the string that is between startTag and endTag. if
	 *         the specified tags don't exist in the given string, an empty
	 *         string is returned
	 */
	private static String readTaggedData(String data, String startTag, String endTag)
	{
		String tempData = data;

		while (!tempData.startsWith(startTag) && tempData.length() > 0)
		{
			tempData = tempData.substring(1); // removes 1 char from the
			// beginning
		}

		while (!tempData.endsWith(endTag) && tempData.length() > 0)
		{
			tempData = tempData.substring(0, tempData.length() - 1); // removes
			// 1
			// char
			// from
			// the
			// end
		}

		// remove the tags
		if (tempData.startsWith(startTag))
			tempData = tempData.substring(startTag.length());
		if (tempData.endsWith(endTag))
			tempData = tempData.substring(0,
					tempData.length() - endTag.length());

		return tempData.trim();
	}

	/**
	 * parses the given string and changes the game state accordingly
	 * 
	 * @param message
	 */
	public void parseData(String message)
	{
		updateAgents(readTaggedData(message,
				Config.Communicator.Tags.agentsStartTag,
				Config.Communicator.Tags.agentsEndTag));

		updateMap(readTaggedData(message,
				Config.Communicator.Tags.tilesStartTag,
				Config.Communicator.Tags.tilesEndTag));

		updateCycleData(readTaggedData(message,
				Config.Communicator.Tags.dataStartTag,
				Config.Communicator.Tags.dataEndTag));
	}

	/**
	 * parses the given string and changes the game state accordingly
	 * @param message
	 */
	public static void parseConfig(String message)	
	{
		updateConfig(readTaggedData(message,
				Config.Communicator.Tags.configStartTag,
				Config.Communicator.Tags.configEndTag));
	}

	/**
	 * updates some data (available supplies/current cycle number)
	 * @param message
	 */
	private void updateCycleData(String message)
	{
		String[] lines = message.split("\n");

		int n;

		for(String line : lines) {
			if(line.length() == 0) {
				break;
			}

			String[] args = line.split(" ");

			try {
				n = Integer.parseInt(args[1]);
			} catch(ArrayIndexOutOfBoundsException e) {
				break;
			}

			switch(line.charAt(0)) {
				case 'c': // current cycle number
					ownerBot.setCycleNumber(n);
					break;

				case 's': // amount of supplies available
					ownerBot.setSuppliesAmount(n);
					break;
			}
		}
	}

	/**
	 * updates the map according to the received message
	 * 
	 * @param message
	 */
	private void updateMap(String message)
	{
		String[] lines = message.split("\n");

		gameField.clearAllSupplies();

		for(String line : lines) {
			if(line.length() == 0) {
				break;
			}

			String[] args = line.split(" ");

			int x, y;

			try {
				x = Integer.parseInt(args[1]);
				y = Integer.parseInt(args[2]);
			} catch(ArrayIndexOutOfBoundsException e) {
				break;
			}

			switch(line.charAt(0))
			{
				case 'l': //lava
					gameField.setTileType(x, y, TileType.LAVA);
					break;
				case 's': //supplies
					gameField.putSuppliesAt(x, y);
					break;
			}
		}
	}

	/**
	 * updates the agents list according to the received message
	 * 
	 * @param message
	 */
	private void updateAgents(String message)
	{
		for(Unit unit : selfAgents.getAllUnits()) {
			unit.setDead(true);
		}
		for(Unit unit : enemyAgents.getAllUnits()) {
			unit.setDead(true);
		}

		for(Building building : selfAgents.getHQsList()) {
			building.setDead(true);
		}
		for(Building building : enemyAgents.getHQsList()) {
			building.setDead(true);
		}
		
		
		String[] lines = message.split("\n");

		for(String line : lines) {
			if (line.length() == 0) {
				break;
			}

			String[] args = line.split(" ");
			int x, y, owner, health, id;

			try {
				x = Integer.parseInt(args[1]);
				y = Integer.parseInt(args[2]);

				owner = Integer.parseInt(args[3]);

				health = Integer.parseInt(args[4]);

				id = Integer.parseInt(args[5]);
			} catch(ArrayIndexOutOfBoundsException e) {
				break;
			}

			Agent tempAgent;
			Agents currentOwner;

			if (owner == this.teamNumber) {
				currentOwner = selfAgents;
			} else {	
				currentOwner = enemyAgents;
			}

			tempAgent = currentOwner.getUnitByID(id);
			if (tempAgent == null) {
				tempAgent = currentOwner.getHQByID(id);
			}

			if(tempAgent != null) {
				tempAgent.setPosition(new Point(x, y));
				tempAgent.setHealth(health);
				tempAgent.setDead(false);
			} else {
				switch(line.charAt(0))
				{				
					case 'b': // headquarters
						tempAgent = new Building.HeadQuarters(x, y, health);
						tempAgent.setTeamNumber(owner);
						tempAgent.setID(id);

						currentOwner.addHQ(tempAgent);
						break;
					case 'r': // ranged unit
						tempAgent = new Unit.Ranged(x, y, health);
						tempAgent.setTeamNumber(owner);
						tempAgent.setID(id);

						currentOwner.addRangedUnit(tempAgent);
						break;
					case 'm': // melee unit
						tempAgent = new Unit.Melee(x, y, health);
						tempAgent.setTeamNumber(owner);
						tempAgent.setID(id);

						currentOwner.addMeleeUnit(tempAgent);
						break;
				}
			}
		}
		
		selfAgents.removeDeadAgents();
		enemyAgents.removeDeadAgents();
	}

	/**
	 * updates the game config according to the given message
	 * @param message
	 */
	private static void updateConfig(String message)
	{
		String[] lines = message.split("\n");

		int n;
		for(String line : lines) {
			if (line.length() == 0) {
				break;
			}

			String[] args = line.split(" ");

			try {
				n = Integer.parseInt(args[1]);
			} catch(ArrayIndexOutOfBoundsException e) {
				break;
			}

			switch (line.charAt(0))
			{
				case 'w': // map width
					Config.Game.mapWidth = n;
					break;
				case 'h': // map height
					Config.Game.mapHeight = n;
					break;
				case 'o': // bot owner's team number
					Config.Bot.teamNumber = n;
					break;
				case 't': // time limit per cycle
					Config.Game.cycleTimeLimit = n;
					break;
				case 'c': // cycle count limit
					Config.Game.cycleCountLimit = n;
					break;
			}
		}
	}
}
