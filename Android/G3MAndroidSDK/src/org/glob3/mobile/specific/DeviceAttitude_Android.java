package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IDeviceAttitude;
import org.glob3.mobile.generated.InterfaceOrientation;
import org.glob3.mobile.generated.MutableMatrix44D;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class DeviceAttitude_Android extends IDeviceAttitude implements SensorEventListener {
    private SensorManager _sensorManager = null;
    
    float[] _lastMagFields = new float[3];
    float[] _lastAccels = new float[3];
    private float[] _rotationMatrix = new float[16];
    private float[] _inclinationMatrix = new float[16];
    
    
    public DeviceAttitude_Android(Context context){
    	_sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

	@Override
	public void startTrackingDeviceOrientation() {
	       int DELAY = 15; // 200;
	       _sensorManager.registerListener(this, _sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), DELAY);
	       _sensorManager.registerListener(this, _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), DELAY);
	       _sensorManager.registerListener(this, _sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), DELAY);
	        
		
	}

	@Override
	public void stopTrackingDeviceOrientation() {
		// TODO Auto-generated method stub

        _sensorManager.unregisterListener(this);
	}

	@Override
	public boolean isTracking() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void copyValueOfRotationMatrix(MutableMatrix44D rotationMatrix) {
		// TODO Auto-generated method stub
		
		if (SensorManager.getRotationMatrix(_rotationMatrix, _inclinationMatrix, _lastAccels, _lastMagFields)) {
	    	rotationMatrix.setValue(_rotationMatrix[0],_rotationMatrix[1],_rotationMatrix[2],_rotationMatrix[3],
	    			_rotationMatrix[4],_rotationMatrix[5],_rotationMatrix[6],_rotationMatrix[7],
	    			_rotationMatrix[8],_rotationMatrix[9],_rotationMatrix[10],_rotationMatrix[11],
	    			_rotationMatrix[12],_rotationMatrix[13],_rotationMatrix[14],_rotationMatrix[15]);
		}
		
	}

	@Override
	public InterfaceOrientation getCurrentInterfaceOrientation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

}
