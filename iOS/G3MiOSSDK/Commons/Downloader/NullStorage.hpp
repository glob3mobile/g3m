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
  bool contains(const URL& url) {
    return false;
  }
  
  void save(const URL& url,
            const ByteBuffer& buffer) {
  }
  
  const ByteBuffer* read(const URL& url) {
    return NULL;
  }
};

#endif
