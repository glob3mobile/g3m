package org.glob3.mobile.generated; 
//
//  TiledVectorLayerTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

//
//  TiledVectorLayerTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//



//class TiledVectorLayer;
//class IDownloader;
//class GEOObject;
//class GEORasterSymbolizer;

public class TiledVectorLayerTileImageProvider extends TileImageProvider
{

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class ImageAssembler;

  private static class CanvasImageListener extends IImageListener
  {
    private ImageAssembler _imageAssembler;
    private final String _imageId;
    public CanvasImageListener(ImageAssembler imageAssembler, String imageId)
    {
       _imageAssembler = imageAssembler;
       _imageId = imageId;
    }

    public final void imageCreated(IImage image)
    {
      _imageAssembler.imageCreated(image, _imageId);
    }
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class GEOObjectHolder;

  private static class GEOJSONBufferRasterizer extends GAsyncTask
  {
    private final URL _url;
    private ImageAssembler _imageAssembler;
    private IByteBuffer _buffer;
    private final GEOObjectHolder _geoObjectHolder;
    private GEOObject _geoObject;
    private final boolean _geoObjectFromCache;
    private ICanvas _canvas;
    private final int _imageWidth;
    private final int _imageHeight;

    private GEORasterSymbolizer _symbolizer;
    private final String _tileId;
    private final Sector _tileSector ;
    private final boolean _tileIsMercator;
    private final int _tileLevel;

    private void rasterizeGEOObject(GEOObject geoObject)
    {
      final long coordinatesCount = geoObject.getCoordinatesCount();
      if (coordinatesCount > 5000)
      {
        ILogger.instance().logWarning("GEOObject for tile=\"%s\" has with too many vertices=%d", _tileId, coordinatesCount);
      }
    
      final GEORasterProjection projection = new GEORasterProjection(_tileSector, _tileIsMercator, _imageWidth, _imageHeight);
      geoObject.rasterize(_symbolizer, _canvas, projection, _tileLevel);
    
      if (projection != null)
         projection.dispose();
    }


    public GEOJSONBufferRasterizer(ImageAssembler imageAssembler, URL url, IByteBuffer buffer, GEOObjectHolder geoObjectHolder, int imageWidth, int imageHeight, GEORasterSymbolizer symbolizer, String tileId, Sector tileSector, boolean tileIsMercator, int tileLevel) // GEOObjectHolder, never both -  buffer or
    {
       _imageAssembler = imageAssembler;
       _url = url;
       _buffer = buffer;
       _geoObjectHolder = geoObjectHolder;
       _geoObjectFromCache = geoObjectHolder != null;
       _imageWidth = imageWidth;
       _imageHeight = imageHeight;
       _symbolizer = symbolizer;
       _tileId = tileId;
       _tileSector = new Sector(tileSector);
       _tileIsMercator = tileIsMercator;
       _tileLevel = tileLevel;
       _canvas = null;
       _geoObject = null;
    }

    public void dispose()
    {
      if (_imageAssembler != null)
      {
        _imageAssembler.deletedRasterizer();
      }
    
      _symbolizer = null;
      if (_buffer != null)
         _buffer.dispose();
      if (_geoObject != null)
         _geoObject.dispose();
      if (_geoObjectHolder != null)
      {
        _geoObjectHolder._release();
      }
      if (_canvas != null)
         _canvas.dispose();
    
      super.dispose();
    }

