

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.FloatBufferBuilderFromCartesian3D;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.Vector2D;


public class Polygon2D {
   ArrayList<Vector2D> _coor2D;


   private boolean isConvexPolygonCounterClockWise() {
      final Vector2D v0 = _coor2D.get(0);
      final Vector2D v1 = _coor2D.get(1);
      final Vector2D v2 = _coor2D.get(2);

      //double z =  (xi - xi-1) * (yi+1 - yi) - (yi - yi-1) * (xi+1 - xi)
      final double z = ((v1._x - v0._x) * (v2._y - v1._y)) - ((v1._y - v0._y) * (v2._x - v1._x));
      return z > 0;
   }


   private double angleInRadiansOfCorner(final int i) {

      int isub1 = (i - 1) % (_coor2D.size() - 2);
      if (isub1 == -1) {
         isub1 = _coor2D.size() - 2;
      }
      final int iadd1 = (i + 1) % (_coor2D.size() - 2); //Last one is repeated


      final Vector2D v1 = _coor2D.get(iadd1).sub(_coor2D.get(i));
      final Vector2D v2 = _coor2D.get(isub1).sub(_coor2D.get(i));

      return IMathUtils.instance().atan2(v2._y - v1._x, v2._x - v1._x);
   }


   private boolean isConcave() {
      final double a0 = angleInRadiansOfCorner(0);
      for (int i = 1; i < (_coor2D.size() - 1); i++) {
         final double ai = angleInRadiansOfCorner(i);
         if ((ai * a0) < 0) {
            return true;
         }
      }
      return false;
   }


   private double concavePolygonArea() {
      double sum = 0;
      for (int i = 0; i < (_coor2D.size() - 1); i++) {
         final Vector2D vi = _coor2D.get(i);
         final Vector2D vi1 = _coor2D.get(i + 1);
         sum += ((vi._x * vi1._y) - (vi1._x * vi._y));
      }

      return sum / 2;
   }


   private boolean isConcavePolygonCounterClockWise() {
      final double area = concavePolygonArea();
      return area > 0;
   }


   private boolean isPolygonCounterClockWise() {
      if (isConcave()) {
         return isConcavePolygonCounterClockWise();
      }
      return isConvexPolygonCounterClockWise();

   }


   public Polygon2D(final ArrayList<Vector2D> coor) {
      _coor2D = coor;

      if (!isPolygonCounterClockWise()) {
         ILogger.instance().logError("NO CCW POLYGON");
      }
   }


   ///////


   public boolean addTrianglesCuttingEars(final FloatBufferBuilderFromCartesian3D fbb,
                                          final FloatBufferBuilderFromCartesian3D normals) {


      //As seen in http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf

      int i1 = 0, i2 = 0, i3 = 0;
      final double angleInDegrees = 0;
      //   ILogger.instance().logInfo("Looking for ears");

      final boolean[] removed = new boolean[_coor2D.size()];
      int cornersLeft = _coor2D.size();
      for (int i = 0; i < (_coor2D.size() - 1); i++) {
         removed[i] = false;
      }

      while (cornersLeft >= 4) {


         boolean earFound = false;


         for (int i = 0; i < (_coor2D.size() - 1); i++) {

            int q = i;
            while (removed[q]) {
               q = (q + 1) % (_coor2D.size());
            }
            i1 = q;
            q = (q + 1) % (_coor2D.size());

            while (removed[q]) {
               q = (q + 1) % (_coor2D.size());
            }
            i2 = q;
            q = (q + 1) % (_coor2D.size());

            while (removed[q]) {
               q = (q + 1) % (_coor2D.size());
            }
            i3 = q;
            q = (q + 1) % (_coor2D.size());

            //            angleInDegrees = positiveCounterClockWiseAngleInDegreesOfCorner(i1, i2, i3);
            //
            //            acceptableAngle = IMathUtils.instance().isBetween((float) angleInDegrees, (float) 0.0, (float) 180.0)
            //                              || Double.isNaN(angleInDegrees);

            final boolean edgeInside = isEdgeInside(i1, i3);
            if (!edgeInside) {
               ILogger.instance().logInfo("T: %d, %d, %d -> Edge Not Inside", i1, i2, i3);
               continue;
            }

            final boolean edgeIntersects = edgeIntersectsAnyOtherEdge(i1, i3);
            if (edgeIntersects) {
               ILogger.instance().logInfo("T: %d, %d, %d -> Edge Intersects", i1, i2, i3);
               continue;
            }

            final boolean triangleContainsVertex = isAnyVertexInsideTriangle(i1, i2, i3);
            if (triangleContainsVertex) {
               ILogger.instance().logInfo("T: %d, %d, %d -> Triangle contains vertex", i1, i2, i3);
               continue;
            }

            ILogger.instance().logInfo("T: %d, %d, %d -> IS EAR!", i1, i2, i3);
            earFound = true;
            break;
         }
         //ILogger.instance().logInfo("!!!! Angle %f", angleInDegrees);


         if (earFound) { //Valid triangle (ear)
            fbb.add(_coor3D.get(i1));
            fbb.add(_coor3D.get(i2));
            fbb.add(_coor3D.get(i3));
            normals.add(_normal);
            normals.add(_normal);
            normals.add(_normal);

            //Removing ear
            removed[i2] = true;
            cornersLeft--;

            //            ILogger.instance().logInfo("T: %d, %d, %d -> Angle %f", i1, i2, i3, angleInDegrees);
         }
         else {
            ILogger.instance().logError("NO EAR!!!!");
            return false;
         }

      }
      return true;
   }


   private boolean isEdgeInside(final int i,
                                final int j) {

      final int nVertices = _coor2D.size() - 1;

      int iadd1 = i + 1;
      int isub1 = i - 1;

      if (iadd1 == (nVertices + 1)) {
         iadd1 = 0;
      }
      if (isub1 == -1) {
         isub1 = nVertices - 1;
      }

      final Vector2D v1 = _coor2D.get(iadd1).sub(_coor2D.get(i));
      final Vector2D v2 = _coor2D.get(isub1).sub(_coor2D.get(i));
      final Vector2D v3 = _coor2D.get(j).sub(_coor2D.get(i));

      double av1 = v1.angle()._degrees;
      final double av2 = v2.angle()._degrees;
      final double av3 = v3.angle()._degrees;

      if (av1 > av2) {
         av1 -= 360;
      }

      if ((av1 <= av3) && (av3 <= av2)) {
         return true;
      }

      return false;


   }


}
