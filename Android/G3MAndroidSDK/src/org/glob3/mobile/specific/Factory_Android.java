package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.ITimer;

public class Factory_Android extends IFactory {

	@Override
	public ITimer createTimer() {
		return new Timer_Android();
	}

	@Override
	public void deleteTimer(ITimer timer) {}

}