    public final void runInBackground(G3MContext context)
    {
      if (_imageAssembler != null)
      {
        _canvas = IFactory.instance().createCanvas();
        _canvas.initialize(_imageWidth, _imageHeight);
    
        if (_geoObjectHolder != null)
        {
          rasterizeGEOObject(_geoObjectHolder._geoObject);
        }
        else if (_buffer != null)
        {
          if (_buffer.size() > 0)
          {
            final boolean isBSON = IStringUtils.instance().endsWith(_url._path, "bson");
            final boolean showStatistics = false;
            _geoObject = (isBSON ? GEOJSONParser.parseBSON(_buffer, showStatistics) : GEOJSONParser.parseJSON(_buffer, showStatistics));
            if (_geoObject != null)
            {
              rasterizeGEOObject(_geoObject);
            }
          }
        }
      }
    
    //  if ((_imageAssembler != NULL) && (_buffer != NULL)) {
    //    _canvas = IFactory::instance()->createCanvas();
    //    _canvas->initialize(_imageWidth, _imageHeight);
    //
    //    if (_buffer->size() > 0) {
    //      const bool isBSON = IStringUtils::instance()->endsWith(_url._path, "bson");
    //
    //      if (_geoObject2 == NULL) {
    //        const bool showStatistics = false;
    //        _geoObject = (isBSON
    //                      ? GEOJSONParser::parseBSON(_buffer, showStatistics)
    //                      : GEOJSONParser::parseJSON(_buffer, showStatistics));
    //      }
    //
    //      if (_geoObject != NULL) {
    //        const long long coordinatesCount = _geoObject->getCoordinatesCount();
    //        if (coordinatesCount > 5000) {
    //          ILogger::instance()->logWarning("GEOObject for tile=\"%s\" has with too many vertices=%d",
    //                                          _tileId.c_str(),
    //                                          coordinatesCount
    //                                          );
    //        }
    //
    //        if (_imageAssembler != NULL) {
    //          const GEORasterProjection* projection = new GEORasterProjection(_tileSector,
    //                                                                          _tileIsMercator,
    //                                                                          _imageWidth,
    //                                                                          _imageHeight);
    //          _geoObject->rasterize(_symbolizer,
    //                                _canvas,
    //                                projection,
    //                                _tileLevel);
    //
    //          delete projection;
    //        }
    //        // delete geoObject;
    //      }
    //    }
    //  }
    }

    public final void onPostExecute(G3MContext context)
    {
      if (_imageAssembler != null)
      {
        ICanvas canvas = _canvas;
        _canvas = null; // moves ownership of _canvas to _imageAssembler
    
        GEOObject transferedGEOObject;
        if (_geoObjectFromCache)
        {
          // This _geoObject had come from the cache, no need to transfer to put into the cache again
          // the _geoObject will be removed in the destructor
          transferedGEOObject = null;
        }
        else
        {
          transferedGEOObject = _geoObject;
          _geoObject = null; // moves ownership of _geoObject to _imageAssembler
        }
    
        _imageAssembler.rasterizedGEOObject(_url, transferedGEOObject, canvas);
      }
    }

    public final void cancel()
    {
      _imageAssembler = null;
    }

    public final void deletedImageAssembler()
    {
      _imageAssembler = null;
    }

  }


  private static class GEOJSONBufferDownloadListener extends IBufferDownloadListener
  {
    private ImageAssembler _imageAssembler;

    public GEOJSONBufferDownloadListener(ImageAssembler imageAssembler)
    {
       _imageAssembler = imageAssembler;
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      if (_imageAssembler == null)
      {
        if (buffer != null)
           buffer.dispose();
      }
      else
      {
        _imageAssembler.bufferDownloaded(url, buffer);
      }
    }

    public final void onError(URL url)
    {
      if (_imageAssembler != null)
      {
        _imageAssembler.bufferDownloadError(url);
      }
    }

    public final void onCancel(URL url)
    {
      if (_imageAssembler != null)
      {
        _imageAssembler.bufferDownloadCanceled();
      }
    }

    public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      // do nothing
    }

    public final void deletedImageAssembler()
    {
      _imageAssembler = null;
    }

  }


  private static class ImageAssembler
  {
    private TiledVectorLayerTileImageProvider _tileImageProvider;
    private final String _tileId;
    private TileImageListener _listener;
    private final boolean _deleteListener;
    private IDownloader _downloader;
    private final IThreadUtils _threadUtils;
    private final int _imageWidth;
    private final int _imageHeight;

    private TileImageContribution _contribution;


    private boolean _canceled;

    private GEOJSONBufferDownloadListener _downloadListener;
    private long _downloadRequestId;

    private GEOJSONBufferRasterizer _rasterizer;

    private GEORasterSymbolizer _symbolizer;

    private final Sector _tileSector ;
    private final boolean _tileIsMercator;
    private final int _tileLevel;

    public ImageAssembler(TiledVectorLayerTileImageProvider tileImageProvider, Tile tile, TileImageContribution contribution, TileImageListener listener, boolean deleteListener, Vector2I imageResolution, IDownloader downloader, IThreadUtils threadUtils)
    {
       _tileImageProvider = tileImageProvider;
       _tileId = tile._id;
       _tileSector = new Sector(tile._sector);
       _tileIsMercator = tile._mercator;
       _tileLevel = tile._level;
       _contribution = contribution;
       _listener = listener;
       _deleteListener = deleteListener;
       _imageWidth = imageResolution._x;
       _imageHeight = imageResolution._y;
       _downloader = downloader;
       _threadUtils = threadUtils;
       _canceled = false;
       _downloadListener = null;
       _downloadRequestId = -1;
       _rasterizer = null;
       _symbolizer = null;
    }

