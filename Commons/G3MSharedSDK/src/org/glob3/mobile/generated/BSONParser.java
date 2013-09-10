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


//class IByteBuffer;
//class ByteBufferIterator;
//class JSONBaseObject;
//class JSONString;
//class JSONArray;
//class JSONNumber;
//class JSONDouble;
//class JSONInteger;
//class JSONLong;
//class JSONBoolean;
//class JSONObject;

public class BSONParser
{
  private BSONParser()
  {

  }

  private static JSONBaseObject parseValue(byte type, ByteBufferIterator iterator, boolean nullAsObject)
  {
    switch (type)
    {
      case 0x02:
        return parseString(iterator);
      case 0x04:
        return parseArray(iterator, nullAsObject);
      case 0x01:
        return parseDouble(iterator);
      case 0x10:
        return parseInt32(iterator);
      case 0x12:
        return parseInt64(iterator);
      case 0x08:
        return parseBool(iterator);
      case 0x03:
        return parseObject(iterator, nullAsObject);
      case 0x44:
        return parseCustomizedArray(iterator, nullAsObject);
      case 0x0A:
        return nullAsObject ? new JSONNull() : null;
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
  private static JSONArray parseArray(ByteBufferIterator iterator, boolean nullAsObject)
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
      JSONBaseObject value = parseValue(type, iterator, nullAsObject);
      if (value != null)
      {
        result.add(value);
      }
    }
  
    return result;
  }
  private static JSONArray parseCustomizedArray(ByteBufferIterator iterator, boolean nullAsObject)
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
      JSONBaseObject value = parseValue(type, iterator, nullAsObject);
      if (value != null)
      {
        result.add(value);
      }
    }
  
    return result;
  }
  private static JSONNumber parseDouble(ByteBufferIterator iterator)
  {
    final double doubleValue = iterator.nextDouble();
    final float floatValue = (float) doubleValue;
    if (doubleValue == floatValue)
    {
      return new JSONFloat(floatValue);
    }
    return new JSONDouble(doubleValue);
  }
  private static JSONInteger parseInt32(ByteBufferIterator iterator)
  {
    return new JSONInteger(iterator.nextInt32());
  }
  private static JSONLong parseInt64(ByteBufferIterator iterator)
  {
    return new JSONLong(iterator.nextInt64());
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
  private static JSONObject parseObject(ByteBufferIterator iterator, boolean nullAsObject)
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
      JSONBaseObject value = parseValue(type, iterator, nullAsObject);
      if (value != null)
      {
        result.put(key, value);
      }
    }
  
    return result;
  }


  public static JSONBaseObject parse(IByteBuffer buffer)
  {
     return parse(buffer, false);
  }
  public static JSONBaseObject parse(IByteBuffer buffer, boolean nullAsObject)
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
      JSONBaseObject value = parseValue(type, iterator, nullAsObject);
      if (value != null)
      {
        result.put(key, value);
      }
    }
  
    return result;
  }

}