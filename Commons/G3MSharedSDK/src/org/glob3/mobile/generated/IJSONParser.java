package org.glob3.mobile.generated; 
//
//  IJSONParser.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  IJSONParser.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public abstract class IJSONParser
{
  private static IJSONParser _instance = null;


  public static void setInstance(IJSONParser parser)
  {
    if (_instance != null)
    {
      ILogger.instance().logWarning("IJSONParser instance already set!");
      if (_instance != null)
         _instance.dispose();
    }
    _instance = parser;
  }

  public static IJSONParser instance()
  {
    return _instance;
  }

  public static String escapeHtmlText(String text)
  {
    final IStringUtils su = IStringUtils.instance();
    String result = su.replaceSubstring(text, "/", "\\/");
    result = su.replaceSubstring(result, "\"", "\\\"");
    result = su.replaceSubstring(result, "\n", "");
    result = su.replaceSubstring(result, "\t", "");
    result = su.replaceSubstring(result, "\r", "");
  
  
  
  
    return result;
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