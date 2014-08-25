package org.glob3.mobile.generated; 
//
//  PointCloudsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//

//
//  PointCloudsRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//




//class Sector;
//class IDownloader;

public class PointCloudsRenderer extends DefaultRenderer
{


//  class TileData {
//  public:
//    const std::string _quadKey;
//    const Sector      _sector;
//
//    TileData(const std::string& quadKey,
//             const Sector&      sector) :
//    _quadKey(quadKey),
//    _sector(sector)
//    {
//
//    }
//  };


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class PointCloud;


//  class PointCloudNodesLayoutFetcher : public RCObject {
//  private:
//    PointCloud* _pointCloud;
//
//  public:
//    PointCloudNodesLayoutFetcher(IDownloader* downloader,
//                                 PointCloud* pointCloud,
//                                 const std::vector<const Tile*>& tilesStartedRendering,
//                                 const std::vector<const Tile*>& tilesStoppedRendering);
//
//  };
//

  private static class PointCloudMetadataDownloadListener extends IBufferDownloadListener
  {
    private PointCloud _pointCloud;

    public PointCloudMetadataDownloadListener(PointCloud pointCloud)
    {
       _pointCloud = pointCloud;
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      _pointCloud.downloadedMetadata(buffer);
    }

    public final void onError(URL url)
    {
      _pointCloud.errorDownloadingMetadata();
    }

    public final void onCancel(URL url)
    {
      // do nothing
    }

