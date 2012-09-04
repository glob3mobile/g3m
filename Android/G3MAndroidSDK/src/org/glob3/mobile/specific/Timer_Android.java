

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ITimer;
import org.glob3.mobile.generated.TimeInterval;

import android.os.SystemClock;


public class Timer_Android
         extends
            ITimer {

   long _startTime;


   public Timer_Android() {
      start();
   }


   @Override
   public TimeInterval now() {
      final long t = SystemClock.uptimeMillis();
      return TimeInterval.fromMilliseconds(t);
   }


   @Override
   public void start() {
      _startTime = SystemClock.uptimeMillis();
   }


   @Override
   public TimeInterval elapsedTime() {
      final long t = SystemClock.uptimeMillis();
      return TimeInterval.fromMilliseconds(t - _startTime);
   }

}
