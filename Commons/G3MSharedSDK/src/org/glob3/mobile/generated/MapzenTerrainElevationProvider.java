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

  public void dispose()
  {
    super.dispose();
  }


  public static MapzenTerrainElevationProvider createDefault()
  {
    return new MapzenTerrainElevationProvider();
  }

  public MapzenTerrainElevationProvider()
  {
  
  }

  public final RenderState getRenderState()
  {
    return RenderState.error("MapzenTerrainElevationProvider: under construction");
  }

  public final void initialize(G3MContext context)
  {
    IDownloader downloader = context.getDownloader();
  
  ///#error Diego at work!
  //  downloader->requestBuffer(<#const URL &url#>,
  //                            <#long long priority#>,
  //                            <#const TimeInterval &timeToCache#>,
  //                            <#bool readExpired#>,
  //                            <#IBufferDownloadListener *listener#>,
  //                            <#bool deleteListener#>);
  }

}