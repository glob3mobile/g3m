//
//  IFactory.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IFactory_h
#define G3MiOSSDK_IFactory_h

#include "ILogger.hpp"


class ITimer;
class IImage;

class IFactory {
public:
  virtual ITimer* createTimer() const = 0;
  
  virtual void deleteTimer(const ITimer* timer) const = 0;

  virtual IImage* createImageFromFileName(const std::string filename) const = 0;

  virtual void deleteImage(const IImage* image) const = 0;

  // a virtual destructor is needed for conversion to Java
  virtual ~IFactory() {}
};

#endif
