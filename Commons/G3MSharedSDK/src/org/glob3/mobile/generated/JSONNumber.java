package org.glob3.mobile.generated; 
public class JSONNumber extends JSONBaseObject
{
  private int _intValue;
  private float _floatValue;
  private double _doubleValue;
  private final number_type _type;

  private JSONNumber(int intValue, float floatValue, double doubleValue, number_type type)
  {
	  _intValue = intValue;
	  _floatValue = floatValue;
	  _doubleValue = doubleValue;
	  _type = type;
  }


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
//ORIGINAL LINE: const number_type getType() const
  public final number_type getType()
  {
	return _type;
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double value() const
  public final double value()
  {
	if (_type == number_type.double_type)
	{
	  return _doubleValue;
	}
	else if (_type == number_type.int_type)
	{
	  return _intValue;
	}
	else if (_type == number_type.float_type)
	{
	  return _floatValue;
	}
	return 0;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONNumber* asNumber() const
  public final JSONNumber asNumber()
  {
	return this;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
  
	switch (_type)
	{
	  case int_type:
		//isb->addString("int/");
		isb.addInt(_intValue);
		break;
  
	  case float_type:
		//isb->addString("float/");
		isb.addFloat(_floatValue);
		break;
  
	  case double_type:
		//isb->addString("double/");
		isb.addDouble(_doubleValue);
		break;
  
	  default:
		isb.addString("[unknown number type]");
		break;
	}
  
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONNumber* deepCopy() const
  public final JSONNumber deepCopy()
  {
	return new JSONNumber(_intValue, _floatValue, _doubleValue, _type);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void acceptVisitor(JSONVisitor* visitor) const
  public final void acceptVisitor(JSONVisitor visitor)
  {
	visitor.visitNumber(this);
  }

}