    public void dispose()
    {
      if (_downloadListener != null)
      {
        _downloadListener.deletedImageAssembler();
      }
      if (_rasterizer != null)
      {
        _rasterizer.deletedImageAssembler();
      }
      if (_deleteListener)
      {
        if (_listener != null)
           _listener.dispose();
      }
      TileImageContribution.releaseContribution(_contribution);
      _symbolizer = null;
    }

    public final void start(TiledVectorLayer layer, Tile tile, long tileDownloadPriority, boolean logDownloadActivity)
    {
    //  _downloadListener = new GEOJSONBufferDownloadListener(this);
    //
    //  _symbolizer = layer->symbolizerCopy();
    
    //  _downloadRequestId = layer->requestGEOJSONBuffer(tile,
    //                                                   _downloader,
    //                                                   tileDownloadPriority,
    //                                                   logDownloadActivity,
    //                                                   _downloadListener,
    //                                                   true /* deleteListener */);
    
    
      TiledVectorLayer.RequestGEOJSONBufferData requestData = layer.getRequestGEOJSONBufferData(tile);
    
    //  GEOObject* geoObject = _tileImageProvider->getGEOObjectFor(requestData->_url);
      final GEOObjectHolder geoObjectHolder = _tileImageProvider.getGEOObjectFor(requestData._url);
      if (geoObjectHolder == null)
      {
        _symbolizer = layer.symbolizerCopy();
        _downloadListener = new GEOJSONBufferDownloadListener(this);
    
        if (logDownloadActivity)
        {
          ILogger.instance().logInfo("Downloading %s", requestData._url._path);
        }
        _downloadRequestId = _downloader.requestBuffer(requestData._url, tileDownloadPriority, requestData._timeToCache, requestData._readExpired, _downloadListener, true); // deleteListener
      }
      else
      {
    //    geoObjectDownloaded(geoObject,
    //                        _symbolizer);
    //    aa
    
        final GEORasterSymbolizer symbolizer = layer.symbolizerCopy();
    
        _rasterizer = new GEOJSONBufferRasterizer(this, requestData._url, null, geoObjectHolder, _imageWidth, _imageHeight, symbolizer, _tileId, _tileSector, _tileIsMercator, _tileLevel); // buffer,
        _threadUtils.invokeAsyncTask(_rasterizer, true);
      }
    
      requestData = null;
    }

    public final void cancel()
    {
      _canceled = true;
      if (_downloadRequestId >= 0)
      {
        _downloader.cancelRequest(_downloadRequestId);
        _downloadRequestId = -1;
      }
      if (_rasterizer != null)
      {
        _rasterizer.cancel();
      }
    
      _listener.imageCreationCanceled(_tileId);
      _tileImageProvider.requestFinish(_tileId);
    }

    public final void bufferDownloaded(URL url, IByteBuffer buffer)
    {
      _downloadListener = null;
      _downloadRequestId = -1;
    
      if (_canceled)
      {
        if (buffer != null)
           buffer.dispose();
      }
      else
      {
        final GEORasterSymbolizer symbolizer = _symbolizer;
        _symbolizer = null; // moves ownership of _symbolizer to GEOJSONBufferRasterizer
    
        _rasterizer = new GEOJSONBufferRasterizer(this, url, buffer, null, _imageWidth, _imageHeight, symbolizer, _tileId, _tileSector, _tileIsMercator, _tileLevel); // geoObject
        _threadUtils.invokeAsyncTask(_rasterizer, true);
      }
    }
    public final void bufferDownloadError(URL url)
    {
      _downloadListener = null;
      _downloadRequestId = -1;
    
      _listener.imageCreationError(_tileId, "Download error - " + url._path);
      _tileImageProvider.requestFinish(_tileId);
    }
    public final void bufferDownloadCanceled()
    {
      _downloadListener = null;
      _downloadRequestId = -1;
    }

    public final void rasterizedGEOObject(URL url, GEOObject geoObject, ICanvas canvas)
    {
    
      if (geoObject != null)
      {
        _tileImageProvider.takeGEOObjectFor(url, geoObject);
      }
    
      if (canvas == null)
      {
        _listener.imageCreationError(_tileId, "GEOJSON parser error");
        if (_deleteListener)
        {
          if (_listener != null)
             _listener.dispose();
        }
      }
      else
      {
        canvas.createImage(new CanvasImageListener(this, url._path), true); // autodelete
    
        if (canvas != null)
           canvas.dispose();
      }
    }
    public final void deletedRasterizer()
    {
      _rasterizer = null;
    }

