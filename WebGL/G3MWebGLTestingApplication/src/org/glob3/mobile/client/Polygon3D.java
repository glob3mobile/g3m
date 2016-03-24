

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.FloatBufferBuilderFromCartesian3D;
import org.glob3.mobile.generated.ShortBufferBuilder;
import org.glob3.mobile.generated.Vector2D;
import org.glob3.mobile.generated.Vector3D;


public class Polygon3D {


   final ArrayList<Vector3D>            _coor3D;
   ArrayList<Vector2D>                  _coor2D;
   Vector3D                             _cCWNormal = null;

   org.glob3.mobile.generated.Polygon2D _polygon2D;


   public Polygon3D(final ArrayList<Vector3D> coor3D) {
      _coor3D = coor3D;

      final Vector3D e1 = _coor3D.get(0).sub(_coor3D.get(1));
      final Vector3D e2 = _coor3D.get(2).sub(_coor3D.get(1));


      _cCWNormal = e1.cross(e2);
      _coor2D = createCoordinates2D(_coor3D, _cCWNormal);

      _polygon2D = new org.glob3.mobile.generated.Polygon2D(_coor2D);
      if (!_polygon2D.areVerticesCounterClockWise()) {
         _cCWNormal = _cCWNormal.times(-1);
      }


      //
      //
      //
      //
      //      final boolean isCC = isPolygonCounterClockWise();
      //
      //      if (!isCC) {
      //         _normal = _normal.times(-1);
      //         _coor2D = createCoordinates2D(_coor3D, _normal);
      //      }
   }


   //   private boolean isConvexPolygonCounterClockWise() {
   //      final Vector2D v0 = _coor2D.get(0);
   //      final Vector2D v1 = _coor2D.get(1);
   //      final Vector2D v2 = _coor2D.get(2);
   //
   //      //double z =  (xi - xi-1) * (yi+1 - yi) - (yi - yi-1) * (xi+1 - xi)
   //      final double z = ((v1._x - v0._x) * (v2._y - v1._y)) - ((v1._y - v0._y) * (v2._x - v1._x));
   //      return z > 0;
   //   }


   //   private double concavePolygonArea() {
   //      double sum = 0;
   //      for (int i = 0; i < (_coor2D.size() - 1); i++) {
   //         final Vector2D vi = _coor2D.get(i);
   //         final Vector2D vi1 = _coor2D.get(i + 1);
   //         sum += ((vi._x * vi1._y) - (vi1._x * vi._y));
   //      }
   //
   //      return sum / 2;
   //   }


   //   private boolean isConcavePolygonCounterClockWise() {
   //      final double area = concavePolygonArea();
   //      return area > 0;
   //   }


   //   private boolean isPolygonCounterClockWise() {
   //      if (isConcave()) {
   //         return isConcavePolygonCounterClockWise();
   //      }
   //      return isConvexPolygonCounterClockWise();
   //
   //   }


   //   private double angleInRadiansOfCorner(final int i) {
   //
   //      int isub1 = (i - 1) % (_coor2D.size() - 2);
   //      if (isub1 == -1) {
   //         isub1 = _coor2D.size() - 2;
   //      }
   //      final int iadd1 = (i + 1) % (_coor2D.size() - 2); //Last one is repeated
   //
   //
   //      final Vector2D v1 = _coor2D.get(iadd1).sub(_coor2D.get(i));
   //      final Vector2D v2 = _coor2D.get(isub1).sub(_coor2D.get(i));
   //
   //      return IMathUtils.instance().atan2(v2._y - v1._x, v2._x - v1._x);
   //   }


   //   private boolean isConcave() {
   //      final double a0 = angleInRadiansOfCorner(0);
   //      for (int i = 1; i < (_coor2D.size() - 1); i++) {
   //         final double ai = angleInRadiansOfCorner(i);
   //         if ((ai * a0) < 0) {
   //            return true;
   //         }
   //      }
   //      return false;
   //   }


