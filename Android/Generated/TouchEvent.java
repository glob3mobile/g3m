package org.glob3.mobile.generated; 
public class TouchEvent
{
  private final TouchEventType _eventType;
  private final java.util.ArrayList<const Pointer> _pointers = new java.util.ArrayList<const Pointer>();

  private TouchEvent(TouchEventType type, java.util.ArrayList<const Pointer> pointers)
  {
	  _eventType = type;
	  _pointers = pointers;
  }

  public TouchEvent(TouchEvent other)
  {
	  _eventType = other._eventType;
	  _pointers = other._pointers;

  }

  public static TouchEvent create(TouchEventType type, java.util.ArrayList<const Pointer> pointers)
  {
	return new TouchEvent(type, pointers);
  }

  public static TouchEvent create(TouchEventType type, Pointer pointer)
  {
	Pointer[] pa = { pointer };
	final java.util.ArrayList<const Pointer> pointers = new java.util.ArrayList<const Pointer>(pa, pa+1);
	return TouchEvent.create(type, pointers);
  }

  public final TouchEventType getType()
  {
	return _eventType;
  }

  public void dispose()
  {
	for (int i = 0; i < _pointers.size(); i++)
	{
	  if (_pointers.get(i) != null)
		  _pointers.get(i).dispose();
	}
  }

}