package com.reactionapps.missilecommand;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

public class USBConnection implements MissileLauncherConnection, Runnable {

	private static final String TAG = "USBConnection";

	private UsbDeviceConnection connection;
	private UsbManager usbManager;
	private UsbDevice device;
	private UsbEndpoint endPoint;

	private ControlCommand command;

	public USBConnection(UsbManager manager, UsbDevice device) {
		this.usbManager = manager;
		this.device = device;
		this.initialize();
	}

	@Override
	public void run() {
		while (this.connection != null) {

			if (this.command != null) {
				switch (this.command) {
				case UP:
					this.sendCommand((byte) 0x02);
					break;
				case DOWN:
					this.sendCommand((byte) 0x01);
					break;
				case LEFT:
					this.sendCommand((byte) 0x04);
					break;
				case RIGHT:
					this.sendCommand((byte) 0x08);
					break;
				case FIRE:
					this.sendCommand((byte) 0x10);
					break;
				case STOP:
					this.sendCommand((byte) 0x20);
				default:
					break;
				}
				this.command = null;
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	private void initialize() {
		if (this.device != null) {
			if (this.device.getInterfaceCount() != 1) {
				return;
			}

			UsbInterface intf = device.getInterface(0);

			if (intf.getEndpointCount() != 1) {
				return;
			}

			UsbEndpoint endpoint = intf.getEndpoint(0);
			if (endpoint.getType() != UsbConstants.USB_ENDPOINT_XFER_INT) {
				return;
			}

			this.endPoint = endpoint;

			if (this.device != null) {
				UsbDeviceConnection connection = this.usbManager.openDevice(this.device);
				if (connection != null && connection.claimInterface(intf, true)) {
					this.connection = connection;
				} else {
					this.connection = null;
				}
			}
		}
	}

	private synchronized void sendCommand(byte command) {
		if (this.connection != null) {
			byte[] bytes = { 0x02, command, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
			int result = this.connection
					.controlTransfer(0x21, 0x09, 0x0, 0, bytes, bytes.length, 0);

			if (result < 0) {
				// TODO log error message.
			}
		}
	}

	public void sendCommand(ControlCommand command) {
		this.command = command;
	}

	@Override
	public void close() {
		this.device = null;
		this.endPoint = null;
		this.connection.close();
	}
}