    public final void imageCreated(IImage image, String imageId)
    {
      // retain the _contribution before calling the listener, as it takes full ownership of the contribution
      TileImageContribution.retainContribution(_contribution);
    
      _listener.imageCreated(_tileId, image, imageId, _contribution);
      _tileImageProvider.requestFinish(_tileId);
    }

  }


  private TiledVectorLayer _layer;
  private IDownloader _downloader;
  private final IThreadUtils _threadUtils;

  private final java.util.HashMap<String, ImageAssembler> _assemblers = new java.util.HashMap<String, ImageAssembler>();

  private static class GEOObjectHolder extends RCObject
  {
    public final GEOObject _geoObject;

    public GEOObjectHolder(GEOObject geoObject)
    {
       _geoObject = geoObject;
    }

    public void dispose()
    {
      if (_geoObject != null)
         _geoObject.dispose();
    }

  }

  private static class CacheEntry
  {
    public final String _path;
    public final GEOObjectHolder _geoObjectHolder;

    public CacheEntry(String path, GEOObject geoObject)
    {
       _path = path;
       _geoObjectHolder = new GEOObjectHolder(geoObject);
    }

    public void dispose()
    {
      _geoObjectHolder._release();
    }
  }

  private java.util.LinkedList<CacheEntry> _geoObjectsCache = new java.util.LinkedList<CacheEntry>();

  public void dispose()
  {
  
    for (java.util.Iterator<CacheEntry> it = _geoObjectsCache.iterator(); it.hasNext();)
    {
      CacheEntry entry = it.next();
      if (entry != null)
         entry.dispose();
    }
  
    super.dispose();
  }


  public TiledVectorLayerTileImageProvider(TiledVectorLayer layer, IDownloader downloader, IThreadUtils threadUtils)
  {
     _layer = layer;
     _downloader = downloader;
     _threadUtils = threadUtils;
  }

  public final void layerDeleted(TiledVectorLayer layer)
  {
    if (layer != _layer)
    {
      throw new RuntimeException("Logic error");
    }
    _layer = null;
  }

  public final TileImageContribution contribution(Tile tile)
  {
    return (_layer == null) ? null : _layer.contribution(tile);
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2I resolution, long tileDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener, FrameTasksExecutor frameTasksExecutor)
  {
  
    ImageAssembler assembler = new ImageAssembler(this, tile, contribution, listener, deleteListener, resolution, _downloader, _threadUtils);
  
    _assemblers.put(tile._id, assembler);
  
    assembler.start(_layer, tile, tileDownloadPriority, logDownloadActivity);
  }

  public final void cancel(String tileId)
  {
    final ImageAssembler assembler = _assemblers.get(tileId);
    if (assembler != null) {
      assembler.cancel();
    }
  }

  public final void requestFinish(String tileId)
  {
    final ImageAssembler assembler = _assemblers.remove(tileId);
    if (assembler != null) {
      assembler.dispose();
    }
  }

  public final TiledVectorLayerTileImageProvider.GEOObjectHolder getGEOObjectFor(URL url)
  {
  //  _geoObjectsCacheRequests++;
    final String path = url._path;
    for (java.util.Iterator<CacheEntry> it = _geoObjectsCache.iterator(); it.hasNext();)
    {
      CacheEntry entry = it.next();
      if (entry._path.equals(path))
      {
  //      _geoObjectsCacheHits++;
  
        // move hit entry to the top of the cache (LRU rules)
        it.remove();
        _geoObjectsCache.addFirst(entry);
  
        final GEOObjectHolder geoObjectHolder = entry._geoObjectHolder;
        geoObjectHolder._retain();
        return geoObjectHolder;
      }
    }
    return null;
  }
  public final void takeGEOObjectFor(URL url, GEOObject geoObject)
  {
    if (_geoObjectsCache.size() > 48)
    {
      CacheEntry lastEntry = _geoObjectsCache.getLast();
      _geoObjectsCache.removeLast();
      if (lastEntry != null)
         lastEntry.dispose();
    }
    _geoObjectsCache.addFirst(new CacheEntry(url._path, geoObject));
  }

}