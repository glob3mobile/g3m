
package org.glob3.mobile.specific;

import java.util.logging.*;
import org.glob3.mobile.generated.*;
import com.google.gwt.i18n.client.NumberFormat;

public final class Logger_WebGL extends ILogger {

   private final Logger _logger;

   private final boolean _logInfo;
   private final boolean _logWarning;
   private final boolean _logError;

   public Logger_WebGL(final LogLevel level) {
      super(level);

      _logger = Logger.getLogger("");

      final int levelValue = _level.getValue();
      _logInfo    = levelValue <= LogLevel.InfoLevel.getValue();
      _logWarning = levelValue <= LogLevel.WarningLevel.getValue();
      _logError   = levelValue <= LogLevel.ErrorLevel.getValue();

      logInfo("created Logger_WebGL level=" + level);
   }

   @Override
   public void logInfo(final String message, final Object... args) {
      if (_logInfo) {
         _logger.log(Level.INFO, stringFormat(message, args));
      }
   }

   @Override
   public void logWarning(final String message, final Object... args) {
      if (_logWarning) {
         _logger.log(Level.WARNING, stringFormat(message, args));
      }
   }

   @Override
   public void logError(final String message, final Object... args) {
      if (_logError) {
         _logger.log(Level.SEVERE, stringFormat(message, args));
      }
   }

   private static boolean isFlagOrWidth(final char c) {
      return "-+ #0123456789".indexOf(c) >= 0;
   }

   private static boolean isLengthModifier(final char c) {
      return "hlLqjzt".indexOf(c) >= 0;
   }

   private static boolean isConversion(final char c) {
      return "sdiufFeEgGxXcb".indexOf(c) >= 0;
   }

   private static String formatArgument(final Object argument, final int precision) {
      if ((precision >= 0) && (argument instanceof Number)) {
         final StringBuilder pattern = new StringBuilder("0");
         if (precision > 0) {
            pattern.append('.');
            for (int i = 0; i < precision; i++) {
               pattern.append('0');
            }
         }
         return NumberFormat.getFormat(pattern.toString()).format(((Number) argument).doubleValue());
      }
      return String.valueOf(argument);
   }

   // GWT has no String.format, so this reads the printf specifiers by hand
   private static String stringFormat(final String format, final Object... args) {
      final StringBuilder sb     = new StringBuilder(2048);
      final int           length = format.length();

      int argsI = 0;
      int i     = 0;
      while (i < length) {
         final char c = format.charAt(i);
         if ((c != '%') || ((i + 1) >= length)) {
            sb.append(c);
            i++;
            continue;
         }
         if (format.charAt(i + 1) == '%') {
            sb.append('%');
            i += 2;
            continue;
         }

         final int specifierStart = i;
         i++;
         while ((i < length) && isFlagOrWidth(format.charAt(i))) {
            i++;
         }
         int precision = -1;
         if ((i < length) && (format.charAt(i) == '.')) {
            i++;
            final int precisionStart = i;
            while ((i < length) && Character.isDigit(format.charAt(i))) {
               i++;
            }
            precision = (i > precisionStart) ? Integer.parseInt(format.substring(precisionStart, i)) : 0;
         }
         while ((i < length) && isLengthModifier(format.charAt(i))) {
            i++;
         }

         if ((i < length) && isConversion(format.charAt(i))) {
            i++;
            if (argsI < args.length) {
               sb.append(formatArgument(args[argsI], precision));
               argsI++;
            }
            else {
               sb.append(format.substring(specifierStart, i));
            }
         }
         else {
            sb.append(format.substring(specifierStart, i));
         }
      }
      return sb.toString();
   }

}
