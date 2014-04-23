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

  public final void create(Tile tile, Vector2I resolution, long tileDownloadPriority, TileImageListener listener, boolean deleteListener)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  
    /*
    const URL          url;
    const TimeInterval timeToCache;
    const bool         readExpired;
    const float        layerAlpha;
    const Sector&      imageSector;
    const RectangleF&  imageRectangle;
     */
  
  //  const long long requestId = _downloader->requestImage(url,
  //                                                        tileDownloadPriority,
  //                                                        timeToCache,
  //                                                        readExpired,
  //                                                        new RLTIP_ImageDownloadListener(listener,
  //                                                                                        deleteListener,
  //                                                                                        layerAlpha),
  //                                                        true /* deleteListener */);
  
    listener.imageCreationError(tile, "Not yet implemented");
    if (deleteListener)
    {
      if (listener != null)
         listener.dispose();
    }
  
  }

  public final void cancel(Tile tile)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  }

}