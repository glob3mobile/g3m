//
//  GPUAttributeVec2Float.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#ifndef GPUAttributeVec2Float_hpp
#define GPUAttributeVec2Float_hpp

#include "GPUAttribute.hpp"


class GPUAttributeVec2Float: public GPUAttribute {
private:
  ~GPUAttributeVec2Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeVec2Float(const std::string& name,
                        int id);
  
};

#endif
