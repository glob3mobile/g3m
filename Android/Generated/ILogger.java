package org.glob3.mobile.generated; 
public abstract class ILogger
{
  protected final LogLevel _level;

  protected ILogger(LogLevel level)
  {
	  _level = level;
  }

  public abstract void logInfo(String x, Object... LegacyParamArray);
  public abstract void logWarning(String x, Object... LegacyParamArray);
  public abstract void logError(String x, Object... LegacyParamArray);
  public void dispose()
  {
  }
}