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
//class ITimer;

public class PointCloudsRenderer extends DefaultRenderer
{

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class PointCloud;


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


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class TileLayout;

  private static class TileLayoutBufferDownloadListener extends IBufferDownloadListener
  {
    private TileLayout _tileLayout;

    public TileLayoutBufferDownloadListener(TileLayout tileLayout)
    {
      _tileLayout = tileLayout;
      _tileLayout._retain();
    }

    public void dispose()
    {
      _tileLayout._release();
      _tileLayout = null;
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      _tileLayout.onDownload(url, buffer, expired);
    }

    public final void onError(URL url)
    {
      _tileLayout.onError(url);
    }

    public final void onCancel(URL url)
    {
      _tileLayout.onCancel(url);
    }

    public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      // do nothing
    }
  }


  private static class TileLayoutStopper extends RCObject
  {
    private PointCloud _pointCloud;
    private final int _totalSteps;
    private java.util.ArrayList<String> _tilesToStop = new java.util.ArrayList<String>();

    private int _stepsDone;

    public void dispose()
    {

    }

    public TileLayoutStopper(PointCloud pointCloud, int totalSteps, java.util.ArrayList<String> tilesToStop)
    {
       _pointCloud = pointCloud;
       _totalSteps = totalSteps;
       _stepsDone = 0;
      _tilesToStop = new java.util.ArrayList<String>(tilesToStop);
    }


    public final void stepDone()
    {
      _stepsDone++;
      if (_stepsDone == _totalSteps)
      {
        _pointCloud.stopTiles(_tilesToStop);
      }
    }
  }


  private static class TileLayout extends RCObject
  {
    private PointCloud _pointCloud;

    private final String _cloudName;
    private final String _tileID;
    private final String _tileQuadKey;
    private TileLayoutStopper _stopper;

    private boolean _isInitialized;

    private IDownloader _downloader;
    private long _layoutRequestID;

    private boolean _canceled;

    private java.util.ArrayList<String> _nodesIDs = new java.util.ArrayList<String>();

    public void dispose()
    {
    //  ILogger::instance()->logInfo(" => Stop rendering tile " + _tileID + " (" + _tileQuadKey + ") for cloud \"" + _cloudName + "\"");
      if (_stopper != null)
      {
        _stopper._release();
      }
    
      super.dispose();
    }

    public TileLayout(PointCloud pointCloud, String cloudName, String tileID, String tileQuadKey, TileLayoutStopper stopper)
    {
       _pointCloud = pointCloud;
       _cloudName = cloudName;
       _tileID = tileID;
       _tileQuadKey = tileQuadKey;
       _stopper = stopper;
       _isInitialized = false;
       _downloader = null;
       _layoutRequestID = -1;
       _canceled = false;
      //ILogger::instance()->logInfo(" => Start rendering tile " + _tileID + " (" + _tileQuadKey + ") for cloud \"" + _cloudName + "\"");
      if (_stopper != null)
      {
        _stopper._retain();
      }
    }


    public final boolean isInitialized()
    {
      return _isInitialized;
    }

    public final void initialize(G3MContext context, URL serverURL, long downloadPriority, TimeInterval timeToCache, boolean readExpired)
    {
    
    //  ILogger::instance()->logInfo("  => Initializing tile " + _tileID + " (" + _tileQuadKey + ") for cloud \"" + _cloudName + "\"");
    
      if (_downloader == null)
      {
        _downloader = context.getDownloader();
      }
      _layoutRequestID = _downloader.requestBuffer(new URL(serverURL, _cloudName + "/layout/" + _tileQuadKey), downloadPriority, timeToCache, readExpired, new TileLayoutBufferDownloadListener(this), true);
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      if (!_canceled)
      {
        final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(buffer);
        if (jsonBaseObject != null)
        {
          final JSONArray jsonArray = jsonBaseObject.asArray();
          if (jsonArray != null)
          {
            //ILogger::instance()->logInfo("\"%s\" => %s", _tileQuadKey.c_str(), jsonArray->description().c_str());
            //ILogger::instance()->logInfo("\"%s\"", _tileQuadKey.c_str());
    
            final int size = jsonArray.size();
            for (int i = 0; i < size; i++)
            {
              final String nodeID = jsonArray.getAsString(i).value();
              //ILogger::instance()->logInfo("  => %s", nodeID.c_str());
              _pointCloud.createNode(nodeID);
              _nodesIDs.add(nodeID);
            }
    
            _isInitialized = true;
          }
          if (jsonBaseObject != null)
             jsonBaseObject.dispose();
        }
      }
    
      if (buffer != null)
         buffer.dispose();
    
      _layoutRequestID = -1;
    
      if (_stopper != null)
      {
        _stopper.stepDone();
      }
    }

    public final void onError(URL url)
    {
      ILogger.instance().logError("Error downloading %s", url.getPath());
      _layoutRequestID = -1;
      if (_stopper != null)
      {
        _stopper.stepDone();
      }
    }

    public final void onCancel(URL url)
    {
      _layoutRequestID = -1;
      if (_stopper != null)
      {
        _stopper.stepDone();
      }
    }

    public final void cancel()
    {
      _canceled = true;
      if (_downloader != null && _layoutRequestID >= 0)
      {
        ILogger.instance().logInfo(" => Canceling initialization of tile " + _tileID + " (" + _tileQuadKey + ") for cloud \"" + _cloudName + "\"");
        _downloader.cancelRequest(_layoutRequestID);
      }
    
      final int size = _nodesIDs.size();
      for (int i = 0; i < size; i++)
      {
        final String nodeID = _nodesIDs.get(i);
        _pointCloud.removeNode(nodeID);
      }
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
    private boolean _visibleTilesNeedsInitialization;
    private ITimer _initializationTimer;

    public PointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired)
//    _visibleTilesNeedsInitialization(false),
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
       _initializationTimer = null;
    }

    public void dispose()
    {
      for (final java.util.Map.Entry<String, TileLayout> entry : _visibleTiles.entrySet()) {
        final TileLayout tileLayout = entry.getValue();
        tileLayout.cancel();
        tileLayout._release();
      }
    
      if (_initializationTimer != null)
         _initializationTimer.dispose();
    
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
          // const std::string name = jsonObject->getAsString("name", "");
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
      if (_visibleTilesNeedsInitialization)
      {
        if (_initializationTimer == null)
        {
          _initializationTimer = rc.getFactory().createTimer();
        }
        else
        {
          _initializationTimer.start();
        }
    
        _visibleTilesNeedsInitialization = false;
          for (final TileLayout tileLayout : _visibleTiles.values()) {
          if (!tileLayout.isInitialized())
          {
            tileLayout.initialize(rc, _serverURL, _downloadPriority, _timeToCache, _readExpired);
            if (_initializationTimer.elapsedTimeInMilliseconds() > 20)
            {
              _visibleTilesNeedsInitialization = true; // force another initialization lap for the next frame
              break;
            }
          }
        }
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
    //        const std::string tileID = tile->_id;
    //        if (_visibleTiles.find(tileID) != _visibleTiles.end()) {
    //          THROW_EXCEPTION("Logic error");
    //        }
    //
    //        _visibleTiles[tileID] = new PointCloudsRenderer::TileLayout(this,
    //                                                                    _cloudName,
    //                                                                    tileID,
    //                                                                    BingMapsLayer::getQuadKey(tile));
    //        _visibleTilesNeedsInitialization = true;
          }
        }
    
        final int tilesStoppedRenderingSize = tilesStoppedRendering.size();
        for (int i = 0; i < tilesStoppedRenderingSize; i++)
        {
          final String tileID = tilesStoppedRendering.get(i);
          _tilesStoppedRendering.add(tileID);
    //      if (_visibleTiles.find(tileID) != _visibleTiles.end()) {
    //        PointCloudsRenderer::TileLayout* tileLayout = _visibleTiles[tileID];
    //        tileLayout->cancel();
    //        tileLayout->_release();
    //        _visibleTiles.erase(tileID);
    //      }
        }
    
        final int startedSize = _tilesStartedRendering.size();
        final int stoppedSize = _tilesStoppedRendering.size();
        final boolean anyStarted = startedSize > 0;
        final boolean anyStopped = stoppedSize > 0;
        if (anyStarted || anyStopped)
        {
          if (anyStarted)
          {
            if (anyStopped)
            {
              // wait initialization of started before proceeding with the stopped
              // ILogger::instance()->logInfo("===> case 1: %d %d", startedSize, stoppedSize);
    
              TileLayoutStopper stopper = new TileLayoutStopper(this, startedSize, _tilesStoppedRendering);
    
              for (int i = 0; i < startedSize; i++)
              {
                final Tile tile = _tilesStartedRendering.get(i);
                final String tileID = tile._id;
                if (_visibleTiles.containsKey(tileID))
                {
                  throw new RuntimeException("Logic error");
                }
                _visibleTiles.put(tileID, new PointCloudsRenderer.TileLayout(this, _cloudName, tileID, BingMapsLayer.getQuadKey(tile), stopper));
                _visibleTilesNeedsInitialization = true;
              }
    
              stopper._release();
            }
            else
            {
              // just initialize the started
              //ILogger::instance()->logInfo("===> case 2: %d %d", startedSize, stoppedSize);
    
              for (int i = 0; i < startedSize; i++)
              {
                final Tile tile = _tilesStartedRendering.get(i);
                final String tileID = tile._id;
                if (_visibleTiles.containsKey(tileID))
                {
                  throw new RuntimeException("Logic error");
                }
                _visibleTiles.put(tileID, new PointCloudsRenderer.TileLayout(this, _cloudName, tileID, BingMapsLayer.getQuadKey(tile), null));
                _visibleTilesNeedsInitialization = true;
              }
            }
          }
          else
          {
            // proceed with the stopped
            // ILogger::instance()->logInfo("===> case 3: %d %d", startedSize, stoppedSize);
            stopTiles(_tilesStoppedRendering);
          }
    
          _tilesStartedRendering.clear();
          _tilesStoppedRendering.clear();
        }
      }
    }

    public final void createNode(String nodeID)
    {
      ILogger.instance().logInfo(" creating node: %s", nodeID);
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning DGD at work!
    }
    public final void removeNode(String nodeID)
    {
      ILogger.instance().logInfo(" removing node: %s", nodeID);
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning DGD at work!
    }

    public final void stopTiles(java.util.ArrayList<String> tilesToStop)
    {
    
      final int tilesToStopSize = tilesToStop.size();
      for (int i = 0; i < tilesToStopSize; i++)
      {
        final String tileID = tilesToStop.get(i);
    
        if (_visibleTiles.containsKey(tileID))
        {
          PointCloudsRenderer.TileLayout tileLayout = _visibleTiles.get(tileID);
          tileLayout.cancel();
          tileLayout._release();
          _visibleTiles.remove(tileID);
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning DGD at work: render nodes

//  for (std::map<std::string, TileLayout*>::iterator it = _visibleTiles.begin();
//       it != _visibleTiles.end();
//       it++) {
//    TileLayout* tileLayout = it->second;
//    tileLayout->render(rc, glState);
//  }
}
