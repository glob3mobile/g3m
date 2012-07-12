//
//  TextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "TextureMapping.hpp"


TextureMapping::TextureMapping(int tID, std::vector<MutableVector2D> texCoords): _textureId(tID)
{
  float *tC = new float[3* texCoords.size()];
  int p = 0;
  for (int i = 0; i < texCoords.size(); i++) {
    tC[p++] = texCoords[i].x();
    tC[p++] = texCoords[i].y();
  }
  
  _texCoords = tC;
  
}
