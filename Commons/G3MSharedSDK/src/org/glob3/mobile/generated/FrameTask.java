package org.glob3.mobile.generated; 
//
//  FrameTask.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

//
//  FrameTask.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//


//class G3MRenderContext;

public abstract class FrameTask
{
  public void dispose()
  {

  }

  public abstract boolean isCanceled(G3MRenderContext rc);

  public abstract void execute(G3MRenderContext rc);

}