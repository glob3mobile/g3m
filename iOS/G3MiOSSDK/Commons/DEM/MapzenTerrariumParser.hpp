//
//  MapzenTerrariumParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//

#ifndef MapzenTerrariumParser_hpp
#define MapzenTerrariumParser_hpp

class FloatBufferTerrainElevationGrid;
class IImage;
class Sector;


class MapzenTerrariumParser {
private:
  MapzenTerrariumParser() {
  }

public:
  static FloatBufferTerrainElevationGrid* parse(IImage* image,
                                                const Sector& sector,
                                                double deltaHeight);



};

#endif
