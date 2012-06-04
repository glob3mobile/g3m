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

  private final InitializationContext _ic;

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

//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//  boolean onTouchEvent(const TouchEvent& event);

  public final void addRenderer(Renderer renderer)
  {
	_renderers.add(renderer);
	renderer.initialize(_ic);
  }
}
//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//boolean CompositeRenderer::onTouchEvent(const TouchEvent &event)
//{
//  for (int i = 0; i < _renderers.size(); i++)
//  {
//	//THE EVENT IS PROCESSED ONLY BY THE FIRST RENDERER
//	if (_renderers[i]->onTouchEvent(event))
//	{
//	  return true;
//	}
//  }
//  return false;
//}
