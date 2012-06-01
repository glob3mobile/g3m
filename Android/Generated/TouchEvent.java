package org.glob3.mobile.generated; 
public class TouchEvent extends java.util.ArrayList<Pointer>
{
	public TouchEvent(TouchEventType type)
	{
		_eventType = type;
	}

	public final TouchEventType getType()
	{
		return _eventType;
	}

	private TouchEventType _eventType;
}