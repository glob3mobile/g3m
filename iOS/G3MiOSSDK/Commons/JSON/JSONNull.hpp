//
//  JSONNull.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/19/13.
//
//

#ifndef __G3MiOSSDK__JSONNull__
#define __G3MiOSSDK__JSONNull__


#include "JSONBaseObject.hpp"

class JSONNull : public JSONBaseObject {
private:

public:
  JSONNull()
  {
  }

  const JSONNull* asNull() const {
    return this;
  }

  const std::string description() const;

  JSONNull* deepCopy() const {
    return new JSONNull();
  }

  void acceptVisitor(JSONVisitor* visitor) const;
  
};

#endif
