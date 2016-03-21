

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.FloatBufferBuilderFromCartesian3D;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.Vector2D;
import org.glob3.mobile.generated.Vector3D;


public class Polygon3D {


   ArrayList<Vector3D> _coor3D;
   ArrayList<Vector2D> _coor2D;
   Vector3D            _normal;
   boolean             _isClockwise;


   public Polygon3D(final ArrayList<Vector3D> coor3D,
                    final Vector3D normal) {
      _coor3D = coor3D;
      _normal = normal;

      createCoordinates2D(_coor3D, _normal);

      //Clockwise
      final double angleSum = getAngleSumInDegrees();
      _isClockwise = (angleSum < 0);
      ILogger.instance().logInfo("Polygon with %d vert. -> Interior angle = %f", _coor3D.size(), angleSum);
   }


   private double getAngleSumInDegrees() {
      double angleSum = 0;
      for (int i = 0; i < _coor3D.size(); i++) {
         final int i1 = i;
         final int i2 = (i + 1) % (_coor3D.size());
         final int i3 = (i + 2) % (_coor3D.size());

         final double angleInDegrees = angleInDegreesOfCorner(i1, i2, i3);

         if (!Double.isNaN(angleInDegrees)) {
            angleSum += angleInDegrees;
         }
      }
      return angleSum;
   }


   public void createCoordinates2D(final ArrayList<Vector3D> c3D,
                                   final Vector3D normal) {

      final Vector3D z = Vector3D.upZ();
      final Vector3D rotationAxis = z.cross(normal);

      Angle a = null;
      if (rotationAxis.isZero()) {
         a = Angle.fromDegrees(180);
      }
      else {
         a = normal.signedAngleBetween(rotationAxis, z);
      }

      _coor2D = new ArrayList<Vector2D>();
      for (int i = 0; i < c3D.size(); i++) {
         Vector3D v3D = null;
         if (a.isNan() || a.isZero()) {
            v3D = c3D.get(i);
         }
         else {
            if ((a._degrees == 180) || (a._degrees == -180)) {
               v3D = c3D.get(i).rotateAroundAxis(Vector3D.upX(), a);
            }
            else {
               v3D = c3D.get(i).rotateAroundAxis(rotationAxis, a);
            }
         }
         _coor2D.add(new Vector2D(v3D._x, v3D._y));
      }

   }


   static private boolean isInsideTriangle(final Vector2D p,
                                           final Vector2D cornerA,
                                           final Vector2D cornerB,
                                           final Vector2D cornerC) {

      final double alpha = (((cornerB._y - cornerC._y) * (p._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (p._y - cornerC._y)))
               / (((cornerB._y - cornerC._y) * (cornerA._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (cornerA._y - cornerC._y)));
      final double beta = (((cornerC._y - cornerA._y) * (p._x - cornerC._x)) + ((cornerA._x - cornerC._x) * (p._y - cornerC._y)))
               / (((cornerB._y - cornerC._y) * (cornerA._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (cornerA._y - cornerC._y)));
      final double gamma = 1.0 - alpha - beta;

      if ((alpha > 0) && (beta > 0) && (gamma > 0)) {
         return true;
      }
      return false;
   }


   private double angleInDegreesOfCorner(final int i1,
                                         final int i2,
                                         final int i3) {

      final Vector3D v1 = _coor3D.get(i1);
      final Vector3D v2 = _coor3D.get(i2);
      final Vector3D v3 = _coor3D.get(i3);

      final Vector3D v21 = v1.sub(v2);
      final Vector3D v23 = v3.sub(v2);

      final double angleInDegrees = v21.signedAngleBetween(v23, _normal)._degrees;

      return angleInDegrees;
   }


   private double positiveCounterClockWiseAngleInDegreesOfCorner(final int i1,
                                                                 final int i2,
                                                                 final int i3) {

      double angleInDegrees = angleInDegreesOfCorner(i1, i2, i3);

      if (_isClockwise) {
         angleInDegrees *= -1;
      }

      if (angleInDegrees < 0) {
         angleInDegrees += 360;
      }

      return angleInDegrees;

   }


   private boolean isAnyVertexInsideTriangle(final int i1,
                                             final int i2,
                                             final int i3) {

      final Vector2D cornerA = _coor2D.get(i1);
      final Vector2D cornerB = _coor2D.get(i2);
      final Vector2D cornerC = _coor2D.get(i3);

      for (int j = 0; j < _coor3D.size(); j++) {
         if ((j != i1) && (j != i2) && (j != i3)) {
            final Vector2D p = _coor2D.get(j);
            if (isInsideTriangle(p, cornerA, cornerB, cornerC)) {
               return true;
            }
         }
      }

      return false;

   }


   public boolean addTrianglesCuttingEars(final FloatBufferBuilderFromCartesian3D fbb,
                                          final FloatBufferBuilderFromCartesian3D normals) {


      //As seen in http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf

      int i1 = 0, i2 = 0, i3 = 0;
      double angleInDegrees = 0;
      boolean acceptableAngle = false;
      //   ILogger.instance().logInfo("Looking for ears");

      final boolean[] removed = new boolean[_coor3D.size()];
      int cornersLeft = _coor3D.size();
      for (int i = 0; i < (_coor3D.size() - 1); i++) {
         removed[i] = false;
      }

      while (cornersLeft >= 4) {


         for (int i = 0; i < (_coor3D.size() - 1); i++) {

            int q = i;
            while (removed[q]) {
               q = (q + 1) % (_coor3D.size());
            }
            i1 = q;
            q = (q + 1) % (_coor3D.size());

            while (removed[q]) {
               q = (q + 1) % (_coor3D.size());
            }
            i2 = q;
            q = (q + 1) % (_coor3D.size());

            while (removed[q]) {
               q = (q + 1) % (_coor3D.size());
            }
            i3 = q;
            q = (q + 1) % (_coor3D.size());

            angleInDegrees = positiveCounterClockWiseAngleInDegreesOfCorner(i1, i2, i3);

            acceptableAngle = IMathUtils.instance().isBetween((float) angleInDegrees, (float) 0.0, (float) 180.0)
                     || Double.isNaN(angleInDegrees);

            if (acceptableAngle) {
               //Internal angle is concave
               //Checking inclusion of other vertices

               acceptableAngle = !isAnyVertexInsideTriangle(i1, i2, i3);

               if (acceptableAngle) {
                  break;
               }

            }
         }
         //ILogger.instance().logInfo("!!!! Angle %f", angleInDegrees);


         if (acceptableAngle) { //Valid triangle (ear)
            fbb.add(_coor3D.get(i1));
            fbb.add(_coor3D.get(i2));
            fbb.add(_coor3D.get(i3));
            normals.add(_normal);
            normals.add(_normal);
            normals.add(_normal);

            removed[i2] = true;
            cornersLeft--;

            //            ILogger.instance().logInfo("T: %d, %d, %d -> Angle %f", i1, i2, i3, angleInDegrees);
         }
         else {
            ILogger.instance().logError("NO EAR!!!!");
            return false;
         }


         //Removing ear

      }
      return true;
   }

}
