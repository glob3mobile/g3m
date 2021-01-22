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
//class XPCPoint;
//class DirectMesh;


public class XPCNode extends RCObject
{

  private final String _id;

  private final Sector _sector;

  private final double _minZ;
  private final double _maxZ;

  private java.util.ArrayList<XPCNode> _children;
  private int _childrenSize;

  private java.util.ArrayList<XPCPoint> _points;
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
  
    final float deltaHeight = pointCloud.getDeltaHeight();
    final float verticalExaggeration = pointCloud.getVerticalExaggeration();
  
    java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(10);
    points.add( planet.toCartesian( _sector.getNE()     , (_minZ + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getNE()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getNW()     , (_minZ + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getNW()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getSE()     , (_minZ + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getSE()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getSW()     , (_minZ + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getSW()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getCenter() , (_minZ + deltaHeight) * verticalExaggeration ) );
    points.add( planet.toCartesian( _sector.getCenter() , (_maxZ + deltaHeight) * verticalExaggeration ) );
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: check if the sphere fits into the parent's one
    //  if (_parent) {
    //    _parent->updateBoundingSphereWith(rc, vectorSet, _boundingSphere);
    //  }
  
    //return Sphere::enclosingSphere(points, 0.1);
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
  
    final long deltaPriority = 100 - _id.length(); // + _pointsCount
  
    _contentRequestID = pointCloud.requestNodeContentBuffer(_downloader, treeID, _id, deltaPriority, new XPCNodeContentDownloadListener(pointCloud, this, rc.getThreadUtils(), rc.getPlanet()), true);
  }


  //void XPCNode::cancelTasks() {
  //  if (_featuresTask != NULL) {
  //    _featuresTask->cancel();
  //    _featuresTask = NULL;
  //  }
  //}
  
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
    if (_points != null)
    {
      for (int i = 0; i < _points.size(); i++)
      {
        XPCPoint point = _points.get(i);
        if (point != null)
           point.dispose();
      }
      _points = null;
      _points = null;
    }
  
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
        child.unload();
        child._release();
      }
  
      _children = null;
      _children = null;
      _childrenSize = 0;
    }
  }
  private void unload()
  {
  //  cancelTasks();
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


  private XPCNode(String id, Sector sector, double minZ, double maxZ)
  {
     _id = id;
     _sector = sector;
     _minZ = minZ;
     _maxZ = maxZ;
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
     _points = null;
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
  
    if (_points != null)
    {
      for (int i = 0; i < _points.size(); i++)
      {
        XPCPoint point = _points.get(i);
        if (point != null)
           point.dispose();
      }
  
      _points = null;
    }
  
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
  
    final double minZ = it.nextDouble();
    final double maxZ = it.nextDouble();
  
    return new XPCNode(nodeID, sector, minZ, maxZ);
  }


  public final long render(XPCPointCloud pointCloud, String treeID, G3MRenderContext rc, GLState glState, Frustum frustum, long nowInMS)
  {
  
    long renderedCount = 0;
  
    boolean renderedInThisFrame = false;
  
    final Sphere bounds = getBounds(rc, pointCloud);
    if (bounds != null)
    {
      final boolean isVisible = bounds.touchesFrustum(frustum);
      if (isVisible)
      {
        if ((_projectedArea == -1) || ((_projectedAreaTS + 100) < nowInMS))
        {
          final double projectedArea = bounds.projectedArea(rc);
          _projectedArea = projectedArea;
          _projectedAreaTS = nowInMS;
        }
  
        final boolean isBigEnough = (_projectedArea >= pointCloud.getMinProjectedArea());
        if (isBigEnough)
        {
          renderedInThisFrame = true;
  
  //        ILogger::instance()->logInfo("- Rendering node \"%s\"", _id.c_str());
  
          // bounds->render(rc, glState, Color::blue());
  
          if (_loadedContent)
          {
            if (_mesh != null)
            {
              _mesh.render(rc, glState);
              renderedCount += _mesh.getRenderVerticesCount();
            }
          }
          else
          {
            if (!_loadingContent)
            {
              _canceled = false;
              _loadingContent = true;
              loadContent(pointCloud, treeID, rc);
            }
          }
  
          if (_children != null)
          {
            for (int i = 0; i < _childrenSize; i++)
            {
              XPCNode child = _children.get(i);
              renderedCount += child.render(pointCloud, treeID, rc, glState, frustum, nowInMS);
            }
          }
  
        }
      }
    }
  
    if (_renderedInPreviousFrame != renderedInThisFrame)
    {
      if (_renderedInPreviousFrame)
      {
        unload();
      }
      _renderedInPreviousFrame = renderedInThisFrame;
    }
  
    return renderedCount;
  }

  public final Sector getSector()
  {
    return _sector;
  }

  public final void errorDownloadingContent()
  {
    // I don't know how to deal with it (DGD)  :(
  }

  public final void setContent(java.util.ArrayList<XPCNode> children, java.util.ArrayList<XPCPoint> points, DirectMesh mesh)
  {
    _loadedContent = true;
  
    for (int i = 0; i < _childrenSize; i++)
    {
      XPCNode child = _children.get(i);
      child._release();
    }
  
    _children = children;
    _childrenSize = (_children == null) ? 0 : _children.size();
  
    if (_points != null)
    {
      for (int i = 0; i < _points.size(); i++)
      {
        XPCPoint point = _points.get(i);
        if (point != null)
           point.dispose();
      }
  
      _points = null;
    }
  
    _points = points;
  
    if (_mesh != null)
       _mesh.dispose();
    _mesh = mesh;
  }

  public final boolean isCanceled()
  {
    return _canceled;
  }

}