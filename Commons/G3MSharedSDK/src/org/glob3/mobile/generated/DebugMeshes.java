package org.glob3.mobile.generated;
//
//  DebugMeshes.cpp
//  G3M
//

//
//  DebugMeshes.hpp
//  G3M
//


//class Mesh;
//class Color;
//class Box;
//class Sphere;
//class Ray;
//class CoordinateSystem;


public class DebugMeshes
{
  private DebugMeshes()
  {
  }

  public static Mesh createBoxWireframe(Box box, Color color)
  {
    final Vector3D lower = box._lower;
    final Vector3D upper = box._upper;
  
    double[] v = { lower._x, lower._y, lower._z, lower._x, upper._y, lower._z, lower._x, upper._y, upper._z, lower._x, lower._y, upper._z, upper._x, lower._y, lower._z, upper._x, upper._y, lower._z, upper._x, upper._y, upper._z, upper._x, lower._y, upper._z };
  
    short[] i = { 0, 1, 1, 2, 2, 3, 3, 0, 1, 5, 5, 6, 6, 2, 2, 1, 5, 4, 4, 7, 7, 6, 6, 5, 4, 0, 0, 3, 3, 7, 7, 4, 3, 2, 2, 6, 6, 7, 7, 3, 0, 1, 1, 5, 5, 4, 4, 0 };
  
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
    final int numVertices = 8;
    for (int n = 0; n < numVertices; n++)
    {
      vertices.add(v[n *3], v[n *3+1], v[n *3+2]);
    }
  
    ShortBufferBuilder indices = new ShortBufferBuilder();
    final int numIndices = 48;
    for (int n = 0; n < numIndices; n++)
    {
      indices.add(i[n]);
    }
  
    Mesh mesh = new IndexedMesh(GLPrimitive.lines(), vertices.getCenter(), vertices.create(), true, indices.create(), true, 2, 1, new Color(color));
  
    if (vertices != null)
       vertices.dispose();
  
    return mesh;
  }

  public static Mesh createSphereWireframe(Sphere sphere, Color color)
  {
    final short resolution = 16;
    final double delta = DefineConstants.PI / (resolution-1);
  
    // create vertices
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
    for (int i = 0; i<2 *resolution-2; i++)
    {
      final double longitude = -DefineConstants.PI + i *delta;
      for (int j = 0; j<resolution; j++)
      {
        final double latitude = -DefineConstants.PI/2 + j *delta;
        final double h = Math.cos(latitude);
        final double x = h * Math.cos(longitude);
        final double y = h * Math.sin(longitude);
        final double z = Math.sin(latitude);
        vertices.add(new Vector3D(x,y,z).times(sphere._radius).add(sphere._center));
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
  
    Mesh mesh = new IndexedMesh(GLPrimitive.lines(), vertices.getCenter(), vertices.create(), true, indices.create(), true, 1, 1, new Color(color));
  
    if (vertices != null)
       vertices.dispose();
  
    return mesh;
  }

  public static Mesh createRayMesh(Ray ray, Color color, float lineWidth)
  {
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithGivenCenter(ray._origin);
  
    vertices.add(ray._origin);
    vertices.add(ray._origin.add(ray._direction.times(100000)));
  
    Mesh mesh = new DirectMesh(GLPrimitive.lineStrip(), true, vertices.getCenter(), vertices.create(), lineWidth, 1, new Color(color), null, true); // bool depthTest -  const IFloatBuffer* colors
  
    if (vertices != null)
       vertices.dispose();
  
    return mesh;
  }

  public static Mesh createCoordinateSystemMesh(CoordinateSystem coordinateSystem, double size, Color xColor, Color yColor, Color zColor)
  {
    final Vector3D origin = coordinateSystem._origin;
  
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
    FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithGivenCenter(origin);
    fbb.add(origin);
    fbb.add(origin.add(coordinateSystem._x.normalized().times(size)));
    colors.add(xColor);
    colors.add(xColor);
  
    fbb.add(origin);
    fbb.add(origin.add(coordinateSystem._y.normalized().times(size)));
    colors.add(yColor);
    colors.add(yColor);
  
    fbb.add(origin);
    fbb.add(origin.add(coordinateSystem._z.normalized().times(size)));
    colors.add(zColor);
    colors.add(zColor);
  
    DirectMesh dm = new DirectMesh(GLPrimitive.lines(), true, fbb.getCenter(), fbb.create(), 5.0f, 1.0f, null, colors.create(), false, null);
  
    if (fbb != null)
       fbb.dispose();
  
    return dm;
  }

}