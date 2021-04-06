package org.glob3.mobile.generated;
public class TouchEvent
{
  private final TouchEventType _eventType;
  private final java.util.ArrayList<Touch> _touchs;


  private TouchEvent(TouchEventType type, java.util.ArrayList<Touch> touchs)
  {
     _eventType = type;
     _touchs = touchs;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TouchEvent(TouchEvent other);


  public static TouchEvent create(TouchEventType type, java.util.ArrayList<Touch> touchs)
  {
    return new TouchEvent(type, touchs);
  }

  public static TouchEvent create(TouchEventType type, Touch touch)
  {
    final java.util.ArrayList<Touch> touchs = new java.util.ArrayList<Touch>(java.util.Arrays.asList(touch)); //CHANGED BY CONVERSOR RULE

    return create(type, touchs);
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

}