package org.glob3.mobile.generated; 
//
//  IStringBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public abstract class IStringBuilder
{

  private static IStringBuilder _instance;



//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IStringBuilder* getNewInstance() const = 0;
  protected abstract IStringBuilder getNewInstance();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  static void setInstance(IStringBuilder isb);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  static IStringBuilder newStringBuilder();

  public abstract IStringBuilder add(double d);
  public abstract IStringBuilder add(RefObject<String> c);
  public abstract IStringBuilder add(String s);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String getString() const = 0;
  public abstract String getString();


}