package org.glob3.mobile.generated; 
//
//  IThreadUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 30/08/12.
//
//

//
//  IThreadUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 30/08/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Context;

public abstract class IThreadUtils
{
  protected Context _context;


  public IThreadUtils()
  {
	  _context = 0;

  }

  public void initialize(Context context)
  {
	_context = context;
  }

  public void dispose()
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void invokeInRendererThread(GTask* task, boolean autoDelete) const = 0;
  public abstract void invokeInRendererThread(GTask task, boolean autoDelete);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void invokeInBackground(GTask* task, boolean autoDelete) const = 0;
  public abstract void invokeInBackground(GTask task, boolean autoDelete);

}