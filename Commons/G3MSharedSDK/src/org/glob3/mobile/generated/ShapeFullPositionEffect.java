package org.glob3.mobile.generated; 
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



//class Shape;

public class ShapeFullPositionEffect extends EffectWithDuration
{
  private Shape _shape;

  private final Geodetic3D _fromPosition ;
  private final Geodetic3D _toPosition ;

  private final Angle _fromPitch ;
  private final Angle _toPitch ;

  private final Angle _fromHeading ;
  private final Angle _toHeading ;

  private final Angle _fromRoll ;
  private final Angle _toRoll ;

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
    final double alpha = getAlpha(when);
  
    final Geodetic3D pos = Geodetic3D.linearInterpolation(_fromPosition, _toPosition, alpha);
    _shape.setPosition(pos);
  
    if (!_fromPitch.isNan() && !_toPitch.isNan())
    {
      _shape.setPitch(Angle.linearInterpolation(_fromPitch, _toPitch, alpha));
    }
  
    if (!_fromHeading.isNan() && !_toHeading.isNan())
    {
      _shape.setHeading(Angle.linearInterpolation(_fromHeading, _toHeading, alpha));
    }
  
    if (!_fromRoll.isNan() && !_toRoll.isNan())
    {
      _shape.setRoll(Angle.linearInterpolation(_fromRoll, _toRoll, alpha));
    }
  }

  public final void cancel(TimeInterval when)
  {
    if (_forceToPositionOnCancel)
    {
      _shape.setPosition(_toPosition);
      if (!_toPitch.isNan())
      {
        _shape.setPitch(_toPitch);
      }
  
      if (!_toHeading.isNan())
      {
        _shape.setHeading(_toHeading);
      }
  
      if (!_toRoll.isNan())
      {
        _shape.setRoll(_toRoll);
      }
    }
  }
  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    if (_forceToPositionOnStop)
    {
      _shape.setPosition(_toPosition);
      if (!_toPitch.isNan())
      {
        _shape.setPitch(_toPitch);
      }
  
      if (!_toHeading.isNan())
      {
        _shape.setHeading(_toHeading);
      }
  
      if (!_toRoll.isNan())
      {
        _shape.setRoll(_toRoll);
      }
    }
  }

}