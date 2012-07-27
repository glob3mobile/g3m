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

class URL {
private:
  const std::string _path;
  
  URL& operator=(const URL& that);
  
public:  
  
  URL(const URL& that) :
  _path(that._path) {
    
  }
  
  explicit URL(const std::string& path):
  _path(path)
  {
  };
  
  std::string getPath() const {
    return _path;
  }
};


#endif
