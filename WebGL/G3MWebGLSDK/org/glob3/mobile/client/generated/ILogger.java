package org.glob3.mobile.generated; 
public abstract class ILogger
{
  protected final LogLevel _level;

  protected ILogger(LogLevel level)
  {
	  _level = level;
  }

  protected static ILogger _instance = null;

  public static void setInstance(ILogger logger)
  {
	if (_instance != null)
	{
	  System.out.print("Warning, ILooger instance set two times\n");
	}
	_instance = logger;
  }

  public static ILogger instance()
  {
	return _instance;
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