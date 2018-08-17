package org.glob3.mobile.generated;import java.util.*;

public enum TouchEventType
{
  Down,
  Up,
  Move,
  LongPress,
  DownUp;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TouchEventType forValue(int value)
	{
		return values()[value];
	}
}
