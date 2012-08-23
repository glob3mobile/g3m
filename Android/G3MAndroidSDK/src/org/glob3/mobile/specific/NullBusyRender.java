package org.glob3.mobile.specific;

import org.glob3.mobile.generated.EventContext;
import org.glob3.mobile.generated.InitializationContext;
import org.glob3.mobile.generated.RenderContext;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.TouchEvent;

public class NullBusyRender extends Renderer  {

	@Override
	public void initialize(InitializationContext ic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isReadyToRender(RenderContext rc) {
		return true;
	}

	@Override
	public int render(RenderContext rc) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onTouchEvent(EventContext ec, TouchEvent touchEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onResizeViewportEvent(EventContext ec, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
