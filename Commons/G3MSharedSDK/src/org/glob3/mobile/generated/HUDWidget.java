package org.glob3.mobile.generated; 
//
//  HUDWidget.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

//
//  HUDWidget.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//


//class G3MContext;
//class G3MEventContext;
//class G3MRenderContext;
//class GLState;



public abstract class HUDWidget
{
  private boolean _enable;

  public HUDWidget()
  {
     _enable = true;
  }

  public final void setEnable(boolean enable)
  {
    _enable = enable;
  }

  public final boolean isEnable()
  {
    return _enable;
  }

  public void dispose()
  {

  }

  public abstract void initialize(G3MContext context);

  public abstract void onResizeViewportEvent(G3MEventContext ec, int width, int height);

  public abstract RenderState getRenderState(G3MRenderContext rc);

  public abstract void render(G3MRenderContext rc, GLState glState);

}