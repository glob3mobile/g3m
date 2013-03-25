

package org.glob3.mobile.specific;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LogLevel;


public class Logger_JavaDesktop
    extends
      ILogger {

  private final Locale _locale = new Locale("myLocale");


  public Logger_JavaDesktop(final LogLevel level) {
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
      Logger.getLogger(getClass().getName()).log(Level.INFO, "Info: ", res);
    }
    catch (final IllegalFormatException e) {
      Logger.getLogger(getClass().getName()).log(Level.INFO, "Info: ",
          x + " " + Arrays.toString(legacyParamArray));
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
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "Warning: ",
          res);
    }
    catch (final IllegalFormatException e) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "Warning: ",
          x + " " + Arrays.toString(legacyParamArray));
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
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error: ", res);
    }
    catch (final IllegalFormatException e) {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error: ",
          x + " " + Arrays.toString(legacyParamArray));
    }

  }

}
