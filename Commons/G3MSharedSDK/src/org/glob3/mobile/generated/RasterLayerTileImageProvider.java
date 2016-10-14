package org.glob3.mobile.generated; 
//
//  RasterLayerTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

//
//  RasterLayerTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//




//class RasterLayer;
//class IDownloader;


public class RasterLayerTileImageProvider extends TileImageProvider
{
  private RasterLayer _layer;
  private IDownloader _downloader;

  private final java.util.HashMap<String, Long> _requestsIDsPerTile = new java.util.HashMap<String, Long>();

  public void dispose()
  {
    for (java.util.Map.Entry<String, Long> entry : _requestsIDsPerTile.entrySet()) {
      _downloader.cancelRequest(entry.getValue());
    }
  
    super.dispose();
  }


  public RasterLayerTileImageProvider(RasterLayer layer, IDownloader downloader)
  {
     _layer = layer;
     _downloader = downloader;
  }

  public final TileImageContribution contribution(Tile tile)
  {
    return (_layer == null) ? null : _layer.contribution(tile);
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2S resolution, long tileTextureDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener, FrameTasksExecutor frameTasksExecutor)
  {
    final String tileID = tile._id;
  
    final long requestID = _layer.requestImage(tile, _downloader, tileTextureDownloadPriority, logDownloadActivity, new RLTIP_ImageDownloadListener(this, tileID, contribution, listener, deleteListener), true); // deleteListener
  
    if (requestID >= 0)
    {
      _requestsIDsPerTile.put(tileID, requestID);
    }
  }

  public final void cancel(String tileID)
  {
    final Long requestID = _requestsIDsPerTile.remove(tileID);
    if (requestID != null) {
      _downloader.cancelRequest(requestID);
    }
  }


  public final void requestFinish(String tileID)
  {
    _requestsIDsPerTile.remove(tileID);
  }

  public final void layerDeleted(RasterLayer layer)
  {
    if (layer != _layer)
    {
      throw new RuntimeException("Logic error");
    }
    _layer = null;
  }

}