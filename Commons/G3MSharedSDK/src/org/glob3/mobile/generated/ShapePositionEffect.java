package org.glob3.mobile.generated;import java.util.*;

//
//  ShapePositionEffect.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/1/13.
//
//

//
//  ShapePositionEffect.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/1/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Shape;

public class ShapePositionEffect extends EffectWithDuration
{
  private Shape _shape;

  private final Geodetic3D _fromPosition = new Geodetic3D();
  private final Geodetic3D _toPosition = new Geodetic3D();

  public ShapePositionEffect(TimeInterval duration, Shape shape, Geodetic3D fromPosition, Geodetic3D toPosition)
  {
	  this(duration, shape, fromPosition, toPosition, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: ShapePositionEffect(const TimeInterval& duration, Shape* shape, const Geodetic3D& fromPosition, const Geodetic3D& toPosition, const boolean linearTiming=false) : EffectWithDuration(duration, linearTiming), _shape(shape), _fromPosition(fromPosition), _toPosition(toPosition)
  public ShapePositionEffect(TimeInterval duration, Shape shape, Geodetic3D fromPosition, Geodetic3D toPosition, boolean linearTiming)
  {
	  super(duration, linearTiming);
	  _shape = shape;
	  _fromPosition = new Geodetic3D(fromPosition);
	  _toPosition = new Geodetic3D(toPosition);

  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double alpha = getAlpha(when);
	final double alpha = getAlpha(new TimeInterval(when));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Geodetic3D pos = Geodetic3D::linearInterpolation(_fromPosition, _toPosition, alpha);
	final Geodetic3D pos = Geodetic3D.linearInterpolation(new Geodetic3D(_fromPosition), new Geodetic3D(_toPosition), alpha);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setPosition(pos);
	_shape.setPosition(new Geodetic3D(pos));
  }

  public final void cancel(TimeInterval when)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setPosition(_toPosition);
	_shape.setPosition(new Geodetic3D(_toPosition));
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setPosition(_toPosition);
	_shape.setPosition(new Geodetic3D(_toPosition));
  }

}
