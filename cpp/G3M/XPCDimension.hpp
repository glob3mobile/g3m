//
//  XPCDimension.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#ifndef XPCDimension_hpp
#define XPCDimension_hpp

#include <vector>
#include <string>


class XPCDimension {
private:
  const std::string   _name;
  const unsigned char _size;
  const std::string   _type;


public:
  XPCDimension(const std::string& name,
               const unsigned char size,
               const std::string& type) :
  _name(name),
  _size(size),
  _type(type)
  {

  }

  ~XPCDimension() {

  }

};

#endif
