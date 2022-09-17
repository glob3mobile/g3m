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
    //  if (_touchs.empty()) return 0;
    //  return _touchs[0]->getTapCount();
    return _touchs.isEmpty() ? 0 : _touchs.get(0).getTapCount();
  }

  public void dispose()
  {
    for (int i = 0; i < _touchs.size(); i++)
    {
      if (_touchs.get(i) != null)
         _touchs.get(i).dispose();
    }
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(TouchEvent type=");
  
    {
      String eventTypeName = "";
      switch (_eventType)
      {
        case Down:
          eventTypeName = "Down";
          break;
  
        case Up:
          eventTypeName = "Up";
          break;
  
        case Move:
          eventTypeName = "Move";
          break;
  
        case LongPress:
          eventTypeName = "LongPress";
          break;
  
        case DownUp:
          eventTypeName = "DownUp";
          break;
  
        default:
          eventTypeName = "<<unkown>>";
          break;
      }
  
      isb.addString(eventTypeName);
    }
  
    isb.addString(", touches=(");
    for (int i = 0; i < _touchs.size(); i++)
    {
      if (i > 0)
      {
        isb.addString(", ");
      }
      final Touch touch = _touchs.get(i);
      isb.addString(touch.description());
    }
    isb.addString(")");
  
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  @Override
  public String toString() {
    return description();
  }

}