package org.glob3.mobile.generated;import java.util.*;

//
//  Vector3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

//
//  Vector3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//





//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableVector3D;

public class Vector3D
{

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//	Vector3D operator =(Vector3D that);


	public static Vector3D zero = new Vector3D(0,0,0);

	public final double _x;
	public final double _y;
	public final double _z;


	public Vector3D(double x, double y, double z)
	{
		_x = x;
		_y = y;
		_z = z;

	}

	public void dispose()
	{
	}

	public Vector3D(Vector3D v)
	{
		_x = v._x;
		_y = v._y;
		_z = v._z;

	}

	public static Vector3D nan()
	{
//C++ TO JAVA CONVERTER TODO TASK: The #define macro NAND was defined in alternate ways and cannot be replaced in-line:
		return new Vector3D(NAND, NAND, NAND);
	}

	//  static Vector3D zero() {
	//    return Vector3D(0, 0, 0);
	//  }

	public static Vector3D upX()
	{
		return new Vector3D(1, 0, 0);
	}

	public static Vector3D downX()
	{
		return new Vector3D(-1, 0, 0);
	}

	public static Vector3D upY()
	{
		return new Vector3D(0, 1, 0);
	}

	public static Vector3D downY()
	{
		return new Vector3D(0, -1, 0);
	}

	public static Vector3D upZ()
	{
		return new Vector3D(0, 0, 1);
	}

