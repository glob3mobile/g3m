package org.glob3.mobile.generated; 
//
//  CameraDoubleTapHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  CameraDoubleTapHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//




//***************************************************************

public class DoubleTapEffect extends EffectWithDuration
{

  public DoubleTapEffect(TimeInterval duration, Vector3D axis, Angle angle, double distance)
  {
	  super(duration);
	  _axis = new Vector3D(axis);
	  _angle = new Angle(angle);
	  _distance = distance;
  }

  public void start(RenderContext rc, TimeInterval now)
  {
	super.start(rc, now);
	_lastPercent = 0;
  }

  public void doStep(RenderContext rc, TimeInterval now)
  {
	//const double percent = gently(percentDone(now), 0.2, 0.9);
	//const double percent = pace( percentDone(now) );
	final double percent = percentDone(now);
	Camera camera = rc.getNextCamera();
	final double step = percent - _lastPercent;
	camera.rotateWithAxis(_axis, _angle.times(step));
	camera.moveForward(_distance *step);
	_lastPercent = percent;
  }

  public void stop(RenderContext rc, TimeInterval now)
  {
	super.stop(rc, now);
  }

  public void cancel(TimeInterval now)
  {
	// do nothing, just leave the effect in the intermediate state
  }

  private Vector3D _axis ;
  private Angle _angle ;
  private double _distance;
  private double _lastPercent;
}