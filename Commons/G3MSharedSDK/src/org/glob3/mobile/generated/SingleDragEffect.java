package org.glob3.mobile.generated; 
//
//  CameraSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  CameraSingleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//





public class SingleDragEffect extends EffectWithForce
{

  public SingleDragEffect(Vector3D axis, Angle angle)
  {
     super(angle._degrees, 0.975);
     _axis = new Vector3D(axis);
  }

  public void start(G3MRenderContext rc, TimeInterval when)
  {
  }

  public void doStep(G3MRenderContext rc, TimeInterval when)
  {
    super.doStep(rc, when);
    rc.getNextCamera().rotateWithAxis(_axis, Angle.fromDegrees(getForce()));
  }

  public void stop(G3MRenderContext rc, TimeInterval when)
  {
    rc.getNextCamera().rotateWithAxis(_axis, Angle.fromDegrees(getForce()));
  }

  public void cancel(TimeInterval when)
  {
    // do nothing, just leave the effect in the intermediate state
  }

  private Vector3D _axis ;
}