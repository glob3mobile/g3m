package org.glob3.mobile.generated;
public class TouchEvent
{
  private final TouchEventType _type;
  private final java.util.ArrayList<Touch> _touchs;
  private final double _wheelDelta;



  ///#include "ILogger.hpp"
  
  
  private TouchEvent(TouchEventType type, java.util.ArrayList<Touch> touchs, double wheelDelta)
  {
     _type = type;
     _touchs = touchs;
     _wheelDelta = wheelDelta;
    //  ILogger* logger = ILogger::instance();
    //  if (logger != NULL) {
    //    logger->logInfo(description());
    //  }
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TouchEvent(TouchEvent other);


  public static TouchEvent create(TouchEventType type, java.util.ArrayList<Touch> touchs)
  {
     return create(type, touchs, 0.0);
  }
  public static TouchEvent create(TouchEventType type, java.util.ArrayList<Touch> touchs, double wheelDelta)
  {
    return new TouchEvent(type, touchs, wheelDelta);
  }

  public static TouchEvent create(TouchEventType type, Touch touch)
  {
     return create(type, touch, 0.0);
  }
  public static TouchEvent create(TouchEventType type, Touch touch, double wheelDelta)
  {
    final java.util.ArrayList<Touch> touchs = new java.util.ArrayList<Touch>(java.util.Arrays.asList(touch)); //CHANGED BY CONVERSOR RULE

    return create(type, touchs, wheelDelta);
  }

  public final TouchEventType getType()
  {
    return _type;
  }

  public final Touch getTouch(int i)
  {
    return _touchs.get(i);
  }

  public final int getTouchCount()
  {
    return _touchs.size();
  }

  public final double getMouseWheelDelta()
  {
    return _wheelDelta;
  }

  public final byte getTapCount()
  {
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
    isb.addString("(TouchEvent ");
  
    {
      String typeName = "";
      switch (_type)
      {
        case Down:
          typeName = "Down";
          break;
  
        case Up:
          typeName = "Up";
          break;
  
        case Move:
          typeName = "Move";
          break;
  
        case LongPress:
          typeName = "LongPress";
          break;
  
        case DownUp:
          typeName = "DownUp";
          break;
  
        case MouseWheel:
          typeName = "MouseWheel";
          break;
  
        default:
          typeName = "<<unkown>>";
          break;
      }
  
      isb.addString(typeName);
    }
  
    isb.addString(" touches=(");
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
  
    if (_wheelDelta != 0)
    {
      isb.addString(", wheelDelta=");
      isb.addDouble(_wheelDelta);
    }
  
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

  public final TouchEvent clone()
  {
    final java.util.ArrayList<Touch> clonedTouchs = new java.util.ArrayList<Touch>();
  
    for (int i = 0; i < _touchs.size(); i++)
    {
      final Touch touch = _touchs.get(i);
      clonedTouchs.add(touch.clone());
    }
  
    return new TouchEvent(_type, clonedTouchs, _wheelDelta);
  }

  public final boolean isEquals(TouchEvent that)
  {
    if (that == null)
    {
      return false;
    }
  
    if (this.getType() != that.getType())
    {
      return false;
    }
  
    if (this.getTapCount() != that.getTapCount())
    {
      return false;
    }
  
    if (this.getTouchCount() != that.getTouchCount())
    {
      return false;
    }
  
    for (int i = 0; i < _touchs.size(); i++)
    {
      final Touch thisTouch = this._touchs.get(i);
      final Touch thatTouch = that._touchs.get(i);
      if (!thisTouch.isEquals(thatTouch))
      {
        return false;
      }
    }
  
    return true;
  }

}