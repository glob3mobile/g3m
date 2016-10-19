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
//class Sector;
//class MeshRenderer;


public abstract class MapzenTerrainElevationProvider extends MercatorPyramidTerrainElevationProvider
{
  private static int _idCounter = 0;

  private final String _apiKey;

  private final long _downloadPriority;
  private final TimeInterval _timeToCache;
  private final boolean _readExpired;

  private MeshRenderer _meshRenderer;


  private final String _instanceID;

  private G3MContext _context;


  private FloatBufferTerrainElevationGrid _rootGrid;
  private boolean _errorDownloadingRootGrid;

  private void requestTile(int z, int x, int y, Sector sector, double deltaHeight)
  {
    IDownloader downloader = _context.getDownloader();
  
    final IStringUtils su = IStringUtils.instance();
    final String path = "https://tile.mapzen.com/mapzen/terrain/v1/terrarium/" + su.toString(z) + "/" + su.toString(x) + "/" + su.toString(y) + ".png?api_key=" + _apiKey;
  
    downloader.requestImage(new URL(path), _downloadPriority, _timeToCache, _readExpired, new MapzenTerrainElevationProvider_ImageDownloadListener(_context, this, z, x, y, sector, deltaHeight), true);
  }

  public void dispose()
  {
    if (_rootGrid != null)
    {
      _rootGrid._release();
    }
  
    super.dispose();
  }


  public MapzenTerrainElevationProvider(String apiKey, long downloadPriority, TimeInterval timeToCache, boolean readExpired, MeshRenderer meshRenderer)
  {
     super(256, 256);
     _apiKey = apiKey;
     _downloadPriority = downloadPriority;
     _timeToCache = timeToCache;
     _readExpired = readExpired;
     _meshRenderer = meshRenderer;
     _context = null;
     _instanceID = "MapzenTerrainElevationProvider_" + IStringUtils.instance().toString(++_idCounter);
     _rootGrid = null;
     _errorDownloadingRootGrid = false;
  
  }

  public final RenderState getRenderState()
  {
    if (_errorDownloadingRootGrid)
    {
      return RenderState.error("Error downloading Mapzen root grid");
    }
    return (_rootGrid == null) ? RenderState.busy() : RenderState.ready();
  }

  public final void initialize(G3MContext context)
  {
    _context = context;
  
    // request root grid
    requestTile(0, 0, 0, Sector.FULL_SPHERE, 0); // deltaHeight -  y -  x -  z
  
    //  const int z = 9;
    //  const int x = 271;
    //  const int y = 180;
    //  const double deltaHeight = 0;
    //
    //  const Sector sector = MercatorUtils::getSector(z, x, y);
    //  ILogger::instance()->logInfo( sector.description() );
    //  requestTile(z, x, y,
    //              sector,
    //              deltaHeight);
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
      final boolean sticky = true;
      insertGrid(grid, sticky);
    }
    else
    {
      //    _meshRenderer->addMesh( grid->createDebugMesh(EllipsoidalPlanet::createEarth(),
      //                                                  1, // verticalExaggeration,
      //                                                  Geodetic3D::zero(),
      //                                                  4 // pointSize
      //                                                  ) );
      //
      //     grid->_release();
      throw new RuntimeException("Not yet done");
    }
  }

  public final void onDownloadError(int z, int x, int y)
  {
    ILogger.instance().logError("Error downloading Mapzen terrarium at %i/%i/%i", z, x, y);
    if ((z == 0) && (x == 0) && (y == 0))
    {
      _errorDownloadingRootGrid = true;
    }
  }

  public final long subscribe(Sector sector, Vector2I extent, TerrainElevationListener listener)
  {
    //  return _pyramid->subscribe(sector, extent, listener);
    throw new RuntimeException("Not yet done");
  }

  public final void unsubscribe(long subscriptionID, boolean deleteListener)
  {
    //  _pyramid->unsubscribe(subscriptionID, deleteListener);
    throw new RuntimeException("Not yet done");
  }

}