

package org.glob3.mobile.specific;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LogLevel;

import android.util.Log;


public class Logger_Android
         extends
            ILogger {


   Locale _locale = new Locale("myLocale");


   protected Logger_Android(final LogLevel level) {
      super(level);

      // Create a DecimalFormat instance for this format
      final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(this._locale);
      otherSymbols.setDecimalSeparator(',');
      otherSymbols.setGroupingSeparator('.');
   }


   @Override
   public void logInfo(final String x,
                       final Object... LegacyParamArray) {

      if (_level == LogLevel.SilenceLevel) {
         return;
      }

      final String res = String.format(_locale, x, LegacyParamArray);
      Log.d("Info: ", res);
   }


   @Override
   public void logWarning(final String x,
                          final Object... LegacyParamArray) {
      if (_level == LogLevel.SilenceLevel) {
         return;
      }
      if (_level == LogLevel.InfoLevel) {
         return;
      }

      final String res = String.format(_locale, x, LegacyParamArray);
      Log.w("Warning: ", res);
   }


   @Override
   public void logError(final String x,
                        final Object... LegacyParamArray) {
      if (_level != LogLevel.ErrorLevel) {
         return;
      }

      final String res = String.format(_locale, x, LegacyParamArray);
      Log.e("Error: ", res);
   }

}
