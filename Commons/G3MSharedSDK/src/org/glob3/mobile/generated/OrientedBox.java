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
//class Mesh;
//class Color;


public class OrientedBox extends BoundingVolume
{

  private final double _lowerX;
  private final double _lowerY;
  private final double _lowerZ;
  private final double _upperX;
  private final double _upperY;
  private final double _upperZ;
  private final MutableMatrix44D _transformMatrix;

  private java.util.ArrayList<Vector3D> _cornersD = null; // cache for getCorners() method

  private Mesh _mesh;
  private void createMesh(Color color)
  {
    float[] v = { (float)_upperX, (float)_upperY, (float)_upperZ, (float)_lowerX, (float)_upperY, (float)_upperZ, (float)_upperX, (float)_lowerY, (float)_upperZ, (float)_lowerX, (float)_lowerY, (float)_upperZ, (float)_upperX, (float)_upperY, (float)_lowerZ, (float)_lowerX, (float)_upperY, (float)_lowerZ, (float)_upperX, (float)_lowerY, (float)_lowerZ, (float)_lowerX, (float)_lowerY, (float)_lowerZ };
  
    short[] i = { 0, 1, 1, 3, 3, 2, 2, 0, 0, 4, 1, 5, 3, 7, 2, 6, 4, 5, 5, 7, 7, 6, 6, 4 };
  
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    final int numVertices = 8;
    for (int n = 0; n<numVertices; n++)
    {
      vertices.add(v[n *3], v[n *3+1], v[n *3+2]);
    }
  
    final int numIndices = 24;
    for (int n = 0; n<numIndices; n++)
    {
      indices.add(i[n]);
    }
  
    _mesh = new IndexedMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, color);
    if (vertices != null)
       vertices.dispose();
  }

  public OrientedBox(Vector3D lower, Vector3D upper, MutableMatrix44D transformMatrix)
  {
     _lowerX = lower._x;
     _lowerY = lower._y;
     _lowerZ = lower._z;
     _upperX = upper._x;
     _upperY = upper._y;
     _upperZ = upper._z;
     _transformMatrix = new MutableMatrix44D(transformMatrix);
     _mesh = null;
  }

  public OrientedBox(Box box, MutableMatrix44D transformMatrix)
  {
     _lowerX = box.getLower()._x;
     _lowerY = box.getLower()._y;
     _lowerZ = box.getLower()._z;
     _upperX = box.getUpper()._x;
     _upperY = box.getUpper()._y;
     _upperZ = box.getUpper()._z;
     _transformMatrix = new MutableMatrix44D(transformMatrix);
     _mesh = null;
  }

  public void dispose()
  {
    if (_transformMatrix != null)
       _transformMatrix.dispose();
    if (_mesh != null)
       if (_mesh != null)
          _mesh.dispose();
    super.dispose();
  }


  public final double projectedArea(G3MRenderContext rc)
  {
    int __TODO_OrientedBox_projectedArea;
    return 0;
  }

  public final void render(G3MRenderContext rc, GLState parentState)
  {
    if (_mesh == null)
    {
      createMesh(Color.newFromRGBA(1.0f, 0.0f, 1.0f, 1.0f));
    }
    _mesh.render(rc, parentState);
  }

  public final boolean touches(BoundingVolume that)
  {
    int __TODO_OrientedBox_touches;
    return true;
  }

  public final boolean touchesBox(Box that)
  {
    int __TODO_OrientedBox_touchesBox;
    return true;
  }

  public final boolean touchesSphere(Sphere that)
  {
    int __TODO_OrientedBox_touchesSphere;
    return true;
  }


  public final boolean touchesFrustum(Frustum frustum)
  {
    return frustum.touchesWithOrientedBox(this);
  }

  public final boolean contains(Vector3D point)
  {
    int __TODO_OrientedBox_contains;
    return true;
  }

  public final boolean fullContains(BoundingVolume that)
  {
    int __TODO_OrientedBox_fullContains;
    return true;
  }

  public final boolean fullContainedInBox(Box that)
  {
    int __TODO_OrientedBox_fullContainedInBox;
    return true;
  }

  public final boolean fullContainedInSphere(Sphere that)
  {
    int __TODO_OrientedBox_fullContainedInSphere;
    return true;
  }


  public final BoundingVolume mergedWith(BoundingVolume that)
  {
    int __TODO_OrientedBox_mergedWith;
    return (BoundingVolume) null;
  }


  public final BoundingVolume mergedWithBox(Box that)
  {
    int __TODO_OrientedBox_mergedWithBox;
    return (BoundingVolume) null;
  }


  public final BoundingVolume mergedWithSphere(Sphere that)
  {
    int __TODO_OrientedBox_mergedWithSphere;
    return (BoundingVolume) null;
  }



  public final Sphere createSphere()
  {
    int __TODO_OrientedBox_createSphere;
    return (Sphere) null;
  }



  public final java.util.ArrayList<Vector3D> getCorners()
  {
    int __TODO_GUS_implement_all_the_OrientedBox_empty_methods;
  
  
    if (_cornersD == null) {
      _cornersD = new java.util.ArrayList<Vector3D>(8);
      _cornersD.add(new Vector3D(_upperX, _upperY, _upperZ).transformedBy(_transformMatrix, 1));
      _cornersD.add(new Vector3D(_lowerX, _upperY, _upperZ).transformedBy(_transformMatrix, 1));
      _cornersD.add(new Vector3D(_upperX, _lowerY, _upperZ).transformedBy(_transformMatrix, 1));
      _cornersD.add(new Vector3D(_lowerX, _lowerY, _upperZ).transformedBy(_transformMatrix, 1));
      _cornersD.add(new Vector3D(_upperX, _upperY, _lowerZ).transformedBy(_transformMatrix, 1));
      _cornersD.add(new Vector3D(_lowerX, _upperY, _lowerZ).transformedBy(_transformMatrix, 1));
      _cornersD.add(new Vector3D(_upperX, _lowerY, _lowerZ).transformedBy(_transformMatrix, 1));
      _cornersD.add(new Vector3D(_lowerX, _lowerY, _lowerZ).transformedBy(_transformMatrix, 1));
    }
    return _cornersD;
  
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
    Quadric front = Quadric.fromPlane(1, 0, 0, -_upperX).transformBy(inverse, transpose);
    Quadric back = Quadric.fromPlane(-1, 0, 0, _lowerX).transformBy(inverse, transpose);
    Quadric left = Quadric.fromPlane(0, -1, 0, _lowerY).transformBy(inverse, transpose);
    Quadric right = Quadric.fromPlane(0, 1, 0, -_upperY).transformBy(inverse, transpose);
    Quadric top = Quadric.fromPlane(0, 0, 1, -_upperZ).transformBy(inverse, transpose);
    Quadric bottom = Quadric.fromPlane(0, 0, -1, _lowerZ).transformBy(inverse, transpose);
  
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