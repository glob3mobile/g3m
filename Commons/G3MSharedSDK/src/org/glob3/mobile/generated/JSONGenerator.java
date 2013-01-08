package org.glob3.mobile.generated; 
//
//  JSONGenerator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//

//
//  JSONGenerator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBaseObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IStringBuilder;

public class JSONGenerator extends JSONVisitor
{
  private IStringBuilder _isb;

  private JSONGenerator()
  {
	_isb = IStringBuilder.newStringBuilder();
  }

  private String getString()
  {
	return _isb.getString();
  }

  public void dispose()
  {
	if (_isb != null)
		_isb.dispose();
  }

  public static String generate(JSONBaseObject value)
  {
	JSONGenerator generator = new JSONGenerator();
	value.acceptVisitor(generator);
  
	String result = generator.getString();
  
	if (generator != null)
		generator.dispose();
	return result;
  }

  public final void visitBoolean(JSONBoolean value)
  {
	if (value.value())
	{
	  _isb.addString("true");
	}
	else
	{
	  _isb.addString("false");
	}
  }
  public final void visitNumber(JSONNumber value)
  {
	switch (value.getType())
	{
	  case int_type:
		_isb.addInt(value.intValue());
		break;
	  case float_type:
		_isb.addFloat(value.floatValue());
		break;
	  case double_type:
		_isb.addDouble(value.doubleValue());
		break;
  
	  default:
		break;
	}
  }
  public final void visitString(JSONString value)
  {
	_isb.addString("\"");
	_isb.addString(value.value());
	_isb.addString("\"");
  }

  public final void visitArrayBeforeChildren(JSONArray value)
  {
	_isb.addString("[");
  }
  public final void visitArrayInBetweenChildren(JSONArray value)
  {
	_isb.addString(",");
  }
  public final void visitArrayBeforeChild(JSONArray value, int i)
  {
  
  }
  public final void visitArrayAfterChildren(JSONArray value)
  {
	_isb.addString("]");
  }

  public final void visitObjectBeforeChildren(JSONObject value)
  {
	_isb.addString("{");
  }
  public final void visitObjectInBetweenChildren(JSONObject value)
  {
	_isb.addString(",");
  }
  public final void visitObjectBeforeChild(JSONObject value, String key)
  {
	_isb.addString("\"");
	_isb.addString(key);
	_isb.addString("\":");
  }
  public final void visitObjectAfterChildren(JSONObject value)
  {
	_isb.addString("}");
  }

}