

package org.glob3.mobile.specific;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Locale;

import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LogLevel;

import android.util.Log;


public final class Logger_Android
         extends
            ILogger {

   private final Locale _locale = new Locale("myLocale");


   Logger_Android(final LogLevel level) {
      super(level);
   }


   @Override
   public void logInfo(final String x,
                       final Object... legacyParamArray) {

      if (_level == LogLevel.SilenceLevel) {
         return;
      }

      try {
         final String res = String.format(_locale, x, legacyParamArray);
         Log.i("Info: ", res);
      }
      catch (final IllegalFormatException e) {
         Log.e("Info: ", x + " " + Arrays.toString(legacyParamArray));
      }
   }


   @Override
   public void logWarning(final String x,
                          final Object... legacyParamArray) {
      if (_level == LogLevel.SilenceLevel) {
         return;
      }
      if (_level == LogLevel.InfoLevel) {
         return;
      }

      try {
         final String res = String.format(_locale, x, legacyParamArray);
         Log.w("Warning: ", res);
      }
      catch (final IllegalFormatException e) {
         Log.e("Warning: ", x + " " + Arrays.toString(legacyParamArray));
      }
   }


   @Override
   public void logError(final String x,
                        final Object... legacyParamArray) {
      if (_level != LogLevel.ErrorLevel) {
         return;
      }

      try {
         final String res = String.format(_locale, x, legacyParamArray);
         Log.e("Error: ", res);
      }
      catch (final IllegalFormatException e) {
         Log.e("Error: ", x + " " + Arrays.toString(legacyParamArray));
      }

   }

}
