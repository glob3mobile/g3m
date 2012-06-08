package org.glob3.mobile.generated; 
//
//  IRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public interface Renderer
{
  void initialize(InitializationContext ic);

  int render(RenderContext rc);

  boolean onTouchEvent(TouchEvent touchEvent);

  //virtual ~Renderer() { };
}