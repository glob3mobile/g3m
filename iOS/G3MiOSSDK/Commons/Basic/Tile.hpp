//
//  Tile.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Tile_h
#define G3MiOSSDK_Tile_h

#include "Sector.h"
#include "Planet.hpp"
#include "Context.hpp"
#include "MutableVector3D.hpp"

class Tile {
  
public:
  Tile(const Sector &bbox): BBox(bbox), vertices(NULL) {}
  ~Tile();
  
  void createVertices(const Planet *planet);
  void render(const RenderContext* rc);
  
  static void createIndices(unsigned int resol, bool skirts);
  static void deleteIndices();
  
private:
  const Sector BBox;
  float *vertices;
  
  static unsigned int _resolution;
  static bool _skirts;
  static unsigned int numIndices, numBorderIndices, numInnerIndices;
  static unsigned char *indices, *borderIndices, *innerIndices;
  
  MutableVector3D center;


  
};

#endif
