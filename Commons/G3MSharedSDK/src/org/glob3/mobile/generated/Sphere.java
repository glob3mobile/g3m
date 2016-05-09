package org.glob3.mobile.generated; 
//
//  Sphere.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

//
//  Sphere.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//




public class Sphere extends BoundingVolume
{

  private Mesh _mesh;




  public static Sphere enclosingSphere(java.util.ArrayList<Vector3D> points)
  {
    if (points.size() < 2)
    {
      return null;
    }
  
    final Vector3D first = points.get(0);
  
    MutableVector3D xmin = new MutableVector3D(first);
    MutableVector3D xmax = new MutableVector3D(first);
    MutableVector3D ymin = new MutableVector3D(first);
    MutableVector3D ymax = new MutableVector3D(first);
    MutableVector3D zmin = new MutableVector3D(first);
    MutableVector3D zmax = new MutableVector3D(first);
  
    for (int i = 1; i < points.size(); i++)
    {
      final Vector3D p = points.get(i);
      if (p._x < xmin.x())
         xmin.copyFrom(p);
      if (p._x > xmax.x())
         xmax.copyFrom(p);
      if (p._y < ymin.y())
         ymin.copyFrom(p);
      if (p._y > ymax.y())
         ymax.copyFrom(p);
      if (p._z < zmin.z())
         zmin.copyFrom(p);
      if (p._z > zmax.z())
         zmax.copyFrom(p);
    }
  
    double xSpan = xmax.squaredDistanceTo(xmin);
    double ySpan = ymax.squaredDistanceTo(ymin);
    double zSpan = zmax.squaredDistanceTo(zmin);
    MutableVector3D dia1 = new MutableVector3D(xmin);
    MutableVector3D dia2 = new MutableVector3D(xmax);
    double maxSpan = xSpan;
    if (ySpan > maxSpan)
    {
      maxSpan = ySpan;
      dia1.copyFrom(ymin);
      dia2.copyFrom(ymax);
    }
    if (zSpan > maxSpan)
    {
      dia1.copyFrom(zmin);
      dia2.copyFrom(zmax);
    }
  
    MutableVector3D center = new MutableVector3D((dia1.x() + dia2.x()) / 2, (dia1.y() + dia2.y()) / 2, (dia1.z() + dia2.z()) / 2);
  
    double sqRad = dia2.squaredDistanceTo(center);
    double radius = IMathUtils.instance().sqrt(sqRad);
    for (int i = 0; i < points.size(); i++)
    {
      final Vector3D p = points.get(i);
      double d = center.squaredDistanceTo(p);
      if (d > sqRad)
      {
        double r = IMathUtils.instance().sqrt(d);
        radius = (radius + r) * 0.5f;
        sqRad = radius * radius;
        double offset = r - radius;
        //center = (radius * center + offset * p) / r;
        center.set((radius * center.x() + offset * p._x) / r, (radius * center.y() + offset * p._y) / r, (radius * center.z() + offset * p._z) / r);
      }
    }
  
    return new Sphere(center.asVector3D(), radius);
  }

  public final Vector3D _center ;
  public final double _radius;
  public final double _radiusSquared;


  public Sphere(Vector3D center, double radius)
  {
     _center = new Vector3D(center);
     _radius = radius;
     _radiusSquared = radius * radius;
     _mesh = null;
  }

  public Sphere(Sphere that)
  {
     _center = new Vector3D(that._center);
     _radius = that._radius;
     _radiusSquared = that._radiusSquared;
     _mesh = null;
  }

  public final Vector3D getCenter()
  {
    return _center;
  }

  public final double getRadius()
  {
    return _radius;
  }

  public final double getRadiusSquared()
  {
    return _radiusSquared;
  }

  public final double projectedArea(G3MRenderContext rc)
  {
    return rc.getCurrentCamera().getProjectedSphereArea(this);
  }
  //  Vector2I projectedExtent(const G3MRenderContext* rc) const;

  public final void render(G3MRenderContext rc, GLState parentState, Color color)
  {
    if (_mesh == null)
    {
      _mesh = createWireframeMesh(color, (short) 16);
    }
    _mesh.render(rc, parentState);
  }

  public final boolean touches(BoundingVolume that)
  {
    if (that == null)
    {
      return false;
    }
    return that.touchesSphere(this);
  }

