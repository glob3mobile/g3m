package org.glob3.mobile.generated; 
//
//  MutableVector2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  MutableVector2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




public class MutableVector2D
{
  private double _x;
  private double _y;


  public MutableVector2D()
  {
     _x = 0.0;
     _y = 0.0;
  }

  public final Vector2D asVector2D()
  {
    Vector2D v = new Vector2D(_x, _y);
    return v;
  }

  public MutableVector2D(double x, double y)
  {
     _x = x;
     _y = y;

  }

  public MutableVector2D(MutableVector2D v)
  {
     _x = v.x();
     _y = v.y();

  }

  public static MutableVector2D nan()
  {
    return new MutableVector2D(java.lang.Double.NaN, java.lang.Double.NaN);
  }

  public final boolean isEquals(double x, double y)
  {
    return _x == x && _y == y;
  }

  public final boolean isNan()
  {
    return (_x != _x) || (_y != _y);
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MutableVector2D normalized();

  public final double length()
  {
    return IMathUtils.instance().sqrt(squaredLength());
  }

  public final double squaredLength()
  {
    return _x * _x + _y * _y;
  }

  public final MutableVector2D add(MutableVector2D v)
  {
    return new MutableVector2D(_x + v._x, _y + v._y);
  }

  public final MutableVector2D sub(MutableVector2D v)
  {
    return new MutableVector2D(_x - v._x, _y - v._y);
  }

  public final MutableVector2D times(MutableVector2D v)
  {
    return new MutableVector2D(_x * v._x, _y * v._y);
  }

  public final MutableVector2D times(double magnitude)
  {
    return new MutableVector2D(_x * magnitude, _y * magnitude);
  }

  public final MutableVector2D div(MutableVector2D v)
  {
    return new MutableVector2D(_x / v._x, _y / v._y);
  }

  public final MutableVector2D div(double v)
  {
    return new MutableVector2D(_x / v, _y / v);
  }

  public final Angle angle()
  {
    double a = IMathUtils.instance().atan2(_y, _x);
    return Angle.fromRadians(a);
  }

  public final double x()
  {
    return _x;
  }

  public final double y()
  {
    return _y;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(MV2D ");
    isb.addDouble(_x);
    isb.addString(", ");
    isb.addDouble(_y);
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }

}