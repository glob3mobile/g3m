package org.glob3.mobile.generated; 
public class JSONNumber extends JSONBaseObject
{
  private final Number_value _value = new Number_value();
  private final number_type _type;

  public JSONNumber(int value)
  {
	  _value = new Number_value(new Number_value(value));
	  _type = number_type.int_type;
  }
  //JSONNumber(long value):_value(Number_value(value)), _type(long_type){}
  public JSONNumber(float value)
  {
	  _value = new Number_value(new Number_value(value));
	  _type = number_type.float_type;
  }
  public JSONNumber(double value)
  {
	  _value = new Number_value(new Number_value(value));
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
	return _value._int_value;
  }
  //const long getLongValue()const;

  /*const long JSONNumber::getLongValue()const{
	if (_type != long_type){
	  ILogger::instance()->logError("The value you are requesting is not of type long - returning 0!");
	  return 0;
	}
	return _value.long_value;
  }*/
  
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const float getFloatValue()const
  public final float getFloatValue()
  {
	if (_type != number_type.float_type)
	{
	  ILogger.instance().logError("The value you are requesting is not of type float - returning 0!");
	  return 0;
	}
	return _value._float_value;
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
	return _value._double_value;
  }
  public final JSONNumber getNumber()
  {
	return this;
  }

}