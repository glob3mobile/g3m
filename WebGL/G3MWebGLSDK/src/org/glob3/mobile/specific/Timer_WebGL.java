
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ITimer;
import org.glob3.mobile.generated.TimeInterval;

public final class Timer_WebGL extends ITimer {

   private long _startTimeInMilliseconds;

   public Timer_WebGL() {
      start();
   }

   @Override
   public TimeInterval now() {
      return TimeInterval.fromMilliseconds(System.currentTimeMillis());
   }

   @Override
   public TimeInterval fromDaysFromNow(final double days) {
      final long daysInMilliseconds = Math.round(days //
            * 24 /* hours */
            * 60 /* minutes */
            * 60 /* seconds */
            * 1000 /* milliseconds */ );

      final long milliseconds = System.currentTimeMillis() + daysInMilliseconds;

      return TimeInterval.fromMilliseconds(milliseconds);
   }

   @Override
   public long nowInMilliseconds() {
      return System.currentTimeMillis();
   }

   @Override
   public void start() {
      _startTimeInMilliseconds = System.currentTimeMillis();
   }

   @Override
   public TimeInterval elapsedTime() {
      return TimeInterval.fromMilliseconds(System.currentTimeMillis() - _startTimeInMilliseconds);
   }

   @Override
   public long elapsedTimeInMilliseconds() {
      return System.currentTimeMillis() - _startTimeInMilliseconds;
   }

}
