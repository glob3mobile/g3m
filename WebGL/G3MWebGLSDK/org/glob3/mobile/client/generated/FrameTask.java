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




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderContext;


public abstract class FrameTask
{
  public void dispose()
  {

  }

  public abstract boolean isCanceled(RenderContext rc);

  public abstract void execute(RenderContext rc);

}