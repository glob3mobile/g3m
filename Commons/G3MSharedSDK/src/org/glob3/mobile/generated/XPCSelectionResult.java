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
  private Sphere _hotArea;
  private double _minimumSquaredDistanceToRay;
  private MutableVector3D _bestPoint = new MutableVector3D();

  public final Ray _ray;

  public XPCSelectionResult(Ray ray)
  {
     _ray = ray;
     _hotArea = null;
     _minimumSquaredDistanceToRay = Double.NaN;
  }

  public void dispose()
  {
    if (_ray != null)
       _ray.dispose();
    if (_hotArea != null)
       _hotArea.dispose();
  }

//  const Ray* getRay() const;

  public final Sphere getHotArea()
  {
    if (_hotArea == null)
    {
      _hotArea = new Sphere(_bestPoint.asVector3D(), IMathUtils.instance().sqrt(_minimumSquaredDistanceToRay));
    }
    return _hotArea;
  }


  //const Ray* XPCSelectionResult::getRay() const {
  //  return _ray;
  //}
  
  public final void render(G3MRenderContext rc, GLState glState)
  {
    _ray.render(rc, glState, Color.YELLOW, 1);
  
    getHotArea().render(rc, glState, Color.YELLOW);
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO__ RENDER SELECTED POINT
  }

  public final boolean isInterestedIn(Sphere area)
  {
    if ((_minimumSquaredDistanceToRay != _minimumSquaredDistanceToRay))
    {
      return true;
    }
    if (_hotArea == null)
    {
      _hotArea = new Sphere(_bestPoint.asVector3D(), IMathUtils.instance().sqrt(_minimumSquaredDistanceToRay));
    }
    return getHotArea().touchesSphere(area);
  }

  public final boolean evaluateCantidate(MutableVector3D point)
  {
    final double squaredDistanceToRay = _ray.squaredDistanceTo(point);
    if ((_minimumSquaredDistanceToRay != _minimumSquaredDistanceToRay) || (squaredDistanceToRay < _minimumSquaredDistanceToRay))
    {
      _bestPoint.copyFrom(point);
      _minimumSquaredDistanceToRay = squaredDistanceToRay;
  
      if (_hotArea != null)
         _hotArea.dispose();
      _hotArea = null;
  
      return true;
    }
  
    return false;
  }

}