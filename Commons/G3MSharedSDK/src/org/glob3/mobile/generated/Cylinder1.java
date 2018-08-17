package org.glob3.mobile.generated;//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Chano adding stuff

//#define PROXIMITY_VALUE 25


//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Chano adding stuff


public class Cylinder
{
   public Mesh createMesh(Color color, int nSegments, Planet planet)
   {
   
       Vector3D d = _end.sub(_start);
       Vector3D r = d._z == 0? new Vector3D(0.0,0.0,1.0) : new Vector3D(1.0, 1.0, (-d._x -d._y) / d._z);
   
       Vector3D p = _start.add(r.times(_radius / r.length()));
   
       final double totalAngle = _endAngle - _startAngle;
   
   
       MutableMatrix44D m = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(totalAngle / nSegments), d, _start);
   
       FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
       FloatBufferBuilderFromCartesian3D fbbC1 = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
       fbbC1.add(_start);
       FloatBufferBuilderFromCartesian3D fbbC2 = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
       fbbC2.add(_end);
   
       FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
       FloatBufferBuilderFromCartesian3D normalsC1 = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
       normalsC1.add(d.times(-1.0));
       FloatBufferBuilderFromCartesian3D normalsC2 = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
       normalsC2.add(d);
   
       FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
   
       MutableVector3D x = new MutableVector3D(p);
       x = x.transformedBy(MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(_startAngle), d, _start), 1.0);
       java.util.ArrayList<Vector3D > vs = new java.util.ArrayList<Vector3D >();
   
       for (int i = 0; i < nSegments; ++i)
       {
   
           //Tube
           Vector3D newStartPoint = x.asVector3D().transformedBy(m, 1.0);
           Vector3D newEndPoint = newStartPoint.add(d);
           x.set(newStartPoint._x, newStartPoint._y, newStartPoint._z);
   
           fbb.add(newStartPoint);
           vs.add(new Vector3D(newStartPoint));
           fbb.add(newEndPoint);
           vs.add(new Vector3D(newEndPoint));
   
           Geodetic3D stPoint = planet.toGeodetic3D(newStartPoint);
           Geodetic3D endPoint = planet.toGeodetic3D(newEndPoint);
           _info.addLatLng(stPoint._latitude._degrees, stPoint._longitude._degrees, stPoint._height);
           _info.addLatLng(endPoint._latitude._degrees, endPoint._longitude._degrees, endPoint._height);
   
           normals.add(newStartPoint.sub(_start));
           normals.add(newEndPoint.sub(_end));
   
           //Cover1
           fbbC1.add(newStartPoint);
           normalsC1.add(d.times(-1.0));
           //Cover2
           fbbC2.add(newEndPoint);
           normalsC2.add(d);
   
           colors.add(color);
           colors.add(color);
       }
   
       //Still covers
       /*Vector3D newStartPoint = x.asVector3D().transformedBy(m, 1.0);
        fbbC1->add(newStartPoint);
        normalsC1->add(d.times(-1.0));
        Vector3D newEndPoint = newStartPoint.add(d);
        fbbC2->add(newEndPoint);
        normalsC2->add(d);*/
   
   
       ShortBufferBuilder ind = new ShortBufferBuilder();
       for (int i = 0; i < nSegments *2; ++i)
       {
           ind.add((short)i);
       }
       ind.add((short)0);
       ind.add((short)1);
   
       IFloatBuffer vertices = fbb.create();
       IndexedMesh im = new IndexedMesh(GLPrimitive.triangleStrip(), fbb.getCenter(), vertices, true, ind.create(), true, 1.0, 1.0, null, colors.create(), 1.0f, DEPTH_ENABLED, normals.create(), false, 0, 0); //NULL, - new Color(color),
   
       createSphere(vs);
   
   
       if (normals != null)
          normals.dispose();
       if (fbb != null)
          fbb.dispose();
   
   
       //return cm;
       return im;
   
   }
}
