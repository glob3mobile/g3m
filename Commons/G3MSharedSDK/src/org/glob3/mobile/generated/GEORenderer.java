package org.glob3.mobile.generated; 
//
//  GEORenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEORenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//



//class GEOObject;

public class GEORenderer extends LeafRenderer
{
  private G3MContext _context;
  private java.util.ArrayList<GEOObject> _children = new java.util.ArrayList<GEOObject>();


  public final void addGEOObject(GEOObject geoObject)
  {
    _children.add(geoObject);
    if (_context != null)
    {
      geoObject.initialize(_context);
    }
  }

  public final void onResume(G3MContext context)
  {

  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

  public final void initialize(G3MContext context)
  {
    _context = context;
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      GEOObject geoObject = _children.get(i);
      geoObject.initialize(_context);
    }
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      GEOObject geoObject = _children.get(i);
      if (!geoObject.isReadyToRender(rc))
      {
        return false;
      }
    }
  
    return true;
  }

  public final void render(G3MRenderContext rc, GLState parentState)
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      GEOObject geoObject = _children.get(i);
      geoObject.render(rc, parentState);
    }
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final void start()
  {

  }

  public final void stop()
  {

  }

}