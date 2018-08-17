package org.glob3.mobile.generated;import java.util.*;

//
//  IStringBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//

//
//  IStringBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//



public abstract class IStringBuilder
{

  private static IStringBuilder _instance = null;



//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IStringBuilder* getNewInstance() const = 0;
  protected abstract IStringBuilder getNewInstance();

  public static void setInstance(IStringBuilder isb)
  {
	if (_instance != null)
	{
	  ILogger.instance().logWarning("IStringBuilder set two times");
	}
	_instance = isb;
  }

  public static IStringBuilder newStringBuilder()
  {
	return _instance.getNewInstance();
  }

  public abstract IStringBuilder addDouble(double d);
  public abstract IStringBuilder addFloat(float f);

  public abstract IStringBuilder addInt(int i);
  public abstract IStringBuilder addLong(long l);

  public abstract IStringBuilder addString(String s);
  public abstract IStringBuilder addBool(boolean b);

  public abstract IStringBuilder clear();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String getString() const = 0;
  public abstract String getString();

  // a virtual destructor is needed for conversion to Java
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean contentEqualsTo(const String& that) const = 0;
  public abstract boolean contentEqualsTo(String that);

}
