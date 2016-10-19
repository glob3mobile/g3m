//
//  TerrainElevationListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#ifndef TerrainElevationListener_hpp
#define TerrainElevationListener_hpp

class TerrainElevationGrid;


class TerrainElevationListener {
public:
#ifdef C_CODE
  virtual ~TerrainElevationListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual void onGrid(const TerrainElevationGrid* grid) = 0;

};

#endif
