package org.glob3.mobile.client;

import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.EffectWithDuration;
import org.glob3.mobile.generated.G3MRenderContext;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.Vector3D;

public class ViewChangeFrom3DTo2D extends EffectWithDuration
{
	private final Vector3D _translation ;
	private final double _distance;
	private double _lastAlpha;


	public ViewChangeFrom3DTo2D()
	{
		super(TimeInterval.fromSeconds(2), true);
	     _translation = new Vector3D(10000,0,0);
	     _distance = 1000;
	}

	public final void start(G3MRenderContext rc, TimeInterval when)
	{
		super.start(rc, when);
		_lastAlpha = 0;
	}

	public final void doStep(G3MRenderContext rc, TimeInterval when)
	{
		final double alpha = getAlpha(when);
		Camera camera = rc.getNextCamera();
		final double step = alpha - _lastAlpha;
		ILogger.instance().logInfo("alpha=%f", alpha);
		_lastAlpha = alpha;
	}

	public final void stop(G3MRenderContext rc, TimeInterval when)
	{
		Camera camera = rc.getNextCamera();
		final double step = 1.0 - _lastAlpha;
		camera.translateCamera(_translation.times(step));
		camera.moveForward(_distance * step);
		ILogger.instance().logInfo("acabo effecto");
		//printf("acabo double tap en altura %.2f\n", camera->getGeodeticPosition()._height);
	}

	public final void cancel(TimeInterval when)
	{
	}

}
