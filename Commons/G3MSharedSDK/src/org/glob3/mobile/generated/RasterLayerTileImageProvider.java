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
  
  ////const URL& url
  ////const TimeInterval& timeToCache
  ////bool readExpired
  //
  //  const long long requestId = _downloader->requestImage(const URL& url,
  //                                                        tileDownloadPriority,
  //                                                        const TimeInterval& timeToCache,
  //                                                        bool readExpired,
  //                                                        new RasterLayerTileImageProvider_IImageDownloadListener(),
  //                                                        true /* deleteListener */);
  //
  ////  aa;
  
    std.set<String> errors = new std.set<String>();
    errors.insert("Not yet implemented");
  
    listener.imageCreationError(tile, errors);
    if (deleteListener)
    {
      if (listener != null)
         listener.dispose();
    }
  }

  public final void cancel(Tile tile)
  {
  //  aa;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  }

}