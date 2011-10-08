package com.reactionapps.missilecommand;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

public class MissileCommandActivity extends Activity {

	private MissileLauncherConnection client;

	private static final String TAG = "MissileCommandActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		this.initializeClient();

		ImageButton upButton = (ImageButton) super.findViewById(R.id.up_button);
		upButton.setOnTouchListener(new ControlButtonTouchListener(ControlCommand.UP));

		ImageButton downButton = (ImageButton) super.findViewById(R.id.down_button);
		downButton.setOnTouchListener(new ControlButtonTouchListener(ControlCommand.DOWN));

		ImageButton leftButton = (ImageButton) super.findViewById(R.id.left_button);
		leftButton.setOnTouchListener(new ControlButtonTouchListener(ControlCommand.LEFT));

		ImageButton rightButton = (ImageButton) super.findViewById(R.id.right_button);
		rightButton.setOnTouchListener(new ControlButtonTouchListener(ControlCommand.RIGHT));

		ImageButton fireButton = (ImageButton) super.findViewById(R.id.fire_button);
		fireButton.setOnTouchListener(new FireButtonTouchListener());
	}

	private void initializeClient() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String connectionType = preferences.getString("connectionType", "usb");

		if (connectionType.equals("usb")) {
			Intent intent = super.getIntent();
			String action = intent.getAction();
			
			if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
				UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
				UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				this.client = new USBConnection(manager, device);	
			}
		} else if (connectionType.equals("network")) {
			String host = preferences.getString("host", "127.0.0.1");
			int port = Integer.parseInt(preferences.getString("port", "9999"));
			this.client = new ClientConnection(host, port);
		}
		
		if (this.client != null) {
			Toast t = Toast.makeText(this, "Connected to the Missile Launcher", Toast.LENGTH_SHORT);
			t.show();
		} else {
			Toast t = Toast.makeText(this, "Could not connect to the Missile Launcher", Toast.LENGTH_SHORT);
			t.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = super.getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.preferences_activity) {
			Intent preferences = new Intent(this, TurretCommandPreferenceActivity.class);
			super.startActivity(preferences);
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (this.client != null) {
			this.client.close();
			this.client = null;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.initializeClient();

		if (this.client != null) {
			Thread thread = new Thread(this.client);
			thread.start();
		} else {
			// TODO Show some kind of error message.
		}
	}

	public void sendCommand(ControlCommand command) {
		if (this.client != null) {
			this.client.sendCommand(command);
		}
	}
}