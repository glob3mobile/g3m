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
  
  URL():_path(""){}
  
  explicit URL(const std::string& path):
  _path(path)
  {
  };
  
  URL(const URL& parent,
      const std::string& path):
  _path(parent.getPath() + "/" + path)
  {
  };
  
  std::string getPath() const {
    return _path;
  }
  
  static URL null() {
    return URL("__NULL__");
  }
  
  bool isNull() const {
    return (_path == "__NULL__");
  }
  
  bool isEqualsTo(const URL& that) const {
    return (_path == that._path);
  }
  
  const std::string description() const;
  
};


#endif
