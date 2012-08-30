package org.glob3.mobile.generated; 
public abstract class ILogger
{
  protected final LogLevel _level;

  protected ILogger(LogLevel level)
  {
	  _level = level;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void logInfo(const String x, ...) const = 0;
  public abstract void logInfo(String x, Object... LegacyParamArray);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void logWarning(const String x, ...) const = 0;
  public abstract void logWarning(String x, Object... LegacyParamArray);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void logError(const String x, ...) const = 0;
  public abstract void logError(String x, Object... LegacyParamArray);

  // a virtual destructor is needed for conversion to Java
  public void dispose()
  {
  }
}