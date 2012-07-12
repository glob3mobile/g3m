//
//  TextureMapping.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureMapping_hpp
#define G3MiOSSDK_TextureMapping_hpp

class TextureMapping
{
private:
  const float const *         _texCoords;
  const int            _textureId;
  
public:
  TextureMapping(int tID, float tC[]): _texCoords(tC), _textureId(tID){}
  
  ~TextureMapping(){ delete[] _texCoords;}
  
  int getID() const { return _textureId;}
  const float* getCoords() const { return _texCoords;}
  
};

#endif
