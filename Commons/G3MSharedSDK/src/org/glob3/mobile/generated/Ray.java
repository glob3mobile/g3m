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