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
//class GEOSymbolizer;
//class MeshRenderer;
//class MarksRenderer;
//class ShapesRenderer;


public class GEORenderer extends LeafRenderer
{
  private java.util.ArrayList<GEOObject> _children = new java.util.ArrayList<GEOObject>();

  private final GEOSymbolizer _symbolizer;
  private MeshRenderer _meshRenderer;
  private ShapesRenderer _shapesRenderer;
  private MarksRenderer _marksRenderer;


  public GEORenderer(GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer)
  {
     _symbolizer = symbolizer;
     _meshRenderer = meshRenderer;
     _shapesRenderer = shapesRenderer;
     _marksRenderer = marksRenderer;

  }

  public void dispose()
  {
    if (_symbolizer != null)
       _symbolizer.dispose();
  
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      GEOObject geoObject = _children.get(i);
      if (geoObject != null)
         geoObject.dispose();
    }
  }

  public final void addGEOObject(GEOObject geoObject)
  {
    _children.add(geoObject);
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

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void render(G3MRenderContext rc, GLState parentState)
  {
    final int childrenCount = _children.size();
    if (childrenCount > 0)
    {
      final GEOSymbolizationContext sc = new GEOSymbolizationContext(_symbolizer, _meshRenderer, _shapesRenderer, _marksRenderer);
  
      for (int i = 0; i < childrenCount; i++)
      {
        final GEOObject geoObject = _children.get(i);
        if (geoObject != null)
        {
          geoObject.symbolize(rc, sc);
  
          if (geoObject != null)
             geoObject.dispose();
        }
      }
      _children.clear();
    }
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final void start(G3MRenderContext rc)
  {

  }

  public final void stop(G3MRenderContext rc)
  {

  }

}