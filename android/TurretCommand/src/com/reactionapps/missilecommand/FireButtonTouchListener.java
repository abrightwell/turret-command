package com.reactionapps.missilecommand;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class FireButtonTouchListener implements OnTouchListener {

	@Override
	public boolean onTouch(View view, MotionEvent event) {

		Context context = view.getContext();
		int action = event.getAction();

		if (context instanceof MissileCommandActivity) {
			MissileCommandActivity activity = (MissileCommandActivity) context;
			
			if (action == MotionEvent.ACTION_DOWN) {
				activity.sendCommand(ControlCommand.FIRE);
			}
		}
		
		return false;
	}

}
