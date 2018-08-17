package org.glob3.mobile.generated;//
//  CameraOrbitFixedPositionEffect.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 27/07/2018.
//

//
//  CameraOrbitFixedPositionEffect.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 27/07/2018.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Shape;

public class CameraOrbitFixedPositionEffect extends EffectWithDuration
{
	private Geodetic3D _pivot = new Geodetic3D();

	private final double _fromDistance;
	private final double _toDistance;

	private final double _fromAzimuthInRadians;
	private final double _toAzimuthInRadians;

	private final double _fromAltitudeInRadians;
	private final double _toAltitudeInRadians;


	public CameraOrbitFixedPositionEffect(TimeInterval duration, Geodetic3D pivot, double fromDistance, double toDistance, Angle fromAzimuth, Angle toAzimuth, Angle fromAltitude, Angle toAltitude)
	{
		this(duration, pivot, fromDistance, toDistance, fromAzimuth, toAzimuth, fromAltitude, toAltitude, false);
	}
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: CameraOrbitFixedPositionEffect(const TimeInterval& duration, const Geodetic3D& pivot, double fromDistance, double toDistance, const Angle& fromAzimuth, const Angle& toAzimuth, const Angle& fromAltitude, const Angle& toAltitude, const boolean linearTiming=false) : EffectWithDuration(duration, linearTiming), _pivot(pivot), _fromDistance(fromDistance), _toDistance(toDistance), _fromAzimuthInRadians(fromAzimuth._radians), _toAzimuthInRadians(toAzimuth._radians), _fromAltitudeInRadians(fromAltitude._radians), _toAltitudeInRadians(toAltitude._radians)
	public CameraOrbitFixedPositionEffect(TimeInterval duration, Geodetic3D pivot, double fromDistance, double toDistance, Angle fromAzimuth, Angle toAzimuth, Angle fromAltitude, Angle toAltitude, boolean linearTiming)
	{
		super(duration, linearTiming);
		_pivot = new Geodetic3D(pivot);
		_fromDistance = fromDistance;
		_toDistance = toDistance;
		_fromAzimuthInRadians = fromAzimuth._radians;
		_toAzimuthInRadians = toAzimuth._radians;
		_fromAltitudeInRadians = fromAltitude._radians;
		_toAltitudeInRadians = toAltitude._radians;

	}

	public final void doStep(G3MRenderContext rc, TimeInterval when)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double alpha = getAlpha(when);
		final double alpha = getAlpha(new TimeInterval(when));
    
		final IMathUtils mu = IMathUtils.instance();
		final double distance = mu.linearInterpolation(_fromDistance, _toDistance, alpha);
		final double azimuthInRadians = mu.linearInterpolation(_fromAzimuthInRadians, _toAzimuthInRadians, alpha);
		final double altitudeInRadians = mu.linearInterpolation(_fromAltitudeInRadians, _toAltitudeInRadians, alpha);
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: rc->getNextCamera()->setPointOfView(_pivot, distance, Angle::fromRadians(azimuthInRadians), Angle::fromRadians(altitudeInRadians));
		rc.getNextCamera().setPointOfView(new Geodetic3D(_pivot), distance, Angle.fromRadians(azimuthInRadians), Angle.fromRadians(altitudeInRadians));
	}

	public final void cancel(TimeInterval when)
	{
		// do nothing
	}

	public final void stop(G3MRenderContext rc, TimeInterval when)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: rc->getNextCamera()->setPointOfView(_pivot, _toDistance, Angle::fromRadians(_toAzimuthInRadians), Angle::fromRadians(_toAltitudeInRadians));
		rc.getNextCamera().setPointOfView(new Geodetic3D(_pivot), _toDistance, Angle.fromRadians(_toAzimuthInRadians), Angle.fromRadians(_toAltitudeInRadians));
    
	}

}
