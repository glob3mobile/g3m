//
//  ITexturizerData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/7/13.
//
//

#ifndef __G3MiOSSDK__ITexturizerData__
#define __G3MiOSSDK__ITexturizerData__

class ITexturizerData {
public:
#ifdef C_CODE
  virtual ~ITexturizerData() { }
#else
  virtual ~ITexturizerData() = 0;
#endif
};

#endif
