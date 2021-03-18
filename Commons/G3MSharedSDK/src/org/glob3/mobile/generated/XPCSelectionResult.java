package org.glob3.mobile.generated;
//
//  XPCSelectionResult.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/22/21.
//

//
//  XPCSelectionResult.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/22/21.
//


//class XPCPointCloud;
//class Ray;
//class G3MRenderContext;
//class GLState;
//class Sphere;
//class Planet;



public class XPCSelectionResult
{
  private double _nearestSquaredDistance;
  private MutableVector3D _nearestPoint = new MutableVector3D();


  // point full id
  private XPCPointCloud _pointCloud;
  private String _treeID;
  private String _nodeID;
  private int _pointIndex;


  private Sphere _selectionSphere;

  public final Ray _ray;

  public XPCSelectionResult(Ray ray)
  {
     _ray = ray;
     _selectionSphere = null;
     _nearestSquaredDistance = Double.NaN;
     _pointCloud = null;
     _treeID = "";
     _nodeID = "";
     _pointIndex = -1;
  }

  public void dispose()
  {
    if (_ray != null)
       _ray.dispose();
    if (_selectionSphere != null)
       _selectionSphere.dispose();
  
    if (_pointCloud != null)
    {
      _pointCloud._release();
    }
  }

  public final Sphere getSelectionSphere()
  {
    if (_selectionSphere == null)
    {
      _selectionSphere = new Sphere(_nearestPoint.asVector3D(), IMathUtils.instance().sqrt(_nearestSquaredDistance));
    }
    return _selectionSphere;
  }

  public final boolean notifyPointCloud(Planet planet)
  {
    final Vector3D cartesian = _nearestPoint.asVector3D();
    final Geodetic3D scaledGeodetic = planet.toGeodetic3D(cartesian);
  
    return _pointCloud.selectedPoint(cartesian, scaledGeodetic, _treeID, _nodeID, _pointIndex, IMathUtils.instance().sqrt(_nearestSquaredDistance));
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    _ray.render(rc, glState, Color.YELLOW, 1);
  
    getSelectionSphere().render(rc, glState, Color.YELLOW);
  }

  public final boolean isInterestedIn(Sphere area)
  {
    if ((_nearestSquaredDistance != _nearestSquaredDistance))
    {
      return true;
    }
  
    final double squaredDistanceToCenter = _ray.squaredDistanceTo(area._center);
  
    return (squaredDistanceToCenter - area.getRadiusSquared()) < _nearestSquaredDistance;
  }

  public final boolean evaluateCantidate(MutableVector3D cartesianPoint, XPCPointCloud pointCloud, String treeID, String nodeID, int pointIndex)
  {
    final double candidateSquaredDistance = _ray.squaredDistanceTo(cartesianPoint);
    if ((_nearestSquaredDistance != _nearestSquaredDistance) || (candidateSquaredDistance < _nearestSquaredDistance))
    {
  
      _nearestPoint.set(cartesianPoint);
      _nearestSquaredDistance = candidateSquaredDistance;
  
      if (pointCloud != _pointCloud)
      {
        if (_pointCloud != null)
        {
          _pointCloud._release();
        }
        _pointCloud = pointCloud;
        if (_pointCloud != null)
        {
          _pointCloud._retain();
        }
      }
      _treeID = treeID;
      _nodeID = nodeID;
      _pointIndex = pointIndex;
  
      if (_selectionSphere != null)
         _selectionSphere.dispose();
      _selectionSphere = null;
  
      return true;
    }
  
    return false;
  }

}