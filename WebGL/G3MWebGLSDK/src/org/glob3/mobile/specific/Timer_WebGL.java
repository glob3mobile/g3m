

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ITimer;
import org.glob3.mobile.generated.TimeInterval;


public final class Timer_WebGL
         extends
            ITimer {

   private long _startTime;


   public Timer_WebGL() {
      start();
   }


   @Override
   public TimeInterval now() {
      //      final double currentTime = Duration.currentTimeMillis();
      //      return TimeInterval.fromSeconds(currentTime / 1000);
      return TimeInterval.fromMilliseconds(System.currentTimeMillis());
   }


   @Override
   public void start() {
      //      _startTime = Duration.currentTimeMillis();
      _startTime = System.currentTimeMillis();
   }


   @Override
   public TimeInterval elapsedTime() {
      //      final double currentTime = Duration.currentTimeMillis();
      //      return TimeInterval.fromSeconds((currentTime - _startTime) / 1000);
      return TimeInterval.fromMilliseconds(System.currentTimeMillis() - _startTime);
   }

}
