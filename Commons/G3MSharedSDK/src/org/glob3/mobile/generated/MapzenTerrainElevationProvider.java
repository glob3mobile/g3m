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
  private static int _idCounter = 0;

  private final String _apiKey;

  private final long _downloadPriority;
  private final TimeInterval _timeToCache;
  private final boolean _readExpired;

  private final String _instanceID;

  private G3MContext _context;

  public void dispose()
  {
    super.dispose();
  }


  public MapzenTerrainElevationProvider(String apiKey, long downloadPriority, TimeInterval timeToCache, boolean readExpired)
  {
     _apiKey = apiKey;
     _downloadPriority = downloadPriority;
     _timeToCache = timeToCache;
     _readExpired = readExpired;
     _context = null;
     _instanceID = "MapzenTerrainElevationProvider_" + (++_idCounter);
  
  }

  public final RenderState getRenderState()
  {
    return RenderState.error("MapzenTerrainElevationProvider: under construction");
  }

  public final void initialize(G3MContext context)
  {
    _context = context;
    IDownloader downloader = context.getDownloader();
  
    // https://tile.mapzen.com/mapzen/terrain/v1/terrarium/{z}/{x}/{y}.png?api_key=mapzen-xxxxxxx
  
    downloader.requestImage(new URL("https://tile.mapzen.com/mapzen/terrain/v1/terrarium/0/0/0.png?api_key=" + _apiKey), _downloadPriority, _timeToCache, _readExpired, new MapzenTerrainElevationProvider_ImageDownloadListener(this, Sector.FULL_SPHERE, 0), true); // deltaHeight
  }

  public final void cancel()
  {
    _context.getDownloader().cancelRequestsTagged(_instanceID);
  }

}