   private ArrayList<Vector2D> createCoordinates2D(final ArrayList<Vector3D> c3D,
            final Vector3D normal) {

      final Vector3D z = Vector3D.upZ();
      final Vector3D rotationAxis = z.cross(normal);
      final ArrayList<Vector2D> coor2D = new ArrayList<Vector2D>();

      if (rotationAxis.isZero()) {

         if (normal._z > 0) {
            for (int i = 0; i < c3D.size(); i++) {
               final Vector3D v3D = c3D.get(i);
               coor2D.add(new Vector2D(v3D._x, v3D._y));
            }
         }
         else {
            for (int i = 0; i < c3D.size(); i++) {
               final Vector3D v3D = c3D.get(i);
               coor2D.add(new Vector2D(v3D._x, -v3D._y));
            }
         }

         return coor2D;
      }

      final Angle a = normal.signedAngleBetween(rotationAxis, z);

      for (int i = 0; i < c3D.size(); i++) {
         final Vector3D v3D = c3D.get(i).rotateAroundAxis(rotationAxis, a);
         coor2D.add(new Vector2D(v3D._x, v3D._y));
      }

      return coor2D;
   }


   //   static private boolean isInsideTriangle(final Vector2D p,
   //                                           final Vector2D cornerA,
   //                                           final Vector2D cornerB,
   //                                           final Vector2D cornerC) {
   //
   //      final double alpha = (((cornerB._y - cornerC._y) * (p._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (p._y - cornerC._y)))
   //               / (((cornerB._y - cornerC._y) * (cornerA._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (cornerA._y - cornerC._y)));
   //      final double beta = (((cornerC._y - cornerA._y) * (p._x - cornerC._x)) + ((cornerA._x - cornerC._x) * (p._y - cornerC._y)))
   //               / (((cornerB._y - cornerC._y) * (cornerA._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (cornerA._y - cornerC._y)));
   //      final double gamma = 1.0 - alpha - beta;
   //
   //      if ((alpha > 0) && (beta > 0) && (gamma > 0)) {
   //         return true;
   //      }
   //      return false;
   //   }


   //   private static boolean isEdgeInside(final int i,
   //                                       final int j,
   //                                       final ArrayList<Vector2D> remainingCorners) {
   //      //http://stackoverflow.com/questions/693837/how-to-determine-a-diagonal-is-in-or-out-of-a-concave-polygon
   //
   //      final int nVertices = remainingCorners.size() - 1;
   //
   //      int iadd1 = i + 1;
   //      int isub1 = i - 1;
   //
   //      if (iadd1 == (nVertices + 1)) {
   //         iadd1 = 0;
   //      }
   //      if (isub1 == -1) {
   //         isub1 = nVertices - 1;
   //      }
   //
   //      final Vector2D v1 = remainingCorners.get(iadd1).sub(remainingCorners.get(i));
   //      final Vector2D v2 = remainingCorners.get(isub1).sub(remainingCorners.get(i));
   //      final Vector2D v3 = remainingCorners.get(j).sub(remainingCorners.get(i));
   //
   //      double av1 = v1.angle()._degrees;
   //      double av2 = v2.angle()._degrees;
   //      double av3 = v3.angle()._degrees;
   //
   //      while (av1 < 0) {
   //         av1 += 360;
   //      }
   //
   //      while (av2 < 0) {
   //         av2 += 360;
   //      }
   //
   //      while (av3 < 0) {
   //         av3 += 360;
   //      }
   //
   //      while (av1 > av2) {
   //         av2 += 360;
   //      }
   //
   //      if ((av1 <= av3) && (av3 <= av2)) {
   //         return true;
   //      }
   //      av3 += 360;
   //      if ((av1 <= av3) && (av3 <= av2)) {
   //         return true;
   //      }
   //
   //      return false;
   //
   //
   //   }


   //   private static boolean segmentsIntersect(final Vector2D a,
   //                                            final Vector2D b,
   //                                            final Vector2D c,
   //                                            final Vector2D d) {
   //      //http://www.smipple.net/snippet/sparkon/%5BC%2B%2B%5D%202D%20lines%20segment%20intersection%20
   //      final double den = (((d._y - c._y) * (b._x - a._x)) - ((d._x - c._x) * (b._y - a._y)));
   //      final double num1 = (((d._x - c._x) * (a._y - c._y)) - ((d._y - c._y) * (a._x - c._x)));
   //      final double num2 = (((b._x - a._x) * (a._y - c._y)) - ((b._y - a._y) * (a._x - c._x)));
   //      final double u1 = num1 / den;
   //      final double u2 = num2 / den;
   //      //          std::cout << u1 << ":" << u2 << std::endl;
   //      if ((den == 0) && (num1 == 0) && (num2 == 0)) {
   //         /* The two lines are coincidents */
   //         return false;
   //      }
   //      if (den == 0) {
   //         /* The two lines are parallel */
   //         return false;
   //      }
   //      if ((u1 < 0) || (u1 > 1) || (u2 < 0) || (u2 > 1)) {
   //         /* Lines do not collide */
   //         return false;
   //      }
   //      /* Lines DO collide */
   //      return true;
   //   }


