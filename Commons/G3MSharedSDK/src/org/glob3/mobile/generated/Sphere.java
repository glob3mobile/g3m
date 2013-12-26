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

  //Vector2I Sphere::projectedExtent(const G3MRenderContext* rc) const {
  //  int TODO_remove_this; // Agustin: no implementes este método que va a desaparecer
  //  return Vector2I::zero();
  //}
  
  private void createWireframeMesh(Color color, short resolution)
  {
    IMathUtils mu = IMathUtils.instance();
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
  
    _mesh = new IndexedMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, color);
  
    if (vertices != null)
       vertices.dispose();
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

  public void dispose()
  {
    if (_mesh != null)
      if (_mesh != null)
         _mesh.dispose();
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

  public final void render(G3MRenderContext rc, GLState parentState)
  {
    if (_mesh == null)
    {
      createWireframeMesh(Color.newFromRGBA(1.0f, 1.0f, 0.0f, 1.0f), (short) 16);
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

  public static java.util.ArrayList<Double> intersectionCenteredSphereWithRay(Vector3D origin, Vector3D direction, double radius)
  {
    java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
  
    // By laborious algebraic manipulation....
    final double a = direction._x * direction._x + direction._y * direction._y + direction._z * direction._z;
  
    final double b = 2.0 * (origin._x * direction._x + origin._y * direction._y + origin._z * direction._z);
  
    final double c = origin._x * origin._x + origin._y * origin._y + origin._z * origin._z - radius * radius;
  
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

  public static Vector3D closestIntersectionCenteredSphereWithRay(Vector3D origin, Vector3D direction, double radius)
  {
    java.util.ArrayList<Double> distances = Sphere.intersectionCenteredSphereWithRay(origin, direction, radius);
    if (distances.isEmpty())
    {
      return Vector3D.nan();
    }
    return origin.add(direction.times(distances.get(0)));
  }

}