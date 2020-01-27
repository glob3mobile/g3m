//
//  GPUAttributeVec1Float.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#ifndef GPUAttributeVec1Float_hpp
#define GPUAttributeVec1Float_hpp

#include "GPUAttribute.hpp"


class GPUAttributeVec1Float: public GPUAttribute {
private:
  ~GPUAttributeVec1Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeVec1Float(const std::string& name,
                        int id);

};

#endif
