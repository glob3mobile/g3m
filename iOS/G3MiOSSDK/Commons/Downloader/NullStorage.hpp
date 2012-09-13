//
//  NullStorage.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_NullStorage_hpp
#define G3MiOSSDK_NullStorage_hpp

#include "IStorage.hpp"

class NullStorage: public IStorage {
public:
  bool containsBuffer(const URL& url) {
    return false;
  }
  
  void saveBuffer(const URL& url,
                  const IByteBuffer& buffer) {
    
  }
  
  const IByteBuffer* readBuffer(const URL& url) {
    return NULL;
  }
  
  bool containsImage(const URL& url) {
    return false;
  }
  
  void saveImage(const URL& url,
                 const IImage& buffer) {
    
  }
  
  const IImage* readImage(const URL& url) {
    return NULL;
  }
  
};

#endif
