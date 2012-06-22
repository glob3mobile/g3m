//
//  Tile.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Tile_h
#define G3MiOSSDK_Tile_h

#include "Sector.hpp"
#include "Planet.hpp"
#include "Context.hpp"
#include "MutableVector3D.hpp"

class Tile {
  
public:
  Tile(const Sector &bounds): _bounds(bounds), _vertices(NULL) {}
  ~Tile();
  
  void createVertices(const Planet *planet);
  void render(const RenderContext* rc);
  
  static void createIndices(unsigned int resol, bool skirts);
  static void deleteIndices();
  
private:
  const Sector _bounds;
  float *_vertices;
  
  static unsigned int _resolution;
  static bool _skirts;
  static unsigned int _numIndices, _numBorderIndices, _numInnerIndices;
  static unsigned char *_indices, *_borderIndices, *_innerIndices;
  
  MutableVector3D _center;


  
};

#endif
