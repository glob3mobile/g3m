

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

public class GEORenderer
         extends
            LeafRenderer {

   private static class LoadQueueItem {
      public final URL          _url;
      public final TimeInterval _timeToCache;
      public GEOSymbolizer      _symbolizer;
      public final long         _priority;
      public final boolean      _readExpired;
      public final boolean      _isBSON;


      public LoadQueueItem(final URL url,
                           final GEOSymbolizer symbolizer,
                           final long priority,
                           final TimeInterval timeToCache,
                           final boolean readExpired,
                           final boolean isBSON) {
         _url = url;
         _symbolizer = symbolizer;
         _priority = priority;
         _timeToCache = timeToCache;
         _readExpired = readExpired;
         _isBSON = isBSON;

      }


      public void dispose() {
      }
   }


   private void drainLoadQueue() {
      final int loadQueueSize = _loadQueue.size();
      for (int i = 0; i < loadQueueSize; i++) {
         final LoadQueueItem item = _loadQueue.get(i);
         requestBuffer(item._url, item._symbolizer, item._priority, item._timeToCache, item._readExpired, item._isBSON);
         if (item != null) {
            item.dispose();
         }
      }

      _loadQueue.clear();
   }

   private final java.util.ArrayList<GEORenderer_ObjectSymbolizerPair> _children  = new java.util.ArrayList<GEORenderer_ObjectSymbolizerPair>();

   private final GEOSymbolizer                                         _defaultSymbolizer;

   private final MeshRenderer                                          _meshRenderer;
   private final ShapesRenderer                                        _shapesRenderer;
   private final MarksRenderer                                         _marksRenderer;
   private final GEOTileRasterizer                                     _geoTileRasterizer;

   private G3MContext                                                  _context;

   private final java.util.ArrayList<LoadQueueItem>                    _loadQueue = new java.util.ArrayList<LoadQueueItem>();


   private void requestBuffer(final URL url,
                              final GEOSymbolizer symbolizer,
                              final long priority,
                              final TimeInterval timeToCache,
                              final boolean readExpired,
                              final boolean isBSON) {
      //  ILogger::instance()->logInfo("Requesting GEOObject from \"%s\"", url.getPath().c_str());
      final IDownloader downloader = _context.getDownloader();
      downloader.requestBuffer(url, priority, timeToCache, readExpired, new GEORenderer_GEOObjectBufferDownloadListener(this,
               symbolizer, _context.getThreadUtils(), isBSON), true);
   }


   /**
    * Creates a GEORenderer.
    * 
    * defaultSymbolizer: Default Symbolizer, can be NULL. In case of NULL, one instance of GEOSymbolizer must be passed in every
    * call to addGEOObject();
    * 
    * meshRenderer: Can be NULL as long as no GEOMarkSymbol is used in any symbolizer. shapesRenderer: Can be NULL as long as no
    * GEOShapeSymbol is used in any symbolizer. marksRenderer: Can be NULL as long as no GEOMeshSymbol is used in any symbolizer.
    */
   public GEORenderer(final GEOSymbolizer defaultSymbolizer,
                      final MeshRenderer meshRenderer,
                      final ShapesRenderer shapesRenderer,
                      final MarksRenderer marksRenderer,
                      final GEOTileRasterizer geoTileRasterizer) {
      _defaultSymbolizer = defaultSymbolizer;
      _meshRenderer = meshRenderer;
      _shapesRenderer = shapesRenderer;
      _marksRenderer = marksRenderer;
      _geoTileRasterizer = geoTileRasterizer;
      _context = null;
   }


   @Override
   public void dispose() {
      if (_defaultSymbolizer != null) {
         _defaultSymbolizer.dispose();
      }

      final int childrenCount = _children.size();
      for (int i = 0; i < childrenCount; i++) {
         final GEORenderer_ObjectSymbolizerPair pair = _children.get(i);
         if (pair != null) {
            pair.dispose();
         }
      }

      super.dispose();
   }


   /**
    * Add a new GEOObject.
    * 
    * symbolizer: The symbolizer to be used for the given geoObject. Can be NULL as long as a defaultSymbolizer was given in the
    * GEORenderer constructor.
    */
   public final void addGEOObject(final GEOObject geoObject) {
      addGEOObject(geoObject, null);
   }


   public final void addGEOObject(final GEOObject geoObject,
                                  final GEOSymbolizer symbolizer) {
      if ((symbolizer == null) && (_defaultSymbolizer == null)) {
         ILogger.instance().logError(
                  "Can't add a geoObject without a symbolizer if the defaultSymbolizer was not given in the GEORenderer constructor");
         if (geoObject != null) {
            geoObject.dispose();
         }
      }
      else {
         _children.add(new GEORenderer_ObjectSymbolizerPair(geoObject, symbolizer));
      }
   }


   @Override
   public final void onResume(final G3MContext context) {

   }


   @Override
   public final void onPause(final G3MContext context) {

   }


   @Override
   public final void onDestroy(final G3MContext context) {

   }


   @Override
   public final void initialize(final G3MContext context) {
      _context = context;

      if (_context != null) {
         drainLoadQueue();
      }
   }


   @Override
   public final RenderState getRenderState(final G3MRenderContext rc) {
      return RenderState.ready();
   }


   @Override
   public final void render(final G3MRenderContext rc,
                            final GLState glState) {
      final int childrenCount = _children.size();
      if (childrenCount > 0) {
         for (int i = 0; i < childrenCount; i++) {
            final GEORenderer_ObjectSymbolizerPair pair = _children.get(i);

            if (pair._geoObject != null) {

               final GEOSymbolizer symbolizer = (pair._symbolizer == null) ? _defaultSymbolizer : pair._symbolizer;

               pair._geoObject.symbolize(rc, symbolizer, _meshRenderer, _shapesRenderer, _marksRenderer, _geoTileRasterizer);
            }

            if (pair != null) {
               pair.dispose();
            }
         }
         _children.clear();
      }
   }


   @Override
   public final boolean onTouchEvent(final G3MEventContext ec,
                                     final TouchEvent touchEvent) {
      return false;
   }


   @Override
   public final void onResizeViewportEvent(final G3MEventContext ec,
                                           final int width,
                                           final int height) {

   }


   @Override
   public final void start(final G3MRenderContext rc) {

   }


   @Override
   public final void stop(final G3MRenderContext rc) {

   }


   public final MeshRenderer getMeshRenderer() {
      return _meshRenderer;
   }


   public final MarksRenderer getMarksRenderer() {
      return _marksRenderer;
   }


   public final ShapesRenderer getShapesRenderer() {
      return _shapesRenderer;
   }


   public final GEOTileRasterizer getGeoTileRasterizer() {
      return _geoTileRasterizer;
   }


   public final void loadJSON(final URL url) {
      loadJSON(url, null, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
   }


   public final void loadJSON(final URL url,
                              final GEOSymbolizer symbolizer) {
      loadJSON(url, symbolizer, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
   }


   public final void loadJSON(final URL url,
                              final GEOSymbolizer symbolizer,
                              final long priority,
                              final TimeInterval timeToCache,
                              final boolean readExpired) {
      if (_context == null) {
         _loadQueue.add(new LoadQueueItem(url, symbolizer, priority, timeToCache, readExpired, false)); // isBson
      }
      else {
         requestBuffer(url, symbolizer, priority, timeToCache, readExpired, false); // isBson
      }
   }


   public final void loadBSON(final URL url) {
      loadBSON(url, null, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
   }


   public final void loadBSON(final URL url,
                              final GEOSymbolizer symbolizer) {
      loadBSON(url, symbolizer, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
   }


   public final void loadBSON(final URL url,
                              final GEOSymbolizer symbolizer,
                              final long priority,
                              final TimeInterval timeToCache,
                              final boolean readExpired) {
      if (_context == null) {
         _loadQueue.add(new LoadQueueItem(url, symbolizer, priority, timeToCache, readExpired, true)); // isBson
      }
      else {
         requestBuffer(url, symbolizer, priority, timeToCache, readExpired, true); // isBson
      }
   }


   @Override
   public final void setEnable(final boolean enable) {
      super.setEnable(enable);

      if (_meshRenderer != null) {
         _meshRenderer.setEnable(enable);
      }
      if (_shapesRenderer != null) {
         _shapesRenderer.setEnable(enable);
      }
      if (_marksRenderer != null) {
         _marksRenderer.setEnable(enable);
      }
      if (_geoTileRasterizer != null) {
         _geoTileRasterizer.setEnable(enable);
      }
   }
}
