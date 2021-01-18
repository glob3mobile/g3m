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


public class XPCNode
{

  private final String _id;

  private final Sector _sector;

  private final double _minZ;
  private final double _maxZ;

  private final java.util.ArrayList<XPCNode> _children;
  private final int _childrenSize;

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


  public XPCNode(String id, Sector sector, double minZ, double maxZ)
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
  
  }

  public void dispose()
  {
    if (_sector != null)
       _sector.dispose();
  
    for (int i = 0; i < _childrenSize; i++)
    {
      XPCNode child = _children.get(i);
      if (child != null)
         child.dispose();
    }
  }

  public final long render(XPCPointCloud pointCloud, G3MRenderContext rc, GLState glState, Frustum frustum, long nowInMS)
  {
  
    long renderedCount = 0;
  
    boolean renderedInThisFrame = false;
  
    final Sphere bounds = getBounds(rc, pointCloud);
    if (bounds != null)
    {
      final boolean isVisible = bounds.touchesFrustum(frustum);
      if (isVisible)
      {
        if ((_projectedArea == -1) || ((_projectedAreaTS + 25) < nowInMS))
        {
          final double projectedArea = bounds.projectedArea(rc);
          _projectedArea = projectedArea;
          _projectedAreaTS = nowInMS;
        }
  
        final boolean isBigEnough = (_projectedArea >= pointCloud.getMinProjectedArea());
        if (isBigEnough)
        {
          renderedInThisFrame = true;
  
          if (_loadedContent)
          {
            renderedCount += rawRender(pointCloud, rc, glState);
          }
          else
          {
            if (!_loadingContent)
            {
              _loadingContent = true;
              loadContent(rc);
            }
          }
  
          if (_children != null)
          {
            for (int i = 0; i < _childrenSize; i++)
            {
              XPCNode child = _children.get(i);
              renderedCount += child.render(pointCloud, rc, glState, frustum, nowInMS);
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

}