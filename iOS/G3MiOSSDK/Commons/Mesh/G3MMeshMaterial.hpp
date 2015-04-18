//
//  G3MMeshMaterial.hp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/14.
//
//

#ifndef __G3MiOSSDK__G3MMeshMaterial__
#define __G3MiOSSDK__G3MMeshMaterial__

#include <string>
class Color;
class URL;

class G3MMeshMaterial {
public:
  const std::string _id;
  const Color*      _color;
  const URL*        _textureURL;

  G3MMeshMaterial(const std::string& id,
                  const Color*      color,
                  const URL*        textureURL) :
  _id(id),
  _color(color),
  _textureURL(textureURL)
  {

  }

  ~G3MMeshMaterial();
};

#endif