	public static Vector3D downZ()
	{
		return new Vector3D(0, 0, -1);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNan() const
	public final boolean isNan()
	{
		return ((_x != _x) || (_y != _y) || (_z != _z));
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const Vector3D& v) const
	public final boolean isEquals(Vector3D v)
	{
		return (v._x == _x && v._y == _y && v._z == _z);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isZero() const
	public final boolean isZero()
	{
		return (_x == 0) && (_y == 0) && (_z == 0);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D normalized() const
	public final Vector3D normalized()
	{
		if (isNan())
		{
			return nan();
		}
		if (isZero())
		{
			return zero;
		}
		final double d = length();
		return new Vector3D(_x / d, _y / d, _z / d);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double length() const
	public final double length()
	{
		return IMathUtils.instance().sqrt(squaredLength());
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double squaredLength() const
	public final double squaredLength()
	{
		return _x * _x + _y * _y + _z * _z;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double dot(const Vector3D& v) const
	public final double dot(Vector3D v)
	{
		return _x * v._x + _y * v._y + _z * v._z;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isPerpendicularTo(const Vector3D& v) const
	public final boolean isPerpendicularTo(Vector3D v)
	{
		return IMathUtils.instance().abs(_x * v._x + _y * v._y + _z * v._z) < 0.00001;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D add(const Vector3D& v) const
	public final Vector3D add(Vector3D v)
	{
		return new Vector3D(_x + v._x, _y + v._y, _z + v._z);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D add(double d) const
	public final Vector3D add(double d)
	{
		return new Vector3D(_x + d, _y + d, _z + d);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D sub(const Vector3D& v) const
	public final Vector3D sub(Vector3D v)
	{
		return new Vector3D(_x - v._x, _y - v._y, _z - v._z);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D sub(const MutableVector3D& v) const
	public final Vector3D sub(MutableVector3D v)
	{
		return new Vector3D(_x - v.x(), _y - v.y(), _z - v.z());
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D sub(double d) const
	public final Vector3D sub(double d)
	{
		return new Vector3D(_x - d, _y - d, _z - d);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D times(const Vector3D& v) const
	public final Vector3D times(Vector3D v)
	{
		return new Vector3D(_x * v._x, _y * v._y, _z * v._z);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D times(const double magnitude) const
	public final Vector3D times(double magnitude)
	{
		return new Vector3D(_x * magnitude, _y * magnitude, _z * magnitude);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D div(const Vector3D& v) const
	public final Vector3D div(Vector3D v)
	{
		return new Vector3D(_x / v._x, _y / v._y, _z / v._z);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D div(const double v) const
	public final Vector3D div(double v)
	{
		return new Vector3D(_x / v, _y / v, _z / v);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D cross(const Vector3D& other) const
	public final Vector3D cross(Vector3D other)
	{
		return new Vector3D(_y * other._z - _z * other._y, _z * other._x - _x * other._z, _x * other._y - _y * other._x);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle angleBetween(const Vector3D& other) const
	public final Angle angleBetween(Vector3D other)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return Angle::fromRadians(Vector3D::angleInRadiansBetween(*this, other));
		return Angle.fromRadians(Vector3D.angleInRadiansBetween(this, new Vector3D(other)));
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double angleInRadiansBetween(const Vector3D& other) const
	public final double angleInRadiansBetween(Vector3D other)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return angleInRadiansBetween(*this, other);
		return angleInRadiansBetween(this, new Vector3D(other));
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle signedAngleBetween(const Vector3D& other, const Vector3D& up) const
	public final Angle signedAngleBetween(Vector3D other, Vector3D up)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Angle angle = angleBetween(other);
		final Angle angle = angleBetween(new Vector3D(other));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (cross(other).dot(up) > 0)
		if (cross(new Vector3D(other)).dot(new Vector3D(up)) > 0)
		{
			return angle;
		}
    
		return angle.times(-1);
	}

	public static double normalizedDot(Vector3D a, Vector3D b)
	{
		final double aLength = a.length();
		final double a_x = a._x / aLength;
		final double a_y = a._y / aLength;
		final double a_z = a._z / aLength;
    
		final double bLength = b.length();
		final double b_x = b._x / bLength;
		final double b_y = b._y / bLength;
		final double b_z = b._z / bLength;
    
		return ((a_x * b_x) + (a_y * b_y) + (a_z * b_z));
	}

	public static double normalizedDot(Vector3D a, MutableVector3D b)
	{
		final double aLength = a.length();
		final double a_x = a._x / aLength;
		final double a_y = a._y / aLength;
		final double a_z = a._z / aLength;
    
		final double bLength = b.length();
		final double b_x = b.x() / bLength;
		final double b_y = b.y() / bLength;
		final double b_z = b.z() / bLength;
    
		return ((a_x * b_x) + (a_y * b_y) + (a_z * b_z));
	}

	public static double angleInRadiansBetween(Vector3D a, Vector3D b)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double c = Vector3D::normalizedDot(a, b);
		double c = Vector3D.normalizedDot(new Vector3D(a), new Vector3D(b));
		if (c > 1.0)
		{
			c = 1.0;
		}
		else if (c < -1.0)
		{
			c = -1.0;
		}
		return IMathUtils.instance().acos(c);
	}

	public static double angleInRadiansBetween(Vector3D a, MutableVector3D b)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double c = Vector3D::normalizedDot(a, b);
		double c = Vector3D.normalizedDot(new Vector3D(a), new MutableVector3D(b));
		if (c > 1.0)
		{
			c = 1.0;
		}
		else if (c < -1.0)
		{
			c = -1.0;
		}
		return IMathUtils.instance().acos(c);
	}

	public static Angle angleBetween(Vector3D a, Vector3D b)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return Angle::fromRadians(angleInRadiansBetween(a, b));
		return Angle.fromRadians(angleInRadiansBetween(new Vector3D(a), new Vector3D(b)));
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D rotateAroundAxis(const Vector3D& axis, const Angle& theta) const
	public final Vector3D rotateAroundAxis(Vector3D axis, Angle theta)
	{
		final double u = axis._x;
		final double v = axis._y;
		final double w = axis._z;
    
	//C++ TO JAVA CONVERTER TODO TASK: The #define macro COS was defined in alternate ways and cannot be replaced in-line:
		final double cosTheta = COS(theta._radians);
	//C++ TO JAVA CONVERTER TODO TASK: The #define macro SIN was defined in alternate ways and cannot be replaced in-line:
		final double sinTheta = SIN(theta._radians);
    
		final double ms = axis.squaredLength();
		final double m = IMathUtils.instance().sqrt(ms);
    
		return new Vector3D(((u * (u * _x + v * _y + w * _z)) + (((_x * (v * v + w * w)) - (u * (v * _y + w * _z))) * cosTheta) + (m * ((-w * _y) + (v * _z)) * sinTheta)) / ms, ((v * (u * _x + v * _y + w * _z)) + (((_y * (u * u + w * w)) - (v * (u * _x + w * _z))) * cosTheta) + (m * ((w * _x) - (u * _z)) * sinTheta)) / ms, ((w * (u * _x + v * _y + w * _z)) + (((_z * (u * u + v * v)) - (w * (u * _x + v * _y))) * cosTheta) + (m * (-(v * _x) + (u * _y)) * sinTheta)) / ms);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D transformedBy(const MutableMatrix44D &m, const double homogeneus) const
	public final Vector3D transformedBy(MutableMatrix44D m, double homogeneus)
	{
		//int __TODO_move_to_matrix;
		return new Vector3D(_x * m.get0() + _y * m.get4() + _z * m.get8() + homogeneus * m.get12(), _x * m.get1() + _y * m.get5() + _z * m.get9() + homogeneus * m.get13(), _x * m.get2() + _y * m.get6() + _z * m.get10() + homogeneus * m.get14());
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D asMutableVector3D() const
	public final MutableVector3D asMutableVector3D()
	{
		return new MutableVector3D(_x, _y, _z);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double maxAxis() const
	public final double maxAxis()
	{
		if (_x >= _y && _x >= _z)
		{
			return _x;
		}
		else if (_y >= _z)
		{
			return _y;
		}
		else
		{
			return _z;
		}
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double minAxis() const
	public final double minAxis()
	{
		if (_x <= _y && _x <= _z)
		{
			return _x;
		}
		else if (_y <= _z)
		{
			return _y;
		}
		else
		{
			return _z;
		}
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double axisAverage() const
	public final double axisAverage()
	{
		return ((_x + _y + _z) / 3);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D projectionInPlane(const Vector3D& normal) const
	public final Vector3D projectionInPlane(Vector3D normal)
	{
		Vector3D axis = normal.cross(this);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableMatrix44D m = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(90), axis);
		MutableMatrix44D m = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(90), new Vector3D(axis));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D projected = normal.transformedBy(m, 0).normalized();
		Vector3D projected = normal.transformedBy(new MutableMatrix44D(m), 0).normalized();
		return projected.times(this.length());
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
	public final String description()
	{
		IStringBuilder isb = IStringBuilder.newStringBuilder();
		isb.addString("(V3D ");
		isb.addDouble(_x);
		isb.addString(", ");
		isb.addDouble(_y);
		isb.addString(", ");
		isb.addDouble(_z);
		isb.addString(")");
		final String s = isb.getString();
		if (isb != null)
			isb.dispose();
		return s;
	}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final Override public String toString()
	{
		return description();
	}
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector3D clamp(const Vector3D& min, const Vector3D& max) const
	public final Vector3D clamp(Vector3D min, Vector3D max)
	{
    
		final IMathUtils mu = IMathUtils.instance();
    
		final double x = mu.clamp(_x, min._x, max._x);
		final double y = mu.clamp(_y, min._y, max._y);
		final double z = mu.clamp(_z, min._z, max._z);
    
		return new Vector3D(x, y, z);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const double squaredDistanceTo(const Vector3D& that) const
	public final double squaredDistanceTo(Vector3D that)
	{
		final double dx = _x - that._x;
		final double dy = _y - that._y;
		final double dz = _z - that._z;
		return (dx * dx) + (dy * dy) + (dz * dz);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const double distanceTo(const Vector3D& that) const
	public final double distanceTo(Vector3D that)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return IMathUtils::instance()->sqrt(squaredDistanceTo(that));
		return IMathUtils.instance().sqrt(squaredDistanceTo(new Vector3D(that)));
	}

	public static Vector3D getCenter(java.util.ArrayList<Vector3D> points)
	{
		double _minX = points.get(0)._x;
		double _minY = points.get(0)._y;
		double _minZ = points.get(0)._z;
		double _maxX = points.get(0)._x;
		double _maxY = points.get(0)._y;
		double _maxZ = points.get(0)._z;
		for (int i = 1; i < points.size(); i++)
		{
			Vector3D p = points.get(i);
			if (p.isNan())
			{
				continue;
			}
			if (_minX > p._x)
			{
				_minX = p._x;
			}
			if (_minY > p._y)
			{
				_minY = p._y;
			}
			if (_minZ > p._z)
			{
				_minX = p._x;
			}
    
			if (_maxX < p._x)
			{
				_maxX = p._x;
			}
			if (_maxY < p._y)
			{
				_maxY = p._y;
			}
			if (_maxZ < p._z)
			{
				_maxZ = p._z;
			}
		}
    
		return new Vector3D((_minX + _maxX) / 2.0, (_minY + _maxY) / 2.0, (_minZ + _maxZ) / 2.0);
	}


	public static Vector3D interpolate(Vector3D a, Vector3D b, double v)
	{
		double v1 = (1-v);
		return new Vector3D(a._x * v1 + b._x * v, a._y * v1 + b._y * v, a._z * v1 + b._z * v);
	}

	public static Vector3D rayIntersectsTriangle(Vector3D rayOrigin, Vector3D rayDirection, Vector3D vertex0, Vector3D vertex1, Vector3D vertex2)
	{
		//From: https://en.wikipedia.org/wiki/MÃ¶llerâ€"Trumbore_intersection_algorithm#C++_Implementation
    
		//Origin to 0
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D v0 = vertex0.sub(rayOrigin);
		Vector3D v0 = vertex0.sub(new Vector3D(rayOrigin));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D v1 = vertex1.sub(rayOrigin);
		Vector3D v1 = vertex1.sub(new Vector3D(rayOrigin));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D v2 = vertex2.sub(rayOrigin);
		Vector3D v2 = vertex2.sub(new Vector3D(rayOrigin));
		Vector3D rayVector = rayDirection.normalized();
    
		final float EPSILON = 0.00001f;
		//    float a,f,u,v;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D edge1 = v1.sub(v0);
		Vector3D edge1 = v1.sub(new Vector3D(v0));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D edge2 = v2.sub(v0);
		Vector3D edge2 = v2.sub(new Vector3D(v0));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D h = rayVector.cross(edge2);
		Vector3D h = rayVector.cross(new Vector3D(edge2));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double a = edge1.dot(h);
		double a = edge1.dot(new Vector3D(h));
		if (a > -EPSILON && a < EPSILON)
		{
			return Vector3D.nan();
		}
		double f = 1/a;
		Vector3D s = v0.times(-1.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double u = f * (s.dot(h));
		double u = f * (s.dot(new Vector3D(h)));
		if (u < 0.0 || u > 1.0)
		{
			return Vector3D.nan();
		}
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D q = s.cross(edge1);
		Vector3D q = s.cross(new Vector3D(edge1));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double v = f * rayVector.dot(q);
		double v = f * rayVector.dot(new Vector3D(q));
		if (v < 0.0 || u + v > 1.0)
		{
			return Vector3D.nan();
		}
		// At this stage we can compute t to find out where the intersection point is on the line.
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double t = f * edge2.dot(q);
		double t = f * edge2.dot(new Vector3D(q));
		if (t > EPSILON) // ray intersection
		{
			return rayOrigin.add(rayVector.times(t));
		}
		else // This means that there is a line intersection but not a ray intersection.
		{
			return Vector3D.nan();
		}
	}

}
