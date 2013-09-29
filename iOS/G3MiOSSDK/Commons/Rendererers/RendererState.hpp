//
//  RendererState.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

#ifndef __G3MiOSSDK__RendererState__
#define __G3MiOSSDK__RendererState__

#include <vector>
#include <string>

class RendererState {
private:
  enum Type {
    READY,
    BUSY,
    ERROR
  };

  const Type                     _type;
  const std::vector<std::string> _errors;

  RendererState(Type type) :
  _type(type)
  {
  }

  RendererState(const std::vector<std::string>& errors) :
  _type(ERROR),
  _errors(errors)
  {
  }

public:
  static RendererState ready();
  static RendererState busy();
  static RendererState error(const std::vector<std::string>& errors);

  bool isReady() const {
    return (_type == READY);
  }

  bool isBusy() const {
    return (_type == BUSY);
  }

  bool isError() const {
    return (_type == ERROR);
  }
  
};

#endif
