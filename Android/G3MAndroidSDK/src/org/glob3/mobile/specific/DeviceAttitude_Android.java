

package org.glob3.mobile.specific;


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
   private final Display       _defaultDisplay;
   //   private final float         _lowPassFilterRatio;

   //   private float[]             _magnetometerReading  = null;
   //   private float[]             _accelerometerReading = null;

   //   private final float[]       _rotationVectorReading = new float[3];
   private final float[]       _rotationMatrix = new float[16];

   private boolean             _tracking       = false;


   //   public DeviceAttitude_Android(final Context context,
   //                                 final float lowPassFilterRatio) {
   //      _sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
   //      _defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
   //      _lowPassFilterRatio = lowPassFilterRatio;
   //   }
   //   public DeviceAttitude_Android(final Context context) {
   //      // this(context, 0.09f); // for shitty devices
   //      this(context, 0.1f);
   //   }

   public DeviceAttitude_Android(final Context context) {
      _sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
      _defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
   }


   @Override
   public void startTrackingDeviceOrientation() {
      if (!_tracking) {
         _tracking = true;

         _sensorManager.unregisterListener(this);

         //         final Sensor accelerometerSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
         //         if (!_sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST)) {
         //            ILogger.instance().logError("TYPE_ACCELEROMETER sensor not supported.");
         //            _tracking = false;
         //         }
         //
         //         final Sensor magneticSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
         //         if (!_sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST)) {
         //            ILogger.instance().logError("TYPE_MAGNETIC_FIELD sensor not supported.");
         //            // _tracking = false;
         //         }

         final Sensor rotationVectorSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
         if (!_sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST)) {
            ILogger.instance().logError("TYPE_ROTATION_VECTOR sensor not supported.");
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


   @Override
   public void copyValueOfRotationMatrix(final MutableMatrix44D rotationMatrix) {
      //      rotationMatrix.setValid(false);
      //      if (_accelerometerReading != null) {
      //         if (_magnetometerReading != null) {
      //            if (SensorManager.getRotationMatrix(_rotationMatrix, null, _accelerometerReading, _magnetometerReading)) {
      //               rotationMatrix.setValue( //
      //                        _rotationMatrix[0], _rotationMatrix[4], _rotationMatrix[8], _rotationMatrix[12], //
      //                        _rotationMatrix[1], _rotationMatrix[5], _rotationMatrix[9], _rotationMatrix[13], //
      //                        _rotationMatrix[2], _rotationMatrix[6], _rotationMatrix[10], _rotationMatrix[14], //
      //                        _rotationMatrix[3], _rotationMatrix[7], _rotationMatrix[11], _rotationMatrix[15]);
      //            }
      //         }
      //         else {
      //            final double gx = _accelerometerReading[0] / 9.81f;
      //            final double gy = _accelerometerReading[1] / 9.81f;
      //            final double gz = _accelerometerReading[2] / 9.81f;
      //
      //            // http://theccontinuum.com/2012/09/24/arduino-imu-pitch-roll-from-accelerometer/
      //            final float pitch = (float) -Math.atan(gy / Math.sqrt((gx * gx) + (gz * gz)));
      //            final float roll = (float) -Math.atan(gx / Math.sqrt((gy * gy) + (gz * gz)));
      //            final float azimuth = 0; // Impossible to guess
      //
      //            final float[] mAccMagOrientation = { azimuth, pitch, roll };
      //            if (SensorManager.getRotationMatrix(_rotationMatrix, null, _accelerometerReading, mAccMagOrientation)) {
      //               rotationMatrix.setValue( //
      //                        _rotationMatrix[0], _rotationMatrix[4], _rotationMatrix[8], _rotationMatrix[12], //
      //                        _rotationMatrix[1], _rotationMatrix[5], _rotationMatrix[9], _rotationMatrix[13], //
      //                        _rotationMatrix[2], _rotationMatrix[6], _rotationMatrix[10], _rotationMatrix[14], //
      //                        _rotationMatrix[3], _rotationMatrix[7], _rotationMatrix[11], _rotationMatrix[15]);
      //            }
      //         }
      //      }

      rotationMatrix.setValue( //
               _rotationMatrix[0], _rotationMatrix[4], _rotationMatrix[8], _rotationMatrix[12], //
               _rotationMatrix[1], _rotationMatrix[5], _rotationMatrix[9], _rotationMatrix[13], //
               _rotationMatrix[2], _rotationMatrix[6], _rotationMatrix[10], _rotationMatrix[14], //
               _rotationMatrix[3], _rotationMatrix[7], _rotationMatrix[11], _rotationMatrix[15]);
   }


   @Override
   public InterfaceOrientation getCurrentInterfaceOrientation() {
      switch (_defaultDisplay.getRotation()) {
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
      ILogger.instance().logInfo("Sensor \"" + sensor.getName() + "\" changed accuracy to: " + accuracy);
   }


   //   private static float[] lowPass(final float[] current,
   //                                  final float[] previous,
   //                                  final float lowPassFilterRatio) {
   //      if ((previous == null) || (lowPassFilterRatio >= 1.0)) {
   //         return current.clone();
   //      }
   //      final int length = current.length;
   //      for (int i = 0; i < length; i++) {
   //         previous[i] = previous[i] + (lowPassFilterRatio * (current[i] - previous[i]));
   //         // previous[i] = (current[i] * lowPassFilterRatio) + (previous[i] * (1.0f - lowPassFilterRatio));
   //      }
   //      return previous;
   //   }


   @Override
   public void onSensorChanged(final SensorEvent event) {
      switch (event.sensor.getType()) {
      //         case Sensor.TYPE_ACCELEROMETER:
      //            _accelerometerReading = lowPass(event.values, _accelerometerReading, _lowPassFilterRatio);
      //            break;
      //         case Sensor.TYPE_MAGNETIC_FIELD:
      //            _magnetometerReading = lowPass(event.values, _magnetometerReading, _lowPassFilterRatio);
      //            break;
         case Sensor.TYPE_ROTATION_VECTOR:
            //            System.arraycopy(event.values, 0, _rotationVectorReading, 0, _rotationVectorReading.length);
            SensorManager.getRotationMatrixFromVector(_rotationMatrix, event.values);
            break;
         default:
            return;
      }


   }

}
