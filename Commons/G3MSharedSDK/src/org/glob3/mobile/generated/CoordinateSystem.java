package org.glob3.mobile.generated; 
//
//  CoordinateSystem.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/01/14.
//
//






//For camera



/*
Mesh* CoordinateSystem::createMesh(double size, const Color& xColor, const Color& yColor, const Color& zColor) const{

  FloatBufferBuilderFromColor colors;

  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithGivenCenter(_origin);
  fbb->add(_origin);
  fbb->add(_origin.add(_x.normalized().times(size)));
  colors.add(xColor);
  colors.add(xColor);

  fbb->add(_origin);
  fbb->add(_origin.add(_y.normalized().times(size)));
  colors.add(yColor);
  colors.add(yColor);

  fbb->add(_origin);
  fbb->add(_origin.add(_z.normalized().times(size)));
  colors.add(zColor);
  colors.add(zColor);

  DirectMesh* dm = new DirectMesh(GLPrimitive::lines(),
                                  true,
                                  fbb->getCenter(),
                                  fbb->create(),
                                  (float)5.0,
                                  (float)1.0,
                                  NULL,
                                  colors.create(),
                                  (float)1.0,
                                  false,
                                  NULL);

  delete fbb;

  return dm;
}
 */







public class CoordinateSystem
{
	
	Vector3D _x;
	Vector3D _y;
	Vector3D _z;
	Vector3D _origin;
	
	
   public CoordinateSystem global()
   {
     return new CoordinateSystem(Vector3D.upX(), Vector3D.upY(), Vector3D.upZ(), Vector3D.zero);
   }
   public CoordinateSystem(Vector3D x, Vector3D y, Vector3D z, Vector3D origin)
   {
      _x = x.normalized();
      _y = y.normalized();
      _z = z.normalized();
      _origin = origin;
     //TODO CHECK CONSISTENCY
     if (!areOrtogonal(x, y, z))
     {
       ILogger.instance().logError("Inconsistent CoordinateSystem created.");
     }
   }
   public CoordinateSystem(Vector3D viewDirection, Vector3D up, Vector3D origin)
   {
      _x = viewDirection.cross(up).normalized();
      _y = viewDirection.normalized();
      _z = up.normalized();
      _origin = origin;
   }
   public boolean areOrtogonal(Vector3D x, Vector3D y, Vector3D z)
   {
     return x.isPerpendicularTo(y) && x.isPerpendicularTo(z) && y.isPerpendicularTo(z);
   }
   public CoordinateSystem changeOrigin(Vector3D newOrigin)
   {
     return new CoordinateSystem(_x, _y, _z, newOrigin);
   }
   public TaitBryanAngles getTaitBryanAngles(CoordinateSystem global)
   {
   
     //We know...
   
     final Vector3D u = global._x;
     final Vector3D v = global._y;
     final Vector3D w = global._z;
   
     final Vector3D uppp = _x;
     final Vector3D vppp = _y;
     final Vector3D wppp = _z;
   
     final Vector3D wp = w;
     final Vector3D vpp = vppp;
   
     //We calculate
   
     //Pitch "especial" positions
     double x = vpp.dot(wp);
     if (x < -0.99999 && x > -1.000001)
     {
       //Pitch -90
       final Vector3D wpp = wppp; //No Roll
   
       Angle pitch = Angle.fromDegrees(-90);
       Angle roll = Angle.zero();
       Angle heading = v.signedAngleBetween(wpp, w);
   
       return new TaitBryanAngles(heading, pitch, roll);
   
     }
     else if (x > 0.99999 && x < 1.000001)
     {
       //Pitch 90
       final Vector3D wpp = wppp; //No Roll
   
       Angle pitch = Angle.fromDegrees(90);
       Angle roll = Angle.zero();
       Angle heading = v.signedAngleBetween(wpp, w).sub(Angle.fromDegrees(180));
   
       return new TaitBryanAngles(heading, pitch, roll);
     }
   
     //Normal formula
     final Vector3D up = vpp.cross(wp);
     final Vector3D vp = wp.cross(up);
   
     final Vector3D upp = up;
     final Vector3D wpp = upp.cross(vpp);
   
     Angle heading = u.signedAngleBetween(up, w);
     Angle pitch = vp.signedAngleBetween(vpp, up);
     Angle roll = wpp.signedAngleBetween(wppp, vpp);
   
     return new TaitBryanAngles(heading, pitch, roll);
   }
   public CoordinateSystem applyTaitBryanAngles(TaitBryanAngles angles)
   {
     return applyTaitBryanAngles(angles._heading, angles._pitch, angles._roll);
   }
   public CoordinateSystem applyTaitBryanAngles(Angle heading, Angle pitch, Angle roll)
   {
   
     //Check out Agustin Trujillo's review of this topic
     //This implementation is purposely explicit on every step
   
     final Vector3D u = _x;
     final Vector3D v = _y;
     final Vector3D w = _z;
   
     //Heading rotation
     boolean isHeadingZero = heading.isZero();
   
     MutableMatrix44D hm = isHeadingZero ? MutableMatrix44D.invalid() : MutableMatrix44D.createGeneralRotationMatrix(heading, w, Vector3D.zero);
   
     final Vector3D up = isHeadingZero ? u : u.transformedBy(hm, 1.0);
     final Vector3D vp = isHeadingZero ? v : v.transformedBy(hm, 1.0);
     final Vector3D wp = w;
   
     //Pitch rotation
     boolean isPitchZero = pitch.isZero();
   
     MutableMatrix44D pm = isPitchZero? MutableMatrix44D.invalid() : MutableMatrix44D.createGeneralRotationMatrix(pitch, up, Vector3D.zero);
   
     final Vector3D upp = up;
     final Vector3D vpp = isPitchZero? vp : vp.transformedBy(pm, 1.0);
     final Vector3D wpp = isPitchZero? wp : wp.transformedBy(pm, 1.0);
   
     //Roll rotation
     boolean isRollZero = roll.isZero();
   
     MutableMatrix44D rm = isRollZero? MutableMatrix44D.invalid() : MutableMatrix44D.createGeneralRotationMatrix(roll, vpp, Vector3D.zero);
   
     final Vector3D uppp = isRollZero? upp : upp.transformedBy(rm, 1.0);
     final Vector3D vppp = vpp;
     final Vector3D wppp = isRollZero? wpp : wpp.transformedBy(rm, 1.0);
   
     return new CoordinateSystem(uppp, vppp, wppp, _origin);
   }
   public boolean isEqualsTo(CoordinateSystem that)
   {
     return _x.isEquals(that._x) && _y.isEquals(that._y) && _z.isEquals(that._z);
   }
}