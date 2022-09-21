package org.glob3.mobile.generated;
//
//  CoordinateSystem.cpp
//  G3M
//
//  Created by Jose Miguel SN on 29/01/14.
//
//

//
//  CoordinateSystem.hpp
//  G3M
//
//  Created by Jose Miguel SN on 29/01/14.
//
//



//class TaitBryanAngles;
//class Mesh;
//class Color;
//class Camera;


public class CoordinateSystem
{

  private static boolean checkConsistency(Vector3D x, Vector3D y, Vector3D z)
  {
    if (x.isNan() || y.isNan() || z.isNan())
    {
      return false;
    }
    return true;
    //  return areOrtogonal(x, y, z);
  }

//  static bool areOrtogonal(const Vector3D& x, const Vector3D& y, const Vector3D& z);


  public final Vector3D _x ;
  public final Vector3D _y ;
  public final Vector3D _z ;
  public final Vector3D _origin ;


  public static CoordinateSystem global()
  {
    return new CoordinateSystem(Vector3D.UP_X, Vector3D.UP_Y, Vector3D.UP_Z, Vector3D.ZERO);
  }

  public static CoordinateSystem fromCamera(Camera camera)
  {
    final Vector3D viewDirection = camera.getViewDirection();
    final Vector3D up = camera.getUp();
    final Vector3D origin = camera.getCartesianPosition();
  
    return new CoordinateSystem(viewDirection.cross(up).normalized(), viewDirection.normalized(), up.normalized(), origin);
  }

  public CoordinateSystem(CoordinateSystem that)
  {
     _x = that._x;
     _y = that._y;
     _z = that._z;
     _origin = that._origin;
  }

  public CoordinateSystem(Vector3D x, Vector3D y, Vector3D z, Vector3D origin)
  {
     _x = x.normalized();
     _y = y.normalized();
     _z = z.normalized();
     _origin = origin;
    if (!checkConsistency(x, y, z))
    {
      throw new RuntimeException("Inconsistent CoordinateSystem created.");
    }
  }

  public void dispose()
  {

  }

  public final CoordinateSystem applyTaitBryanAngles(TaitBryanAngles angles)
  {
    return applyTaitBryanAngles(angles._heading, angles._pitch, angles._roll);
  }

  public final CoordinateSystem applyTaitBryanAngles(Angle heading, Angle pitch, Angle roll)
  {
  
    //Check out Agustin Trujillo's review of this topic
    //This implementation is purposely explicit on every step
  
    final Vector3D u = _x;
    final Vector3D v = _y;
    final Vector3D w = _z;
  
    //Heading rotation
    boolean isHeadingZero = heading.isZero();
  
    MutableMatrix44D hm = isHeadingZero ? MutableMatrix44D.invalid() : MutableMatrix44D.createGeneralRotationMatrix(heading, w, Vector3D.ZERO);
  
    final Vector3D up = isHeadingZero ? u : u.transformedBy(hm, 1.0);
    final Vector3D vp = isHeadingZero ? v : v.transformedBy(hm, 1.0);
    final Vector3D wp = w;
  
    //Pitch rotation
    boolean isPitchZero = pitch.isZero();
  
    MutableMatrix44D pm = isPitchZero? MutableMatrix44D.invalid() : MutableMatrix44D.createGeneralRotationMatrix(pitch, up, Vector3D.ZERO);
  
    final Vector3D upp = up;
    final Vector3D vpp = isPitchZero? vp : vp.transformedBy(pm, 1.0);
    final Vector3D wpp = isPitchZero? wp : wp.transformedBy(pm, 1.0);
  
    //Roll rotation
    boolean isRollZero = roll.isZero();
  
    MutableMatrix44D rm = isRollZero? MutableMatrix44D.invalid() : MutableMatrix44D.createGeneralRotationMatrix(roll, vpp, Vector3D.ZERO);
  
    final Vector3D uppp = isRollZero? upp : upp.transformedBy(rm, 1.0);
    final Vector3D vppp = vpp;
    final Vector3D wppp = isRollZero? wpp : wpp.transformedBy(rm, 1.0);
  
    return new CoordinateSystem(uppp, vppp, wppp, _origin);
  }


