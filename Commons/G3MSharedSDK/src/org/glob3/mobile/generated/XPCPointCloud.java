package org.glob3.mobile.generated;
//
//  XPCPointCloud.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

//
//  XPCPointCloud.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//



//class XPCPointColorizer;
//class XPCMetadataListener;
//class G3MContext;
//class G3MRenderContext;
//class GLState;
//class Frustum;
//class XPCMetadata;
//class IDownloader;
//class IBufferDownloadListener;
//class IIntBuffer;
//class XPCSelectionResult;
//class XPCPointSelectionListener;
//class Vector3D;
//class Geodetic3D;
//class ITimer;
//class BoundingVolume;


public class XPCPointCloud extends RCObject
{
  private final URL _serverURL;
  private final String _cloudName;
  private final long _downloadPriority;

  private final TimeInterval _timeToCache;
  private final boolean _readExpired;

  private XPCPointColorizer _pointColorizer;
  private boolean _deletePointColorizer;

  private double _minProjectedArea;

  private float _pointSize;
  private boolean _dynamicPointSize;
  private boolean _depthTest;

  private float _verticalExaggeration;
  private double _deltaHeight;

  private XPCMetadataListener _metadataListener;
  private final boolean _deleteMetadataListener;

  private XPCPointSelectionListener _pointSelectionListener;
  private boolean _deletePointSelectionListener;

  private final boolean _verbose;

  private boolean _downloadingMetadata;
  private boolean _errorDownloadingMetadata;
  private boolean _errorParsingMetadata;

  private boolean _canceled;

  private IIntBuffer _requiredDimensionIndices;

  private XPCMetadata _metadata;
  private long _lastRenderedCount;


  private BoundingVolume _fence;

  private void initializePointColorizer()
  {
    IIntBuffer requiredDimensionIndices = null;
    if (_pointColorizer != null)
    {
      requiredDimensionIndices = _pointColorizer.initialize(_metadata);
    }
    if (_requiredDimensionIndices != requiredDimensionIndices)
    {
      if (_requiredDimensionIndices != null)
         _requiredDimensionIndices.dispose();
      _requiredDimensionIndices = requiredDimensionIndices;
    }
  }

