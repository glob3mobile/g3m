package org.glob3.mobile.generated;//
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




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Angle;



public class RotateWithAxisEffect extends EffectWithForce
{
  private final Vector3D _axis = new Vector3D();
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: EffectWithForce::doStep(rc, when);
	super.doStep(rc, new TimeInterval(when));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: rc->getNextCamera()->rotateWithAxis(_axis, Angle::fromDegrees(_degrees *getForce()));
	rc.getNextCamera().rotateWithAxis(new Vector3D(_axis), Angle.fromDegrees(_degrees *getForce()));
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: rc->getNextCamera()->rotateWithAxis(_axis, Angle::fromDegrees(_degrees *getForce()));
	rc.getNextCamera().rotateWithAxis(new Vector3D(_axis), Angle.fromDegrees(_degrees *getForce()));
  }

  public final void cancel(TimeInterval when)
  {
  }
}
