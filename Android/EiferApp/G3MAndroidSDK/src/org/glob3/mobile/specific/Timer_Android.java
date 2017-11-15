

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ITimer;
import org.glob3.mobile.generated.TimeInterval;

import android.os.SystemClock;


public final class Timer_Android
         extends
            ITimer {

   private long _startTimeInMilliseconds;


   Timer_Android() {
      //start();
      _startTimeInMilliseconds = SystemClock.uptimeMillis();
   }


   @Override
   public TimeInterval now() {
      return TimeInterval.fromMilliseconds(SystemClock.uptimeMillis());
   }


   @Override
   public long nowInMilliseconds() {
      return SystemClock.uptimeMillis();
   }


   @Override
   public void start() {
      _startTimeInMilliseconds = SystemClock.uptimeMillis();
   }


   @Override
   public TimeInterval elapsedTime() {
      return TimeInterval.fromMilliseconds(SystemClock.uptimeMillis() - _startTimeInMilliseconds);
   }


   @Override
   public long elapsedTimeInMilliseconds() {
      return SystemClock.uptimeMillis() - _startTimeInMilliseconds;
   }


}
