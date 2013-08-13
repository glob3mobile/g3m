package org.glob3.mobile.generated; 
//
//  GTask.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

//
//  GTask.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//


//class G3MContext;


public abstract class GTask extends Disposable
{
  public void dispose()
  {
    JAVA_POST_DISPOSE
  }

  public abstract void run(G3MContext context);
}