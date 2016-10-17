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




//class FloatBufferTerrainElevationGrid;


public class MapzenTerrainElevationProvider extends TerrainElevationProvider
{
  private static int _idCounter = 0;

  private final String _apiKey;

  private final long _downloadPriority;
  private final TimeInterval _timeToCache;
  private final boolean _readExpired;

  private final String _instanceID;

  private G3MContext _context;

  private FloatBufferTerrainElevationGrid _rootGrid;
  private boolean _errorDownloadingRoot;

  public void dispose()
  {
    if (_rootGrid != null)
    {
      _rootGrid._release();
    }
    super.dispose();
  }


  public MapzenTerrainElevationProvider(String apiKey, long downloadPriority, TimeInterval timeToCache, boolean readExpired)
  {
     _apiKey = apiKey;
     _downloadPriority = downloadPriority;
     _timeToCache = timeToCache;
     _readExpired = readExpired;
     _context = null;
     _instanceID = "MapzenTerrainElevationProvider_" + IStringUtils.instance().toString(++_idCounter);
     _rootGrid = null;
     _errorDownloadingRoot = false;
  
  }

  public final RenderState getRenderState()
  {
    if (_errorDownloadingRoot)
    {
      return RenderState.error("Error downloading Mapzen root grid");
    }
    return (_rootGrid == null) ? RenderState.busy() : RenderState.ready();
  }

  public final void initialize(G3MContext context)
  {
    _context = context;
    IDownloader downloader = context.getDownloader();
  
    // https://tile.mapzen.com/mapzen/terrain/v1/terrarium/{z}/{x}/{y}.png?api_key=mapzen-xxxxxxx
  
    // MapzenTerrariumParser
  
    downloader.requestImage(new URL("https://tile.mapzen.com/mapzen/terrain/v1/terrarium/0/0/0.png?api_key=" + _apiKey), _downloadPriority, _timeToCache, _readExpired, new MapzenTerrainElevationProvider_ImageDownloadListener(this, 0, 0, 0, Sector.FULL_SPHERE, 0), true); // deltaHeight -  y -  x -  z
  }

  public final void cancel()
  {
    _context.getDownloader().cancelRequestsTagged(_instanceID);
  }

  public final void onGrid(int z, int x, int y, FloatBufferTerrainElevationGrid grid)
  {
    if ((z == 0) && (x == 0) && (y == 0))
    {
      if (_rootGrid != null)
      {
        _rootGrid._release();
      }
      _rootGrid = grid;
    }
    else
    {
      throw new RuntimeException("Not yet done");
    }
  }

  public final void onDownloadError(int z, int x, int y)
  {
    ILogger.instance().logError("Error downloading Mapzen terrarium at %i/%i/%i", z, x, y);
    if ((z == 0) && (x == 0) && (y == 0))
    {
      _errorDownloadingRoot = true;
    }
  }

}