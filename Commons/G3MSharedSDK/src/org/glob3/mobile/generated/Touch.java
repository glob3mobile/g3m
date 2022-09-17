package org.glob3.mobile.generated;
//
//  TouchEvent.cpp
//  G3M
//
//  Created by José Miguel S N on 31/05/12.
//

//
//  TouchEvent.hpp
//  G3M
//
//  Created by José Miguel S N on 31/05/12.
//





public class Touch
{
  private final Vector2F _pos;
  private final byte _tapCount;
  private final double _wheelDelta;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Touch(Touch other);

  public Touch(Vector2F pos, byte tapCount)
  {
     this(pos, tapCount, 0.0);
  }
  public Touch(Vector2F pos, byte tapCount, double wheelDelta)
  {
     _pos = pos;
     _tapCount = tapCount;
     _wheelDelta = wheelDelta;
    if (_pos == null) {
      throw new RuntimeException("_pos is null");
    }
  }

  public final Touch clone()
  {
    return new Touch(_pos, _tapCount, _wheelDelta);
  }

  public final Vector2F getPos()
  {
     return _pos;
  }
  public final byte getTapCount()
  {
     return _tapCount;
  }
  public final double getMouseWheelDelta()
  {
     return _wheelDelta;
  }

  public void dispose()
  {
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(Touch pos=");
  
    isb.addString((_pos == null) ? "null" : _pos.description());
  
    isb.addString(", tapCount=");
    isb.addInt(_tapCount);
  
    isb.addString(", wheelDelta=");
    isb.addDouble(_wheelDelta);
  
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