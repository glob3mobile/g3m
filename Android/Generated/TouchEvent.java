package org.glob3.mobile.generated; 
public class TouchEvent
{
  private final TouchEventType _eventType;
  private final java.util.ArrayList<Pointer> _pointers = new java.util.ArrayList<Pointer>();

  private TouchEvent(TouchEventType type, java.util.ArrayList<Pointer> pointers)
  {
	  _eventType = type;
	  _pointers = pointers;
  }

  public TouchEvent(TouchEvent other)
  {
	  _eventType = other._eventType;
	  _pointers = other._pointers;

  }

  public static TouchEvent create(TouchEventType type, java.util.ArrayList<Pointer> pointers)
  {
	return new TouchEvent(type, pointers);
  }

  public static TouchEvent create(TouchEventType type, Pointer pointer)
  {
	Pointer[] pa = { pointer };
	final java.util.ArrayList<Pointer> pointers = new java.util.ArrayList<Pointer>(pa, pa+1);
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