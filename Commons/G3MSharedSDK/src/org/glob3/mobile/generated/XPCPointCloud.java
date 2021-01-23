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


public class XPCPointCloud extends RCObject
{
  private final URL _serverURL;
  private final String _cloudName;
  private final long _downloadPriority;
  private final TimeInterval _timeToCache;
  private final boolean _readExpired;
  private XPCPointColorizer _pointColorizer;
  private final boolean _deletePointColorizer;
  private final double _minProjectedArea;
  private final float _pointSize;
  private final boolean _dynamicPointSize;
  private final boolean _depthTest;
  private final float _verticalExaggeration;
  private final float _deltaHeight;
  private XPCMetadataListener _metadataListener;
  private final boolean _deleteMetadataListener;
  private final boolean _verbose;

  private boolean _downloadingMetadata;
  private boolean _errorDownloadingMetadata;
  private boolean _errorParsingMetadata;

  private IIntBuffer _requiredDimensionIndices;

  private XPCMetadata _metadata;
  private long _lastRenderedCount;


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
  
    if (_metadata != null)
       _metadata.dispose();
  
    if (_requiredDimensionIndices != null)
       _requiredDimensionIndices.dispose();
  
    super.dispose();
  }

  public XPCPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, float deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener, boolean verbose)
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
     _verbose = verbose;
     _downloadingMetadata = false;
     _errorDownloadingMetadata = false;
     _errorParsingMetadata = false;
     _metadata = null;
     _lastRenderedCount = 0;
     _requiredDimensionIndices = null;
  
  }

  public final String getCloudName()
  {
    return _cloudName;
  }

  public final boolean isVerbose()
  {
    return _verbose;
  }

  public final float getVerticalExaggeration()
  {
    return _verticalExaggeration;
  }

  public final float getDeltaHeight()
  {
    return _deltaHeight;
  }

  public final double getMinProjectedArea()
  {
    return _minProjectedArea;
  }

  public final XPCMetadata getMetadada()
  {
    return _metadata;
  }

  public final float getDevicePointSize()
  {
    return _pointSize * IFactory.instance().getDeviceInfo().getDevicePixelRatio();
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
  
    context.getDownloader().requestBuffer(metadataURL, _downloadPriority, _timeToCache, _readExpired, new XPCMetadataDownloadListener(this, context.getThreadUtils()), true);
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

  public final void render(G3MRenderContext rc, GLState glState, Frustum frustum, long nowInMS, boolean renderDebug, XPCSelectionResult selectionResult)
  {
    if (_metadata != null)
    {
      final long renderedCount = _metadata.render(this, rc, glState, frustum, nowInMS, renderDebug, selectionResult);
  
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
  
  
    if (_metadata != metadata)
    {
      if (_metadata != null)
         _metadata.dispose();
    }
    _metadata = metadata;
  
  
    IIntBuffer requiredDimensionIndices = null;
    if (_pointColorizer != null)
    {
      requiredDimensionIndices = _pointColorizer.initialize(_metadata);
    }
    if (_requiredDimensionIndices != requiredDimensionIndices)
    {
      if (_requiredDimensionIndices != null)
         _requiredDimensionIndices.dispose();
    }
    _requiredDimensionIndices = requiredDimensionIndices;
  
  
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
    return ((_metadata != null) && _metadata.selectPoints(selectionResult));
  }

}