package org.glob3.mobile.generated; 
//
//  CameraEffects.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 17/07/13.
//
//

//
//  CameraEffects.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 17/07/13.
//
//




//class Angle;



public class RotateWithAxisEffect extends EffectWithForce
{
  private final Vector3D _axis ; //VERSION 1.0

  public RotateWithAxisEffect(Vector3D axis, Angle angle)
  {
     super(angle._degrees, 0.975);
     _axis = new Vector3D(axis);
  }

  public void dispose()
  {
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    super.doStep(rc, when);
    rc.getNextCamera().rotateWithAxis(_axis, Angle.fromDegrees(getForce()));
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    rc.getNextCamera().rotateWithAxis(_axis, Angle.fromDegrees(getForce()));
  }

  public final void cancel(TimeInterval when)
  {
  }
}