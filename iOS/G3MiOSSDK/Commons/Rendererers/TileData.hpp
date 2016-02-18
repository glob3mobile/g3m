//
//  TileData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/21/16.
//
//

#ifndef TileData_hpp
#define TileData_hpp


#define TimedCacheTVTDataID 0
#define TimedCacheTLTDataID 1
#define ProjectedCornersDistanceTLTDataID 2

class TileData {
public:
  
  //This id MUST be unique for every kind of TileData
  //It also MUST be an integer positive value, as it is used as index within the tile
  const int _id;
  
  TileData(int id):_id(id){}
  
#ifdef C_CODE
  virtual ~TileData() {
  }
#endif
#ifdef JAVA_CODE
  void dispose() {
  }
#endif
};

#endif
