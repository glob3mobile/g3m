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


  private final long _downloadPriority;
  private final TimeInterval _timeToCache;
  private final boolean _readExpired;

  private final String _instanceID;

  private G3MContext _context;

  public void dispose()
  {
    super.dispose();
  }


  public MapzenTerrainElevationProvider(long downloadPriority, TimeInterval timeToCache, boolean readExpired)
  {
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
  
    downloader.requestImage(new URL("http://terrain-preview.mapzen.com/terrarium/0/0/0.png"), _downloadPriority, _timeToCache, _readExpired, new MapzenTerrainElevationProvider_ImageDownloadListener(this), true);
  }

  public final void cancel()
  {
    _context.getDownloader().cancelRequestsTagged(_instanceID);
  }

}