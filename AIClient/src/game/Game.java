package game;

import io.MessageParser;

import java.io.IOException;
import java.net.UnknownHostException;

import config.Config;

import ai.Bot;
import ai.MyBot;

import network.Communicator;

/**
 * manages a full length game
 * 
 * @author farzad
 * 
 */
public class Game
{
	private State			gameState;
	private Communicator	communicator;
	private MessageParser	parser;
	Bot						bot;

	public Game()
	{
		this.gameState = State.WAITING;
	}

	/**
	 * connects to the server.
	 */
	private void connectToServer()
	{
		if (gameState != State.WAITING) {
			System.err.println("Already connected to the server!");
			return;
		}

		gameState = State.CONNECTING;

		while (gameState != State.CONNECTED) {
			try {
				this.communicator = new Communicator(Config.Game.serverAddress,
						Config.Game.serverPort);

				gameState = State.CONNECTED;

			} catch (UnknownHostException e) {
				System.err
				.println("host name not known. cannot connect to server. retrying in 10 seconds");

				wait(10000);
			} catch (IOException e) {
				System.err
				.println("could not get i/o stream(s) for the server... retrying in 10 seconds");
				wait(10000);
			}
		}
	}

	private void recvGameConfig()
	{
		if (gameState != State.CONNECTED) {
			System.err.println("cannot receive game configurations right now.");
			return;
		}

		gameState = State.CONFIGURING;

		MessageParser.parseConfig(communicator.recvData());

		if (Config.Game.mapHeight != -1 && Config.Game.mapWidth != -1
				&& Config.Game.cycleCountLimit != -1
				&& Config.Game.cycleTimeLimit != -1
				&& Config.Bot.teamNumber != -1) {
			gameState = State.READY;
		}
	}

	/**
	 * runs the next cycle in the game.
	 */
	private void runNextCycle()
	{
		if (gameState != State.RUNNING) {
			return;
		}


		parser.parseData(communicator.recvData());

		communicator.sendData(bot.tick());

	}

	/**
	 * creates a bot, starts the game and runs it, until the game is over
	 */
	private void start()
	{
		if (gameState != State.READY) {
			return;
		}

		bot = new MyBot();
		parser = new MessageParser(bot);

		gameState = State.RUNNING;

		while (gameState == State.RUNNING) {
			runNextCycle();
		}
	}

	/**
	 * initializes and starts a game.
	 */
	public void run()
	{
		connectToServer();
		recvGameConfig();

		start();
	}

	/**
	 * sleeps the game for the given time
	 * 
	 * @param time
	 */
	private void wait(int time)
	{
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.err.println("interrupt failed.");
		}
	}

	/**
	 * represents a state in the game.
	 * 
	 * @author farzad
	 * 
	 */
	private enum State
	{
		WAITING, CONNECTING, CONNECTED, CONFIGURING, READY, RUNNING, FINISHED;
	}
}
