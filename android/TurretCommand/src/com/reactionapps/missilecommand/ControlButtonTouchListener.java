package com.reactionapps.missilecommand;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ControlButtonTouchListener implements OnTouchListener {

	private ControlCommand command;

	public ControlButtonTouchListener(ControlCommand command) {
		this.command = command;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		Context context = view.getContext();
		int action = event.getAction();

		if (context instanceof MissileCommandActivity) {
			MissileCommandActivity activity = (MissileCommandActivity) context;

			if (action == MotionEvent.ACTION_DOWN) {
				activity.sendCommand(this.command);
			} else if (action == MotionEvent.ACTION_UP) {
				activity.sendCommand(ControlCommand.STOP);
			}
		}

		return false;
	}
}
