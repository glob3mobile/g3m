//
//  IImage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IImage_h
#define G3MiOSSDK_IImage_h

class IImage {
public:
  virtual void loadFromFileName(std::string filename) = 0;
  
  // a virtual destructor is needed for conversion to Java
  virtual ~IImage() {}
};

#endif
