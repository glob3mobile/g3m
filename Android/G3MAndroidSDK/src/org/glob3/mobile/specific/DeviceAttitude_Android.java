

package org.glob3.mobile.specific;

import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.WINDOW_SERVICE;
import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;
import static android.hardware.SensorManager.SENSOR_DELAY_GAME;
import static android.hardware.SensorManager.getRotationMatrix;

import org.glob3.mobile.generated.IDeviceAttitude;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.InterfaceOrientation;
import org.glob3.mobile.generated.MutableMatrix44D;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;


public class DeviceAttitude_Android
   extends
      IDeviceAttitude
   implements
      SensorEventListener {

   private final SensorManager _sensorManager;
   private final Display       _display;
   private final float         _lowPassFilterRatio;

   private float[]             _lastMagFields;
   private float[]             _lastAccels;
   private final float[]       _rotationMatrix    = new float[16];
   private final float[]       _inclinationMatrix = new float[16];

   private boolean             _tracking          = false;


   public DeviceAttitude_Android(final Context context,
                                 final float lowPassFilterRatio) {
      _sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
      _display = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
      _lowPassFilterRatio = lowPassFilterRatio;
   }


   public DeviceAttitude_Android(final Context context) {
      this(context, 0.1f);
   }


   @Override
   public void startTrackingDeviceOrientation() {
      if (!_tracking) {
         _tracking = true;

         if (!_sensorManager.registerListener(this, _sensorManager.getDefaultSensor(TYPE_MAGNETIC_FIELD), SENSOR_DELAY_GAME)) {
            ILogger.instance().logError("TYPE_MAGNETIC_FIELD sensor not supported.");
            _tracking = false;
         }

         if (!_sensorManager.registerListener(this, _sensorManager.getDefaultSensor(TYPE_ACCELEROMETER), SENSOR_DELAY_GAME)) {
            ILogger.instance().logError("TYPE_ACCELEROMETER sensor not supported.");
            _tracking = false;
         }
      }
   }


   @Override
   public void stopTrackingDeviceOrientation() {
      if (_tracking) {
         _sensorManager.unregisterListener(this);
         _tracking = false;
      }
   }


   @Override
   public boolean isTracking() {
      return _tracking;
   }


   private float[] lowPass(final float[] input,
                           final float[] output) {
      if ((output == null) || (_lowPassFilterRatio >= 1.0)) {
         return input;
      }
      for (int i = 0; i < input.length; i++) {
         output[i] = output[i] + (_lowPassFilterRatio * (input[i] - output[i]));
      }
      return output;
   }


   @Override
   public void copyValueOfRotationMatrix(final MutableMatrix44D rotationMatrix) {
      if ((_lastAccels == null) || (_lastMagFields == null)) {
         rotationMatrix.setValid(false);
      }
      else {
         if (getRotationMatrix(_rotationMatrix, _inclinationMatrix, _lastAccels, _lastMagFields)) {
            rotationMatrix.setValue( //
                     _rotationMatrix[0], _rotationMatrix[4], _rotationMatrix[8], _rotationMatrix[12], //
                     _rotationMatrix[1], _rotationMatrix[5], _rotationMatrix[9], _rotationMatrix[13], //
                     _rotationMatrix[2], _rotationMatrix[6], _rotationMatrix[10], _rotationMatrix[14], //
                     _rotationMatrix[3], _rotationMatrix[7], _rotationMatrix[11], _rotationMatrix[15]);
         }
         else {
            rotationMatrix.setValid(false);
         }
      }
   }


   @Override
   public InterfaceOrientation getCurrentInterfaceOrientation() {
      switch (_display.getRotation()) {
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
   public void onAccuracyChanged(final Sensor sensor,
                                 final int accuracy) {
      ILogger.instance().logInfo("Sensor " + sensor.getName() + " changed accuracy to " + accuracy);
   }


   @Override
   public void onSensorChanged(final SensorEvent event) {
      switch (event.sensor.getType()) {
         case TYPE_ACCELEROMETER:
            _lastAccels = lowPass(event.values.clone(), _lastAccels);
            break;
         case TYPE_MAGNETIC_FIELD:
            _lastMagFields = lowPass(event.values.clone(), _lastMagFields);
            break;
         default:
            return;
      }
   }

}
