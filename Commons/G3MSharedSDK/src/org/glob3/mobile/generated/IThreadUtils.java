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




//class G3MContext;

public abstract class IThreadUtils extends Disposable
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
  super.dispose();

  }

  public abstract void invokeInRendererThread(GTask task, boolean autoDelete);

  public abstract void invokeInBackground(GTask task, boolean autoDelete);

}