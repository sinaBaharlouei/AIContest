package config;

/**
 * stores the game config data
 * 
 * @author farzad
 * 
 */
public abstract class Config
{
	public static abstract class Communicator
	{
		public static abstract class Tags
		{
			public static final String packetStartTag = "start";
			public static final String packetEndTag = "end";
			
			
			public static final String configStartTag = "config";
			public static final String tilesStartTag = "tile";
			public static final String dataStartTag = "data";
			public static final String agentsStartTag = "agent";
			
			public static final String configEndTag = "/config";
			public static final String tilesEndTag = "/tile";
			public static final String dataEndTag = "/data";
			public static final String agentsEndTag = "/agent";
		}
	}
	
	public static abstract class Game
	{
		public static int		cycleTimeLimit	= -1;
		public static int		cycleCountLimit = -1;

		public static int		mapWidth		= -1;
		public static int		mapHeight		= -1;

		public static String	serverAddress	= "127.0.0.1";
		public static int		serverPort		= 10000;
	}
	
	public static abstract class Bot
	{
		public static int		teamNumber = -1;
	}

	public static abstract class Unit
	{
		public static abstract class Melee
		{
			public static final int	viewRadius2		= 18;
			public static final int	maxAttackRange2	= 2;
			public static final int	maxDamage		= 900;
			public static final int	creationCost	= 10;
		}

		public static abstract class Ranged
		{
			public static final int	viewRadius2		= 2;
			public static final int	maxAttackRange2	= 12;
			public static final int	maxDamage		= 300;
			public static final int	creationCost	= 15;
		}
	}

	public static abstract class Building
	{
		public static class HeadQuarters
		{
			public static final int	viewRadius2	= 30;
		}
	}

}
