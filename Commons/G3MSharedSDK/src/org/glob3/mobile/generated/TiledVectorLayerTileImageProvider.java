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


public class TiledVectorLayerTileImageProvider extends TileImageProvider
{

  private static class GEOJSONBufferDownloadListener extends IBufferDownloadListener
  {
    private TiledVectorLayerTileImageProvider _tiledVectorLayerTileImageProvider;
    private final String _tileId;
    private final TileImageContribution _contribution;
    private TileImageListener _listener;
    private final boolean _deleteListener;

    public GEOJSONBufferDownloadListener(TiledVectorLayerTileImageProvider tiledVectorLayerTileImageProvider, String tileId, TileImageContribution contribution, TileImageListener listener, boolean deleteListener)
    {
       _tiledVectorLayerTileImageProvider = tiledVectorLayerTileImageProvider;
       _tileId = tileId;
       _contribution = contribution;
       _listener = listener;
       _deleteListener = deleteListener;
    }

    public void dispose()
    {
      _tiledVectorLayerTileImageProvider.requestFinish(_tileId);
    
      if (_deleteListener)
      {
        if (_listener != null)
           _listener.dispose();
      }
    
      TileImageContribution.deleteContribution(_contribution);
    
      super.dispose();
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
    //  _listener->imageCreated(_tileId,
    //                          image,
    //                          url.getPath(),
    //                          _contribution);
    //  _contribution = NULL;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
      _listener.imageCreationError(_tileId, "NOT YET IMPLEMENTED");
    
      if (buffer != null)
         buffer.dispose();
      TileImageContribution.deleteContribution(_contribution);
      _contribution = null;
    }

    public final void onError(URL url)
    {
      _listener.imageCreationError(_tileId, "Download error - " + url.getPath());
    }

    public final void onCancel(URL url)
    {
      _listener.imageCreationCanceled(_tileId);
    }

    public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      // do nothing
    }

  }

  private final TiledVectorLayer _layer;
  private IDownloader _downloader;

  private final java.util.HashMap<String, Long> _requestsIdsPerTile = new java.util.HashMap<String, Long>();


  public TiledVectorLayerTileImageProvider(TiledVectorLayer layer, IDownloader downloader)
  {
     _layer = layer;
     _downloader = downloader;
  }


  public final TileImageContribution contribution(Tile tile)
  {
    return _layer.contribution(tile);
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2I resolution, long tileDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener, FrameTasksExecutor frameTasksExecutor)
  {
    final String tileId = tile._id;
  
    final long requestId = _layer.requestGEOJSONBuffer(tile, _downloader, tileDownloadPriority, logDownloadActivity, new GEOJSONBufferDownloadListener(this, tileId, contribution, listener, deleteListener), true); // deleteListener
  
    if (requestId >= 0)
    {
      _requestsIdsPerTile.put(tileId, requestId);
    }
  }

  public final void cancel(String tileId)
  {
    final Long requestId = _requestsIdsPerTile.remove(tileId);
    if (requestId != null) {
      _downloader.cancelRequest(requestId);
    }
  }

  public final void requestFinish(String tileId)
  {
    _requestsIdsPerTile.remove(tileId);
  }

}