   //   private static boolean edgeIntersectsAnyOtherEdge(final int i,
   //                                                     final int j,
   //                                                     final ArrayList<Vector2D> remainingCorners) {
   //
   //      final Vector2D a = remainingCorners.get(i);
   //      final Vector2D b = remainingCorners.get(j);
   //
   //      for (int k = 0; k < (remainingCorners.size() - 2); k++) {
   //
   //         final int kadd1 = (k + 1) % (remainingCorners.size() - 1);
   //
   //         if ((i == k) || (i == kadd1) || (j == k) || (j == kadd1)) {
   //            continue;
   //         }
   //
   //         final Vector2D c = remainingCorners.get(k);
   //         final Vector2D d = remainingCorners.get(kadd1);
   //
   //         if (segmentsIntersect(a, b, c, d)) {
   //            return true;
   //         }
   //
   //
   //      }
   //
   //      return false;
   //   }


   //   private static boolean isAnyVertexInsideTriangle(final int i1,
   //                                                    final int i2,
   //                                                    final int i3,
   //                                                    final ArrayList<Vector2D> remainingCorners) {
   //
   //      final Vector2D cornerA = remainingCorners.get(i1);
   //      final Vector2D cornerB = remainingCorners.get(i2);
   //      final Vector2D cornerC = remainingCorners.get(i3);
   //
   //      for (int j = 0; j < remainingCorners.size(); j++) {
   //         if ((j != i1) && (j != i2) && (j != i3)) {
   //            final Vector2D p = remainingCorners.get(j);
   //            if (isInsideTriangle(p, cornerA, cornerB, cornerC)) {
   //               return true;
   //            }
   //         }
   //      }
   //
   //      return false;
   //
   //   }


   //   public short addTrianglesCuttingEars(final FloatBufferBuilderFromCartesian3D fbb,
   //                                        final FloatBufferBuilderFromCartesian3D normals,
   //                                        final ShortBufferBuilder indexes,
   //                                        final short firstIndex) {
   //
   //      //As seen in http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf
   //
   //      int i1 = 0, i2 = 0, i3 = 0;
   //      //   ILogger.instance().logInfo("Looking for ears");
   //
   //      final ArrayList<Vector2D> remainingCorners = new ArrayList<Vector2D>();
   //      final ArrayList<Vector3D> remainingCorners3D = new ArrayList<Vector3D>();
   //      final ArrayList<Short> remainingIndexes = new ArrayList<Short>();
   //
   //      for (int i = 0; i < _coor3D.size(); i++) {
   //         remainingCorners.add(_coor2D.get(i));
   //         remainingCorners3D.add(_coor3D.get(i));
   //         remainingIndexes.add((short) (i + firstIndex));
   //
   //         fbb.add(_coor3D.get(i));
   //         normals.add(_normal.times(-1));
   //      }
   //
   //      while (remainingCorners.size() > 2) {
   //
   //         boolean earFound = false;
   //         for (int i = 0; i < (remainingCorners.size() - 1); i++) {
   //
   //            i1 = i;
   //            i2 = (i + 1) % (remainingCorners.size());
   //            i3 = (i + 2) % (remainingCorners.size());
   //
   //            final boolean edgeInside = isEdgeInside(i1, i3, remainingCorners);
   //            if (!edgeInside) {
   //               //               ILogger.instance().logInfo("T: %d, %d, %d -> Edge Not Inside", i1, i2, i3);
   //               continue;
   //            }
   //
   //            final boolean edgeIntersects = edgeIntersectsAnyOtherEdge(i1, i3, remainingCorners);
   //            if (edgeIntersects) {
   //               //               ILogger.instance().logInfo("T: %d, %d, %d -> Edge Intersects", i1, i2, i3);
   //               continue;
   //            }
   //
   //            final boolean triangleContainsVertex = isAnyVertexInsideTriangle(i1, i2, i3, remainingCorners);
   //            if (triangleContainsVertex) {
   //               //               ILogger.instance().logInfo("T: %d, %d, %d -> Triangle contains vertex", i1, i2, i3);
   //               continue;
   //            }
   //
   //            //            ILogger.instance().logInfo("T: %d, %d, %d -> IS EAR!", i1, i2, i3);
   //            earFound = true;
   //            break;
   //         }
   //
   //
   //         if (earFound) { //Valid triangle (ear)
   //            indexes.add(remainingIndexes.get(i1));
   //            indexes.add(remainingIndexes.get(i2));
   //            indexes.add(remainingIndexes.get(i3));
   //
   //            //Removing ear
   //            remainingCorners.remove(i2);
   //            remainingCorners3D.remove(i2);
   //            remainingIndexes.remove(i2);
   //         }
   //         else {
   //            ILogger.instance().logError("NO EAR!!!!");
   //         }
   //
   //      }
   //      return (short) (firstIndex + _coor2D.size());
   //   }


