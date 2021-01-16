package org.glob3.mobile.generated;
//
//  XPCDimension.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

//
//  XPCDimension.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//



//class JSONArray;
//class JSONObject;


public class XPCDimension
{

  private static XPCDimension fromJSON(JSONObject jsonObject)
  {
    final String name = jsonObject.getAsString("name").value();
    final String type = jsonObject.getAsString("type").value();
  
    if ((!type.equals("float")) && (!type.equals("int")))
    {
      return null;
    }
  
    return new XPCDimension(name, type);
  }


  public static java.util.ArrayList<XPCDimension> fromJSON(JSONArray jsonArray)
  {
    if (jsonArray == null)
    {
      return null;
    }
  
    java.util.ArrayList<XPCDimension> result = new java.util.ArrayList<XPCDimension>();
  
    final int size = jsonArray.size();
  
    for (int i = 0; i < size; i++)
    {
      final JSONObject jsonObject = jsonArray.getAsObject(i);
      XPCDimension dimension = fromJSON(jsonObject);
      if (dimension == null)
      {
  
        for (int j = 0; j < result.size(); j++)
        {
          if (result.get(j) != null)
             result.get(j).dispose();
        }
        result = null;
  
        return null;
      }
      result.add(dimension);
    }
  
    return result;
  }

  public void dispose()
  {

  }

  private final String _name;
  private final String _type;


  private XPCDimension(String name, String type)
  {
     _name = name;
     _type = type;

  }


}