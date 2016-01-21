//
//  TileLODTesterData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/21/16.
//
//

#ifndef TileLODTesterData_hpp
#define TileLODTesterData_hpp

class TileLODTesterData {
  //Empty class. Each TileLODTester will implement a different set of associated data and will
  //store it inside the tile using its unique level id
public:
#ifdef C_CODE
  virtual ~TileLODTesterData() { }
#endif
#ifdef JAVA_CODE
  void dispose() {}
#endif
};

#endif