   public short addTrianglesCuttingEars(final FloatBufferBuilderFromCartesian3D fbb,
                                        final FloatBufferBuilderFromCartesian3D normals,
                                        final ShortBufferBuilder indexes,
                                        final short firstIndex) {

      //As seen in http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf
      for (int i = 0; i < _coor3D.size(); i++) {
         fbb.add(_coor3D.get(i));
         normals.add(_cCWNormal.times(-1));
      }

      final org.glob3.mobile.generated.Polygon2D p2D = new org.glob3.mobile.generated.Polygon2D(_coor2D);
      return p2D.addTrianglesIndexesByEarClipping(indexes, firstIndex);
   }


   //   public boolean addTrianglesCuttingEars(final FloatBufferBuilderFromCartesian3D fbb,
   //                                          final FloatBufferBuilderFromCartesian3D normals) {
   //
   //      //As seen in http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf
   //
   //      int i1 = 0, i2 = 0, i3 = 0;
   //      //   ILogger.instance().logInfo("Looking for ears");
   //
   //      final ArrayList<Vector2D> remainingCorners = new ArrayList<Vector2D>();
   //      final ArrayList<Vector3D> remainingCorners3D = new ArrayList<Vector3D>();
   //
   //      for (int i = 0; i < _coor3D.size(); i++) {
   //         remainingCorners.add(_coor2D.get(i));
   //         remainingCorners3D.add(_coor3D.get(i));
   //      }
   //
   //      while (remainingCorners.size() > 2) {
   //
   //         boolean earFound = false;
   //         for (int i = 0; i < (remainingCorners.size() - 1); i++) {
   //
   //            i1 = i;
   //            i2 = (i + 1) % (remainingCorners.size());
   //            i3 = (i + 2) % (remainingCorners.size());
   //
   //            final boolean edgeInside = isEdgeInside(i1, i3, remainingCorners);
   //            if (!edgeInside) {
   //               //               ILogger.instance().logInfo("T: %d, %d, %d -> Edge Not Inside", i1, i2, i3);
   //               continue;
   //            }
   //
   //            final boolean edgeIntersects = edgeIntersectsAnyOtherEdge(i1, i3, remainingCorners);
   //            if (edgeIntersects) {
   //               //               ILogger.instance().logInfo("T: %d, %d, %d -> Edge Intersects", i1, i2, i3);
   //               continue;
   //            }
   //
   //            final boolean triangleContainsVertex = isAnyVertexInsideTriangle(i1, i2, i3, remainingCorners);
   //            if (triangleContainsVertex) {
   //               //               ILogger.instance().logInfo("T: %d, %d, %d -> Triangle contains vertex", i1, i2, i3);
   //               continue;
   //            }
   //
   //            //            ILogger.instance().logInfo("T: %d, %d, %d -> IS EAR!", i1, i2, i3);
   //            earFound = true;
   //            break;
   //         }
   //
   //
   //         if (earFound) { //Valid triangle (ear)
   //            fbb.add(remainingCorners3D.get(i1));
   //            fbb.add(remainingCorners3D.get(i2));
   //            fbb.add(remainingCorners3D.get(i3));
   //            normals.add(_normal.times(-1));
   //            normals.add(_normal.times(-1));
   //            normals.add(_normal.times(-1));
   //
   //            //Removing ear
   //            remainingCorners.remove(i2);
   //            remainingCorners3D.remove(i2);
   //
   //            //            ILogger.instance().logInfo("T: %d, %d, %d -> Angle %f", i1, i2, i3, angleInDegrees);
   //         }
   //         else {
   //            ILogger.instance().logError("NO EAR!!!!");
   //            return false;
   //         }
   //
   //      }
   //      return true;
   //   }
}
