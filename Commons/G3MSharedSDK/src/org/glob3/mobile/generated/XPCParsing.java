package org.glob3.mobile.generated;
//
//  XPCParsing.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/16/21.
//

//
//  XPCParsing.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/16/21.
//



//class Sector;
//class JSONArray;


public class XPCParsing
{

  public static Sector parseSector(JSONArray jsonArray)
  {
    final double lowerLat = jsonArray.getAsNumber(0).value();
    final double lowerLon = jsonArray.getAsNumber(1).value();
    final double upperLat = jsonArray.getAsNumber(2).value();
    final double upperLon = jsonArray.getAsNumber(3).value();
  
    return Sector.newFromDegrees(lowerLat, lowerLon, upperLat, upperLon);
  }
  public static java.util.ArrayList<String> parseStrings(JSONArray jsonArray)
  {
  
    final int size = jsonArray.size();
  
    java.util.ArrayList<String> result = new java.util.ArrayList<String>();
  
    for (int i = 0; i < size; i++)
    {
      String String = jsonArray.getAsString(i).value();
      result.add(String);
    }
  
    return result;
  }


  private XPCParsing()
  {

  }


}