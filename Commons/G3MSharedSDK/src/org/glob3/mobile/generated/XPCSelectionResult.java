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


//class Ray;
//class G3MRenderContext;
//class GLState;
//class Sphere;


public class XPCSelectionResult
{
  private double _nearestSquaredDistance;
  private MutableVector3D _nearestPoint = new MutableVector3D();
  private Sphere _selectionSphere;

  public final Ray _ray;

  public XPCSelectionResult(Ray ray)
  {
     _ray = ray;
     _selectionSphere = null;
     _nearestSquaredDistance = Double.NaN;
  }

  public void dispose()
  {
    if (_ray != null)
       _ray.dispose();
    if (_selectionSphere != null)
       _selectionSphere.dispose();
  }

  public final Sphere getSelectionSphere()
  {
    if (_selectionSphere == null)
    {
      _selectionSphere = new Sphere(_nearestPoint.asVector3D(), IMathUtils.instance().sqrt(_nearestSquaredDistance));
    }
    return _selectionSphere;
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

  public final boolean evaluateCantidate(MutableVector3D candidate)
  {
    final double candidateSquaredDistance = _ray.squaredDistanceTo(candidate);
    if ((_nearestSquaredDistance != _nearestSquaredDistance) || (candidateSquaredDistance < _nearestSquaredDistance))
    {
  
      _nearestPoint.copyFrom(candidate);
      _nearestSquaredDistance = candidateSquaredDistance;
  
      if (_selectionSphere != null)
         _selectionSphere.dispose();
      _selectionSphere = null;
  
      return true;
    }
  
    return false;
  }

}