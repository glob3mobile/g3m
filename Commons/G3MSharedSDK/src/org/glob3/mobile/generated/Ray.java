package org.glob3.mobile.generated;
//
//  Ray.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/24/17.
//
//

//
//  Ray.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/24/17.
//
//




//class Mesh;
//class G3MRenderContext;
//class GLState;
//class Color;


public class Ray
{
  private Mesh _mesh;

  public final Vector3D _origin ;
  public final Vector3D _direction ;

  public Ray(Vector3D origin, Vector3D direction)
  {
     _origin = origin;
     _direction = direction.normalized();
     _mesh = null;
  }

  public void dispose()
  {
    if (_mesh != null)
       _mesh.dispose();
  }

  public final double distanceTo(Vector3D point)
  {
    return _direction.cross(point._x - _origin._x, point._y - _origin._y, point._z - _origin._z).length();
  }
  public final double squaredDistanceTo(Vector3D point)
  {
    return _direction.cross(point._x - _origin._x, point._y - _origin._y, point._z - _origin._z).squaredLength();
  }

  public final double distanceTo(MutableVector3D point)
  {
    return _direction.cross(point._x - _origin._x, point._y - _origin._y, point._z - _origin._z).length();
  }
  public final double squaredDistanceTo(MutableVector3D point)
  {
    return _direction.cross(point._x - _origin._x, point._y - _origin._y, point._z - _origin._z).squaredLength();
  }

  public final void render(G3MRenderContext rc, GLState parentState, Color color, float lineWidth)
  {
    if (_mesh == null)
    {
      FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithGivenCenter(_origin);
  
      vertices.add(_origin);
      vertices.add(_origin.add(_direction.times(100000)));
  
      _mesh = new DirectMesh(GLPrimitive.lineStrip(), true, vertices.getCenter(), vertices.create(), lineWidth, 1, new Color(color), null, true); // bool depthTest -  const IFloatBuffer* colors
  
      if (vertices != null)
         vertices.dispose();
    }
  
    _mesh.render(rc, parentState);
  }

  public final Vector3D pointAtTime(double t)
  {
    return _origin.add(_direction.times(t));
  }

  public static boolean closestPointsOnTwoRays(Ray ray1, Ray ray2, MutableVector3D closestPointRay1, MutableVector3D closestPointRay2)
  {
  
    closestPointRay1.set(0,0,0);
    closestPointRay2.set(0,0,0);
  
    final double a = ray1._direction.dot(ray1._direction);
    final double b = ray1._direction.dot(ray2._direction);
    final double e = ray2._direction.dot(ray2._direction);
  
    final double d = a *e - b *b;
  
    // lines are not parallel
    if (d != 0.0)
    {
      final Vector3D r = ray1._origin.sub(ray2._origin);
      final double c = ray1._direction.dot(r);
      final double f = ray2._direction.dot(r);
  
      final double s = (b *f - c *e) / d;
      final double t = (a *f - c *b) / d;
  
      closestPointRay1.set(ray1.pointAtTime(s));
      closestPointRay2.set(ray2.pointAtTime(t));
  
      return true;
    }
  
    return false;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(Ray origin=");
    isb.addString(_origin.description());
    isb.addString(", direction=");
    isb.addString(_direction.description());
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }

}