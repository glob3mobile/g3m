package org.glob3.mobile.generated; 
public class JSONNumber extends JSONBaseObject
{
  //const Number_value _value;
  private int _int_value;
  private float _float_value;
  private double _double_value;
  private final number_type _type;


  public JSONNumber(int value)
  {
	  _int_value = value;
	  _float_value = 0.0F;
	  _double_value = 0.0;
	  _type = number_type.int_type;
  }
  public JSONNumber(float value)
  {
	  _int_value = 0;
	  _float_value = value;
	  _double_value = 0.0;
	  _type = number_type.float_type;
  }
  public JSONNumber(double value)
  {
	  _int_value = 0;
	  _float_value = 0.0F;
	  _double_value = value;
	  _type = number_type.double_type;
  }
  public final int getIntValue()
  {
	if (_type != number_type.int_type)
	{
	  ILogger.instance().logError("The value you are requesting is not of type int - returning 0!");
	  return 0;
	}
	return _int_value;
  }
  //const long getLongValue()const;

  /*const long JSONNumber::getLongValue()const{
	if (_type != long_type){
	  ILogger::instance()->logError("The value you are requesting is not of type long - returning 0!");
	  return 0;
	}
	return _value.long_value;
  }*/
  
  public final float getFloatValue()
  {
	if (_type != number_type.float_type)
	{
	  ILogger.instance().logError("The value you are requesting is not of type float - returning 0!");
	  return 0;
	}
	return _float_value;
  }
  public final double getDoubleValue()
  {
	if (_type != number_type.double_type)
	{
	  ILogger.instance().logError("The value you are requesting is not of type double - returning 0!");
	  return 0;
	}
	return _double_value;
  }
  public final JSONNumber getNumber()
  {
	return this;
  }

}