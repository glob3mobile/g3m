package org.glob3.mobile.generated;//
//  CoordinateSystem.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/01/14.
//
//

//
//  CoordinateSystem.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/01/14.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;

public class CoordinateSystem
{

  private static boolean checkConsistency(Vector3D x, Vector3D y, Vector3D z)
  {
	if (x.isNan() || y.isNan() || z.isNan())
	{
	  return false;
	}
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return areOrtogonal(x, y, z);
	return areOrtogonal(new Vector3D(x), new Vector3D(y), new Vector3D(z));
  }

  private static boolean areOrtogonal(Vector3D x, Vector3D y, Vector3D z)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return x.isPerpendicularTo(y) && x.isPerpendicularTo(z) && y.isPerpendicularTo(z);
	return x.isPerpendicularTo(new Vector3D(y)) && x.isPerpendicularTo(new Vector3D(z)) && y.isPerpendicularTo(new Vector3D(z));
  }


  public final Vector3D _x = new Vector3D();
  public final Vector3D _y = new Vector3D();
  public final Vector3D _z = new Vector3D();
  public final Vector3D _origin = new Vector3D();


  public static CoordinateSystem global()
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return CoordinateSystem(Vector3D::upX(), Vector3D::upY(), Vector3D::upZ(), Vector3D::zero);
	return new CoordinateSystem(Vector3D.upX(), Vector3D.upY(), Vector3D.upZ(), new Vector3D(Vector3D.zero));
  }

  public CoordinateSystem(Vector3D x, Vector3D y, Vector3D z, Vector3D origin)
  {
	  _x = new Vector3D(x.normalized());
	  _y = new Vector3D(y.normalized());
	  _z = new Vector3D(z.normalized());
	  _origin = new Vector3D(origin);
	//TODO CHECK CONSISTENCY
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!checkConsistency(x, y, z))
	if (!checkConsistency(new Vector3D(x), new Vector3D(y), new Vector3D(z)))
	{
	  ILogger.instance().logError("Inconsistent CoordinateSystem created.");
	  //THROW_EXCEPTION("Inconsistent CoordinateSystem created.");
	}
  }

  //For camera

  //For camera
  public CoordinateSystem(Vector3D viewDirection, Vector3D up, Vector3D origin)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _x = new Vector3D(viewDirection.cross(up).normalized());
	  _x = new Vector3D(viewDirection.cross(new Vector3D(up)).normalized());
	  _y = new Vector3D(viewDirection.normalized());
	  _z = new Vector3D(up.normalized());
	  _origin = new Vector3D(origin);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!checkConsistency(_x, _y, _z))
	if (!checkConsistency(new Vector3D(_x), new Vector3D(_y), new Vector3D(_z)))
	{
	  ILogger.instance().logError("Inconsistent CoordinateSystem created.");
	  //THROW_EXCEPTION("Inconsistent CoordinateSystem created.");
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createMesh(double size, const Color& xColor, const Color& yColor, const Color& zColor) const
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
  
	DirectMesh dm = new DirectMesh(GLPrimitive.lines(), true, fbb.getCenter(), fbb.create(), (float)5.0, (float)1.0, null, colors.create(), (float)1.0, false, null);
  
	if (fbb != null)
		fbb.dispose();
  
	return dm;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: CoordinateSystem applyTaitBryanAngles(const TaitBryanAngles& angles) const
  public final CoordinateSystem applyTaitBryanAngles(TaitBryanAngles angles)
  {
	return applyTaitBryanAngles(angles._heading, angles._pitch, angles._roll);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: CoordinateSystem applyTaitBryanAngles(const Angle& heading, const Angle& pitch, const Angle& roll) const
  public final CoordinateSystem applyTaitBryanAngles(Angle heading, Angle pitch, Angle roll)
  {
  
	//Check out Agustin Trujillo's review of this topic
	//This implementation is purposely explicit on every step
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D u = _x;
	final Vector3D u = new Vector3D(_x);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D v = _y;
	final Vector3D v = new Vector3D(_y);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D w = _z;
	final Vector3D w = new Vector3D(_z);
  
	//Heading rotation
	boolean isHeadingZero = heading.isZero();
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableMatrix44D hm = isHeadingZero ? MutableMatrix44D::invalid() : MutableMatrix44D::createGeneralRotationMatrix(heading, w, Vector3D::zero);
	MutableMatrix44D hm = isHeadingZero ? MutableMatrix44D.invalid() : MutableMatrix44D.createGeneralRotationMatrix(new Angle(heading), new Vector3D(w), new Vector3D(Vector3D.zero));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D up = isHeadingZero ? u : u.transformedBy(hm, 1.0);
	final Vector3D up = isHeadingZero ? u : u.transformedBy(new MutableMatrix44D(hm), 1.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D vp = isHeadingZero ? v : v.transformedBy(hm, 1.0);
	final Vector3D vp = isHeadingZero ? v : v.transformedBy(new MutableMatrix44D(hm), 1.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D wp = w;
	final Vector3D wp = new Vector3D(w);
  
	//Pitch rotation
	boolean isPitchZero = pitch.isZero();
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableMatrix44D pm = isPitchZero? MutableMatrix44D::invalid() : MutableMatrix44D::createGeneralRotationMatrix(pitch, up, Vector3D::zero);
	MutableMatrix44D pm = isPitchZero? MutableMatrix44D.invalid() : MutableMatrix44D.createGeneralRotationMatrix(new Angle(pitch), new Vector3D(up), new Vector3D(Vector3D.zero));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D upp = up;
	final Vector3D upp = new Vector3D(up);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D vpp = isPitchZero? vp : vp.transformedBy(pm, 1.0);
	final Vector3D vpp = isPitchZero? vp : vp.transformedBy(new MutableMatrix44D(pm), 1.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D wpp = isPitchZero? wp : wp.transformedBy(pm, 1.0);
	final Vector3D wpp = isPitchZero? wp : wp.transformedBy(new MutableMatrix44D(pm), 1.0);
  
	//Roll rotation
	boolean isRollZero = roll.isZero();
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableMatrix44D rm = isRollZero? MutableMatrix44D::invalid() : MutableMatrix44D::createGeneralRotationMatrix(roll, vpp, Vector3D::zero);
	MutableMatrix44D rm = isRollZero? MutableMatrix44D.invalid() : MutableMatrix44D.createGeneralRotationMatrix(new Angle(roll), new Vector3D(vpp), new Vector3D(Vector3D.zero));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D uppp = isRollZero? upp : upp.transformedBy(rm, 1.0);
	final Vector3D uppp = isRollZero? upp : upp.transformedBy(new MutableMatrix44D(rm), 1.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D vppp = vpp;
	final Vector3D vppp = new Vector3D(vpp);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D wppp = isRollZero? wpp : wpp.transformedBy(rm, 1.0);
	final Vector3D wppp = isRollZero? wpp : wpp.transformedBy(new MutableMatrix44D(rm), 1.0);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return CoordinateSystem(uppp, vppp, wppp, _origin);
	return new CoordinateSystem(new Vector3D(uppp), new Vector3D(vppp), new Vector3D(wppp), new Vector3D(_origin));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: CoordinateSystem changeOrigin(const Vector3D& newOrigin) const
  public final CoordinateSystem changeOrigin(Vector3D newOrigin)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return CoordinateSystem(_x, _y, _z, newOrigin);
	return new CoordinateSystem(new Vector3D(_x), new Vector3D(_y), new Vector3D(_z), new Vector3D(newOrigin));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TaitBryanAngles getTaitBryanAngles(const CoordinateSystem& global) const
  public final TaitBryanAngles getTaitBryanAngles(CoordinateSystem global)
  {
  
	//We know...
  
	final Vector3D u = global._x;
	final Vector3D v = global._y;
	final Vector3D w = global._z;
  
	//const Vector3D uppp = _x;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D vppp = _y;
	final Vector3D vppp = new Vector3D(_y);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D wppp = _z;
	final Vector3D wppp = new Vector3D(_z);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D wp = w;
	final Vector3D wp = new Vector3D(w);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D vpp = vppp;
	final Vector3D vpp = new Vector3D(vppp);
  
	//We calculate
  
	//Pitch "especial" positions
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double x = vpp.dot(wp);
	double x = vpp.dot(new Vector3D(wp));
	if (x < -0.99999 && x > -1.000001)
	{
	  //Pitch -90
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D wpp = wppp;
	  final Vector3D wpp = new Vector3D(wppp); //No Roll
  
	  Angle pitch = Angle.fromDegrees(-90);
	  Angle roll = Angle.zero();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Angle heading = v.signedAngleBetween(wpp, w);
	  Angle heading = v.signedAngleBetween(new Vector3D(wpp), new Vector3D(w));
  
	  return new TaitBryanAngles(heading, pitch, roll);
  
	}
	else if (x > 0.99999 && x < 1.000001)
	{
	  //Pitch 90
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D wpp = wppp;
	  final Vector3D wpp = new Vector3D(wppp); //No Roll
  
	  Angle pitch = Angle.fromDegrees(90);
	  Angle roll = Angle.zero();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Angle heading = v.signedAngleBetween(wpp, w).sub(Angle::fromDegrees(180));
	  Angle heading = v.signedAngleBetween(new Vector3D(wpp), new Vector3D(w)).sub(Angle.fromDegrees(180));
  
	  return new TaitBryanAngles(heading, pitch, roll);
	}
  
	//Normal formula
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D up = vpp.cross(wp);
	final Vector3D up = vpp.cross(new Vector3D(wp));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D vp = wp.cross(up);
	final Vector3D vp = wp.cross(new Vector3D(up));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D upp = up;
	final Vector3D upp = new Vector3D(up);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D wpp = upp.cross(vpp);
	final Vector3D wpp = upp.cross(new Vector3D(vpp));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Angle heading = u.signedAngleBetween(up, w);
	Angle heading = u.signedAngleBetween(new Vector3D(up), new Vector3D(w));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Angle pitch = vp.signedAngleBetween(vpp, up);
	Angle pitch = vp.signedAngleBetween(new Vector3D(vpp), new Vector3D(up));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Angle roll = wpp.signedAngleBetween(wppp, vpp);
	Angle roll = wpp.signedAngleBetween(new Vector3D(wppp), new Vector3D(vpp));
  
	return new TaitBryanAngles(heading, pitch, roll);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEqualsTo(const CoordinateSystem& that) const
  public final boolean isEqualsTo(CoordinateSystem that)
  {
	return _x.isEquals(that._x) && _y.isEquals(that._y) && _z.isEquals(that._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: CoordinateSystem applyRotation(const MutableMatrix44D& m) const
  public final CoordinateSystem applyRotation(MutableMatrix44D m)
  {
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return CoordinateSystem(_x.transformedBy(m, 1.0), _y.transformedBy(m, 1.0), _z.transformedBy(m, 1.0), _origin);
	return new CoordinateSystem(_x.transformedBy(new MutableMatrix44D(m), 1.0), _y.transformedBy(new MutableMatrix44D(m), 1.0), _z.transformedBy(new MutableMatrix44D(m), 1.0), new Vector3D(_origin)); //.transformedBy(m, 1.0));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D getRotationMatrix() const
  public final MutableMatrix44D getRotationMatrix()
  {
  
	return new MutableMatrix44D(_x._x, _x._y, _x._z, 0, _y._x, _y._y, _y._z, 0, _z._x, _z._y, _z._z, 0, 0,0,0,1);
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void copyValueOfRotationMatrix(MutableMatrix44D& m) const
  public final void copyValueOfRotationMatrix(tangible.RefObject<MutableMatrix44D> m)
  {
	m.argvalue.setValue(_x._x, _x._y, _x._z, 0, _y._x, _y._y, _y._z, 0, _z._x, _z._y, _z._z, 0, 0, 0, 0, 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isConsistent() const
  public final boolean isConsistent()
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return checkConsistency(_x, _y, _z);
	return checkConsistency(new Vector3D(_x), new Vector3D(_y), new Vector3D(_z));
  }

}