  //bool CoordinateSystem::areOrtogonal(const Vector3D& x,
  //                                    const Vector3D& y,
  //                                    const Vector3D& z) {
  //  return x.isPerpendicularTo(y) && x.isPerpendicularTo(z) && y.isPerpendicularTo(z);
  //}
  
  public final CoordinateSystem changeOrigin(Vector3D newOrigin)
  {
    return new CoordinateSystem(_x, _y, _z, newOrigin);
  }

  public final TaitBryanAngles getTaitBryanAngles(CoordinateSystem global)
  {
  
    //We know...
  
    final Vector3D u = global._x;
    final Vector3D v = global._y;
    final Vector3D w = global._z;
  
    //const Vector3D uppp = _x;
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
  
      Angle pitch = Angle._MINUS_HALF_PI;
      Angle roll = Angle._ZERO;
      Angle heading = v.signedAngleBetween(wpp, w);
  
      return new TaitBryanAngles(heading, pitch, roll);
  
    }
    else if (x > 0.99999 && x < 1.000001)
    {
      //Pitch 90
      final Vector3D wpp = wppp; //No Roll
  
      Angle pitch = Angle._HALF_PI;
      Angle roll = Angle._ZERO;
      Angle heading = v.signedAngleBetween(wpp, w).sub(Angle._PI);
  
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

  public final boolean isEquals(CoordinateSystem that)
  {
    return _x.isEquals(that._x) && _y.isEquals(that._y) && _z.isEquals(that._z);
  }

  public final CoordinateSystem applyRotation(MutableMatrix44D m)
  {
    return new CoordinateSystem(_x.transformedBy(m, 1.0), _y.transformedBy(m, 1.0), _z.transformedBy(m, 1.0), _origin); //.transformedBy(m, 1.0));
  }

  public final MutableMatrix44D getRotationMatrix()
  {
    return new MutableMatrix44D(_x._x, _x._y, _x._z, 0, _y._x, _y._y, _y._z, 0, _z._x, _z._y, _z._z, 0, 0, 0, 0, 1);
  }
  public final MutableMatrix44D getMatrix()
  {
    final MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(_origin);
    final MutableMatrix44D rotation = getRotationMatrix();
    return translation.multiply(rotation);
  }

  public final void copyValueOfRotationMatrix(MutableMatrix44D m)
  {
    m.setValue(_x._x, _x._y, _x._z, 0, _y._x, _y._y, _y._z, 0, _z._x, _z._y, _z._z, 0, 0, 0, 0, 1);
  }

  public final boolean isConsistent()
  {
    return checkConsistency(_x, _y, _z);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("CoordinateSystem x: ");
    isb.addString(_x.description());
    isb.addString(", y: ");
    isb.addString(_y.description());
    isb.addString(", z: ");
    isb.addString(_z.description());
    isb.addString(", origin: ");
    isb.addString(_origin.description());
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  @Override
  public String toString() {
    return description();
  }

  public final Mesh createMesh(double size, Color xColor, Color yColor, Color zColor)
  {
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
    FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithGivenCenter(_origin);
    fbb.add(_origin);
    fbb.add(_origin.add(_x.normalized().times(size)));
    colors.add(xColor);
    colors.add(xColor);
  
    fbb.add(_origin);
    fbb.add(_origin.add(_y.normalized().times(size)));
    colors.add(yColor);
    colors.add(yColor);
  
    fbb.add(_origin);
    fbb.add(_origin.add(_z.normalized().times(size)));
    colors.add(zColor);
    colors.add(zColor);
  
    DirectMesh dm = new DirectMesh(GLPrimitive.lines(), true, fbb.getCenter(), fbb.create(), 5.0f, 1.0f, null, colors.create(), false, null);
  
    if (fbb != null)
       fbb.dispose();
  
    return dm;
  }

  public final Mesh createMesh(double size)
  {
    return createMesh(size, Color.RED, Color.GREEN, Color.BLUE);
  }

}