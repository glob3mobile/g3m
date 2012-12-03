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
//class G3MContext;

public abstract class IThreadUtils
{
  protected G3MContext _context;


  public IThreadUtils()
  {
	  _context = null;

  }

  public abstract void onResume(G3MContext context);

  public abstract void onPause(G3MContext context);

  public abstract void onDestroy(G3MContext context);

  public void initialize(G3MContext context)
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