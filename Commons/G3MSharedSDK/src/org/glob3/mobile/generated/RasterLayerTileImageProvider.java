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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  
    final String tileId = tile._id;
  
    final long requestId = _layer.requestImage(tile, _downloader, tileDownloadPriority, new RLTIP_ImageDownloadListener(tileId, contribution, listener, deleteListener), true); // deleteListener
  
  
  //  aa;
  
  //  listener->imageCreationError(tile->_id, "Not yet implemented");
  //  if (deleteListener) {
  //    delete listener;
  //  }
  
  }

  public final void cancel(Tile tile)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  }

}