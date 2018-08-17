package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TiledVectorLayer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEORasterSymbolizer;

public class TiledVectorLayerTileImageProvider extends TileImageProvider
{

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class ImageAssembler;

  private static class CanvasImageListener implements IImageListener
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final URL _url = new URL();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final URL _url = new internal();
//#endif
	private ImageAssembler _imageAssembler;
	private IByteBuffer _buffer;
	private final GEOObjectHolder _geoObjectHolder;
	private GEOObject _geoObject;
	private final boolean _geoObjectFromCache;
	private ICanvas _canvas;
	private final int _imageWidth;
	private final int _imageHeight;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final GEORasterSymbolizer _symbolizer;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public GEORasterSymbolizer _symbolizer = new internal();
//#endif
	private final String _tileId;
	private final Sector _tileSector = new Sector();
	private final boolean _tileIsMercator;
	private final int _tileLevel;

	private void rasterizeGEOObject(GEOObject geoObject)
	{
	  final long coordinatesCount = geoObject.getCoordinatesCount();
	  if (coordinatesCount > 5000)
	  {
		ILogger.instance().logWarning("GEOObject for tile=\"%s\" has with too many vertices=%d", _tileId.c_str(), coordinatesCount);
	  }
    
	  final GEORasterProjection projection = new GEORasterProjection(_tileSector, _tileIsMercator, _imageWidth, _imageHeight);
	  geoObject.rasterize(_symbolizer, _canvas, projection, _tileLevel);
    
	  if (projection != null)
		  projection.dispose();
	}


	public GEOJSONBufferRasterizer(ImageAssembler imageAssembler, URL url, IByteBuffer buffer, GEOObjectHolder geoObjectHolder, int imageWidth, int imageHeight, GEORasterSymbolizer symbolizer, String tileId, Sector tileSector, boolean tileIsMercator, int tileLevel) // GEOObjectHolder, never both -  buffer or
	{
		_imageAssembler = imageAssembler;
		_url = new URL(url);
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
	  if (_symbolizer != null)
		  _symbolizer.dispose();
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
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  super.dispose();
//#endif
	}

	public final void runInBackground(G3MContext context)
	{
	  if (_imageAssembler != null)
	  {
		_canvas = IFactory.instance().createCanvas(false);
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
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _imageAssembler->rasterizedGEOObject(_url, transferedGEOObject, canvas);
		_imageAssembler.rasterizedGEOObject(new URL(_url), transferedGEOObject, canvas);
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


  private static class GEOJSONBufferDownloadListener implements IBufferDownloadListener
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _imageAssembler->bufferDownloaded(url, buffer);
		_imageAssembler.bufferDownloaded(new URL(url), buffer);
	  }
	}

	public final void onError(URL url)
	{
	  if (_imageAssembler != null)
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _imageAssembler->bufferDownloadError(url);
		_imageAssembler.bufferDownloadError(new URL(url));
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

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final TileImageContribution _contribution;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public TileImageContribution _contribution = new internal();
//#endif


	private boolean _canceled;

	private GEOJSONBufferDownloadListener _downloadListener;
	private long _downloadRequestId;

	private GEOJSONBufferRasterizer _rasterizer;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final GEORasterSymbolizer _symbolizer;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public GEORasterSymbolizer _symbolizer = new internal();
//#endif

	private final Sector _tileSector = new Sector();
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
	  if (_symbolizer != null)
		  _symbolizer.dispose();
	}

	public final void start(TiledVectorLayer layer, Tile tile, long tileDownloadPriority, boolean logDownloadActivity)
	{
    
	  TiledVectorLayer.RequestGEOJSONBufferData requestData = layer.getRequestGEOJSONBufferData(tile);
    
	  final GEOObjectHolder geoObjectHolder = _tileImageProvider.getGEOObjectFor(requestData._url);
	  if (geoObjectHolder == null)
	  {
		_symbolizer = layer.symbolizerCopy();
		_downloadListener = new GEOJSONBufferDownloadListener(this);
    
		if (logDownloadActivity)
		{
		  ILogger.instance().logInfo("Downloading %s", requestData._url._path.c_str());
		}
		_downloadRequestId = _downloader.requestBuffer(requestData._url, tileDownloadPriority, requestData._timeToCache, requestData._readExpired, _downloadListener, true); // deleteListener
	  }
	  else
	  {
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _tileImageProvider->takeGEOObjectFor(url, geoObject);
		_tileImageProvider.takeGEOObjectFor(new URL(url), geoObject);
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


//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final TiledVectorLayer _layer;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public TiledVectorLayer _layer = new internal();
//#endif
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
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
	  THROW_EXCEPTION("Logic error");
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	if (_assemblers.containsKey(tileId))
	{
	  ImageAssembler assembler = _assemblers.get(tileId);
  
	  assembler.cancel();
	}
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final ImageAssembler assembler = _assemblers.get(tileId);
	if (assembler != null)
	{
	  assembler.cancel();
	}
//#endif
  }

  public final void requestFinish(String tileId)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	if (_assemblers.containsKey(tileId))
	{
	  ImageAssembler assembler = _assemblers.get(tileId);
  
	  _assemblers.remove(tileId);
  
	  if (assembler != null)
		  assembler.dispose();
	}
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final ImageAssembler assembler = _assemblers.remove(tileId);
	if (assembler != null)
	{
	  assembler.dispose();
	}
//#endif
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL list 'erase' method in Java:
		it = _geoObjectsCache.erase(it);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		it.remove();
//#endif
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
