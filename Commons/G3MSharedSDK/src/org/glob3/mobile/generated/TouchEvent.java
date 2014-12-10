package org.glob3.mobile.generated; 
public class TouchEvent
{
  private final TouchEventType _eventType;
  private final java.util.ArrayList<Touch> _touchs;
  private final boolean _shiftPressed;
  private final boolean _ctrlPressed;
  private final boolean _altPressed;
  private final double _wheelDelta;


  private TouchEvent(TouchEventType type, java.util.ArrayList<Touch> touchs, boolean shift, boolean ctrl, boolean alt, double wheelDelta)
  {
     _eventType = type;
     _touchs = touchs;
     _shiftPressed = shift;
     _ctrlPressed = ctrl;
     _altPressed = alt;
     _wheelDelta = wheelDelta;
  }

  public TouchEvent(TouchEvent other)
  {
     _eventType = other._eventType;
     _touchs = other._touchs;
     _shiftPressed = other._shiftPressed;
     _ctrlPressed = other._ctrlPressed;
     _altPressed = other._altPressed;
     _wheelDelta = other._wheelDelta;
  }

  public static TouchEvent create(TouchEventType type, java.util.ArrayList<Touch> touchs)
  {
    return new TouchEvent(type, touchs, false, false, false, 0.0);
  }

  public static TouchEvent create(TouchEventType type, java.util.ArrayList<Touch> touchs, boolean shift, boolean ctrl, boolean alt, double wheelDelta)
  {
    return new TouchEvent(type, touchs, shift, ctrl, alt, wheelDelta);
  }

  public static TouchEvent create(TouchEventType type, Touch touch)
  {
    final java.util.ArrayList<Touch> touchs = new java.util.ArrayList<Touch>(java.util.Arrays.asList(touch)); //CHANGED BY CONVERSOR RULE

    return create(type, touchs);
  }

  public static TouchEvent create(TouchEventType type, Touch touch, boolean shift, boolean ctrl, boolean alt, double wheelDelta)
  {
    final java.util.ArrayList<Touch> touchs = new java.util.ArrayList<Touch>(java.util.Arrays.asList(touch)); //CHANGED BY CONVERSOR RULE

    return create(type, touchs, shift, ctrl, alt, wheelDelta);
  }

  public final TouchEventType getType()
  {
    return _eventType;
  }

  public final Touch getTouch(int i)
  {
    return _touchs.get(i);
  }

  public final int getTouchCount()
  {
    return _touchs.size();
  }

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

  public final double getMouseWheelDelta()
  {
    return _wheelDelta;
  }

  public final boolean isCtrlPressed()
  {
    return _ctrlPressed;
  }

  public final boolean isAltPressed()
  {
    return _altPressed;
  }

}