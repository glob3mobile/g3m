//
//  URL.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_URL_hpp
#define G3MiOSSDK_URL_hpp

#include <string>

class Url {
private:
  const std::string _path;

public:  
  
  Url(const Url& that) :
  _path(that._path) {
    
  }
  
  Url():_path(""){}
  
  explicit Url(const std::string& path):
  _path(path)
  {
  };
  
  std::string getPath() const {
    return _path;
  }
};


#endif
