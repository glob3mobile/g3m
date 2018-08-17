package org.glob3.mobile.generated;//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Chano adding stuff




public class Ditch
{
   public Mesh createMesh(Color color, int nSegments, Planet planet, ElevationData ed)
   {
   
       double o1 = (ed == null)? 0.0 : ed.getElevationAt(_start.asGeodetic2D());
       double o2 = (ed == null)? 0.0 : ed.getElevationAt(_end.asGeodetic2D());
   
       Vector3D start = planet.toCartesian(_start._latitude, _start._longitude, o1);
       Vector3D end = planet.toCartesian(_end._latitude, _end._longitude, o2);
       Vector3D normal = planet.centricSurfaceNormal(start);
       Vector3D axis = end.sub(start);
       MutableVector3D p = new MutableVector3D(start.add(axis.cross(normal).normalized().times(_width / 2.0)));
   
       FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
   
       MutableMatrix44D m = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(180.0 / (double)nSegments), axis, start);
   
   
       final IMathUtils mu = IMathUtils.instance();
   
       for (int i = 0; i <= nSegments; ++i)
       {
           double r = 2.0 * mu.abs(((double)i / (double)nSegments) - 0.5);
   
           Vector3D pDir = p.sub(start).normalized();
           double dist = r * _width + (1.0-r) * (o1 - _start._height);
           Vector3D ini = start.add(pDir.times(dist));
           fbb.add(ini);
   
           dist = r * _width + (1.0-r) * (o2 - _end._height);
           Vector3D fin = end.add(pDir.times(dist));
           fbb.add(fin);
   
           p = p.transformedBy(m, 1.0);
       }
   
       DirectMesh dm = new DirectMesh(GLPrimitive.triangleStrip(), true, fbb.getCenter(), fbb.create(), 2.0, 100.0, Color.newFromRGBA(0.0, 0.0, 0.0, 1.0), null, 0.0f, true);
       return dm;
   
       //    ShortBufferBuilder ind;
       //    for (int i = 0; i < fbb->size() / 3 * 2; ++i){
       //        ind.add((short)i);
       //    }
       //    ind.add((short)0);
       //    ind.add((short)1);
       //
       //    IFloatBuffer* vertices = fbb->create();
       //    IndexedMesh* im = new IndexedMesh(GLPrimitive::triangleStrip(),
       //                                      fbb->getCenter(),
       //                                      vertices,
       //                                      true,
       //                                      ind.create(),
       //                                      true,
       //                                      1.0,
       //                                      1.0,
       //                                      NULL,//new Color(color),
       //                                      colors.create(),//NULL,
       //                                      1.0f,
       //                                      DEPTH_ENABLED,
       //                                      normals->create(),
       //                                      false,
       //                                      0,
       //                                      0);
       //
   
   }
}
