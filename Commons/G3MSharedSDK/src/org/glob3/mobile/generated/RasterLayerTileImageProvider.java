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
  private final RasterLayer _layer;
  private IDownloader _downloader;

  private java.util.HashMap<String, Long> _requestsIdsPerTile = new java.util.HashMap<String, Long>();


  public RasterLayerTileImageProvider(RasterLayer layer, IDownloader downloader)
  {
     _layer = layer;
     _downloader = downloader;
  }

  public final TileImageContribution contribution(Tile tile)
  {
    return _layer.contribution(tile);
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2I resolution, long tileDownloadPriority, TileImageListener listener, boolean deleteListener)
  {
    final String tileId = tile._id;
  
    final long requestId = _layer.requestImage(tile, _downloader, tileDownloadPriority, new RLTIP_ImageDownloadListener(this, tileId, contribution, listener, deleteListener), true); // deleteListener
  
    if (requestId >= 0)
    {
      _requestsIdsPerTile.put(tileId, requestId);
    }
  }

  public final void cancel(Tile tile)
  {
    final String tileId = tile._id;
    if (_requestsIdsPerTile.containsKey(tileId))
    {
      final long requestId = _requestsIdsPerTile.get(tileId);
  
      _downloader.cancelRequest(requestId);
  
      _requestsIdsPerTile.remove(tileId);
    }
  }


  public final void requestFinish(String tileId)
  {
    _requestsIdsPerTile.remove(tileId);
  }

}