    public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      // do nothing
    }

  }


  private static class TileLayout
  {
    private final String _tileQuadKey;

    public TileLayout(String tileQuadKey)
    {
       _tileQuadKey = tileQuadKey;
    }

    public void dispose()
    {
    }
  }


  private static class PointCloud
  {
    private final URL _serverURL;
    private final String _cloudName;

    private final long _downloadPriority;
    private final TimeInterval _timeToCache;
    private final boolean _readExpired;
    private IDownloader _downloader;

    private boolean _downloadingMetadata;
    private boolean _errorDownloadingMetadata;
    private boolean _errorParsingMetadata;

    private long _pointsCount;
    private Sector _sector;
    private double _minHeight;
    private double _maxHeight;

    private final java.util.ArrayList<Tile> _tilesStartedRendering = new java.util.ArrayList<Tile>();
    private java.util.ArrayList<String> _tilesStoppedRendering = new java.util.ArrayList<String>();

    private java.util.HashMap<String, TileLayout> _visibleTiles = new java.util.HashMap<String, TileLayout>();

    private void updateNodesLayout()
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning DGD at work!
    
      for (int i = 0; i < _tilesStartedRendering.size(); i++)
      {
        final Tile tile = _tilesStartedRendering.get(i);
        final String tileID = tile._id;
        ILogger.instance().logInfo(" => Start rendering tile " + tileID + " for cloud \"" + _cloudName + "\"");
    
        if (_visibleTiles.containsKey(tileID))
        {
          throw new RuntimeException("Logic error");
        }
        _visibleTiles.put(tileID, new PointCloudsRenderer.TileLayout(BingMapsLayer.getQuadKey(tile)));
      }
    
      for (int i = 0; i < _tilesStoppedRendering.size(); i++)
      {
        final String tileID = _tilesStoppedRendering.get(i);
    
        if (_visibleTiles.containsKey(tileID))
        {
          PointCloudsRenderer.TileLayout tileLayout = _visibleTiles.get(tileID);
          ILogger.instance().logInfo(" => Stop rendering tile " + tileID + " for cloud \"" + _cloudName + "\"");
          if (tileLayout != null)
             tileLayout.dispose();
          _visibleTiles.remove(tileID);
        }
      }
    
      _tilesStartedRendering.clear();
      _tilesStoppedRendering.clear();
    }

    public PointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired)
    {
       _serverURL = serverURL;
       _cloudName = cloudName;
       _downloadPriority = downloadPriority;
       _timeToCache = timeToCache;
       _readExpired = readExpired;
       _downloader = null;
       _downloadingMetadata = false;
       _errorDownloadingMetadata = false;
       _errorParsingMetadata = false;
       _pointsCount = -1;
       _sector = null;
       _minHeight = 0;
       _maxHeight = 0;
    }

    public void dispose()
    {
      java.util.Iterator<String, TileLayout> it;
      for (it = _visibleTiles.iterator(); it.hasNext();)
      {
        TileLayout tileLayout = it.next().getValue();
        ILogger.instance().logInfo(" => (destructor) Stop rendering tile " + it.next().getKey() + " for cloud \"" + _cloudName + "\"");
        if (tileLayout != null)
           tileLayout.dispose();
      }
    
      if (_sector != null)
         _sector.dispose();
    }

    public final void initialize(G3MContext context)
    {
      _downloader = context.getDownloader();
      _downloadingMetadata = true;
      _errorDownloadingMetadata = false;
      _errorParsingMetadata = false;
    
      final URL metadataURL = new URL(_serverURL, _cloudName);
    
      _downloader.requestBuffer(metadataURL, _downloadPriority, _timeToCache, _readExpired, new PointCloudsRenderer.PointCloudMetadataDownloadListener(this), true);
    }

    public final RenderState getRenderState(G3MRenderContext rc)
    {
      if (_downloadingMetadata)
      {
        return RenderState.busy();
      }
    
      if (_errorDownloadingMetadata)
      {
        return RenderState.error("Error downloading metadata of \"" + _cloudName + "\" from \"" + _serverURL.getPath() + "\"");
      }
    
      if (_errorParsingMetadata)
      {
        return RenderState.error("Error parsing metadata of \"" + _cloudName + "\" from \"" + _serverURL.getPath() + "\"");
      }
    
      return RenderState.ready();
    }

    public final void errorDownloadingMetadata()
    {
      _downloadingMetadata = false;
      _errorDownloadingMetadata = true;
    }

    public final void downloadedMetadata(IByteBuffer buffer)
    {
      _downloadingMetadata = false;
    
      final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(buffer, true);
      if (jsonBaseObject != null)
      {
        final JSONObject jsonObject = jsonBaseObject.asObject();
        if (jsonObject != null)
        {
          final String name = jsonObject.getAsString("name", "");
          _pointsCount = (long) jsonObject.getAsNumber("pointsCount", 0);
    
          final JSONObject sectorJSONObject = jsonObject.getAsObject("sector");
          if (sectorJSONObject != null)
          {
            final double lowerLatitude = sectorJSONObject.getAsNumber("lowerLatitude", 0);
            final double lowerLongitude = sectorJSONObject.getAsNumber("lowerLongitude", 0);
            final double upperLatitude = sectorJSONObject.getAsNumber("upperLatitude", 0);
            final double upperLongitude = sectorJSONObject.getAsNumber("upperLongitude", 0);
    
            _sector = new Sector(Geodetic2D.fromDegrees(lowerLatitude, lowerLongitude), Geodetic2D.fromDegrees(upperLatitude, upperLongitude));
          }
    
          _minHeight = jsonObject.getAsNumber("minHeight", 0);
          _maxHeight = jsonObject.getAsNumber("maxHeight", 0);
        }
        else
        {
          _errorParsingMetadata = true;
        }
    
        if (jsonBaseObject != null)
           jsonBaseObject.dispose();
      }
      else
      {
        _errorParsingMetadata = true;
      }
    
      if (buffer != null)
         buffer.dispose();
    }

    public final void render(G3MRenderContext rc, GLState glState)
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning DGD at work!
    }

    public final void changedTilesRendering(java.util.ArrayList<Tile> tilesStartedRendering, java.util.ArrayList<String> tilesStoppedRendering)
    {
      if (_sector != null)
      {
        final int tilesStartedRenderingSize = tilesStartedRendering.size();
        for (int i = 0; i < tilesStartedRenderingSize; i++)
        {
          final Tile tile = tilesStartedRendering.get(i);
          if (!tile._mercator)
          {
            throw new RuntimeException("Tile has to be mercator");
          }
          if (tile._sector.touchesWith(_sector))
          {
            _tilesStartedRendering.add(tile);
          }
        }
    
        final int tilesStoppedRenderingSize = tilesStoppedRendering.size();
        for (int i = 0; i < tilesStoppedRenderingSize; i++)
        {
          final String tileID = tilesStoppedRendering.get(i);
          _tilesStoppedRendering.add(tileID);
        }
    
        if (!_tilesStartedRendering.isEmpty() || !_tilesStoppedRendering.isEmpty())
        {
          updateNodesLayout();
        }
      }
    }

  }


  private static class PointCloudsTileRenderingListener extends TileRenderingListener
  {
    private PointCloudsRenderer _pointCloudsRenderer;
    public PointCloudsTileRenderingListener(PointCloudsRenderer pointCloudsRenderer)
    {
       _pointCloudsRenderer = pointCloudsRenderer;
    }

    public final void changedTilesRendering(java.util.ArrayList<Tile> tilesStartedRendering, java.util.ArrayList<String> tilesStoppedRendering)
    {
      _pointCloudsRenderer.changedTilesRendering(tilesStartedRendering, tilesStoppedRendering);
    }

  }


  private java.util.ArrayList<PointCloud> _clouds = new java.util.ArrayList<PointCloud>();
  private int _cloudsSize;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private TileRenderingListener _tileRenderingListener;

  protected final void onChangedContext()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      cloud.initialize(_context);
    }
  }

  public PointCloudsRenderer()
  {
     _cloudsSize = 0;
    _tileRenderingListener = new PointCloudsTileRenderingListener(this);
  }

  public void dispose()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      if (cloud != null)
         cloud.dispose();
    }
    super.dispose();
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    _errors.clear();
    boolean busyFlag = false;
    boolean errorFlag = false;
  
    for (int i = 0; i < _cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      final RenderState childRenderState = cloud.getRenderState(rc);
  
      final RenderState_Type childRenderStateType = childRenderState._type;
  
      if (childRenderStateType == RenderState_Type.RENDER_ERROR)
      {
        errorFlag = true;
  
        final java.util.ArrayList<String> childErrors = childRenderState.getErrors();
        _errors.addAll(childErrors);
      }
      else if (childRenderStateType == RenderState_Type.RENDER_BUSY)
      {
        busyFlag = true;
      }
    }
  
    if (errorFlag)
    {
      return RenderState.error(_errors);
    }
    else if (busyFlag)
    {
      return RenderState.busy();
    }
    else
    {
      return RenderState.ready();
    }
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      cloud.render(rc, glState);
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  
  }

  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired)
  {
    PointCloud pointCloud = new PointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired);
    if (_context != null)
    {
      pointCloud.initialize(_context);
    }
    _clouds.add(pointCloud);
    _cloudsSize = _clouds.size();
  }

  public final void addPointCloud(URL serverURL, String cloudName)
  {
    addPointCloud(serverURL, cloudName, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
  }

  public final void removeAllPointClouds()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      if (cloud != null)
         cloud.dispose();
    }
    _clouds.clear();
    _cloudsSize = _clouds.size();
  }


  public final void changedTilesRendering(java.util.ArrayList<Tile> tilesStartedRendering, java.util.ArrayList<String> tilesStoppedRendering)
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      cloud.changedTilesRendering(tilesStartedRendering, tilesStoppedRendering);
    }
  }

  public final TileRenderingListener getTileRenderingListener()
  {
    return _tileRenderingListener;
  }

}