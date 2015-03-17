package org.glob3.mobile.client;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.EffectWithDuration;
import org.glob3.mobile.generated.G3MRenderContext;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Gesture;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.MutableVector3D;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.Vector3D;

public class VerticalOrbitEffect extends EffectWithDuration
{
	private MutableVector3D _pivotPoint ;
	private Camera _camera0;
	private double _finalPitch;

	public VerticalOrbitEffect(Angle pitch, TimeInterval duration)
	{
		super(duration, true);
		_pivotPoint = null;
		_camera0 = null;
		_finalPitch = pitch._degrees;
	}

	public final void start(G3MRenderContext rc, TimeInterval when)
	{
		super.start(rc, when);
		_pivotPoint = rc.getWidget().getFirstValidScenePositionForCentralColumn().asMutableVector3D();
		_camera0 = new Camera(rc.getNextCamera());
		
		/*Geodetic3D geo = rc.getPlanet().toGeodetic3D(_pivotPoint.asVector3D());
		ILogger.instance().logInfo("pivot point =%f %f %f", 
				geo._latitude._degrees, geo._longitude._degrees, geo._height);
		ILogger.instance().logInfo("initial pitch=%f",  _initialPitch);*/
	}

	public final void doStep(G3MRenderContext rc, TimeInterval when)
	{
		final double alpha = getAlpha(when);
		final IMathUtils mu = IMathUtils.instance();

		// reset camera to camera0
		Camera camera = rc.getNextCamera();
		camera.copyFrom(_camera0);
		double initialPitch = rc.getNextCamera().getPitch()._degrees;

	    // compute angle between normal and view direction
	    Vector3D view = camera.getViewDirection();
	    Vector3D normal = rc.getPlanet().geodeticSurfaceNormal(_pivotPoint);
	    double dot = normal.normalized().dot(view.normalized().times(-1));
	    double initialAngle = mu.acos(dot) / 3.14159 * 180;
	  	  
	    // horizontal rotation over the original camera horizontal axix
	    Vector3D u = camera.getHorizontalVector();
	    double delta = alpha * (_finalPitch-initialPitch);
	    camera.rotateWithAxisAndPoint(u, _pivotPoint.asVector3D(), Angle.fromDegrees(delta));
	  
	 
	    
	}

	public final void stop(G3MRenderContext rc, TimeInterval when)
	{
		Camera camera = rc.getNextCamera();
	}

	public final void cancel(TimeInterval when)
	{
	}

}
