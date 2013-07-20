package org.glob3.mobile.generated; 
//
//  CameraDoubleTapHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  CameraDoubleTapHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//




//***************************************************************

public class DoubleTapEffect extends EffectWithDuration
{

  public DoubleTapEffect(TimeInterval duration, Vector3D axis, Angle angle, double distance)
  {
     this(duration, axis, angle, distance, false);
  }
  public DoubleTapEffect(TimeInterval duration, Vector3D axis, Angle angle, double distance, boolean linearTiming)
  {
     super(duration, linearTiming);
     _axis = new Vector3D(axis);
     _angle = new Angle(angle);
     _distance = distance;
  }

  public void start(G3MRenderContext rc, TimeInterval when)
  {
    super.start(rc, when);
    _lastAlpha = 0;
  }

  public void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = getAlpha(when);
    Camera camera = rc.getNextCamera();
    final double step = alpha - _lastAlpha;
    camera.rotateWithAxis(_axis, _angle.times(step));
    camera.moveForward(_distance * step);
    _lastAlpha = alpha;
  }

  public void stop(G3MRenderContext rc, TimeInterval when)
  {
    Camera camera = rc.getNextCamera();

    final double step = 1.0 - _lastAlpha;
    camera.rotateWithAxis(_axis, _angle.times(step));
    camera.moveForward(_distance * step);
  }

  public void cancel(TimeInterval when)
  {
    // do nothing, just leave the effect in the intermediate state
  }

  private Vector3D _axis ;
  private Angle _angle ;
  private double _distance;
  private double _lastAlpha;
}