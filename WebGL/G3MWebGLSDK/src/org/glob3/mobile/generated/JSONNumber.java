package org.glob3.mobile.generated; 
public class JSONNumber extends JSONBaseObject
{
  private final number_value _value = new number_value();
  private final number_type _type;

  public JSONNumber(int value)
  {
	  _value = new number_value(new number_value(value));
	  _type = number_type.int_type;
  }
  public JSONNumber(int value)
  {
	  _value = new number_value(new number_value(value));
	  _type = number_type.long_type;
  }
  public JSONNumber(float value)
  {
	  _value = new number_value(new number_value(value));
	  _type = number_type.float_type;
  }
  public JSONNumber(double value)
  {
	  _value = new number_value(new number_value(value));
	  _type = number_type.double_type;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const int getIntValue()const
  public final int getIntValue()
  {
	if (_type != number_type.int_type)
	{
	  ILogger.instance().logError("The value you are requesting is not of type int - returning 0!");
	  return 0;
	}
	return _value.int_value;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const int getLongValue()const
  public final int getLongValue()
  {
	if (_type != number_type.long_type)
	{
	  ILogger.instance().logError("The value you are requesting is not of type long - returning 0!");
	  return 0;
	}
	return _value.long_value;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const float getFloatValue()const
  public final float getFloatValue()
  {
	if (_type != number_type.float_type)
	{
	  ILogger.instance().logError("The value you are requesting is not of type float - returning 0!");
	  return 0;
	}
	return _value.float_value;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const double getDoubleValue()const
  public final double getDoubleValue()
  {
	if (_type != number_type.double_type)
	{
	  ILogger.instance().logError("The value you are requesting is not of type double - returning 0!");
	  return 0;
	}
	return _value.double_value;
  }
  public final JSONNumber getNumber()
  {
	return this;
  }

}