

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ITimer;
import org.glob3.mobile.generated.TimeInterval;

import com.google.gwt.core.client.Duration;


public class Timer_WebGL
         extends
            ITimer {

   double _startTime;


   public Timer_WebGL() {
      start();
   }


   @Override
   public TimeInterval now() {
      final double currentTime = Duration.currentTimeMillis();

      return TimeInterval.fromSeconds(currentTime / 1000);
   }


   @Override
   public void start() {
      _startTime = Duration.currentTimeMillis();
   }


   @Override
   public TimeInterval elapsedTime() {
      final double currentTime = Duration.currentTimeMillis();

      return TimeInterval.fromSeconds((currentTime - _startTime) / 1000);
   }

}
