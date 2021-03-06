package org.glob3.mobile.generated;
//
//  Cylinder.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/2/21.
//

//
//  Cylinder.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/2/21.
//



//class Mesh;
//class Color;


public class Cylinder
{

  public final Vector3D _start ;
  public final Vector3D _end ;
  public final double _startRadius;
  public final double _endRadius;

  public Cylinder(Vector3D start, Vector3D end, double startRadius, double endRadius)
  {
     _start = start;
     _end = end;
     _startRadius = startRadius;
     _endRadius = endRadius;
  }

  public final Mesh createMesh(Color color, int nSegments)
  {
  
    Vector3D d = _end.sub(_start);
    Vector3D r = (d._z == 0) ? new Vector3D(0.0,0.0,1.0) : new Vector3D(1.0, 1.0, (-d._x -d._y) / d._z);
  
    Vector3D pStart = _start.add(r.times(_startRadius / r.length()));
    Vector3D pEnd = _end.add(r.times(_endRadius / r.length()));
    MutableMatrix44D mStart = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(360.0 / nSegments), d, _start);
    MutableMatrix44D mEnd = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(360.0 / nSegments), d, _end);
  
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
    FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
  
    MutableVector3D newStartPoint = new MutableVector3D(pStart);
    MutableVector3D newEndPoint = new MutableVector3D(pEnd);
    for (int i = 0; i < nSegments; ++i)
    {
  
      //Tube
      newStartPoint.set(newStartPoint.transformedBy(mStart, 1.0));
      newEndPoint.set(newEndPoint.transformedBy(mEnd, 1.0));
  
      vertices.add(newStartPoint.asVector3D());
      vertices.add(newEndPoint.asVector3D());
  
      normals.add(newStartPoint.sub(_start));
      normals.add(newEndPoint.sub(_end));
    }
  
    ShortBufferBuilder indices = new ShortBufferBuilder();
    for (int i = 0; i < nSegments *2; ++i)
    {
      indices.add((short) i);
    }
    indices.add((short) 0);
    indices.add((short) 1);
  
    IndexedMesh im = new IndexedMesh(GLPrimitive.triangleStrip(), vertices.getCenter(), vertices.create(), true, indices.create(), true, 1.0f, 1.0f, new Color(color), null, true, normals.create(), false, 0, 0); //colors.create(),
    if (normals != null)
       normals.dispose();
    if (vertices != null)
       vertices.dispose();
  
    return im;
  }

}