package org.glob3.mobile.generated; 
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



//class Shape;

public class ShapePositionEffect extends EffectWithDuration
{
  private Shape _shape;

  private final Geodetic3D _fromPosition ;
  private final Geodetic3D _toPosition ;

  private final boolean _linearInterpolation;

  public ShapePositionEffect(TimeInterval duration, Shape shape, Geodetic3D fromPosition, Geodetic3D toPosition, boolean linearInterpolation)
  {
     super(duration);
     _shape = shape;
     _fromPosition = new Geodetic3D(fromPosition);
     _toPosition = new Geodetic3D(toPosition);
     _linearInterpolation = linearInterpolation;

  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double percent = percentDone(when);
    final double alpha = _linearInterpolation ? percent : pace(percent);
  
    Geodetic3D pos = Geodetic3D.interpolation(_fromPosition, _toPosition, alpha);
    _shape.setPosition(new Geodetic3D(pos));
  }

  public final void cancel(TimeInterval when)
  {
    _shape.setPosition(new Geodetic3D(_toPosition));
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    _shape.setPosition(new Geodetic3D(_toPosition));
  }

}