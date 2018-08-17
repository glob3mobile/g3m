package org.glob3.mobile.generated;//
//  CameraGoToPositionEffect.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/10/12.
//




public class CameraGoToPositionEffect extends EffectWithDuration
{
  private final Geodetic3D _fromPosition = new Geodetic3D();
  private final Geodetic3D _toPosition = new Geodetic3D();

  private final Angle _fromHeading = new Angle();
  private final Angle _toHeading = new Angle();

  private final Angle _fromPitch = new Angle();
  private final Angle _toPitch = new Angle();

  private final boolean _linearHeight;
  private double _middleHeight;


  private double calculateMaxHeight(Planet planet)
  {
	// curve parameters
	final double distanceInDegreesMaxHeight = 180;
	final double maxHeight = planet.getRadii().axisAverage() * 5;


	// rough estimation of distance using lat/lon degrees
	final double deltaLatInDeg = _fromPosition._latitude._degrees - _toPosition._latitude._degrees;
	final double deltaLonInDeg = _fromPosition._longitude._degrees - _toPosition._longitude._degrees;
	final double distanceInDeg = IMathUtils.instance().sqrt((deltaLatInDeg * deltaLatInDeg) + (deltaLonInDeg * deltaLonInDeg));

	if (distanceInDeg >= distanceInDegreesMaxHeight)
	{
	  return maxHeight;
	}

	final double middleHeight = (distanceInDeg / distanceInDegreesMaxHeight) * maxHeight;

	final double averageHeight = (_fromPosition._height + _toPosition._height) / 2;
	if (middleHeight < averageHeight)
	{
	  final double delta = (averageHeight - middleHeight) / 2.0;
	  return averageHeight + delta;
	}
	//    const double averageHeight = (_fromPosition._height + _toPosition._height) / 2;
	//    if (middleHeight < averageHeight) {
	//      return (averageHeight + middleHeight) / 2.0;
	//    }

	return middleHeight;
  }



  public CameraGoToPositionEffect(TimeInterval duration, Geodetic3D fromPosition, Geodetic3D toPosition, Angle fromHeading, Angle toHeading, Angle fromPitch, Angle toPitch, boolean linearTiming, boolean linearHeight)
  {
	  super(duration, linearTiming);
	  _fromPosition = new Geodetic3D(fromPosition);
	  _toPosition = new Geodetic3D(toPosition);
	  _fromHeading = new Angle(fromHeading);
	  _toHeading = new Angle(toHeading);
	  _fromPitch = new Angle(fromPitch);
	  _toPitch = new Angle(toPitch);
	  _linearHeight = linearHeight;
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double alpha = getAlpha(when);
	final double alpha = getAlpha(new TimeInterval(when));

	double height;
	if (_linearHeight)
	{
	  height = IMathUtils.instance().linearInterpolation(_fromPosition._height, _toPosition._height, alpha);
	}
	else
	{
	  height = IMathUtils.instance().quadraticBezierInterpolation(_fromPosition._height, _middleHeight, _toPosition._height, alpha);
	}

	Camera camera = rc.getNextCamera();
	camera.setGeodeticPosition(Angle.linearInterpolation(_fromPosition._latitude, _toPosition._latitude, alpha), Angle.linearInterpolation(_fromPosition._longitude, _toPosition._longitude, alpha), height);


//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Angle heading = Angle::linearInterpolation(_fromHeading, _toHeading, alpha);
	final Angle heading = Angle.linearInterpolation(new Angle(_fromHeading), new Angle(_toHeading), alpha);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: camera->setHeading(heading);
	camera.setHeading(new Angle(heading));

	final Angle middlePitch = Angle.fromDegrees(-90);
	//    const Angle pitch =  (alpha < 0.5)
	//    ? Angle::linearInterpolation(_fromPitch, middlePitch, alpha*2)
	//    : Angle::linearInterpolation(middlePitch, _toPitch, (alpha-0.5)*2);

	if (alpha <= 0.1)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: camera->setPitch(Angle::linearInterpolation(_fromPitch, middlePitch, alpha *10));
	  camera.setPitch(Angle.linearInterpolation(new Angle(_fromPitch), new Angle(middlePitch), alpha *10));
	}
	else if (alpha >= 0.9)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: camera->setPitch(Angle::linearInterpolation(middlePitch, _toPitch, (alpha-0.9)*10));
	  camera.setPitch(Angle.linearInterpolation(new Angle(middlePitch), new Angle(_toPitch), (alpha-0.9)*10));
	}
	else
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: camera->setPitch(middlePitch);
	  camera.setPitch(new Angle(middlePitch));
	}

  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
	Camera camera = rc.getNextCamera();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: camera->setGeodeticPosition(_toPosition);
	camera.setGeodeticPosition(new Geodetic3D(_toPosition));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: camera->setPitch(_toPitch);
	camera.setPitch(new Angle(_toPitch));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: camera->setHeading(_toHeading);
	camera.setHeading(new Angle(_toHeading));
  }

  public final void cancel(TimeInterval when)
  {
	// do nothing, just leave the effect in the intermediate state
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: EffectWithDuration::start(rc, when);
	super.start(rc, new TimeInterval(when));

	_middleHeight = calculateMaxHeight(rc.getPlanet());
  }
}
