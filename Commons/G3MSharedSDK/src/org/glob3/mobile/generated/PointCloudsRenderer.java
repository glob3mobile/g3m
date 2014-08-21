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

//C++ TO JAVA CONVERTER TODO TASK: Multiple inheritance is not available in Java:
public class PointCloudsRenderer extends DefaultRenderer, TileRenderingListener
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


  private static class PointCloud
  {
    private final URL _serverURL = new URL();
    private final String _cloudName;

    private boolean _downloadingMetadata;
    private boolean _errorDownloadingMetadata;
    private boolean _errorParsingMetadata;

    private long _pointsCount;
    private Sector _sector;
    private double _minHeight;
    private double _maxHeight;

    private final java.util.ArrayList<Sector> _startedRendering = new java.util.ArrayList<Sector>();
    private final java.util.ArrayList<Sector> _stoppedRendering = new java.util.ArrayList<Sector>();

    public PointCloud(URL serverURL, String cloudName)
    {
       _serverURL = new URL(serverURL);
       _cloudName = cloudName;
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
      if (_sector != null)
         _sector.dispose();
    }

    public final void initialize(G3MContext context)
    {
      IDownloader downloader = context.getDownloader();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: allows cache
      _downloadingMetadata = true;
      _errorDownloadingMetadata = false;
      _errorParsingMetadata = false;
    
      final URL metadataURL = new URL(_serverURL, _cloudName);
    
      downloader.requestBuffer(metadataURL, DownloadPriority.HIGHEST, TimeInterval.zero(), true, new PointCloudsRenderer.PointCloudMetadataDownloadListener(this), true);
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

//    void startRendering(const Tile* tile);
//
//    void stopRendering(const Tile* tile);

    public final void changedTileRendering(java.util.ArrayList<Tile> started, java.util.ArrayList<Tile> stopped)
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning DGD at work!
      if (_sector != null)
      {
        //ILogger::instance()->logInfo("changedTileRendering");
        for (int i = 0; i < started.size(); i++)
        {
          final Tile tile = started.get(i);
          if (tile._sector.touchesWith(_sector))
          {
            ILogger.instance().logInfo("   Start rendering tile " + tile._id + " for cloud " + _cloudName);
            _startedRendering.add(new Sector(tile._sector));
          }
        }
    
        for (int i = 0; i < stopped.size(); i++)
        {
          final Tile tile = stopped.get(i);
          if (tile._sector.touchesWith(_sector))
          {
            ILogger.instance().logInfo("   Stop rendering tile " + tile._id + " for cloud " + _cloudName);
            _stoppedRendering.add(new Sector(tile._sector));
          }
        }
    
        if (!_startedRendering.isEmpty() || !_stoppedRendering.isEmpty())
        {
    
          _startedRendering.clear();
          _stoppedRendering.clear();
        }
      }
    }

  }


  private java.util.ArrayList<PointCloud> _clouds = new java.util.ArrayList<PointCloud>();
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();


  protected final void onChangedContext()
  {
    final int cloudsSize = _clouds.size();
    for (int i = 0; i < cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      cloud.initialize(_context);
    }
  }


  public void dispose()
  {
    final int cloudsSize = _clouds.size();
    for (int i = 0; i < cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      if (cloud != null)
         cloud.dispose();
    }
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    _errors.clear();
    boolean busyFlag = false;
    boolean errorFlag = false;
  
    final int cloudsSize = _clouds.size();
    for (int i = 0; i < cloudsSize; i++)
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
    final int cloudsSize = _clouds.size();
    for (int i = 0; i < cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      cloud.render(rc, glState);
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  
  }

  public final void addPointCloud(URL serverURL, String cloudName)
  {
    PointCloud pointCloud = new PointCloud(serverURL, cloudName);
    if (_context != null)
    {
      pointCloud.initialize(_context);
    }
    _clouds.add(pointCloud);
  }

  public final void removeAllPointClouds()
  {
    final int cloudsSize = _clouds.size();
    for (int i = 0; i < cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      if (cloud != null)
         cloud.dispose();
    }
    _clouds.clear();
  }


//  void startRendering(const Tile* tile);
//
//  void stopRendering(const Tile* tile);

  public final void changedTileRendering(java.util.ArrayList<Tile> started, java.util.ArrayList<Tile> stopped)
  {
    final int cloudsSize = _clouds.size();
    for (int i = 0; i < cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      cloud.changedTileRendering(started, stopped);
    }
  }


}