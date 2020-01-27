//
//  GPUAttributeVec4Float.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#ifndef GPUAttributeVec4Float_hpp
#define GPUAttributeVec4Float_hpp

#include "GPUAttribute.hpp"


class GPUAttributeVec4Float: public GPUAttribute {
private:
  ~GPUAttributeVec4Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
  
public:
  GPUAttributeVec4Float(const std::string& name,
                        int id);
  
};

#endif
