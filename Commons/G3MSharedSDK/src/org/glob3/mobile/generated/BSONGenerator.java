package org.glob3.mobile.generated; 
//
//  BSONGenerator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//

//
//  BSONGenerator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBaseObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ByteBufferBuilder;


public class BSONGenerator extends JSONVisitor
{
  private ByteBufferBuilder _builder;


  ///#include "IStringBuilder.hpp"
  
  private BSONGenerator()
  {
	_builder = new ByteBufferBuilder();
	_currentKey = "";
  }

  private IByteBuffer createBuffer()
  {
	return _builder.create();
  }

  private String _currentKey;

  private java.util.ArrayList<Integer> _positionsStack = new java.util.ArrayList<Integer>();

  private void addCurrentKey()
  {
	if (!_currentKey.length() == 0)
	{
	  _builder.addStringZeroTerminated(_currentKey);
	}
  }

  public void dispose()
  {
	if (_builder != null)
		_builder.dispose();
  }

  public static IByteBuffer generate(JSONBaseObject value)
  {
	BSONGenerator generator = new BSONGenerator();
	value.acceptVisitor(generator);
  
	IByteBuffer result = generator.createBuffer();
  
	if (generator != null)
		generator.dispose();
	return result;
  }

  public final void visitBoolean(JSONBoolean value)
  {
	_builder.add(0x08);
	addCurrentKey();
	if (value.value())
	{
	  _builder.add(0x01);
	}
	else
	{
	  _builder.add(0x00);
	}
  }
  public final void visitNumber(JSONNumber value)
  {
	switch (value.getType())
	{
	  case int_type:
		_builder.add(0x10);
		addCurrentKey();
		_builder.addInt32(value.intValue());
		break;
	  case float_type:
		_builder.add(0x01);
		addCurrentKey();
		_builder.addDouble(value.floatValue());
		break;
	  case double_type:
		_builder.add(0x01);
		addCurrentKey();
		_builder.addDouble(value.doubleValue());
		break;
  
	  default:
		break;
	}
  
  }
  public final void visitString(JSONString value)
  {
	_builder.add(0x02); // type string
	addCurrentKey();
  
	final String str = value.value();
	_builder.addInt32(str.length() + 1); // 1 for \0 termination
	_builder.addStringZeroTerminated(str);
  }

  public final void visitArrayBeforeChildren(JSONArray value)
  {
  //  _builder->add(0x04); // type array
	_builder.add(0x44); // type customized-array
	addCurrentKey();
  
	_positionsStack.add(_builder.size()); // store current position, to update the size later
	_builder.addInt32(0); // save space for size
  }
  public final void visitArrayInBetweenChildren(JSONArray value)
  {
  
  }
  public final void visitArrayBeforeChild(JSONArray value, int i)
  {
  //  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  //  isb->addInt(i);
  //  _currentKey = isb->getString();
  //  delete isb;
	_currentKey = "";
  }
  public final void visitArrayAfterChildren(JSONArray value)
  {
	int sizePosition = _positionsStack.get(_positionsStack.size() - 1);
	_positionsStack.remove(_positionsStack.size() - 1);
  
	_builder.add(0);
	_builder.setInt32(sizePosition, _builder.size() - sizePosition);
  }

  public final void visitObjectBeforeChildren(JSONObject value)
  {
	if (_positionsStack.size() != 0)
	{
	  // if positions back is not empty, it means the object is not the outer object
	  _builder.add(0x03); // type document
	  addCurrentKey();
	}
  
	_positionsStack.add(_builder.size()); // store current position, to update the size later
	_builder.addInt32(0); // save space for size
  }
  public final void visitObjectInBetweenChildren(JSONObject value)
  {
  
  }
  public final void visitObjectBeforeChild(JSONObject value, String key)
  {
	_currentKey = key;
  }
  public final void visitObjectAfterChildren(JSONObject value)
  {
	int sizePosition = _positionsStack.get(_positionsStack.size() - 1);
	_positionsStack.remove(_positionsStack.size() - 1);
  
	_builder.add(0);
	_builder.setInt32(sizePosition, _builder.size() - sizePosition);
  }

}