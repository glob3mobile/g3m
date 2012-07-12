

package org.glob3.mobile.client.specific;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.glob3.mobile.client.generated.ILogger;
import org.glob3.mobile.client.generated.LogLevel;

import com.google.gwt.regexp.shared.RegExp;


public class Logger_WebGL
         extends
            ILogger {

   Logger _logger;


   protected Logger_WebGL(final LogLevel level) {
      super(level);

      _logger = Logger.getLogger("");
   }


   @Override
   public void logInfo(final String x,
                       final Object... LegacyParamArray) {
      if (_level == LogLevel.SilenceLevel) {
         return;
      }

      final String res = stringFormat(x, LegacyParamArray);
      _logger.log(Level.INFO, res);
   }


   @Override
   public void logWarning(final String x,
                          final Object... LegacyParamArray) {
      if ((_level == LogLevel.SilenceLevel) || (_level == LogLevel.InfoLevel)) {
         return;
      }

      final String res = stringFormat(x, LegacyParamArray);
      _logger.log(Level.WARNING, res);
      _logger.log(Level.WARNING, x);
   }


   @Override
   public void logError(final String x,
                        final Object... LegacyParamArray) {
      if (_level != LogLevel.ErrorLevel) {
         return;
      }

      final String res = stringFormat(x, LegacyParamArray);
      _logger.log(Level.SEVERE, res);
      _logger.log(Level.SEVERE, x);

   }


   public String stringFormat(final String format,
                              final Object... args) {
      final RegExp exp = RegExp.compile("%[sdf]");
      int nextSub = 0;
      String output = "";
      for (int i = 0; i < exp.split(format).length(); i++) {
         output = output + exp.split(format).get(i);
         if (((i + 1) < exp.split(format).length()) && (nextSub < args.length)) {
            output = output + args[nextSub];
         }
         nextSub++;
      }

      return output;
   }


}
