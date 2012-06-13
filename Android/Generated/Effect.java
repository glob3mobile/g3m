package org.glob3.mobile.generated; 
//
//  Effects.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Effects.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




public abstract class Effect
{
  public abstract void start(RenderContext rc, TimeInterval now);

  public abstract void doStep(RenderContext rc, TimeInterval now);

  public abstract boolean isDone(RenderContext rc, TimeInterval now);

  public abstract void stop(RenderContext rc, TimeInterval now);

  public void dispose()
  {
  }
}