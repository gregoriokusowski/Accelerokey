package br.feevale.accelerokey;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class EquilibristSensorEventListener implements SensorEventListener {
	
	private final Equilibrist equilibrist;

	public EquilibristSensorEventListener(Equilibrist equilibrist) {
		this.equilibrist = equilibrist;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		equilibrist.disturb(event.values[1], event.values[0]);
	}

}
