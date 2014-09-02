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




//class IDownloader;
//class Sector;


public class PointCloudsRenderer extends DefaultRenderer
{

  public abstract static class PointCloudMetadataListener
  {
    public void dispose()
    {
    }

    public abstract void onMetadata(long pointsCount, Sector sector, double minHeight, double maxHeight);
  }



//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class PointCloud;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class PointCloudNode;

  private static class PointCloudMetadataParserAsyncTask extends GAsyncTask
  {
    private PointCloud _pointCloud;
    private IByteBuffer _buffer;
    private long _pointsCount;
    private Sector _sector;
    private double _minHeight;
    private double _maxHeight;
    private java.util.ArrayList<PointCloudNode> _nodes;

    public PointCloudMetadataParserAsyncTask(PointCloud pointCloud, IByteBuffer buffer)
    {
       _pointCloud = pointCloud;
       _buffer = buffer;
       _pointsCount = -1;
       _sector = null;
       _minHeight = 0;
       _maxHeight = 0;
       _nodes = null;
    }

    public void dispose()
    {
      if (_sector != null)
         _sector.dispose();
      if (_buffer != null)
         _buffer.dispose();
      if (_nodes != null)
      {
        final int size = _nodes.size();
        for (int i = 0; i < size; i++)
        {
          PointCloudNode node = _nodes.get(i);
          if (node != null)
             node.dispose();
        }
        _nodes = null;
      }
    }

    public final void runInBackground(G3MContext context)
    {
      ByteBufferIterator it = new ByteBufferIterator(_buffer);
    
      _pointsCount = it.nextInt64();
    
      final double lowerLatitude = it.nextDouble();
      final double lowerLongitude = it.nextDouble();
      final double upperLatitude = it.nextDouble();
      final double upperLongitude = it.nextDouble();
    
      _sector = new Sector(Geodetic2D.fromRadians(lowerLatitude, lowerLongitude), Geodetic2D.fromRadians(upperLatitude, upperLongitude));
    
      _minHeight = it.nextDouble();
      _maxHeight = it.nextDouble();
    
      final int nodesCount = it.nextInt32();
      _nodes = new java.util.ArrayList<PointCloudNode>();
    
      for (int i = 0; i < nodesCount; i++)
      {
        final int idLength = it.nextUInt8();
        byte[] id = new byte[idLength];
        it.nextUInt8(idLength, id);
    
        final int byteLevelsCount = it.nextUInt8();
        final int shortLevelsCount = it.nextUInt8();
        final int intLevelsCount = it.nextUInt8();
        final int levelsCountLength = (int) byteLevelsCount + shortLevelsCount + intLevelsCount;
    
        int[] levelsCount = new int[levelsCountLength];
    
        for (int j = 0; j < byteLevelsCount; j++)
        {
    //      levelsCount.push_back( (int) it.nextUInt8() );
          levelsCount[j] = it.nextUInt8();
        }
        for (int j = 0; j < shortLevelsCount; j++)
        {
    //      levelsCount.push_back( (int) it.nextInt16() );
          levelsCount[byteLevelsCount + j] = it.nextInt16();
        }
        for (int j = 0; j < intLevelsCount; j++)
        {
    //      levelsCount.push_back( it.nextInt32() );
          levelsCount[byteLevelsCount + shortLevelsCount + j] = it.nextInt32();
        }
    
        _nodes.add(new PointCloudNode(idLength, id, levelsCountLength, levelsCount));
      }
    
      if (it.hasNext())
      {
        throw new RuntimeException("Logic error");
      }
    
      if (_buffer != null)
         _buffer.dispose();
      _buffer = null;
    }

    public final void onPostExecute(G3MContext context)
    {
      _pointCloud.parsedMetadata(_pointsCount, _sector, _minHeight, _maxHeight, _nodes);
      _sector = null; // moves ownership to pointCloud
      _nodes = null; // moves ownership to pointCloud
    }

  }


  private static class PointCloudMetadataDownloadListener extends IBufferDownloadListener
  {
    private PointCloud _pointCloud;
    private final IThreadUtils _threadUtils;

