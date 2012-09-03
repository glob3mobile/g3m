

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ITimer;
import org.glob3.mobile.generated.TimeInterval;

import com.google.gwt.core.client.Duration;


public class Timer_WebGL
         extends
            ITimer {

   double _startTime;


   @Override
   public TimeInterval now() {
      final int currentTime = (int) Duration.currentTimeMillis();

      return TimeInterval.fromMilliseconds(currentTime);
   }


   @Override
   public void start() {
      _startTime = Duration.currentTimeMillis();
   }


   @Override
   public TimeInterval elapsedTime() {
      final double currentTime = Duration.currentTimeMillis();

      return TimeInterval.fromMilliseconds((int) (currentTime - _startTime));
   }

}
