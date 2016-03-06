package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IDeviceAttitude;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.InterfaceOrientation;
import org.glob3.mobile.generated.MutableMatrix44D;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

@SuppressLint("LintError")
public class DeviceAttitude_Android extends IDeviceAttitude implements
		SensorEventListener {
	private SensorManager _sensorManager = null;

	private Display _display = null;

	float[] _lastMagFields = null;
	float[] _lastAccels = null;
	private float[] _rotationMatrix = new float[16];
	private float[] _inclinationMatrix = new float[16];

	final int SENSOR_DELAY = 2000;

	private boolean _tracking = false;

	private float _lowPassFilterRatio = 0.1f;

	public DeviceAttitude_Android(Context context, float lowPassFilterRatio) {

		_lowPassFilterRatio = lowPassFilterRatio;

		_sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

		_display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	}

	public DeviceAttitude_Android(Context context) {
		_sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

		_display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	}

	@Override
	public void startTrackingDeviceOrientation() {

		_tracking = true;

		if (!_sensorManager.registerListener(this,
				_sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME)) {
			ILogger.instance().logError(
					"TYPE_MAGNETIC_FIELD sensor not supported.");
			_tracking = false;
		}

		if (!_sensorManager.registerListener(this,
				_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME)) {
			ILogger.instance().logError(
					"TYPE_ACCELEROMETER sensor not supported.");
			_tracking = false;
		}

		// if (_sensorManager.registerListener(this,
		// _sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
		// SENSOR_DELAY)) {
		// ILogger.instance().logError(
		// "TYPE_ROTATION_VECTOR sensor not supported.");
		// _tracking = false;
		// }
	}

	@Override
	public void stopTrackingDeviceOrientation() {
		// TODO Auto-generated method stub

		_sensorManager.unregisterListener(this);
		_tracking = false;
	}

	@Override
	public boolean isTracking() {
		// TODO Auto-generated method stub
		return _tracking;
	}

	private float[] lowPass(float[] input, float[] output) {
		if (output == null || _lowPassFilterRatio >= 1.0) {
			return input;
		}
		for (int i = 0; i < input.length; i++) {
			output[i] = output[i] + _lowPassFilterRatio
					* (input[i] - output[i]);
		}
		return output;
	}

	@Override
	public void copyValueOfRotationMatrix(MutableMatrix44D rotationMatrix) {
		// TODO Auto-generated method stub

		if (_lastAccels == null || _lastMagFields == null) {
			rotationMatrix.setValid(false);
		} else {

			if (SensorManager.getRotationMatrix(_rotationMatrix,
					_inclinationMatrix, _lastAccels, _lastMagFields)) {
				rotationMatrix.setValue(_rotationMatrix[0], _rotationMatrix[4],
						_rotationMatrix[8], _rotationMatrix[12],
						_rotationMatrix[1], _rotationMatrix[5],
						_rotationMatrix[9], _rotationMatrix[13],
						_rotationMatrix[2], _rotationMatrix[6],
						_rotationMatrix[10], _rotationMatrix[14],
						_rotationMatrix[3], _rotationMatrix[7],
						_rotationMatrix[11], _rotationMatrix[15]);
			} else {
				rotationMatrix.setValid(false);
			}
		}

	}

	@Override
	public InterfaceOrientation getCurrentInterfaceOrientation() {
		int rotation = _display.getRotation();

		switch (rotation) {
		case Surface.ROTATION_0:
			return InterfaceOrientation.PORTRAIT;
		case Surface.ROTATION_90:
			return InterfaceOrientation.LANDSCAPE_RIGHT;
		case Surface.ROTATION_180:
			return InterfaceOrientation.PORTRAIT_UPSIDEDOWN;
		case Surface.ROTATION_270:
			return InterfaceOrientation.LANDSCAPE_LEFT;
		default:
			return null;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		ILogger.instance().logInfo(
				"Sensor " + sensor.getName() + " changed accuracy to "
						+ accuracy);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			_lastAccels = lowPass(event.values.clone(), _lastAccels);
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			_lastMagFields = lowPass(event.values.clone(), _lastMagFields);
			break;
		default:
			return;
		}

	}

}
