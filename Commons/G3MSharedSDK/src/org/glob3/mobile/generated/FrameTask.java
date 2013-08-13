package org.glob3.mobile.generated; 
//
//  FrameTasksExecutor.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/08/12.
//
//

//
//  FrameTasksExecutor.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/08/12.
//
//




//class G3MRenderContext;


public abstract class FrameTask
{
  public void dispose()
  {
    JAVA_POST_DISPOSE
  }

  public abstract boolean isCanceled(G3MRenderContext rc);

  public abstract void execute(G3MRenderContext rc);

}