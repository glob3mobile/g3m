package org.glob3.mobile.generated; 
public class GlobalMembersG3MWidget
{

	public static double clamp(double value, double lower, double upper)
	{
	  if (value < lower)
	  {
		return lower;
	  }
	  if (value > upper)
	  {
		return upper;
	  }
	  return value;
	}
}