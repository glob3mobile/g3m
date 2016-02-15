package org.glob3.mobile.generated; 
//
//  TileData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/21/16.
//
//

//
//  TileData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/21/16.
//
//



//#define TimedCacheTVTDataID 0
//#define TimedCacheTLTDataID 1
//#define ProjectedCornersDistanceTLTDataID 2
//#define MaxTexelProjectedSizeTLTDataID 3

public class TileData
{

  //This id MUST be unique for every kind of TileData
  //It also MUST be an integer positive value, as it is used as index within the tile
  public final int _id;

  public TileData(int id)
  {
     _id = id;
  }

  void dispose() {
  }
}