package org.glob3.mobile.generated; 
//
//  Vector2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

//
//  Vector2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//




//class MutableVector2D;

public class Vector2D
{


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector2D operator =(Vector2D v);

  public final double _x;
  public final double _y;

  public static Vector2D zero()
  {
    return new Vector2D(0, 0);
  }

  public Vector2D(double x, double y)
  {
     _x = x;
     _y = y;

  }

  public Vector2D(Vector2D v)
  {
     _x = v._x;
     _y = v._y;

  }

  public final double length()
  {
    return IMathUtils.instance().sqrt(squaredLength());
  }

  public final Angle orientation()
  {
     return Angle.fromRadians(IMathUtils.instance().atan2(_y, _x));
  }

  public final double squaredLength()
  {
    return _x * _x + _y * _y;
  }

  public final Vector2D add(Vector2D v)
  {
    return new Vector2D(_x + v._x, _y + v._y);
  }

  public final Vector2D sub(Vector2D v)
  {
    return new Vector2D(_x - v._x, _y - v._y);
  }

  public final Vector2D times(Vector2D v)
  {
    return new Vector2D(_x * v._x, _y * v._y);
  }

  public final Vector2D times(double magnitude)
  {
    return new Vector2D(_x * magnitude, _y * magnitude);
  }

  public final Vector2D div(Vector2D v)
  {
    return new Vector2D(_x / v._x, _y / v._y);
  }

  public final Vector2D div(double v)
  {
    return new Vector2D(_x / v, _y / v);
  }

  public final Angle angle()
  {
    double a = IMathUtils.instance().atan2(_y, _x);
    return Angle.fromRadians(a);
  }

  public static Vector2D nan()
  {
    return new Vector2D(java.lang.Double.NaN, java.lang.Double.NaN);
  }

  public final double maxAxis()
  {
    return (_x >= _y) ? _x : _y;
  }

  public final double minAxis()
  {
    return (_x <= _y) ? _x : _y;
  }

  public final MutableVector2D asMutableVector2D()
  {
    return new MutableVector2D(_x, _y);
  }

  public final boolean isNan()
  {
//    return IMathUtils::instance()->isNan(_x) || IMathUtils::instance()->isNan(_y);

    if (_x != _x)
    {
      return true;
    }
    if (_y != _y)
    {
      return true;
    }
    return false;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(V2D ");
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

  public static Vector2D intersectionOfTwoLines(Vector2D p1, Vector2D r1, Vector2D p2, Vector2D r2)
  {
  
    //u = (p2 - p1) × r1 / (r1 × r2)
    //out = p2 + u x r2
  
    double u = ((p2.sub(p1)).dot(r1)) / r1.dot(r2);
    Vector2D out = p2.add(r2.times(u));
  
    return out;
  
  
  }

  public final double dot(Vector2D v)
  {
    return _x * v._x + _y * v._y;
  }

  public static boolean isPointInsideTriangle(Vector2D p, Vector2D cornerA, Vector2D cornerB, Vector2D cornerC)
  {

    final double alpha = (((cornerB._y - cornerC._y) * (p._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (p._y - cornerC._y))) / (((cornerB._y - cornerC._y) * (cornerA._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (cornerA._y - cornerC._y)));
    final double beta = (((cornerC._y - cornerA._y) * (p._x - cornerC._x)) + ((cornerA._x - cornerC._x) * (p._y - cornerC._y))) / (((cornerB._y - cornerC._y) * (cornerA._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (cornerA._y - cornerC._y)));
    final double gamma = 1.0 - alpha - beta;

    if ((alpha > 0) && (beta > 0) && (gamma > 0))
    {
      return true;
    }
    return false;
  }

  public static double triangleArea(Vector2D a, Vector2D b, Vector2D c)
  {
    //Seen here: http://www.mathopenref.com/coordtrianglearea.html
    double x = (a._x * (b._y - c._y) + b._x * (c._y - a._y) + c._x * (a._y - b._y));
    return IMathUtils.instance().abs(x) / 2;
  }

  public static boolean segmentsIntersect(Vector2D a, Vector2D b, Vector2D c, Vector2D d)
  {
    //http://www.smipple.net/snippet/sparkon/%5BC%2B%2B%5D%202D%20lines%20segment%20intersection%20
    final double den = (((d._y - c._y) * (b._x - a._x)) - ((d._x - c._x) * (b._y - a._y)));
    final double num1 = (((d._x - c._x) * (a._y - c._y)) - ((d._y - c._y) * (a._x - c._x)));
    final double num2 = (((b._x - a._x) * (a._y - c._y)) - ((b._y - a._y) * (a._x - c._x)));
    final double u1 = num1 / den;
    final double u2 = num2 / den;

    if ((den == 0) && (num1 == 0) && (num2 == 0))
    {
      /* The two lines are coincidents */
      return false;
    }
    if (den == 0)
    {
      /* The two lines are parallel */
      return false;
    }
    if ((u1 < 0) || (u1 > 1) || (u2 < 0) || (u2 > 1))
    {
      /* Lines do not collide */
      return false;
    }
    /* Lines DO collide */
    return true;
  }

}