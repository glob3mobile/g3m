package org.glob3.mobile.generated; 
public class TouchEvent
{
  private final TouchEventType _eventType;
  private final java.util.ArrayList<Touch> _touchs; // *** REMOVED CONSTRUCTOR BY MY RULES<Touch>();
  private final boolean _shiftPressed;
  private final boolean _ctrlPressed;
  private final double _wheelDelta;


  private TouchEvent(TouchEventType type, java.util.ArrayList<Touch> touchs, boolean shift, boolean ctrl, double wheelDelta)
  {
	  _eventType = type;
	  _touchs = touchs;
	  _shiftPressed = shift;
	  _ctrlPressed = ctrl;
	  _wheelDelta = wheelDelta;
  }

  public TouchEvent(TouchEvent other)
  {
	  _eventType = other._eventType;
	  _touchs = other._touchs;
	  _shiftPressed = other._shiftPressed;
	  _ctrlPressed = other._ctrlPressed;
	  _wheelDelta = other._wheelDelta;
  }

  public static TouchEvent create(TouchEventType type, java.util.ArrayList<Touch> touchs)
  {
	return new TouchEvent(type, touchs, false, false, 0.0);
  }

  public static TouchEvent create(TouchEventType type, java.util.ArrayList<Touch> touchs, boolean shift, boolean ctrl, double wheelDelta)
  {
	return new TouchEvent(type, touchs, shift, ctrl, wheelDelta);
  }

  public static TouchEvent create(TouchEventType type, Touch touch)
  {
	//const Touch * pa[] = { touch };
	//const std::vector<const Touch*> touchs = std::vector<const Touch*>(pa, &pa[1);
	final java.util.ArrayList<Touch> touchs = new java.util.ArrayList<Touch>(java.util.Arrays.asList(touch)); //CHANGED BY CONVERSOR RULE

	return create(type, touchs);
  }

  public static TouchEvent create(TouchEventType type, Touch touch, boolean shift, boolean ctrl, double wheelDelta)
  {
	final java.util.ArrayList<Touch> touchs = new java.util.ArrayList<Touch>(java.util.Arrays.asList(touch)); //CHANGED BY CONVERSOR RULE

	return create(type, touchs, shift, ctrl, wheelDelta);
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
//ORIGINAL LINE: int getTouchCount() const
  public final int getTouchCount()
  {
	return _touchs.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: byte getTapCount() const
  public final byte getTapCount()
  {
	if (_touchs.isEmpty())
		return 0;
	return _touchs.get(0).getTapCount();
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