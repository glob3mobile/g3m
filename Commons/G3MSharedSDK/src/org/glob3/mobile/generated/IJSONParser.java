package org.glob3.mobile.generated;
//
//  IJSONParser.cpp
//  G3M
//
//  Created by Oliver Koehler on 02/10/12.
//

//
//  IJSONParser.hpp
//  G3M
//
//  Created by Oliver Koehler on 02/10/12.
//




public abstract class IJSONParser
{
  private static IJSONParser _instance = null;


  public static void setInstance(IJSONParser parser)
  {
    if (_instance != null)
    {
      //ILogger::instance()->logWarning("IJSONParser instance already set!");
      if (_instance != null)
         _instance.dispose();
    }
    _instance = parser;
  }

  public static IJSONParser instance()
  {
    return _instance;
  }

  public void dispose()
  {
  }

  public JSONBaseObject parse(String json)
  {
    return parse(json, false);
  }

  public abstract JSONBaseObject parse(String json, boolean nullAsObject);


  public JSONBaseObject parse(IByteBuffer buffer)
  {
    return parse(buffer, false);
  }

  public abstract JSONBaseObject parse(IByteBuffer buffer, boolean nullAsObject);

  public void deleteJSONData(JSONBaseObject object)
  {
    if (object != null)
       object.dispose();
  }

}
