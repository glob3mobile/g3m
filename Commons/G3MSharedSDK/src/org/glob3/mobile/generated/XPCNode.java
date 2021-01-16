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



//class JSONObject;
//class JSONArray;
//class Sector;
//class XPCPointCloud;
//class G3MRenderContext;
//class GLState;
//class Frustum;
//class XPCPointColorizer;
//class Sphere;


public class XPCNode
{

  private static XPCNode fromJSON(JSONObject jsonObject)
  {
  
    final String id = jsonObject.getAsString("id").value();
  
    final Sector sector = XPCParsing.parseSector(jsonObject.getAsArray("sector"));
  
    final int pointsCount = (int) jsonObject.getAsNumber("pointsCount").value();
  
    final double minZ = jsonObject.getAsNumber("minZ").value();
    final double maxZ = jsonObject.getAsNumber("maxZ").value();
  
    return new XPCNode(id, sector, pointsCount, minZ, maxZ);
  }


  private final String _id;

  private final Sector _sector;

  private final int _pointsCount;

  private final double _minZ;
  private final double _maxZ;

  private final java.util.ArrayList<XPCNode> _children;
  private final int _childrenSize;

  private XPCNode(String id, Sector sector, int pointsCount, double minZ, double maxZ)
  {
     _id = id;
     _sector = sector;
     _pointsCount = pointsCount;
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


  public static java.util.ArrayList<XPCNode> fromJSON(JSONArray jsonArray)
  {
    if (jsonArray == null)
    {
      return null;
    }
  
    java.util.ArrayList<XPCNode> result = new java.util.ArrayList<XPCNode>();
  
    final int size = jsonArray.size();
  
    for (int i = 0; i < size; i++)
    {
      final JSONObject jsonObject = jsonArray.getAsObject(i);
      XPCNode dimension = fromJSON(jsonObject);
      if (dimension == null)
      {
        // release the memory allocated up to here
        for (int j = 0; j < result.size(); j++)
        {
          if (result.get(j) != null)
             result.get(j).dispose();
        }
        result = null;
  
        return null;
      }
  
      result.add(dimension);
    }
  
    return result;
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