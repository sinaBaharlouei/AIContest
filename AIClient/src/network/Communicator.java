package network;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import config.Config;


/**
 * Communicator is used for transmitting data between Client and Server
 * @author farzad
 *
 */
public class Communicator
{
	private Socket socket;

	private BufferedReader input;
	private BufferedWriter output;

	public Communicator(String hostAddress, int port) throws UnknownHostException, IOException
	{
		socket = new Socket(hostAddress, port);

		output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	/**
	 * reads a single line from the socket's input stream
	 * @return
	 */
	private String readNextLine()
	{
		try
		{
			return input.readLine();
		} catch (IOException e)
		{
			System.err.println("failed to receive data...");
			return null;
		}
	}

	/**
	 * packs and sends the given string to the server. do not modify.
	 * @param data
	 */
	public void sendData(String data)
	{
		try
		{
			output.write("\n" + Config.Communicator.Tags.packetStartTag + "\n" + data + "\n" + Config.Communicator.Tags.packetEndTag + "\n");
			output.flush();
		}
		catch (IOException e)
		{
			System.err.println("could not send data to the server (IOException)");
		}
	}

	/**
	 * waits for the server to send it's next packet, and returns the received data. do not modify
	 * @return
	 */
	public String recvData()
	{
		String tempMessage = "";
		String tempLine = "";
		
		do
			tempLine = readNextLine();
		while(!tempLine.equals(Config.Communicator.Tags.packetStartTag) && tempLine != null);
		
		tempLine = "";

		while(!tempLine.equals(Config.Communicator.Tags.packetEndTag) && tempLine != null)
		{
			tempMessage += tempLine  + "\n";
			tempLine = readNextLine();
		}
		
		return tempMessage.trim().toLowerCase();
	}
}
