//
//  Layer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Layer_hpp
#define G3MiOSSDK_Layer_hpp

#include <string>
#include "Sector.hpp"

class Layer{

public:
  
  ~Layer(){};
  
  virtual bool fullContains(const Sector& s) const = 0;

  virtual std::string getRequest(const Sector& sector, int width, int height) const = 0;
  
};

#endif
