//
//  JSONNull.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 6/19/13.
//
//

#include "JSONNull.hpp"

#include "JSONVisitor.hpp"

const std::string JSONNull::description() const {
  return "null";
}

const std::string JSONNull::toString() const {
  return "null";
}

void JSONNull::acceptVisitor(JSONVisitor* visitor) const {
  visitor->visitNull();
}
