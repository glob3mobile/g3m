package org.glob3.mobile.generated; 
//
//  CameraEffects.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 17/07/13.
//
//

//
//  CameraEffects.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 17/07/13.
//
//




//class Angle;



public class RotateWithAxisEffect extends EffectWithForce
{
  private final Vector3D _axis ;
  private double _degrees;

  public RotateWithAxisEffect(Vector3D axis, Angle angle)
  {
     super(1, 0.975);
     _axis = new Vector3D(axis);
     _degrees = angle._degrees;
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
    rc.getNextCamera().rotateWithAxis(_axis, Angle.fromDegrees(_degrees *getForce()));
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    rc.getNextCamera().rotateWithAxis(_axis, Angle.fromDegrees(_degrees *getForce()));
  }

  public final void cancel(TimeInterval when)
  {
  }
}