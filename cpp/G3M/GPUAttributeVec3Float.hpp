//
//  GPUAttributeVec3Float.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#ifndef GPUAttributeVec3Float_hpp
#define GPUAttributeVec3Float_hpp

#include "GPUAttribute.hpp"


class GPUAttributeVec3Float: public GPUAttribute {
private:
  ~GPUAttributeVec3Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeVec3Float(const std::string& name,
                        int id);

};

#endif
