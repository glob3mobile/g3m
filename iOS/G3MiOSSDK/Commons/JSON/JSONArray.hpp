//
//  JSONArray.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_JSONArray_hpp
#define G3MiOSSDK_JSONArray_hpp

#include <vector>

#include "JSONBaseObject.hpp"

class JSONArray : public JSONBaseObject{
private:
  std::vector<JSONBaseObject*> _entries;
  
public:
  JSONArray* getArray(){
    return this;
  }
  ~JSONArray();
  JSONBaseObject* getElement(const int index);
  int getSize();
  
  void appendElement( JSONBaseObject* object);
};



#endif
