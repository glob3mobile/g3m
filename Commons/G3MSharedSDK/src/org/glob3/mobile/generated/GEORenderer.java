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


///#include "GPUProgramState.hpp"

//class GEOObject;
//class GEOSymbolizer;
//class MeshRenderer;
//class MarksRenderer;
//class ShapesRenderer;
//class GEOTileRasterizer;
//class GEORenderer_ObjectSymbolizerPair;

public class GEORenderer extends LeafRenderer
{
  private java.util.ArrayList<GEORenderer_ObjectSymbolizerPair> _children = new java.util.ArrayList<GEORenderer_ObjectSymbolizerPair>();

  private final GEOSymbolizer _defaultSymbolizer;

  private MeshRenderer _meshRenderer;
  private ShapesRenderer _shapesRenderer;
  private MarksRenderer _marksRenderer;
  private GEOTileRasterizer _geoTileRasterizer;


  /**
   Creates a GEORenderer.

   defaultSymbolizer: Default Symbolizer, can be NULL.  In case of NULL, one instance of GEOSymbolizer must be passed in every call to addGEOObject();

   meshRenderer:   Can be NULL as long as no GEOMarkSymbol is used in any symbolizer.
   shapesRenderer: Can be NULL as long as no GEOShapeSymbol is used in any symbolizer.
   marksRenderer:  Can be NULL as long as no GEOMeshSymbol is used in any symbolizer.

   */
  public GEORenderer(GEOSymbolizer defaultSymbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOTileRasterizer geoTileRasterizer)
  {
     _defaultSymbolizer = defaultSymbolizer;
     _meshRenderer = meshRenderer;
     _shapesRenderer = shapesRenderer;
     _marksRenderer = marksRenderer;
     _geoTileRasterizer = geoTileRasterizer;
  }

  public void dispose()
  {
    if (_defaultSymbolizer != null)
       _defaultSymbolizer.dispose();
  
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      GEORenderer_ObjectSymbolizerPair pair = _children.get(i);
      if (pair != null)
         pair.dispose();
    }
  
    super.dispose();
  
  }

  /**
   Add a new GEOObject.

   symbolizer: The symbolizer to be used for the given geoObject.  Can be NULL as long as a defaultSymbolizer was given in the GEORenderer constructor.
   */
  public final void addGEOObject(GEOObject geoObject)
  {
     addGEOObject(geoObject, null);
  }
  public final void addGEOObject(GEOObject geoObject, GEOSymbolizer symbolizer)
  {
    if ((symbolizer == null) && (_defaultSymbolizer == null))
    {
      ILogger.instance().logError("Can't add a geoObject without a symbolizer if the defaultSymbolizer was not given in the GEORenderer constructor");
      if (geoObject != null)
         geoObject.dispose();
    }
    else
    {
      _children.add(new GEORenderer_ObjectSymbolizerPair(geoObject, symbolizer));
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

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    final int childrenCount = _children.size();
    if (childrenCount > 0)
    {
      for (int i = 0; i < childrenCount; i++)
      {
        final GEORenderer_ObjectSymbolizerPair pair = _children.get(i);
  
        if (pair._geoObject != null)
        {
          final GEOSymbolizer symbolizer = (pair._symbolizer == null) ? _defaultSymbolizer : pair._symbolizer;
  
          pair._geoObject.symbolize(rc, symbolizer, _meshRenderer, _shapesRenderer, _marksRenderer, _geoTileRasterizer);
        }
  
        if (pair != null)
           pair.dispose();
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

  public final MeshRenderer getMeshRenderer()
  {
    return _meshRenderer;
  }

  public final MarksRenderer getMarksRenderer()
  {
    return _marksRenderer;
  }

  public final ShapesRenderer getShapesRenderer()
  {
    return _shapesRenderer;
  }

  public final GEOTileRasterizer getGeoTileRasterizer()
  {
    return _geoTileRasterizer;
  }

}