package org.glob3.mobile.generated;
//
//  XPCNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

//
//  XPCNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//



//class JSONObject;
//class JSONArray;
//class Sector;


public class XPCNode
{

  private static XPCNode fromJSON(JSONObject jsonObject)
  {
  
    final String id = jsonObject.getAsString("id").value();
  
    final Sector sector = XPCParsing.parseSector(jsonObject.getAsArray("sector"));
  
    final int pointsCount = (int) jsonObject.getAsNumber("pointsCount").value();
  
    final double minZ = jsonObject.getAsNumber("minZ").value();
    final double maxZ = jsonObject.getAsNumber("maxZ").value();
  
    final java.util.ArrayList<String> childIDs = XPCParsing.parseStrings(jsonObject.getAsArray("childIDs"));
    if (childIDs == null)
    {
      if (sector != null)
         sector.dispose();
      return null;
    }
  
    return new XPCNode(id, sector, pointsCount, minZ, maxZ, childIDs);
  }


  private final String _id;

  private final Sector _sector;

  private final int _pointsCount;

  private final double _minZ;
  private final double _maxZ;

  private final java.util.ArrayList<String> _childIDs;


  private XPCNode(String id, Sector sector, int pointsCount, double minZ, double maxZ, java.util.ArrayList<String> childIDs)
  {
     _id = id;
     _sector = sector;
     _pointsCount = pointsCount;
     _minZ = minZ;
     _maxZ = maxZ;
     _childIDs = childIDs;
  
  }


  public static java.util.ArrayList<XPCNode> fromJSON(JSONArray jsonArray)
  {
    if (jsonArray == null)
    {
      return null;
    }
  
    java.util.ArrayList<XPCNode> result = new java.util.ArrayList<XPCNode>();
  
    final int size = jsonArray.size();
  
    for (int i = 0; i < size; i++)
    {
      final JSONObject jsonObject = jsonArray.getAsObject(i);
      XPCNode dimension = fromJSON(jsonObject);
      if (dimension == null)
      {
        // release the memory allocated up to here
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
    if (_sector != null)
       _sector.dispose();
  
  }

}