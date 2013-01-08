package org.glob3.mobile.generated; 
//
//  BSONParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/13.
//
//

//
//  BSONParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IByteBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ByteBufferIterator;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBaseObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONString;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONArray;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONNumber;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBoolean;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONObject;

public class BSONParser
{
  private BSONParser()
  {

  }

  private static JSONBaseObject parseValue(byte type, ByteBufferIterator iterator)
  {
	switch (type)
	{
	  case 0x02:
	  {
		return parseString(iterator);
	  }
	  case 0x04:
	  {
		return parseArray(iterator);
	  }
	  case 0x01:
	  {
		return parseDouble(iterator);
	  }
	  case 0x10:
	  {
		return parseInt(iterator);
	  }
	  case 0x08:
	  {
		return parseBool(iterator);
	  }
	  case 0x03:
	  {
		return parseObject(iterator);
	  }
	  case 0x44:
	  {
		return parseCustomizedArray(iterator);
	  }
	  default:
	  {
		ILogger.instance().logError("Unknown type %d", type);
		return null;
	  }
	}
  }


  private static JSONString parseString(ByteBufferIterator iterator)
  {
	final int stringSize = iterator.nextInt32();
	final String str = iterator.nextZeroTerminatedString();
  
	if ((stringSize - 1) == str.length())
	{
	  return new JSONString(str);
	}
  
	ILogger.instance().logError("Invalid stringSize, expected %d but got %d", stringSize, str.length());
	return null;
  }
  private static JSONArray parseArray(ByteBufferIterator iterator)
  {
	//const int arraySize = iterator->nextInt32();
	iterator.nextInt32(); // consumes the size
  
	JSONArray result = new JSONArray();
	while (iterator.hasNext())
	{
	  final byte type = iterator.nextUInt8();
	  if (type == 0)
	  {
		break;
	  }
  
	  // const std::string key = iterator->nextZeroTerminatedString();
	  iterator.nextZeroTerminatedString(); // consumes the key
	  JSONBaseObject value = parseValue(type, iterator);
	  if (value != null)
	  {
		result.add(value);
	  }
	}
  
	return result;
  }
  private static JSONArray parseCustomizedArray(ByteBufferIterator iterator)
  {
	//const int arraySize = iterator->nextInt32();
	iterator.nextInt32(); // consumes the size
  
	JSONArray result = new JSONArray();
	while (iterator.hasNext())
	{
	  final byte type = iterator.nextUInt8();
	  if (type == 0)
	  {
		break;
	  }
  
	  //const std::string key = iterator->nextZeroTerminatedString();
	  JSONBaseObject value = parseValue(type, iterator);
	  if (value != null)
	  {
		result.add(value);
	  }
	}
  
	return result;
  }
  private static JSONNumber parseDouble(ByteBufferIterator iterator)
  {
	return new JSONNumber(iterator.nextDouble());
  }
  private static JSONNumber parseInt(ByteBufferIterator iterator)
  {
	return new JSONNumber(iterator.nextInt32());
  }
  private static JSONBoolean parseBool(ByteBufferIterator iterator)
  {
	byte b = iterator.nextUInt8();
	if (b == 0x01)
	{
	  return new JSONBoolean(true);
	}
	return new JSONBoolean(false);
  }
  private static JSONObject parseObject(ByteBufferIterator iterator)
  {
	//const int objectSize = iterator->nextInt32();
	iterator.nextInt32(); // consumes the size
  
	JSONObject result = new JSONObject();
	while (iterator.hasNext())
	{
	  final byte type = iterator.nextUInt8();
	  if (type == 0)
	  {
		break;
	  }
  
	  final String key = iterator.nextZeroTerminatedString();
	  JSONBaseObject value = parseValue(type, iterator);
	  if (value != null)
	  {
		result.put(key, value);
	  }
	}
  
	return result;
  }


  public static JSONBaseObject parse(IByteBuffer buffer)
  {
  
	ByteBufferIterator iterator = new ByteBufferIterator(buffer);
	final int bufferSize = iterator.nextInt32();
  
	if (buffer.size() != bufferSize)
	{
	  ILogger.instance().logError("Invalid bufferSize, expected %d but got %d", bufferSize, buffer.size());
	}
  
	JSONObject result = new JSONObject();
  
	while (iterator.hasNext())
	{
	  final byte type = iterator.nextUInt8();
	  if (type == 0)
	  {
		break;
	  }
  
	  final String key = iterator.nextZeroTerminatedString();
	  JSONBaseObject value = parseValue(type, iterator);
	  if (value != null)
	  {
		result.put(key, value);
	  }
	}
  
	return result;
  }

}