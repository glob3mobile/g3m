//
//  RenderState.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

#ifndef __G3MiOSSDK__RenderState__
#define __G3MiOSSDK__RenderState__

#include <vector>
#include <string>

class RenderState {
public:
  enum Type {
    READY,
    BUSY,
    ERROR
  };

  static RenderState ready();
  static RenderState busy();
  static RenderState error(const std::vector<std::string>& errors);

  const Type _type;

  const std::vector<std::string> getErrors() const {
    return _errors;
  }

private:
  const std::vector<std::string> _errors;

  RenderState(Type type) :
  _type(type)
  {
  }

  RenderState(const std::vector<std::string>& errors) :
  _type(ERROR),
  _errors(errors)
  {
  }

};

#endif
