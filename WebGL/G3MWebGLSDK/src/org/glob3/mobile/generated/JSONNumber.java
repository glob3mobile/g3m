package org.glob3.mobile.generated; 
public class JSONNumber extends JSONBaseObject
{
  private int _intValue;
  private float _floatValue;
  private double _doubleValue;
  private final number_type _type;


  public JSONNumber(int value)
  {
	  _intValue = value;
	  _floatValue = 0.0F;
	  _doubleValue = 0.0;
	  _type = number_type.int_type;
  }

  public JSONNumber(float value)
  {
	  _intValue = 0;
	  _floatValue = value;
	  _doubleValue = 0.0;
	  _type = number_type.float_type;
  }

  public JSONNumber(double value)
  {
	  _intValue = 0;
	  _floatValue = 0.0F;
	  _doubleValue = value;
	  _type = number_type.double_type;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int intValue() const
  public final int intValue()
  {
	if (_type != number_type.int_type)
	{
	  ILogger.instance().logError("The value you are requesting is not of type int - returning 0!");
	  return 0;
	}
  
	return _intValue;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float floatValue() const
  public final float floatValue()
  {
	if (_type != number_type.float_type)
	{
	  ILogger.instance().logError("The value you are requesting is not of type float - returning 0!");
	  return 0;
	}
  
	return _floatValue;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double doubleValue() const
  public final double doubleValue()
  {
	if (_type != number_type.double_type)
	{
	  ILogger.instance().logError("The value you are requesting is not of type double - returning 0!");
	  return 0;
	}
  
	return _doubleValue;
  }

  public final JSONNumber asNumber()
  {
	return this;
  }

}