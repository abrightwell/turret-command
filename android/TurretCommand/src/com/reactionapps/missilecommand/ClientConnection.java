package com.reactionapps.missilecommand;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientConnection implements Runnable, MissileLauncherConnection {

	private Socket connection;

	private PrintWriter writer;

	private String host;

	private int port;

	private ControlCommand command;

	public ClientConnection(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void run() {
		this.initialize(this.host, this.port);

		while (this.connection != null && this.connection.isConnected()) {
			if (this.command != null) {
				this.writer.write(this.command.toString());
				this.writer.flush();
				this.command = null;
			}
		}
	}

	private void initialize(String host, int port) {
		try {
			if (this.connection != null) {
				this.connection.close();
			}

			if (this.writer != null) {
				this.writer.close();
			}

			this.connection = new Socket(host, port);

			// Initialize the print writer.
			OutputStream os = this.connection.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);

			this.writer = new PrintWriter(bw);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendCommand(ControlCommand command) {
		this.command = command;
	}

	public void close() {
		try {
			// Close the output stream.
			if (this.writer != null) {
				this.writer.close();
				this.writer = null;
			}

			// Close the connection.
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
