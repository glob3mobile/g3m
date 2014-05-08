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
    public CanvasImageListener(ImageAssembler imageAssembler)
    {
       _imageAssembler = imageAssembler;
    }

    public final void imageCreated(IImage image)
    {
      _imageAssembler.imageCreated(image);
    }
  }

  private static class GEOJSONBufferParser extends GAsyncTask
  {
    private ImageAssembler _imageAssembler;
    private IByteBuffer _buffer;
    private GEOObject _geoObject;

    public GEOJSONBufferParser(ImageAssembler imageAssembler, IByteBuffer buffer)
    {
       _imageAssembler = imageAssembler;
       _buffer = buffer;
       _geoObject = null;
    }

    public void dispose()
    {
      if (_imageAssembler != null)
      {
        _imageAssembler.deletedParser();
      }
    
      if (_buffer != null)
         _buffer.dispose();
      if (_geoObject != null)
         _geoObject.dispose();
      super.dispose();
    }

    public final void runInBackground(G3MContext context)
    {
      if ((_imageAssembler != null) && (_buffer != null))
      {
        _geoObject = GEOJSONParser.parseJSON(_buffer);
      }
    }

    public final void onPostExecute(G3MContext context)
    {
      if (_imageAssembler != null)
      {
        GEOObject geoObject = _geoObject;
        _geoObject = null; // moves ownership of _geoObject to _imageAssembler
        _imageAssembler.parsedGEOObject(geoObject);
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
        _imageAssembler.bufferDownloaded(buffer);
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

    private GEOJSONBufferParser _parser;

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
       _parser = null;
       _symbolizer = null;
    }

    public void dispose()
    {
      if (_downloadListener != null)
      {
        _downloadListener.deletedImageAssembler();
      }
      if (_parser != null)
      {
        _parser.deletedImageAssembler();
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
      _downloadListener = new GEOJSONBufferDownloadListener(this);
    
      _symbolizer = layer.symbolizerCopy();
    
      _downloadRequestId = layer.requestGEOJSONBuffer(tile, _downloader, tileDownloadPriority, logDownloadActivity, _downloadListener, true); // deleteListener
    
    }

    public final void cancel()
    {
      _canceled = true;
      if (_downloadRequestId >= 0)
      {
        _downloader.cancelRequest(_downloadRequestId);
        _downloadRequestId = -1;
      }
      if (_parser != null)
      {
        _parser.cancel();
      }
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO call listener cancel
      _listener.imageCreationCanceled(_tileId);
      _tileImageProvider.requestFinish(_tileId);
    }

    public final void bufferDownloaded(IByteBuffer buffer)
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
        _parser = new GEOJSONBufferParser(this, buffer);
        _threadUtils.invokeAsyncTask(_parser, true);
      }
    }
    public final void bufferDownloadError(URL url)
    {
      _downloadListener = null;
      _downloadRequestId = -1;
    
      _listener.imageCreationError(_tileId, "Download error - " + url.getPath());
      _tileImageProvider.requestFinish(_tileId);
    }
    public final void bufferDownloadCanceled()
    {
      _downloadListener = null;
      _downloadRequestId = -1;
    }

    public final void parsedGEOObject(GEOObject geoObject)
    {
      if (geoObject == null)
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
        ICanvas canvas = IFactory.instance().createCanvas();
    
        canvas.initialize(_imageWidth, _imageHeight);
    
        final GEORasterProjection projection = new GEORasterProjection(_tileSector, _tileIsMercator, _imageWidth, _imageHeight);
        geoObject.rasterize(_symbolizer, canvas, projection, _tileLevel);
    
        if (projection != null)
           projection.dispose();
        if (geoObject != null)
           geoObject.dispose();
    
        canvas.createImage(new CanvasImageListener(this), true); // autodelete
    
        if (canvas != null)
           canvas.dispose();
    
      }
    }
    public final void deletedParser()
    {
      _parser = null;
    }

    public final void imageCreated(IImage image)
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning compose imageId
    
    //  if (_canceled) {
    //    printf("**** break point\n");
    //  }
    
      // retain the _contribution before calling the child, as the child take full ownership of the contribution
      TileImageContribution.retainContribution(_contribution);
    
      _listener.imageCreated(_tileId, image, "VectorTiles" + _tileId, _contribution);
      _tileImageProvider.requestFinish(_tileId);
    }
  }


  private final TiledVectorLayer _layer;
  private IDownloader _downloader;
  private final IThreadUtils _threadUtils;

  private final java.util.HashMap<String, ImageAssembler> _assemblers = new java.util.HashMap<String, ImageAssembler>();


  public TiledVectorLayerTileImageProvider(TiledVectorLayer layer, IDownloader downloader, IThreadUtils threadUtils)
  {
     _layer = layer;
     _downloader = downloader;
     _threadUtils = threadUtils;
  }


  public final TileImageContribution contribution(Tile tile)
  {
    return _layer.contribution(tile);
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

}