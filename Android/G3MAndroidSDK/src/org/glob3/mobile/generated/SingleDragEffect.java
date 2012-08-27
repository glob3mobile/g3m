package org.glob3.mobile.generated; 
//
//  CameraSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  CameraSingleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//




//***************************************************************


public class SingleDragEffect extends EffectWithForce
{

  public SingleDragEffect(Vector3D axis, Angle angle)
  {
	  super(angle.degrees(), 0.975);
	  _axis = new Vector3D(axis);
  }

  public void start(RenderContext rc, TimeInterval now)
  {
  }

  public void doStep(RenderContext rc, TimeInterval now)
  {
	super.doStep(rc, now);
	rc.getNextCamera().rotateWithAxis(_axis, Angle.fromDegrees(getForce()));
  }

  public void stop(RenderContext rc, TimeInterval now)
  {
  }

  public void cancel(TimeInterval now)
  {
	// do nothing, just leave the effect in the intermediate state
  }

  private Vector3D _axis ;

}