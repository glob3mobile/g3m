package org.glob3.mobile.generated; 
//
//  MapzenTerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

//
//  MapzenTerrainElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//





public class MapzenTerrainElevationProvider extends TerrainElevationProvider
{
  private final long _downloadPriority;
  private final TimeInterval _timeToCache = new TimeInterval();
  private final boolean _readExpired;

  public void dispose()
  {
    super.dispose();
  }


//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
  static MapzenTerrainElevationProvider* createDefault(long downloadPriority = DownloadPriority.HIGHER, const TimeInterval& timeToCache = TimeInterval.fromDays(30), boolean readExpired = true);

  public MapzenTerrainElevationProvider(long downloadPriority, TimeInterval timeToCache, boolean readExpired)
  {
     _downloadPriority = downloadPriority;
     _timeToCache = new TimeInterval(timeToCache);
     _readExpired = readExpired;
  
  }

  public final RenderState getRenderState()
  {
    return RenderState.error("MapzenTerrainElevationProvider: under construction");
  }

  public final void initialize(G3MContext context)
  {
    IDownloader downloader = context.getDownloader();
  
  ////#error Diego at work!
  //  downloader->requestImage(URL("http://terrain-preview.mapzen.com/terrarium/0/0/0.png"),
  //                           _downloadPriority,
  //                           _timeToCache,
  //                           _readExpired,
  //                           <#IImageDownloadListener *listener#>,
  //                           <#bool deleteListener#>);
  }

  public final void cancel()
  {
  ///#error man at work!
  }

}