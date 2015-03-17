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

public class ViewChangeFrom3DTo2D extends EffectWithDuration
{
	private MutableVector3D _pivotPoint ;
	private double _initialPitch;
	private Camera _camera0;

	public ViewChangeFrom3DTo2D()
	{
		super(TimeInterval.fromSeconds(2), true);
		_pivotPoint = null;
		_initialPitch = 0;
		_camera0 = null;
	}

	public final void start(G3MRenderContext rc, TimeInterval when)
	{
		super.start(rc, when);
		_pivotPoint = rc.getWidget().getFirstValidScenePositionForCentralColumn().asMutableVector3D();
		_camera0 = new Camera(rc.getNextCamera());
		_initialPitch = rc.getNextCamera().getPitch()._degrees;
		
		/*Geodetic3D geo = rc.getPlanet().toGeodetic3D(_pivotPoint.asVector3D());
		ILogger.instance().logInfo("pivot point =%f %f %f", 
				geo._latitude._degrees, geo._longitude._degrees, geo._height);
		ILogger.instance().logInfo("initial pitch=%f",  _initialPitch);*/
	}

	public final void doStep(G3MRenderContext rc, TimeInterval when)
	{
		final double alpha = getAlpha(when);
		Camera camera = rc.getNextCamera();
		camera.copyFrom(_camera0);
		final IMathUtils mu = IMathUtils.instance();

	    // compute normal to Initial point
	    Vector3D normal = rc.getPlanet().geodeticSurfaceNormal(_pivotPoint);
	  
	    // compute angle between normal and view direction
	    Vector3D view = camera.getViewDirection();
	    double dot = normal.normalized().dot(view.normalized().times(-1));
	    double initialAngle = mu.acos(dot) / 3.14159 * 180;
	  	  
	    // horizontal rotation over the original camera horizontal axix
	    Vector3D u = camera.getHorizontalVector();
	    double delta = alpha*10;
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
