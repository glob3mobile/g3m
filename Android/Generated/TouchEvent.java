package org.glob3.mobile.generated; 
public class TouchEvent
{
  private final TouchEventType _eventType;
  private final java.util.ArrayList<Touch> _touchs; // *** REMOVED CONSTRUCTOR BY MY RULES<Touch>();

  private TouchEvent(TouchEventType type, java.util.ArrayList<Touch> touchs)
  {
	  _eventType = type;
	  _touchs = touchs;
  }

  public TouchEvent(TouchEvent other)
  {
	  _eventType = other._eventType;
	  _touchs = other._touchs;

  }

  public static TouchEvent create(TouchEventType type, java.util.ArrayList<Touch> Touchs)
  {
	return new TouchEvent(type, Touchs);
  }

  public static TouchEvent create(TouchEventType type, Touch touch)
  {
	Touch[] pa = { touch };
	final java.util.ArrayList<Touch> touchs = new java.util.ArrayList<Touch>(pa, pa+1);
	return create(type, touchs);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TouchEventType getType() const
  public final TouchEventType getType()
  {
	return _eventType;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Touch* getTouch(int i) const
  public final Touch getTouch(int i)
  {
	  return _touchs.get(i);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getNumTouch() const
  public final int getNumTouch()
  {
	  return _touchs.size();
  }

  public void dispose()
  {
	for (int i = 0; i < _touchs.size(); i++)
	{
	  if (_touchs.get(i) != null)
		  _touchs.get(i).dispose();
	}
  }

}