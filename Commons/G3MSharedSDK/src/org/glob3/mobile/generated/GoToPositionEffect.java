package org.glob3.mobile.generated; 
//
//  GoToPositionEffect.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class GoToPositionEffect extends EffectWithDuration
{

  public GoToPositionEffect(TimeInterval duration, Geodetic3D initialPos, Geodetic3D finalPos)
  {
	  super(duration);
	  _initialPos = new Geodetic3D(initialPos);
	  _finalPos = new Geodetic3D(finalPos);
  }

//  virtual void start(const G3MRenderContext *rc,
//                     const TimeInterval& when) {
//    EffectWithDuration::start(rc, when);
//  }

  public void doStep(G3MRenderContext rc, TimeInterval when)
  {
	//const double percent = pace( percentDone(when) );
	final double percent = percentDone(when);
	Camera camera = rc.getNextCamera();

	final Geodetic3D g = Geodetic3D.interpolation(_initialPos, _finalPos, percent);

	//camera->setPosition(g);
	camera.orbitTo(g);
  }

  public void stop(G3MRenderContext rc, TimeInterval when)
  {
	rc.getNextCamera().orbitTo(_finalPos);
	super.stop(rc, when);
  }

  public void cancel(TimeInterval when)
  {
	// do nothing, just leave the effect in the intermediate state
  }

  private final Geodetic3D _initialPos ;
  private final Geodetic3D _finalPos ;
}