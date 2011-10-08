package com.reactionapps.missilecommand;

public interface MissileLauncherConnection extends Runnable {
	public static final byte DOWN = 0x1;
	public static final byte UP = 0x2;
	public static final byte LEFT = 0x4;
	public static final byte RIGHT = 0x8;
	public static final byte FIRE = 0x10;
	public static final byte STOP = 0x20;
	
//	public abstract void sendCommand(byte command);
	
	public void close();
	
	public void sendCommand(ControlCommand command);
}
