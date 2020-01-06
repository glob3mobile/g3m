

package org.glob3.mobile.specific;

import java.util.logging.*;

import org.glob3.mobile.generated.*;

import com.google.gwt.regexp.shared.*;


public final class Logger_WebGL
         extends
            ILogger {

   private final Logger _logger;

   private final boolean _logInfo;
   private final boolean _logWarning;
   private final boolean _logError;


   public Logger_WebGL(final LogLevel level) {
      super(level);

      _logger = Logger.getLogger("");

      final int levelValue = _level.getValue();
      _logInfo = levelValue <= LogLevel.InfoLevel.getValue();
      _logWarning = levelValue <= LogLevel.WarningLevel.getValue();
      _logError = levelValue <= LogLevel.ErrorLevel.getValue();
   }


   @Override
   public void logInfo(final String message,
                       final Object... args) {
      if (_logInfo) {
         _logger.log(Level.INFO, stringFormat(message, args));
      }
   }


   @Override
   public void logWarning(final String message,
                          final Object... args) {
      if (_logWarning) {
         _logger.log(Level.WARNING, stringFormat(message, args));
      }
   }


   @Override
   public void logError(final String message,
                        final Object... args) {
      if (_logError) {
         _logger.log(Level.SEVERE, stringFormat(message, args));
      }
   }


   private static final RegExp exp = RegExp.compile("%[sdfib]+");


   private static String stringFormat(final String format,
                                      final Object... args) {
      final StringBuilder sb = new StringBuilder(2048);
      final SplitResult splits = exp.split(format);

      int argsI = 0;
      for (int i = 0; i < splits.length(); i++) {
         sb.append(splits.get(i));
         if (((i + 1) < splits.length()) && (argsI < args.length)) {
            sb.append(args[argsI]);
         }
         argsI++;
      }
      return sb.toString();
   }


}
