package org.glob3.mobile.generated;import java.util.*;

//
//  ShapeFullPositionEffect.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso Pérez on 03/05/13.
//
//

//
//  ShapeFullPositionEffect.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso Pérez on 03/05/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Shape;

public class ShapeFullPositionEffect extends EffectWithDuration
{
  private Shape _shape;

  private final Geodetic3D _fromPosition = new Geodetic3D();
  private final Geodetic3D _toPosition = new Geodetic3D();

  private final Angle _fromPitch = new Angle();
  private final Angle _toPitch = new Angle();

  private final Angle _fromHeading = new Angle();
  private final Angle _toHeading = new Angle();

  private final Angle _fromRoll = new Angle();
  private final Angle _toRoll = new Angle();

  private final boolean _forceToPositionOnCancel;
  private final boolean _forceToPositionOnStop;

  public ShapeFullPositionEffect(TimeInterval duration, Shape shape, Geodetic3D fromPosition, Geodetic3D toPosition, Angle fromPitch, Angle toPitch, Angle fromHeading, Angle toHeading, Angle fromRoll, Angle toRoll, boolean linearTiming, boolean forceToPositionOnCancel)
  {
	  this(duration, shape, fromPosition, toPosition, fromPitch, toPitch, fromHeading, toHeading, fromRoll, toRoll, linearTiming, forceToPositionOnCancel, true);
  }
  public ShapeFullPositionEffect(TimeInterval duration, Shape shape, Geodetic3D fromPosition, Geodetic3D toPosition, Angle fromPitch, Angle toPitch, Angle fromHeading, Angle toHeading, Angle fromRoll, Angle toRoll, boolean linearTiming)
  {
	  this(duration, shape, fromPosition, toPosition, fromPitch, toPitch, fromHeading, toHeading, fromRoll, toRoll, linearTiming, true, true);
  }
  public ShapeFullPositionEffect(TimeInterval duration, Shape shape, Geodetic3D fromPosition, Geodetic3D toPosition, Angle fromPitch, Angle toPitch, Angle fromHeading, Angle toHeading, Angle fromRoll, Angle toRoll)
  {
	  this(duration, shape, fromPosition, toPosition, fromPitch, toPitch, fromHeading, toHeading, fromRoll, toRoll, false, true, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: ShapeFullPositionEffect(const TimeInterval& duration, Shape* shape, const Geodetic3D& fromPosition, const Geodetic3D& toPosition, const Angle& fromPitch, const Angle& toPitch, const Angle& fromHeading, const Angle& toHeading, const Angle& fromRoll, const Angle& toRoll, boolean linearTiming = false, boolean forceToPositionOnCancel = true, boolean forceToPositionOnStop = true) : EffectWithDuration(duration, linearTiming), _shape(shape), _fromPosition(fromPosition), _toPosition(toPosition), _fromPitch(fromPitch), _toPitch(toPitch), _fromHeading(fromHeading), _toHeading(toHeading), _fromRoll(fromRoll), _toRoll(toRoll), _forceToPositionOnCancel(forceToPositionOnCancel), _forceToPositionOnStop(forceToPositionOnStop)
  public ShapeFullPositionEffect(TimeInterval duration, Shape shape, Geodetic3D fromPosition, Geodetic3D toPosition, Angle fromPitch, Angle toPitch, Angle fromHeading, Angle toHeading, Angle fromRoll, Angle toRoll, boolean linearTiming, boolean forceToPositionOnCancel, boolean forceToPositionOnStop)
  {
	  super(duration, linearTiming);
	  _shape = shape;
	  _fromPosition = new Geodetic3D(fromPosition);
	  _toPosition = new Geodetic3D(toPosition);
	  _fromPitch = new Angle(fromPitch);
	  _toPitch = new Angle(toPitch);
	  _fromHeading = new Angle(fromHeading);
	  _toHeading = new Angle(toHeading);
	  _fromRoll = new Angle(fromRoll);
	  _toRoll = new Angle(toRoll);
	  _forceToPositionOnCancel = forceToPositionOnCancel;
	  _forceToPositionOnStop = forceToPositionOnStop;
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
  
	if (!_fromPitch.isNan() && !_toPitch.isNan())
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setPitch(Angle::linearInterpolation(_fromPitch, _toPitch, alpha));
	  _shape.setPitch(Angle.linearInterpolation(new Angle(_fromPitch), new Angle(_toPitch), alpha));
	}
  
	if (!_fromHeading.isNan() && !_toHeading.isNan())
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setHeading(Angle::linearInterpolation(_fromHeading, _toHeading, alpha));
	  _shape.setHeading(Angle.linearInterpolation(new Angle(_fromHeading), new Angle(_toHeading), alpha));
	}
  
	if (!_fromRoll.isNan() && !_toRoll.isNan())
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setRoll(Angle::linearInterpolation(_fromRoll, _toRoll, alpha));
	  _shape.setRoll(Angle.linearInterpolation(new Angle(_fromRoll), new Angle(_toRoll), alpha));
	}
  }

  public final void cancel(TimeInterval when)
  {
	if (_forceToPositionOnCancel)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setPosition(_toPosition);
	  _shape.setPosition(new Geodetic3D(_toPosition));
	  if (!_toPitch.isNan())
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setPitch(_toPitch);
		_shape.setPitch(new Angle(_toPitch));
	  }
  
	  if (!_toHeading.isNan())
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setHeading(_toHeading);
		_shape.setHeading(new Angle(_toHeading));
	  }
  
	  if (!_toRoll.isNan())
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setRoll(_toRoll);
		_shape.setRoll(new Angle(_toRoll));
	  }
	}
  }
  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
	if (_forceToPositionOnStop)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setPosition(_toPosition);
	  _shape.setPosition(new Geodetic3D(_toPosition));
	  if (!_toPitch.isNan())
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setPitch(_toPitch);
		_shape.setPitch(new Angle(_toPitch));
	  }
  
	  if (!_toHeading.isNan())
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setHeading(_toHeading);
		_shape.setHeading(new Angle(_toHeading));
	  }
  
	  if (!_toRoll.isNan())
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _shape->setRoll(_toRoll);
		_shape.setRoll(new Angle(_toRoll));
	  }
	}
  }

}
