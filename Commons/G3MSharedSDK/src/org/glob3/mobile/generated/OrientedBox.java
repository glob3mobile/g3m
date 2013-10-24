package org.glob3.mobile.generated; 
//
//  OrientedBox.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 22/10/13.
//
//

//
//  OrientedBox.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 22/10/13.
//
//



//class Vector3D;
//class MutableMatrix44D;


public class OrientedBox extends BoundingVolume
{

  private final Vector3D _halfExtent ;
  private final MutableMatrix44D _transformMatrix = new MutableMatrix44D();

  public OrientedBox(Vector3D extent, MutableMatrix44D transformMatrix)
  {
     _halfExtent = new Vector3D(extent.times(0.5));
     _transformMatrix = new MutableMatrix44D(transformMatrix);
  }

  public void dispose()
  {
  }

  public final double projectedArea(G3MRenderContext rc)
  {
  }
  public final void render(G3MRenderContext rc, GLState parentState)
  {
  }

  public final boolean touches(BoundingVolume that)
  {
  }
  public final boolean touchesBox(Box that)
  {
  }
  public final boolean touchesSphere(Sphere that)
  {
  }

  public final boolean touchesFrustum(Frustum frustum)
  {
    return frustum.touchesWithOrientedBox(this);
  }

  public final boolean contains(Vector3D point)
  {
  }

  public final boolean fullContains(BoundingVolume that)
  {
  }
  public final boolean fullContainedInBox(Box that)
  {
  }
  public final boolean fullContainedInSphere(Sphere that)
  {
  }

  public final BoundingVolume mergedWith(BoundingVolume that)
  {
  }
  public final BoundingVolume mergedWithBox(Box that)
  {
  }
  public final BoundingVolume mergedWithSphere(Sphere that)
  {
  }

  public final Sphere createSphere()
  {
  }

  public final java.util.ArrayList<Vector3D> getCorners()
  {
    int __TODO_convert_to_java_this_code_as_in_Box_class;
  
    Vector3D[] corners = { new Vector3D(_halfExtent._x, _halfExtent._y, _halfExtent._z).transformedBy(_transformMatrix, 1), new Vector3D(-_halfExtent._x, _halfExtent._y, _halfExtent._z).transformedBy(_transformMatrix, 1), new Vector3D(_halfExtent._x, -_halfExtent._y, _halfExtent._z).transformedBy(_transformMatrix, 1), new Vector3D(-_halfExtent._x, -_halfExtent._y, _halfExtent._z).transformedBy(_transformMatrix, 1), new Vector3D(_halfExtent._x, _halfExtent._y, -_halfExtent._z).transformedBy(_transformMatrix, 1), new Vector3D(-_halfExtent._x, _halfExtent._y, -_halfExtent._z).transformedBy(_transformMatrix, 1), new Vector3D(_halfExtent._x, -_halfExtent._y, -_halfExtent._z).transformedBy(_transformMatrix, 1), new Vector3D(-_halfExtent._x, -_halfExtent._y, -_halfExtent._z).transformedBy(_transformMatrix, 1) };
  
    return new java.util.ArrayList<Vector3D>(corners, corners+8);
  }
  public final java.util.ArrayList<Double> intersectionsDistances(Vector3D origin, Vector3D direction)
  {
    java.util.ArrayList<Double> distances = new java.util.ArrayList<Double>();
  
    double tmin = -1e10;
    double tmax = 1e10;
    double t1;
    double t2;
  
    // create quadrics for the 6 sides
    // QUESTION: CREATE 6 MATRICES EVERYTIME OR SAVE THEM INSIDE THE CLASS??
    MutableMatrix44D inverse = _transformMatrix.inversed();
    MutableMatrix44D transpose = inverse.transposed();
    Quadric front = Quadric.fromPlane(1, 0, 0, -_halfExtent._x).transformBy(inverse, transpose);
    Quadric back = Quadric.fromPlane(-1, 0, 0, -_halfExtent._x).transformBy(inverse, transpose);
    Quadric left = Quadric.fromPlane(0, -1, 0, -_halfExtent._y).transformBy(inverse, transpose);
    Quadric right = Quadric.fromPlane(0, 1, 0, -_halfExtent._y).transformBy(inverse, transpose);
    Quadric top = Quadric.fromPlane(0, 0, 1, -_halfExtent._z).transformBy(inverse, transpose);
    Quadric bottom = Quadric.fromPlane(0, 0, -1, -_halfExtent._z).transformBy(inverse, transpose);
  
    // ALL THIS CODE COULD BE OPTIMIZED
    // FOR EXAMPLE, WHEN CUADRICS ARE PLANES, MATH EXPRESSIONS ARE SIMPLER
  
    // intersecction with X planes
    java.util.ArrayList<Double> frontDistance = front.intersectionsDistances(origin, direction);
    java.util.ArrayList<Double> backDistance = back.intersectionsDistances(origin, direction);
    if (frontDistance.size()==1 && backDistance.size()==1)
    {
      if (frontDistance.get(0) < backDistance.get(0))
      {
        t1 = frontDistance.get(0);
        t2 = backDistance.get(0);
      }
      else
      {
        t2 = frontDistance.get(0);
        t1 = backDistance.get(0);
      }
      if (t1 > tmin)
        tmin = t1;
      if (t2 < tmax)
        tmax = t2;
    }
  
    // intersections with Y planes
    java.util.ArrayList<Double> leftDistance = left.intersectionsDistances(origin, direction);
    java.util.ArrayList<Double> rightDistance = right.intersectionsDistances(origin, direction);
    if (leftDistance.size()==1 && rightDistance.size()==1)
    {
      if (leftDistance.get(0) < rightDistance.get(0))
      {
        t1 = leftDistance.get(0);
        t2 = rightDistance.get(0);
      }
      else
      {
        t2 = leftDistance.get(0);
        t1 = rightDistance.get(0);
      }
      if (t1 > tmin)
        tmin = t1;
      if (t2 < tmax)
        tmax = t2;
    }
  
    // intersections with Z planes
    java.util.ArrayList<Double> topDistance = top.intersectionsDistances(origin, direction);
    java.util.ArrayList<Double> bottomDistance = bottom.intersectionsDistances(origin, direction);
    if (topDistance.size()==1 && bottomDistance.size()==1)
    {
      if (topDistance.get(0) < bottomDistance.get(0))
      {
        t1 = topDistance.get(0);
        t2 = bottomDistance.get(0);
      }
      else
      {
        t2 = topDistance.get(0);
        t1 = bottomDistance.get(0);
      }
      if (t1 > tmin)
        tmin = t1;
      if (t2 < tmax)
        tmax = t2;
    }
  
    if (tmin < tmax)
    {
      distances.add(tmin);
      //distances.push_back(tmax);
    }
  
    return distances;
  }


}