  public void dispose()
  {
    if (_deletePointColorizer)
    {
      if (_pointColorizer != null)
         _pointColorizer.dispose();
    }
    if (_deleteMetadataListener)
    {
      if (_metadataListener != null)
         _metadataListener.dispose();
    }
  
    if (_deletePointSelectionListener)
    {
      if (_pointSelectionListener != null)
         _pointSelectionListener.dispose();
    }
  
    if (_metadata != null)
    {
      _metadata.cancel();
    }
    if (_metadata != null)
       _metadata.dispose();
  
    if (_requiredDimensionIndices != null)
       _requiredDimensionIndices.dispose();
  
    if (_fence != null)
       _fence.dispose();
  
    super.dispose();
  }

  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, double deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener, XPCPointSelectionListener pointSelectionListener, boolean deletePointSelectionListener)
  {
     this(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, deltaHeight, metadataListener, deleteMetadataListener, pointSelectionListener, deletePointSelectionListener, false);
  }
  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, double deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener, XPCPointSelectionListener pointSelectionListener)
  {
     this(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, deltaHeight, metadataListener, deleteMetadataListener, pointSelectionListener, true, false);
  }
  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, double deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener)
  {
     this(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, deltaHeight, metadataListener, deleteMetadataListener, null, true, false);
  }
  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, double deltaHeight, XPCMetadataListener metadataListener)
  {
     this(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, deltaHeight, metadataListener, true, null, true, false);
  }
  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, double deltaHeight)
  {
     this(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, deltaHeight, null, true, null, true, false);
  }
  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration)
  {
     this(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, 0, null, true, null, true, false);
  }
  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest)
  {
     this(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, 1.0f, 0, null, true, null, true, false);
  }
  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize)
  {
     this(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, true, 1.0f, 0, null, true, null, true, false);
  }
  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize)
  {
     this(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, true, true, 1.0f, 0, null, true, null, true, false);
  }
  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea)
  {
     this(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, 1.0f, true, true, 1.0f, 0, null, true, null, true, false);
  }
  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, double deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener, XPCPointSelectionListener pointSelectionListener, boolean deletePointSelectionListener, boolean verbose)
  {
     _serverURL = serverURL;
     _cloudName = cloudName;
     _downloadPriority = downloadPriority;
     _timeToCache = timeToCache;
     _readExpired = readExpired;
     _pointColorizer = pointColorizer;
     _deletePointColorizer = deletePointColorizer;
     _minProjectedArea = minProjectedArea;
     _pointSize = pointSize;
     _dynamicPointSize = dynamicPointSize;
     _depthTest = depthTest;
     _verticalExaggeration = verticalExaggeration;
     _deltaHeight = deltaHeight;
     _metadataListener = metadataListener;
     _deleteMetadataListener = deleteMetadataListener;
     _pointSelectionListener = pointSelectionListener;
     _deletePointSelectionListener = deletePointSelectionListener;
     _verbose = verbose;
     _downloadingMetadata = false;
     _errorDownloadingMetadata = false;
     _errorParsingMetadata = false;
     _metadata = null;
     _lastRenderedCount = 0;
     _requiredDimensionIndices = null;
     _canceled = false;
     _fence = null;
  
  }

  public final String getCloudName()
  {
    return _cloudName;
  }

  public final boolean isVerbose()
  {
    return _verbose;
  }

  public final void setPointColorizer(XPCPointColorizer pointColorizer, boolean deletePointColorizer)
  {
    if (_pointColorizer != pointColorizer)
    {
      if (_deletePointColorizer)
      {
        if (_pointColorizer != null)
           _pointColorizer.dispose();
      }
  
      _pointColorizer = pointColorizer;
  
      if (_metadata != null)
      {
        initializePointColorizer();
        _metadata.reloadNodes();
      }
    }
    _deletePointColorizer = deletePointColorizer;
  }

  public final void setPointSelectionListener(XPCPointSelectionListener pointSelectionListener, boolean deletePointSelectionListener)
  {
    if (_pointSelectionListener != pointSelectionListener)
    {
      if (_deletePointSelectionListener)
      {
        if (_pointSelectionListener != null)
           _pointSelectionListener.dispose();
      }
  
      _pointSelectionListener = pointSelectionListener;
    }
    _deletePointSelectionListener = deletePointSelectionListener;
  }

  public final boolean isDynamicPointSize()
  {
    return _dynamicPointSize;
  }

  public final void setDynamicPointSize(boolean dynamicPointSize)
  {
    _dynamicPointSize = dynamicPointSize;
  }

  public final void setDepthTest(boolean depthTest)
  {
    if (_depthTest != depthTest)
    {
      _depthTest = depthTest;
  
      if (_metadata != null)
      {
        _metadata.reloadNodes();
      }
    }
  }

  public final void setVerticalExaggeration(float verticalExaggeration)
  {
    if (_verticalExaggeration != verticalExaggeration)
    {
      _verticalExaggeration = verticalExaggeration;
  
      if (_metadata != null)
      {
        _metadata.reloadNodes();
      }
    }
  }

  public final void setDeltaHeight(double deltaHeight)
  {
    if (_deltaHeight != deltaHeight)
    {
      _deltaHeight = deltaHeight;
  
      if (_metadata != null)
      {
        _metadata.reloadNodes();
      }
    }
  }

  public final float getVerticalExaggeration()
  {
    return _verticalExaggeration;
  }

  public final double getDeltaHeight()
  {
    return _deltaHeight;
  }

  public final double getMinProjectedArea()
  {
    return _minProjectedArea;
  }

  public final void setMinProjectedArea(double minProjectedArea)
  {
    _minProjectedArea = minProjectedArea;
  }

  public final XPCMetadata getMetadada()
  {
    return _metadata;
  }

  public final float getDevicePointSize()
  {
    return _pointSize * IFactory.instance().getDeviceInfo().getDevicePixelRatio();
  }

  public final float getPointSize()
  {
    return _pointSize;
  }

  public final void setPointSize(float pointSize)
  {
    _pointSize = pointSize;
  }

  public final boolean depthTest()
  {
    return _depthTest;
  }

  public final void initialize(G3MContext context)
  {
    _downloadingMetadata = true;
    _errorDownloadingMetadata = false;
    _errorParsingMetadata = false;
  
    final URL metadataURL = new URL(_serverURL, _cloudName);
  
    ILogger.instance().logInfo("Downloading metadata for \"%s\"", _cloudName);
  
    context.getDownloader().requestBuffer(metadataURL, _downloadPriority + 200, _timeToCache, _readExpired, new XPCMetadataDownloadListener(this, context.getThreadUtils()), true);
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    if (_downloadingMetadata)
    {
      return RenderState.busy();
    }
  
    if (_errorDownloadingMetadata)
    {
      return RenderState.error("Error downloading metadata of \"" + _cloudName + "\" from \"" + _serverURL._path + "\"");
    }
  
    if (_errorParsingMetadata)
    {
      return RenderState.error("Error parsing metadata of \"" + _cloudName + "\" from \"" + _serverURL._path + "\"");
    }
  
    return RenderState.ready();
  }

  public final void render(G3MRenderContext rc, ITimer lastSplitTimer, GLState glState, Frustum frustum, long nowInMS, boolean renderDebug)
  {
    if (_metadata != null)
    {
      final long renderedCount = _metadata.render(this, rc, lastSplitTimer, glState, frustum, nowInMS, renderDebug, _fence);
  
      if (_lastRenderedCount != renderedCount)
      {
        if (_verbose)
        {
          ILogger.instance().logInfo("\"%s\": Rendered %d points", _cloudName, renderedCount);
        }
        _lastRenderedCount = renderedCount;
      }
    }
  }

  public final void errorDownloadingMetadata()
  {
    _downloadingMetadata = false;
    _errorDownloadingMetadata = true;
  }
  public final void errorParsingMetadata()
  {
    _downloadingMetadata = false;
    _errorParsingMetadata = true;
  }
  public final void parsedMetadata(XPCMetadata metadata)
  {
    _lastRenderedCount = 0;
    _downloadingMetadata = false;
  
    ILogger.instance().logInfo("Parsed metadata for \"%s\"", _cloudName);
  
    if (_canceled)
    {
      if (metadata != null)
         metadata.dispose();
      return;
    }
  
    if (_metadata != metadata)
    {
      if (_metadata != null)
         _metadata.dispose();
    }
    _metadata = metadata;
  
    initializePointColorizer();
  
    if (_metadataListener != null)
    {
      _metadataListener.onMetadata(_metadata);
      if (_deleteMetadataListener)
      {
        if (_metadataListener != null)
           _metadataListener.dispose();
      }
      _metadataListener = null;
    }
  }

  public final long requestNodeContentBuffer(IDownloader downloader, String treeID, String nodeID, long deltaPriority, IBufferDownloadListener listener, boolean deleteListener)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString(_cloudName);
    isb.addString("/");
    isb.addString(treeID);
    isb.addString("/");
    isb.addString(nodeID);
  
    if (_requiredDimensionIndices != null)
    {
      for (int i = 0; i < _requiredDimensionIndices.size(); i++)
      {
        isb.addString((i == 0) ? "?requiredDimensionIndices=" : ",");
        isb.addInt(_requiredDimensionIndices.get(i));
      }
    }
  
    final String path = isb.getString();
    if (isb != null)
       isb.dispose();
  
    final URL nodeContentURL = new URL(_serverURL, path);
  
    return downloader.requestBuffer(nodeContentURL, _downloadPriority + deltaPriority, _timeToCache, _readExpired, listener, deleteListener);
  }

  public final IIntBuffer getRequiredDimensionIndices()
  {
    return _requiredDimensionIndices;
  }

  public final XPCPointColorizer getPointsColorizer()
  {
    return _pointColorizer;
  }

  public final boolean selectPoints(XPCSelectionResult selectionResult)
  {
    if ((_pointSelectionListener == null) || (_metadata == null))
    {
      return false;
    }
  
    if (_fence != null)
    {
      if (!_fence.touchesRay(selectionResult._ray))
      {
        return false;
      }
    }
  
    return _metadata.selectPoints(selectionResult, this);
  }

  public final boolean selectedPoint(Vector3D cartesian, Geodetic3D geodetic, String treeID, String nodeID, int pointIndex, double distanceToRay)
  {
    if (_pointSelectionListener == null)
    {
      return false;
    }
  
    return _pointSelectionListener.onSelectedPoint(this, cartesian, geodetic, treeID, nodeID, pointIndex, distanceToRay);
  }

  public final void cancel()
  {
    _canceled = true;
    if (_metadata != null)
    {
      _metadata.cancel();
    }
  }

  public final void setFence(BoundingVolume fence)
  {
    if (_fence != fence)
    {
      if (_fence != null)
         _fence.dispose();
  
      _fence = fence;
  
      if (_metadata != null)
      {
        _metadata.reloadNodes();
      }
    }
  }

}