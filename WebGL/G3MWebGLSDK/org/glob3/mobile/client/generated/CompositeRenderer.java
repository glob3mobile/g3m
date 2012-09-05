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
  
	final int rendersSize = _renderers.size();
	for (int i = 0; i < rendersSize; i++)
	{
	  _renderers.get(i).initialize(ic);
	}
  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	final int rendersSize = _renderers.size();
	for (int i = 0; i < rendersSize; i++)
	{
	  if (!_renderers.get(i).isReadyToRender(rc))
	  {
		return false;
	  }
	}
  
	return true;
  }

  public final void render(RenderContext rc)
  {
	//rc->getLogger()->logInfo("CompositeRenderer::render()");
  
	final int rendersSize = _renderers.size();
	for (int i = 0; i < rendersSize; i++)
	{
	  _renderers.get(i).render(rc);
	}
  }

  public final boolean onTouchEvent(EventContext ec, TouchEvent touchEvent)
  {
	// the events are processed bottom to top
	final int rendersSize = _renderers.size();
	for (int i = rendersSize - 1; i >= 0; i--)
	{
	  if (_renderers.get(i).onTouchEvent(ec, touchEvent))
	  {
		return true;
	  }
	}
	return false;
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {
	// the events are processed bottom to top
	final int rendersSize = _renderers.size();
	for (int i = rendersSize - 1; i >= 0; i--)
	{
	  _renderers.get(i).onResizeViewportEvent(ec, width, height);
	}
  }

  public final void addRenderer(Renderer renderer)
  {
	_renderers.add(renderer);
	if (_ic != null)
	{
	  renderer.initialize(_ic);
	}
  }

  public final void start()
  {
	final int rendersSize = _renderers.size();
	for (int i = 0; i < rendersSize; i++)
	{
	  _renderers.get(i).start();
	}
  }

  public final void stop()
  {
	final int rendersSize = _renderers.size();
	for (int i = 0; i < rendersSize; i++)
	{
	  _renderers.get(i).stop();
	}
  }

}