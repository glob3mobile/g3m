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
//class GEOTileRasterizer;
//class GEORenderer_ObjectSymbolizerPair;

public class GEORenderer extends LeafRenderer
{

  private static class LoadQueueItem
  {
    public final URL _url;
    public final TimeInterval _timeToCache;
    public GEOSymbolizer _symbolizer;
    public final long _priority;
    public final boolean _readExpired;
    public final boolean _isBSON;

    public LoadQueueItem(URL url, GEOSymbolizer symbolizer, long priority, TimeInterval timeToCache, boolean readExpired, boolean isBSON)
    {
       _url = url;
       _symbolizer = symbolizer;
       _priority = priority;
       _timeToCache = timeToCache;
       _readExpired = readExpired;
       _isBSON = isBSON;

    }

    public void dispose()
    {
    }
  }

  private void drainLoadQueue()
  {
    final int loadQueueSize = _loadQueue.size();
    for (int i = 0; i < loadQueueSize; i++)
    {
      LoadQueueItem item = _loadQueue.get(i);
      requestBuffer(item._url, item._symbolizer, item._priority, item._timeToCache, item._readExpired, item._isBSON);
      if (item != null)
         item.dispose();
    }
  
    _loadQueue.clear();
  }

  private java.util.ArrayList<GEORenderer_ObjectSymbolizerPair> _children = new java.util.ArrayList<GEORenderer_ObjectSymbolizerPair>();

  private final GEOSymbolizer _defaultSymbolizer;

  private MeshRenderer _meshRenderer;
  private ShapesRenderer _shapesRenderer;
  private MarksRenderer _marksRenderer;
  private GEOTileRasterizer _geoTileRasterizer;

  private G3MContext _context;

  private java.util.ArrayList<LoadQueueItem> _loadQueue = new java.util.ArrayList<LoadQueueItem>();

  private void requestBuffer(URL url, GEOSymbolizer symbolizer, long priority, TimeInterval timeToCache, boolean readExpired, boolean isBSON)
  {
  //  ILogger::instance()->logInfo("Requesting GEOObject from \"%s\"", url.getPath().c_str());
    IDownloader downloader = _context.getDownloader();
    downloader.requestBuffer(url, priority, timeToCache, readExpired, new GEORenderer_GEOObjectBufferDownloadListener(this, symbolizer, _context.getThreadUtils(), isBSON), true);
  }


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
     _context = null;
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
    _context = context;
  
    if (_context != null)
    {
      drainLoadQueue();
    }
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    return RenderState.ready();
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

  public final void loadJSON(URL url)
  {
    loadJSON(url, null, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
  }

  public final void loadJSON(URL url, GEOSymbolizer symbolizer)
  {
    loadJSON(url, symbolizer, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
  }

  public final void loadJSON(URL url, GEOSymbolizer symbolizer, long priority, TimeInterval timeToCache, boolean readExpired)
  {
    if (_context == null)
    {
      _loadQueue.add(new LoadQueueItem(url, symbolizer, priority, timeToCache, readExpired, false)); // isBson
    }
    else
    {
      requestBuffer(url, symbolizer, priority, timeToCache, readExpired, false); // isBson
    }
  }

  public final void loadBSON(URL url)
  {
    loadBSON(url, null, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
  }

  public final void loadBSON(URL url, GEOSymbolizer symbolizer)
  {
    loadBSON(url, symbolizer, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
  }

  public final void loadBSON(URL url, GEOSymbolizer symbolizer, long priority, TimeInterval timeToCache, boolean readExpired)
  {
    if (_context == null)
    {
      _loadQueue.add(new LoadQueueItem(url, symbolizer, priority, timeToCache, readExpired, true)); // isBson
    }
    else
    {
      requestBuffer(url, symbolizer, priority, timeToCache, readExpired, true); // isBson
    }
  }

  public final void setEnable(boolean enable)
  {
    super.setEnable(enable);
  
    if (_meshRenderer != null)
    {
      _meshRenderer.setEnable(enable);
    }
    if (_shapesRenderer != null)
    {
      _shapesRenderer.setEnable(enable);
    }
    if (_marksRenderer != null)
    {
      _marksRenderer.setEnable(enable);
    }
    if (_geoTileRasterizer != null)
    {
      _geoTileRasterizer.setEnable(enable);
    }
  }

  public final void zRender(G3MRenderContext rc, GLState glState)
  {
  }

}