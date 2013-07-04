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


//class JSONBaseObject;
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


  ///#include "JSONNumber.hpp"
  
  public static String generate(JSONBaseObject value)
  {
    JSONGenerator generator = new JSONGenerator();
    value.acceptVisitor(generator);
  
    String result = generator.getString();
  
    if (generator != null)
       generator.dispose();
    return result;
  }

  //  void visitNumber(const JSONNumber* value);

  //void JSONGenerator::visitNumber(const JSONNumber* value) {
  //  switch ( value->getType() ) {
  //    case int_type:
  //      _isb->addInt(value->intValue());
  //      break;
  //    case float_type:
  //      _isb->addFloat(value->floatValue());
  //      break;
  //    case double_type:
  //      _isb->addDouble(value->doubleValue());
  //      break;
  //
  //    default:
  //      break;
  //  }
  //}
  
  public final void visitDouble(JSONDouble value)
  {
    _isb.addDouble(value.doubleValue());
  }
  public final void visitFloat(JSONFloat value)
  {
    _isb.addFloat(value.floatValue());
  }
  public final void visitInteger(JSONInteger value)
  {
    _isb.addInt(value.intValue());
  }
  public final void visitLong(JSONLong value)
  {
    _isb.addLong(value.longValue());
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
  public final void visitString(JSONString value)
  {
    _isb.addString("\"");
    _isb.addString(value.value());
    _isb.addString("\"");
  }

  public final void visitNull()
  {
    _isb.addString("null");
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