    public PointCloudMetadataDownloadListener(PointCloud pointCloud, IThreadUtils threadUtils)
    {
       _pointCloud = pointCloud;
       _threadUtils = threadUtils;
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      ILogger.instance().logInfo("Downloaded metadata for \"%s\" (bytes=%ld)", _pointCloud.getCloudName(), buffer.size());
    
      _threadUtils.invokeAsyncTask(new PointCloudMetadataParserAsyncTask(_pointCloud, buffer), true);
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



  private static class PointCloudNode
  {
    private final int    _idLenght;
    private final byte[] _id;
    private final int    _levelsCountLenght;
    private final int[]  _levelsCount;


    public PointCloudNode(final int idLenght,
                          final byte[] id,
                          final int levelsCountLenght,
                          final int[] levelsCount) {
      _idLenght = idLenght;
      _id = id;
      _levelsCountLenght = levelsCountLenght;
      _levelsCount = levelsCount;
    }


    public void dispose() {
    }
  }


  private static class PointCloud
  {
    private final URL _serverURL;
    private final String _cloudName;

    private final long _downloadPriority;
    private final TimeInterval _timeToCache;
    private final boolean _readExpired;

    private PointCloudMetadataListener _metadataListener;
    private boolean _deleteListener;

    private boolean _downloadingMetadata;
    private boolean _errorDownloadingMetadata;
    private boolean _errorParsingMetadata;

    private long _pointsCount;
    private Sector _sector;
    private double _minHeight;
    private double _maxHeight;

    public PointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, PointCloudMetadataListener metadataListener, boolean deleteListener)
    {
       _serverURL = serverURL;
       _cloudName = cloudName;
       _downloadPriority = downloadPriority;
       _timeToCache = timeToCache;
       _readExpired = readExpired;
       _metadataListener = metadataListener;
       _deleteListener = deleteListener;
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

    public final String getCloudName()
    {
      return _cloudName;
    }

    public final void initialize(G3MContext context)
    {
      _downloadingMetadata = true;
      _errorDownloadingMetadata = false;
      _errorParsingMetadata = false;
    
      final String planetType = context.getPlanet().getType();
    
      final URL metadataURL = new URL(_serverURL, _cloudName + "?planet=" + planetType + "&format=binary");
    
      ILogger.instance().logInfo("Downloading metadata for \"%s\"", _cloudName);
    
      context.getDownloader().requestBuffer(metadataURL, _downloadPriority, _timeToCache, _readExpired, new PointCloudsRenderer.PointCloudMetadataDownloadListener(this, context.getThreadUtils()), true);
    }


    //void PointCloudsRenderer::PointCloud::downloadedMetadata(IByteBuffer* buffer) {
    //  ILogger::instance()->logInfo("Downloaded metadata for \"%s\" (bytes=%ld)", _cloudName.c_str(), buffer->size());
    //
    //  _threadUtils->invokeAsyncTask(new PointCloudMetadataParserAsyncTask(this, buffer),
    //                                true);
    //
    //  //  _downloadingMetadata = false;
    //  //
    //  //
    //  //#warning DGD at work!
    //  ////  _errorParsingMetadata = true;
    //  //
    //  //  delete buffer;
    //}
    
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

//    void downloadedMetadata(IByteBuffer* buffer);

    public final void parsedMetadata(long pointsCount, Sector sector, double minHeight, double maxHeight)
    {
      _pointsCount = pointsCount;
      _sector = sector;
      _minHeight = minHeight;
      _maxHeight = maxHeight;
    
      _downloadingMetadata = false;
    
      ILogger.instance().logInfo("Parsed metadata for \"%s\"", _cloudName);
    
      if (_metadataListener != null)
      {
        _metadataListener.onMetadata(pointsCount, sector, minHeight, maxHeight);
        if (_deleteListener)
        {
          if (_metadataListener != null)
             _metadataListener.dispose();
        }
        _metadataListener = null;
      }
    
    }

    public final void render(G3MRenderContext rc, GLState glState)
    {
    
    
    }

  }



  private java.util.ArrayList<PointCloud> _clouds = new java.util.ArrayList<PointCloud>();
  private int _cloudsSize;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();



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

  public final void addPointCloud(URL serverURL, String cloudName, PointCloudMetadataListener metadataListener, boolean deleteListener)
  {
    addPointCloud(serverURL, cloudName, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, metadataListener, deleteListener);
  }

  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, PointCloudMetadataListener metadataListener, boolean deleteListener)
  {
    PointCloud pointCloud = new PointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, metadataListener, deleteListener);
    if (_context != null)
    {
      pointCloud.initialize(_context);
    }
    _clouds.add(pointCloud);
    _cloudsSize = _clouds.size();
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

}