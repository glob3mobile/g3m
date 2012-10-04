//
//  JSONArray.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "JSONArray.hpp"
#include "JSONBaseObject.hpp"


JSONBaseObject* JSONArray::getElement(const int index){
  return _entries[index];
}

void JSONArray::appendElement (JSONBaseObject* object){
  _entries.push_back(object);
}

