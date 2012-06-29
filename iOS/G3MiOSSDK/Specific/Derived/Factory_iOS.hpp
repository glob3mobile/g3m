//
//  Factory_iOS.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Factory_iOS_h
#define G3MiOSSDK_Factory_iOS_h

#include "IFactory.hpp"

#include "Timer_iOS.hpp"
#include "Image_iOS.hpp"
#include "Network_iOS.hpp"

class Factory_iOS: public IFactory {
public:
  
  virtual ITimer* createTimer() const {
    return new Timer_iOS();
  }
  
  virtual void deleteTimer(const ITimer* timer) const {
    delete timer;
  }
  
  
  virtual IImage* createImageFromFileName(const std::string filename) const {
    NSString *fn = [NSString stringWithCString:filename.c_str() encoding:[NSString defaultCStringEncoding]];
    
    UIImage* image = [UIImage imageNamed:fn];
    if (!image) {
      printf("Can't read image %s\n",
             filename.c_str());
      
      return NULL;
    }
    
//    printf("Read image %s (%dx%d)\n",
//           filename.c_str(),
//           (int) [image size].width,
//           (int) [image size].height);
    
    return new Image_iOS(image);
  }
  
  virtual void deleteImage(const IImage* image) const {
    delete image;
  }
  
  virtual INetwork* createNetwork() const
  {
    return new Network_iOS();
  }
  
  virtual void deletenetwork(const INetwork* n) const 
  {
    delete n;
  }
  
  
};

#endif
