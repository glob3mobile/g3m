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

enum RenderState_Type {
  RENDER_READY,
  RENDER_BUSY,
  RENDER_ERROR
};

class RenderState {
private:
  const static RenderState READY;
  const static RenderState BUSY;
  
public:
  static RenderState ready();
  static RenderState busy();
  static RenderState error(const std::vector<std::string>& errors);

  const RenderState_Type _type;

  const std::vector<std::string> getErrors() const {
    return _errors;
  }

  RenderState(const RenderState& that) :
  _type(that._type),
  _errors(that._errors)
  {
  }

//  RenderState& operator= (const RenderState& that) {
//    if (this != &that) {
//      _type = that._type;
//      _errors = that._errors;
//    }
//    return *this;
//  }

  ~RenderState() {

  }

private:
#ifdef C_CODE
  const std::vector<std::string> _errors;
#endif
#ifdef JAVA_CODE
  private final java.util.ArrayList<String> _errors;
#endif

  RenderState(RenderState_Type type) :
  _type(type)
  {
#ifdef JAVA_CODE
    _errors = null;
#endif
  }

  RenderState(const std::vector<std::string>& errors) :
  _type(RENDER_ERROR),
  _errors(errors)
  {
  }



};

#endif
