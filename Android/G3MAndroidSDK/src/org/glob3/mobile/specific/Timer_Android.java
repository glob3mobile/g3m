

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ITimer;
import org.glob3.mobile.generated.TimeInterval;

import android.os.SystemClock;


public class Timer_Android
         extends
            ITimer {

   long _startTime;


   @Override
   public TimeInterval now() {
      final int t = (int) SystemClock.uptimeMillis();
      return TimeInterval.fromMilliseconds(t);
   }


   @Override
   public void start() {
      _startTime = SystemClock.uptimeMillis();
   }


   @Override
   public TimeInterval elapsedTime() {
      final long t = SystemClock.uptimeMillis();
      return TimeInterval.fromMilliseconds((int) (t - _startTime));
   }

}