  public final boolean touchesBox(Box that)
  {
    final Vector3D p = that.closestPoint(_center);
    final Vector3D v = p.sub(_center);
    return v.dot(v) <= (_radius * _radius);
  }
  public final boolean touchesFrustum(Frustum frustum)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning This implementation could gives false positives
    // this implementation is not right exact, but it's faster.
    if (frustum.getNearPlane().signedDistance(_center) > _radius)
       return false;
    if (frustum.getFarPlane().signedDistance(_center) > _radius)
       return false;
    if (frustum.getLeftPlane().signedDistance(_center) > _radius)
       return false;
    if (frustum.getRightPlane().signedDistance(_center) > _radius)
       return false;
    if (frustum.getTopPlane().signedDistance(_center) > _radius)
       return false;
    if (frustum.getBottomPlane().signedDistance(_center) > _radius)
       return false;
    return true;
  }
  public final boolean touchesSphere(Sphere that)
  {
    final Vector3D d = _center.sub(that._center);
    final double squaredDist = d.dot(d);
    final double radiusSum = _radius + that._radius;
    return squaredDist <= (radiusSum * radiusSum);
  }

  public final BoundingVolume mergedWith(BoundingVolume that)
  {
    if (that == null)
    {
      return null;
    }
    return that.mergedWithSphere(this);
  }

  public final BoundingVolume mergedWithBox(Box that)
  {
    if (that.fullContainedInSphere(this))
    {
      return new Sphere(this);
    }
  
    final Vector3D upper = that.getUpper();
    final Vector3D lower = that.getLower();
  
    double minX = _center._x - _radius;
    if (lower._x < minX)
    {
       minX = lower._x;
    }
  
    double maxX = _center._x + _radius;
    if (upper._x > maxX)
    {
       maxX = upper._x;
    }
  
    double minY = _center._y - _radius;
    if (lower._y < minY)
    {
       minY = lower._y;
    }
  
    double maxY = _center._y + _radius;
    if (upper._y > maxY)
    {
       maxY = upper._y;
    }
  
    double minZ = _center._z - _radius;
    if (lower._z < minZ)
    {
       minZ = lower._z;
    }
  
    double maxZ = _center._z + _radius;
    if (upper._z > maxZ)
    {
       maxZ = upper._z;
    }
  
    return new Box(new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ));
  
    /* Diego: creo que este test ya no hace falta, porque el coste del método
     fullContainedInBox es casi tanto es casi similar a todo lo anterior
     if (fullContainedInBox(that)) {
     return new Box(*that);
     }
     if (that->fullContainedInSphere(this)) {
     return new Sphere(*this);
     }*/
  
  }
  public final BoundingVolume mergedWithSphere(Sphere that)
  {
    final double d = _center.distanceTo(that._center);
  
    if (d + that._radius <= _radius)
    {
      return new Sphere(this);
    }
    if (d + _radius <= that._radius)
    {
      return new Sphere(that);
    }
  
    final double radius = (d + _radius + that._radius) / 2.0;
    final Vector3D u = _center.sub(that._center).normalized();
    final Vector3D center = _center.add(u.times(radius - _radius));
  
    return new Sphere(center, radius);
  }

  public final boolean contains(Vector3D point)
  {
    return _center.squaredDistanceTo(point) <= _radiusSquared;
  }

  public final boolean fullContains(BoundingVolume that)
  {
    return that.fullContainedInSphere(this);
  }

  public final boolean fullContainedInBox(Box that)
  {
    final Vector3D upper = that.getUpper();
    final Vector3D lower = that.getLower();
    if (_center._x + _radius > upper._x)
       return false;
    if (_center._x - _radius < lower._x)
       return false;
    if (_center._y + _radius > upper._y)
       return false;
    if (_center._y - _radius < lower._y)
       return false;
    if (_center._z + _radius > upper._z)
       return false;
    if (_center._z - _radius < lower._z)
       return false;
    return true;
  }
  public final boolean fullContainedInSphere(Sphere that)
  {
    final double d = _center.distanceTo(that._center);
    return (d + _radius <= that._radius);
  }

  public final Sphere createSphere()
  {
    return new Sphere(this);
  }

  public static Sphere createSphereContainingPoints(java.util.ArrayList<Vector3D> points)
  {
  
    Vector3D center = Vector3D.getCenter(points);
  
    double d = center.squaredDistanceTo(*points.get(0));
    for (int i = 1; i < points.size(); i++)
    {
      double di = center.squaredDistanceTo(*points.get(i));
      if (di > d)
      {
        d = di;
      }
    }
  
    return new Sphere(center, IMathUtils.instance().sqrt(d));
  
  }

  public final java.util.ArrayList<Double> intersectionsDistances(double originX, double originY, double originZ, double directionX, double directionY, double directionZ)
  {
  
    //Sphere is places in the cartesian origin for this math to work
    originX -= _center._x;
    originY -= _center._y;
    originZ -= _center._z;
  
    java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
  
    // By laborious algebraic manipulation....
    final double a = directionX * directionX + directionY * directionY + directionZ * directionZ;
  
    final double b = 2.0 * (originX * directionX + originY * directionY + originZ * directionZ);
  
    final double c = originX * originX + originY * originY + originZ * originZ - _radiusSquared;
  
    // Solve the quadratic equation: ax^2 + bx + c = 0.
    // Algorithm is from Wikipedia's "Quadratic equation" topic, and Wikipedia credits
    // Numerical Recipes in C, section 5.6: "Quadratic and Cubic Equations"
    final double discriminant = b * b - 4 * a * c;
    if (discriminant < 0.0)
    {
      // no intersections
      return intersections;
    }
    else if (discriminant == 0.0)
    {
      // one intersection at a tangent point
      //return new double[1] { -0.5 * b / a };
      intersections.add(-0.5 * b / a);
      return intersections;
    }
  
    final double rootDiscriminant = IMathUtils.instance().sqrt(discriminant);
    final double root1 = (-b + rootDiscriminant) / (2 *a);
    final double root2 = (-b - rootDiscriminant) / (2 *a);
  
    // Two intersections - return the smallest first.
    if (root1 < root2)
    {
      intersections.add(root1);
      intersections.add(root2);
    }
    else
    {
      intersections.add(root2);
      intersections.add(root1);
    }
    return intersections;
  }


  //Vector2I Sphere::projectedExtent(const G3MRenderContext* rc) const {
  //  int TODO_remove_this; // Agustin: no implementes este método que va a desaparecer
  //  return Vector2I::zero();
  //}
  
  public final Mesh createWireframeMesh(Color color, short resolution)
  {
    final IMathUtils mu = IMathUtils.instance();
    final double delta = DefineConstants.PI / (resolution-1);
  
    // create vertices
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
    for (int i = 0; i<2 *resolution-2; i++)
    {
      final double longitude = -DefineConstants.PI + i *delta;
      for (int j = 0; j<resolution; j++)
      {
        final double latitude = -DefineConstants.PI/2 + j *delta;
        final double h = mu.cos(latitude);
        final double x = h * mu.cos(longitude);
        final double y = h * mu.sin(longitude);
        final double z = mu.sin(latitude);
        vertices.add(new Vector3D(x,y,z).times(_radius).add(_center));
      }
    }
  
    // create border indices for vertical lines
    ShortBufferBuilder indices = new ShortBufferBuilder();
    for (short i = 0; i<2 *resolution-2; i++)
    {
      for (short j = 0; j<resolution-1; j++)
      {
        indices.add((short)(j+i *resolution));
        indices.add((short)(j+1+i *resolution));
      }
    }
  
    // create border indices for horizontal lines
    for (short j = 1; j<resolution-1; j++)
    {
      for (short i = 0; i<2 *resolution-3; i++)
      {
        indices.add((short)(j+i *resolution));
        indices.add((short)(j+(i+1)*resolution));
      }
    }
    for (short j = 1; j<resolution-1; j++)
    {
      final short i = (short)(2 *resolution-3);
      indices.add((short)(j+i *resolution));
      indices.add((short)(j));
    }
  
    Mesh mesh = new IndexedMesh(GLPrimitive.lines(), vertices.getCenter(), vertices.create(), true, indices.create(), true, 2, 1, new Color(color), null, 0, true);
  
    if (vertices != null)
       vertices.dispose();
  
    return mesh;
  }

}