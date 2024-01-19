package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;

public class G3M_JavaDesktop {

   public static void initialize() {
      IFactory.setInstance(new Factory_JavaDesktop());
      ILogger.setInstance(new Logger_JavaDesktop(LogLevel.ErrorLevel));
      IStringBuilder.setInstance(new StringBuilder_JavaDesktop(IStringBuilder.DEFAULT_FLOAT_PRECISION));
      IJSONParser.setInstance(new JSONParser_JavaDesktop());
      IMathUtils.setInstance(new MathUtils_JavaDesktop());
   }

   private G3M_JavaDesktop() {
   }
}
