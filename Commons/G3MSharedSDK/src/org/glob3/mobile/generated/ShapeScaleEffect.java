package org.glob3.mobile.generated; 
//
//  ShapeScaleEffect.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/23/12.
//
//

//
//  ShapeScaleEffect.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/23/12.
//
//



//class Shape;

public class ShapeScaleEffect extends EffectWithDuration
{
  private Shape _shape;

  private double _fromScaleX;
  private double _fromScaleY;
  private double _fromScaleZ;

  private double _toScaleX;
  private double _toScaleY;
  private double _toScaleZ;

  public ShapeScaleEffect(TimeInterval duration, Shape shape, double fromScaleX, double fromScaleY, double fromScaleZ, double toScaleX, double toScaleY, double toScaleZ)
  {
     this(duration, shape, fromScaleX, fromScaleY, fromScaleZ, toScaleX, toScaleY, toScaleZ, false);
  }
  public ShapeScaleEffect(TimeInterval duration, Shape shape, double fromScaleX, double fromScaleY, double fromScaleZ, double toScaleX, double toScaleY, double toScaleZ, boolean linearTiming)
  {
     super(duration, linearTiming);
     _shape = shape;
     _fromScaleX = fromScaleX;
     _fromScaleY = fromScaleY;
     _fromScaleZ = fromScaleZ;
     _toScaleX = toScaleX;
     _toScaleY = toScaleY;
     _toScaleZ = toScaleZ;

  }


  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = getAlpha(when);
  
    final IMathUtils mu = IMathUtils.instance();
    final double scaleX = mu.linearInterpolation(_fromScaleX, _toScaleX, alpha);
    final double scaleY = mu.linearInterpolation(_fromScaleY, _toScaleY, alpha);
    final double scaleZ = mu.linearInterpolation(_fromScaleZ, _toScaleZ, alpha);
  
    _shape.setScale(scaleX, scaleY, scaleZ);
  }

  public final void cancel(TimeInterval when)
  {
    _shape.setScale(_toScaleX, _toScaleY, _toScaleZ);
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    _shape.setScale(_toScaleX, _toScaleY, _toScaleZ);
  }


}