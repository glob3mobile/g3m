//
//  TileData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/21/16.
//
//

#ifndef TileData_hpp
#define TileData_hpp

class TileData {
public:
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
