package org.glob3.mobile.generated; 
//
//  CompositeRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  CompositeRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class CompositeRenderer extends Renderer
{
  private java.util.ArrayList<Renderer> _renderers = new java.util.ArrayList<Renderer>();

  private InitializationContext _ic; // CHANGED BY CONVERSOR RULE

  public CompositeRenderer()
  {
	  _ic = null;
	_renderers = new java.util.ArrayList<Renderer>();
  }

  public void dispose()
  {
  }

  public final void initialize(InitializationContext ic)
  {
	_ic = ic;
  
	for (int i = 0; i < _renderers.size(); i++)
	{
	  _renderers.get(i).initialize(ic);
	}
  }

  public final int render(RenderContext rc)
  {
	//rc->getLogger()->logInfo("CompositeRenderer::render()");
  
	int min = 9999;
	for (int i = 0; i < _renderers.size(); i++)
	{
	  int x = _renderers.get(i).render(rc);
	  if (x < min)
		  min = x;
	}
	return min;
  }

  public final boolean onTouchEvent(TouchEvent touchEvent)
  {
	for (int i = 0; i < _renderers.size(); i++)
	{
	  //THE EVENT IS PROCESSED ONLY BY THE FIRST RENDERER
	  if (_renderers.get(i).onTouchEvent(touchEvent))
	  {
		return true;
	  }
	}
	return false;
  }

  public final void addRenderer(Renderer renderer)
  {
	_renderers.add(renderer);
	if (_ic != null)
	{
	  renderer.initialize(_ic);
	}
  }

  public final boolean onResizeViewportEvent(int width, int height)
  {
	boolean res = false;
	for (int i = 0; i < _renderers.size(); i++)
	{
	  //THE EVENT IS PROCESSED ONLY BY ALL RENDERERS
	  res = res | _renderers.get(i).onResizeViewportEvent(width, height);
	}
	return res;
  }
}