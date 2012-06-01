package org.glob3.mobile.generated; 
//
//  InitializationContext.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  InitializationContext.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGL;

public class Context
{
  protected IFactory _factory;

  protected Context(IFactory factory)
  {
	  _factory = factory;
  }


  public final IFactory getFactory()
  {
	return _factory;
  }

}