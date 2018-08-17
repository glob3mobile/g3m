package org.glob3.mobile.generated;import java.util.*;

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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MEventContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderState;


public abstract class HUDWidget
{
  private boolean _enable;

  protected abstract void rawRender(G3MRenderContext rc, GLState glState);

  public HUDWidget()
  {
	  _enable = true;
  }

  public final void setEnable(boolean enable)
  {
	_enable = enable;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnable() const
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

  public final void render(G3MRenderContext rc, GLState glState)
  {
	if (_enable)
	{
	  rawRender(rc, glState);
	}
  }

}
