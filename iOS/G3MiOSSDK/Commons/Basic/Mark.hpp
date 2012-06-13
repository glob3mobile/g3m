//
//  Mark.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Mark_hpp
#define G3MiOSSDK_Mark_hpp

#include <string>
#include "Geodetic3D.hpp"

class Mark {
private:
  const std::string _name;
  const std::string _description;
  const std::string _imageFileName;
  const Geodetic3D  _position;

public:
  Mark(const std::string name,
       const std::string description,
       const std::string imageFileName,
       const Geodetic3D  position) :
  _name(name),
  _description(description),
  _imageFileName(imageFileName),
  _position(position)
  {
    
  }
  
  ~Mark() {}
  
  const std::string getName() {
    return _name;
  }
  
};

#endif
