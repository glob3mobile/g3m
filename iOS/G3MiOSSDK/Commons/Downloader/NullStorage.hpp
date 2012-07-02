//
//  NullStorage.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_NullStorage_hpp
#define G3MiOSSDK_NullStorage_hpp

#include "Storage.hpp"

class NullStorage: public Storage
{
  bool contains(std::string url){ return false;}
  
  void save(std::string url, const ByteBuffer& bb){};
  
  ByteBuffer getByteBuffer(std::string url){ ByteBuffer bb(NULL,0); return bb;}
};


#endif
