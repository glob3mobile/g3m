package org.glob3.mobile.generated; 
//
//  Vector4D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/6/12.
//
//

//
//  Vector4D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/6/12.
//
//



public class Vector4D
{
  private final double _x;
  private final double _y;
  private final double _z;
  private final double _w;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector4D operator =(Vector4D that);


  public Vector4D(double x, double y, double z, double w)
  {
	  _x = x;
	  _y = y;
	  _z = z;
	  _w = w;

  }

  public void dispose()
  {
  }

  public Vector4D(Vector4D v)
  {
	  _x = v._x;
	  _y = v._y;
	  _z = v._z;
	  _w = v._w;

  }

  public static Vector4D nan()
  {
	return new Vector4D(IMathUtils.instance().NanD(), IMathUtils.instance().NanD(), IMathUtils.instance().NanD(), IMathUtils.instance().NanD());
  }

  public static Vector4D zero()
  {
	return new Vector4D(0.0, 0.0, 0.0, 0.0);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNan() const
  public final boolean isNan()
  {
	return (IMathUtils.instance().isNan(_x) || IMathUtils.instance().isNan(_y) || IMathUtils.instance().isNan(_z) || IMathUtils.instance().isNan(_w));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isZero() const
  public final boolean isZero()
  {
	return (_x == 0) && (_y == 0) && (_z == 0) && (_w == 0);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double x() const
  public final double x()
  {
	return _x;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double y() const
  public final double y()
  {
	return _y;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double z() const
  public final double z()
  {
	return _z;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double w() const
  public final double w()
  {
	return _w;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("(V4D ").add(_x).add(", ").add(_y).add(", ").add(_z).add(", ").add(_w).add(")");
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector4D transformedBy(const MutableMatrix44D &m) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector4D transformedBy(MutableMatrix44D m);

}