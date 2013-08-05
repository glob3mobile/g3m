//
//  JSONNull.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/19/13.
//
//

#include "JSONNull.hpp"

#include "JSONVisitor.hpp"

const std::string JSONNull::description() const {
  return "null";
}

void JSONNull::acceptVisitor(JSONVisitor* visitor) const {
  visitor->visitNull();
}
