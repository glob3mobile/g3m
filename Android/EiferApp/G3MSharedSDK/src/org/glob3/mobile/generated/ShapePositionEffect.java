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

  public ShapePositionEffect(TimeInterval duration, Shape shape, Geodetic3D fromPosition, Geodetic3D toPosition)
  {
     this(duration, shape, fromPosition, toPosition, false);
  }
  public ShapePositionEffect(TimeInterval duration, Shape shape, Geodetic3D fromPosition, Geodetic3D toPosition, boolean linearTiming)
  {
     super(duration, linearTiming);
     _shape = shape;
     _fromPosition = new Geodetic3D(fromPosition);
     _toPosition = new Geodetic3D(toPosition);

  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = getAlpha(when);
  
    final Geodetic3D pos = Geodetic3D.linearInterpolation(_fromPosition, _toPosition, alpha);
    _shape.setPosition(pos);
  }

  public final void cancel(TimeInterval when)
  {
    _shape.setPosition(_toPosition);
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    _shape.setPosition(_toPosition);
  }

}