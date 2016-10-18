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


public class MapzenTerrainElevationProvider extends TerrainElevationProvider
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
  
    /*
     Touched on (Tile level=9, row=331, column=271, sector=(Sector (lat=46.558860303117171497d, lon=10.546875d) - (lat=47.040182144806649944d, lon=11.25d)))
     Touched on position (lat=46.64863034601081182d, lon=10.850429115221331244d, height=0)
     Touched on pixels (V2I 110, 208)
     Camera position=(lat=46.668763371822997499d, lon=10.800910848094183336d, height=135933.14638548778021) heading=3.472574 pitch=-90.000000
    
    
    
     const int numRows = (int) (_parameters->_topSectorSplitsByLatitude * _mu->pow(2.0, level));
     const int row     = numRows - tile->_row - 1;
     */
  
  //  const int z = 10;
  //  const int x = 154;
  //  const int y = 304;
    final int z = 9;
    final int x = 271;
    final int y = 180;
    final double deltaHeight = 0;
  
    final Sector sector = MercatorUtils.getSector(z, x, y);
    ILogger.instance().logInfo(sector.description());
    requestTile(z, x, y, sector, deltaHeight);
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
      _meshRenderer.addMesh(grid.createDebugMesh(EllipsoidalPlanet.createEarth(), 1, Geodetic3D.zero(), 4)); // pointSize -  verticalExaggeration,
  
      grid._release();
      //THROW_EXCEPTION("Not yet done");
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

}