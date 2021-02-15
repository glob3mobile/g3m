package org.glob3.mobile.generated;
//
//  XPCNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

//
//  XPCNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//




//class Sector;
//class Sphere;
//class G3MRenderContext;
//class XPCPointCloud;
//class GLState;
//class Frustum;
//class IDownloader;
//class ByteBufferIterator;
//class DirectMesh;
//class XPCSelectionResult;
//class ITimer;
//class XPCRenderingState;


public class XPCNode extends RCObject
{

  private final String _id;

  private final Sector _sector;

  private final int _pointsCount;

  private final double _minHeight;
  private final double _maxHeight;

  private java.util.ArrayList<XPCNode> _children;
  private int _childrenSize;

  private DirectMesh _mesh;

  private Sphere _bounds;
  private Sphere getBounds(G3MRenderContext rc, XPCPointCloud pointCloud)
  {
    if (_bounds == null)
    {
      _bounds = calculateBounds(rc, pointCloud);
    }
    return _bounds;
  }

  private Sphere calculateBounds(G3MRenderContext rc, XPCPointCloud pointCloud)
  {
    final Planet planet = rc.getPlanet();
  
    final double deltaHeight = pointCloud.getDeltaHeight();
    final float verticalExaggeration = pointCloud.getVerticalExaggeration();
  
    java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(10);
    points.add( planet.toCartesian( _sector.getNE()     , (_minHeight + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getNE()     , (_maxHeight + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getNW()     , (_minHeight + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getNW()     , (_maxHeight + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getSE()     , (_minHeight + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getSE()     , (_maxHeight + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getSW()     , (_minHeight + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getSW()     , (_maxHeight + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getCenter() , (_minHeight + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getCenter() , (_maxHeight + deltaHeight) * verticalExaggeration ) );
  
  ///#warning TODO: check if the sphere fits into the parent's one
    //  if (_parent) {
    //    _parent->updateBoundingSphereWith(rc, vectorSet, _boundingSphere);
    //  }
  
    return Sphere.enclosingSphere(points, 0);
  }


  private boolean _renderedInPreviousFrame;

  private double _projectedArea;
  private long _projectedAreaTS;

  private boolean _loadedContent;
  private boolean _loadingContent;
  private boolean _canceled;

  private IDownloader _downloader;
  private long _contentRequestID;

  private void loadContent(XPCPointCloud pointCloud, String treeID, G3MRenderContext rc)
  {
    _downloader = rc.getDownloader();
  
    // const long long deltaPriority = ((100 - _id.length()) * 1000) + _pointsCount;
    // const long long deltaPriority = (_id.length() * 1000) + _pointsCount;
  
    final long deltaPriority = 100 - _id.length();
  
  //  const size_t depth = _id.length();
  //  const long long deltaPriority =  (depth == 0) ? 100 : _id.length();
  
    _contentRequestID = pointCloud.requestNodeContentBuffer(_downloader, treeID, _id, deltaPriority, new XPCNodeContentDownloadListener(pointCloud, this, rc.getThreadUtils(), rc.getPlanet()), true);
  }

  private void cancelLoadContent()
  {
    if (_contentRequestID != -1)
    {
      _downloader.cancelRequest(_contentRequestID);
      _contentRequestID = -1;
    }
  }
  private void unloadContent()
  {
    if (_mesh != null)
       _mesh.dispose();
    _mesh = null;
  
    _loadedContent = false;
  }
  private void unloadChildren()
  {
    if (_children != null)
    {
      for (int i = 0; i < _childrenSize; i++)
      {
        XPCNode child = _children.get(i);
        child.cancel();
        child._release();
      }
  
      _children = null;
      _children = null;
      _childrenSize = 0;
    }
  }

  private XPCNode(String id, Sector sector, int pointsCount, double minHeight, double maxHeight)
  {
     _id = id;
     _sector = sector;
     _pointsCount = pointsCount;
     _minHeight = minHeight;
     _maxHeight = maxHeight;
     _bounds = null;
     _renderedInPreviousFrame = false;
     _projectedArea = -1;
     _projectedAreaTS = -1;
     _loadedContent = false;
     _loadingContent = false;
     _children = null;
     _childrenSize = 0;
     _downloader = null;
     _contentRequestID = -1;
     _mesh = null;
     _canceled = false;
  
  }

  public void dispose()
  {
    if (_sector != null)
       _sector.dispose();
  
    if (_bounds != null)
       _bounds.dispose();
  
    for (int i = 0; i < _childrenSize; i++)
    {
      XPCNode child = _children.get(i);
      child._release();
    }
  
    _children = null;
  
    if (_mesh != null)
       _mesh.dispose();
  }


  public final String getID()
  {
    return _id;
  }

  public static XPCNode fromByteBufferIterator(ByteBufferIterator it)
  {
    final String nodeID = it.nextZeroTerminatedString();
  
    final double lowerLatitudeDegrees = it.nextDouble();
    final double lowerLongitudeDegrees = it.nextDouble();
    final double upperLatitudeDegrees = it.nextDouble();
    final double upperLongitudeDegrees = it.nextDouble();
  
    final Sector sector = Sector.newFromDegrees(lowerLatitudeDegrees, lowerLongitudeDegrees, upperLatitudeDegrees, upperLongitudeDegrees);
  
    final int pointsCount = it.nextInt32();
  
    final double minHeight = it.nextDouble();
    final double maxHeight = it.nextDouble();
  
    return new XPCNode(nodeID, sector, pointsCount, minHeight, maxHeight);
  }

  public final Sector getSector()
  {
    return _sector;
  }

  public final double getMinHeight()
  {
    return _minHeight;
  }

  public final double getMaxHeight()
  {
    return _maxHeight;
  }

  public final void errorDownloadingContent()
  {
    // I don't know how to deal with it (DGD)  :(
  }

  public final void setContent(java.util.ArrayList<XPCNode> children, DirectMesh mesh)
  {
    _loadedContent = true;
  
    for (int i = 0; i < _childrenSize; i++)
    {
      XPCNode child = _children.get(i);
      child._release();
    }
  
    _children = children;
    _childrenSize = (_children == null) ? 0 : _children.size();
  
    if (_mesh != null)
       _mesh.dispose();
    _mesh = mesh;
  }

  public final boolean isCanceled()
  {
    return _canceled;
  }


  public final long render(XPCPointCloud pointCloud, String treeID, G3MRenderContext rc, ITimer lastSplitTimer, GLState glState, Frustum frustum, long nowInMS, boolean renderDebug, XPCSelectionResult selectionResult, XPCRenderingState renderingState)
  {
  
    long renderedCount = 0;
  
    boolean renderedInThisFrame = false;
  
    final Sphere bounds = getBounds(rc, pointCloud);
    if (bounds != null)
    {
      final boolean isVisible = bounds.touchesFrustum(frustum);
      if (isVisible)
      {
  
        if (renderDebug)
        {
          bounds.render(rc, glState, Color.WHITE);
        }
  
        if ((_projectedArea == -1) || ((_projectedAreaTS + 50) < nowInMS)) // 167
        {
          _projectedArea = bounds.projectedArea(rc);
          _projectedAreaTS = nowInMS;
        }
  
        final boolean isBigEnough = (_id.length() == 0) || (_projectedArea >= pointCloud.getMinProjectedArea());
        if (isBigEnough)
        {
          renderedInThisFrame = true;
  
          //if (selectionRay != NULL) {
          //  if (touchesRay(selectionRay)) {
          //    bounds->render(rc, glState, Color::YELLOW);
          //  }
          //}
  
          //ILogger::instance()->logInfo("- Rendering node \"%s\"", _id.c_str());
  
          if (_children != null)
          {
            for (int i = 0; i < _childrenSize; i++)
            {
              XPCNode child = _children.get(i);
              renderedCount += child.render(pointCloud, treeID, rc, lastSplitTimer, glState, frustum, nowInMS, renderDebug, selectionResult, renderingState);
            }
            //if (_childrenSize == 0) {
            //  renderingState._pointSize = pointCloud->getDevicePointSize();
            //}
          }
  
          if (_children == null)
          {
            renderingState.reset();
          }
  
          if (_loadedContent)
          {
            if (_mesh != null)
            {
              renderedCount += _mesh.getRenderVerticesCount();
  
              final float pointSize = pointCloud.getDevicePointSize();
              if (pointCloud.isDynamicPointSize())
              {
                final IMathUtils mu = IMathUtils.instance();
  
                final float dynPointSize = mu.clamp(mu.sqrt((float)(_projectedArea / renderedCount)), pointSize, renderingState._pointSize);
  
                renderingState._pointSize = dynPointSize;
  
                _mesh.setPointSize(dynPointSize);
              }
              else
              {
                _mesh.setPointSize(pointSize);
              }
  
              _mesh.render(rc, glState);
            }
          }
          else
          {
            if (!_loadingContent)
            {
              if ((_id.length() == 0) || (lastSplitTimer.elapsedTimeInMilliseconds() > 0))
              {
                lastSplitTimer.start();
                _canceled = false;
                _loadingContent = true;
                loadContent(pointCloud, treeID, rc);
              }
            }
          }
  
  
        }
      }
    }
  
    if (_renderedInPreviousFrame != renderedInThisFrame)
    {
      if (_renderedInPreviousFrame)
      {
        cancel();
      }
      _renderedInPreviousFrame = renderedInThisFrame;
    }
  
    return renderedCount;
  }

  public final boolean selectPoints(XPCSelectionResult selectionResult, XPCPointCloud pointCloud, String treeID)
  {
    if (_bounds == null)
    {
      return false;
    }
  
    if (!_bounds.touchesRay(selectionResult._ray))
    {
      return false;
    }
  
    if (!selectionResult.isInterestedIn(_bounds))
    {
      return false;
    }
  
    boolean selectedPoint = false;
  
    if (_mesh != null)
    {
      final String nodeID = getID();
  
      final int verticesCount = _mesh.getVerticesCount();
  
      MutableVector3D vertex = new MutableVector3D();
      for (int i = 0; i < verticesCount; i++)
      {
        _mesh.getVertex(i, vertex);
  
        if (selectionResult.evaluateCantidate(vertex, pointCloud, treeID, nodeID, i))
        {
          selectedPoint = true;
        }
      }
    }
  
    if (_children != null)
    {
      for (int i = 0; i < _childrenSize; i++)
      {
        XPCNode child = _children.get(i);
        if (child.selectPoints(selectionResult, pointCloud, treeID))
        {
          selectedPoint = true;
        }
      }
    }
  
    return selectedPoint;
  }

  public final void cancel()
  {
    _canceled = true;
  
    if (_loadingContent)
    {
      _loadingContent = false;
      cancelLoadContent();
    }
  
    if (_loadedContent)
    {
      _loadedContent = false;
      unloadContent();
    }
  
    unloadChildren();
  }

  public final void reload()
  {
    if (_loadingContent)
    {
      _loadingContent = false;
      cancelLoadContent();
    }
  
    if (_loadedContent)
    {
      _loadedContent = false;
      unloadContent();
    }
  
    unloadChildren();
  
    if (_mesh != null)
       _mesh.dispose();
    _mesh = null;
  
    if (_bounds != null)
       _bounds.dispose();
    _bounds = null;
  }

}