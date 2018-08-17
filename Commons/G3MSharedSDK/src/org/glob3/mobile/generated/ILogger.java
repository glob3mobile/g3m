package org.glob3.mobile.generated;import java.util.*;

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
	  ILogger.instance().logWarning("ILooger instance already set!");
	  if (_instance != null)
		  _instance.dispose();
	}
	_instance = logger;
  }

  public static ILogger instance()
  {
	return _instance;
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void logInfo (const String x, ...) const = 0;
  public abstract void logInfo (String x, Object... LegacyParamArray);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void logWarning(const String x, ...) const = 0;
  public abstract void logWarning(String x, Object... LegacyParamArray);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void logError (const String x, ...) const = 0;
  public abstract void logError (String x, Object... LegacyParamArray);

  public void dispose()
  {
  }

}
