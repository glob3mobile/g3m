

package org.glob3.mobile.specific;


import org.glob3.mobile.generated.IDeviceAttitude;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.InterfaceOrientation;
import org.glob3.mobile.generated.MutableMatrix44D;
import org.glob3.mobile.generated.MutableQuaternion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;


// based on https://bitbucket.org/apacha/sensor-fusion-demo/

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

         final Sensor gyroscopeSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
         if (!_sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST)) {
            ILogger.instance().logError("TYPE_GYROSCOPE sensor not supported.");
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
      //         case Sensor.TYPE_ROTATION_VECTOR:
      //            SensorManager.getRotationMatrixFromVector(_rotationMatrix, event.values);
      //            break;
         case Sensor.TYPE_ROTATION_VECTOR:
            processRotationVector(event);
            break;
         case Sensor.TYPE_GYROSCOPE:
            processGyroscope(event);
            break;
      //         default:
      //            return;
      }


   }


   private static final float      NS2S                          = 1.0f / 1000000000.0f;
   private static final double     EPSILON                       = 0.1f;
   private static final float      OUTLIER_THRESHOLD             = 0.85f;
   private static final float      OUTLIER_PANIC_THRESHOLD       = 0.75f;
   private static final float      INDIRECT_INTERPOLATION_WEIGHT = 0.01f;
   private static final int        PANIC_THRESHOLD               = 60;


   private final float[]           _temporaryQuaternion          = new float[4];
   private final MutableQuaternion _quaternionRotationVector     = new MutableQuaternion();
   private final MutableQuaternion _quaternionGyroscope          = new MutableQuaternion();
   private boolean                 _positionInitialised          = false;
   private long                    _timestamp;
   //private double              _gyroscopeRotationVelocity     = 0;
   private final MutableQuaternion _deltaQuaternion              = new MutableQuaternion();
   private int                     _panicCounter;
   private final MutableQuaternion _interpolatedQuaternion       = new MutableQuaternion();

   private final MutableQuaternion _correctedQuaternion          = new MutableQuaternion();

   //   protected final float[]     _currentOrientationRotationMatrix = new float[16];
   private final MutableQuaternion _currentOrientationQuaternion = new MutableQuaternion();
   private final float[]           _tmpArray                     = new float[4];


   private void processRotationVector(final SensorEvent event) {
      // Process rotation vector (just safe it)
      // Calculate angle. Starting with API_18, Android will provide this value as event.values[3], but if not, we have to calculate it manually.
      SensorManager.getQuaternionFromVector(_temporaryQuaternion, event.values);

      // Store in quaternion
      _quaternionRotationVector.setXYZW(_temporaryQuaternion[1], _temporaryQuaternion[2], _temporaryQuaternion[3],
               -_temporaryQuaternion[0]);
      if (!_positionInitialised) {
         // Override
         _quaternionGyroscope.copyFrom(_quaternionRotationVector);
         _positionInitialised = true;
      }
   }


   private void processGyroscope(final SensorEvent event) {
      // Process Gyroscope and perform fusion

      // This timestep's delta rotation to be multiplied by the current rotation
      // after computing it from the gyro sample data.
      if (_timestamp != 0) {
         final float dT = (event.timestamp - _timestamp) * NS2S;
         // Axis of the rotation sample, not normalized yet.
         float axisX = event.values[0];
         float axisY = event.values[1];
         float axisZ = event.values[2];

         // Calculate the angular speed of the sample
         final double gyroscopeRotationVelocity = Math.sqrt((axisX * axisX) + (axisY * axisY) + (axisZ * axisZ));

         // Normalize the rotation vector if it's big enough to get the axis
         if (gyroscopeRotationVelocity > EPSILON) {
            axisX /= gyroscopeRotationVelocity;
            axisY /= gyroscopeRotationVelocity;
            axisZ /= gyroscopeRotationVelocity;
         }

         // Integrate around this axis with the angular speed by the timestep
         // in order to get a delta rotation from this sample over the timestep
         // We will convert this axis-angle representation of the delta rotation
         // into a quaternion before turning it into the rotation matrix.
         final double thetaOverTwo = (gyroscopeRotationVelocity * dT) / 2.0f;
         final double sinThetaOverTwo = Math.sin(thetaOverTwo);
         final double cosThetaOverTwo = Math.cos(thetaOverTwo);
         _deltaQuaternion.setX((float) (sinThetaOverTwo * axisX));
         _deltaQuaternion.setY((float) (sinThetaOverTwo * axisY));
         _deltaQuaternion.setZ((float) (sinThetaOverTwo * axisZ));
         _deltaQuaternion.setW((float) -cosThetaOverTwo);

         // Move current gyro orientation
         _deltaQuaternion.multiplyBy(_quaternionGyroscope, _quaternionGyroscope);

         // Calculate dot-product to calculate whether the two orientation sensors have diverged
         // (if the dot-product is closer to 0 than to 1), because it should be close to 1 if both are the same.
         final float dotProd = _quaternionGyroscope.dot(_quaternionRotationVector);

         // If they have diverged, rely on gyroscope only (this happens on some devices when the rotation vector "jumps").
         if (Math.abs(dotProd) < OUTLIER_THRESHOLD) {
            // Increase panic counter
            if (Math.abs(dotProd) < OUTLIER_PANIC_THRESHOLD) {
               _panicCounter++;
            }

            // Directly use Gyro
            setOrientationQuaternionAndMatrix(_quaternionGyroscope);

         }
         else {
            // Both are nearly saying the same. Perform normal fusion.

            // Interpolate with a fixed weight between the two absolute quaternions obtained from gyro and rotation vector sensors
            // The weight should be quite low, so the rotation vector corrects the gyro only slowly, and the output keeps responsive.
            _quaternionGyroscope.slerp(_quaternionRotationVector, _interpolatedQuaternion,
                     (float) (INDIRECT_INTERPOLATION_WEIGHT * gyroscopeRotationVelocity));

            // Use the interpolated value between gyro and rotationVector
            setOrientationQuaternionAndMatrix(_interpolatedQuaternion);
            // Override current gyroscope-orientation
            _quaternionGyroscope.copyFrom(_interpolatedQuaternion);

            // Reset the panic counter because both sensors are saying the same again
            _panicCounter = 0;
         }

         if (_panicCounter > PANIC_THRESHOLD) {
            Log.d("Rotation Vector",
                     "Panic counter is bigger than threshold; this indicates a Gyroscope failure. Panic reset is imminent.");

            if (gyroscopeRotationVelocity < 3) {
               Log.d("Rotation Vector", "Performing Panic-reset. Resetting orientation to rotation-vector value.");

               // Manually set position to whatever rotation vector says.
               setOrientationQuaternionAndMatrix(_quaternionRotationVector);
               // Override current gyroscope-orientation with corrected value
               _quaternionGyroscope.copyFrom(_quaternionRotationVector);

               _panicCounter = 0;
            }
            else {
               Log.d("Rotation Vector",
                        String.format(
                                 "Panic reset delayed due to ongoing motion (user is still shaking the device). Gyroscope Velocity: %.2f > 3",
                                 gyroscopeRotationVelocity));
            }
         }
      }
      _timestamp = event.timestamp;
   }


   private void setOrientationQuaternionAndMatrix(final MutableQuaternion quaternion) {
      _correctedQuaternion.copyFrom(quaternion);
      // We inverted w in the deltaQuaternion, because currentOrientationQuaternion required it.
      // Before converting it back to matrix representation, we need to revert this process
      _correctedQuaternion.setW(-_correctedQuaternion.getW());

      // Use gyro only
      _currentOrientationQuaternion.copyFrom(quaternion);

      // Set the rotation matrix as well to have both representations
      //      SensorManager.getRotationMatrixFromVector(_rotationMatrix, _correctedQuaternion.array());
      _tmpArray[0] = _correctedQuaternion.getX();
      _tmpArray[1] = _correctedQuaternion.getY();
      _tmpArray[2] = _correctedQuaternion.getZ();
      _tmpArray[3] = _correctedQuaternion.getW();
      SensorManager.getRotationMatrixFromVector(_rotationMatrix, _tmpArray);
   }

}
