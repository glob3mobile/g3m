package org.glob3.mobile.generated; 
//
//  IStringBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  IStringBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
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
	  System.out.print("Warning, ISB instance set two times\n");
	}
	_instance = isb;
  }

  public static IStringBuilder newStringBuilder()
  {
	return _instance.getNewInstance();
  }

  public abstract IStringBuilder add(double d);
  public abstract IStringBuilder add(String s);
  public abstract IStringBuilder addBool(boolean b);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String getString() const = 0;
  public abstract String getString();

  // a virtual destructor is needed for conversion to Java
  public void dispose()
  {
  }

}