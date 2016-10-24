package org.glob3.mobile.generated; 
//
//  MapzenDEMProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

//
//  MapzenDEMProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//




//class FloatBufferDEMGrid;
//class Sector;


public class MapzenDEMProvider extends MercatorPyramidDEMProvider
{
  private static int _instanceCounter = 0;

  private final String _apiKey;

  private final long _downloadPriority;
  private final TimeInterval _timeToCache;
  private final boolean _readExpired;


  private final String _instanceID;

  private G3MContext _context;


  private boolean _rootGridDownloaded;
  private boolean _errorDownloadingRootGrid;

  private void requestTile(int z, int x, int y, Sector sector)
  {
    IDownloader downloader = _context.getDownloader();
  
    final IStringUtils su = IStringUtils.instance();
    final String path = "https://tile.mapzen.com/mapzen/terrain/v1/terrarium/" + su.toString(z) + "/" + su.toString(x) + "/" + su.toString(y) + ".png?api_key=" + _apiKey;
  
    downloader.requestImage(new URL(path), _downloadPriority, _timeToCache, _readExpired, new MapzenDEMProvider_ImageDownloadListener(_context, this, z, x, y, sector, _deltaHeight), true);
  }

  public void dispose()
  {
    super.dispose();
  }


  public MapzenDEMProvider(String apiKey, long downloadPriority, TimeInterval timeToCache, boolean readExpired, double deltaHeight)
  {
     super(deltaHeight, new Vector2S((short)256, (short)256));
     _apiKey = apiKey;
     _downloadPriority = downloadPriority;
     _timeToCache = timeToCache;
     _readExpired = readExpired;
     _context = null;
     _instanceID = "MapzenDEMProvider_" + IStringUtils.instance().toString(++_instanceCounter);
     _rootGridDownloaded = false;
     _errorDownloadingRootGrid = false;
  
  }

  public final RenderState getRenderState()
  {
    if (_errorDownloadingRootGrid)
    {
      return RenderState.error("Error downloading Mapzen root grid");
    }
    return (!_rootGridDownloaded) ? RenderState.busy() : RenderState.ready();
  }

  public final void initialize(G3MContext context)
  {
    _context = context;
  
    // request root grid
    requestTile(0, 0, 0, Sector.FULL_SPHERE); // y -  x -  z
  
    ////    const int z = 9;
    ////    const int x = 271;
    ////    const int y = 180;
    //  const int z = 1;
    //  const int x = 0;
    //  const int y = 0;
    //
    //  const Sector sector = MercatorUtils::getSector(z, x, y);
    //  ILogger::instance()->logInfo( sector.description() );
    //  requestTile(z, x, y,
    //              sector);
  }

  public final void cancel()
  {
    _context.getDownloader().cancelRequestsTagged(_instanceID);
  }

  public final void onGrid(int z, int x, int y, FloatBufferDEMGrid grid)
  {
    boolean stickyGrid = false;
  
    if ((z == 0) && (x == 0) && (y == 0))
    {
      _rootGridDownloaded = true;
      stickyGrid = true;
    }
  
    insertGrid(z, x, y, grid, stickyGrid);
  }

  public final void onDownloadError(int z, int x, int y)
  {
    ILogger.instance().logError("Error downloading Mapzen terrarium at %i/%i/%i", z, x, y);
    if ((z == 0) && (x == 0) && (y == 0))
    {
      _errorDownloadingRootGrid = true;
    }
  }

}