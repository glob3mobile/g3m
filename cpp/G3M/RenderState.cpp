//
//  RenderState.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

#include "RenderState.hpp"

const RenderState RenderState::READY = RenderState(RENDER_READY);
const RenderState RenderState::BUSY  = RenderState(RENDER_BUSY);

RenderState RenderState::ready() {
  return READY;
}

RenderState RenderState::busy() {
  return BUSY;
}

RenderState RenderState::error(const std::vector<std::string>& errors) {
  return RenderState(errors);
}

RenderState RenderState::error(const std::string& error) {
  std::vector<std::string> errors;
  errors.push_back(error);
  return RenderState(errors);
}


void RenderStateCollector::reset() {
  _errors.clear();
  _error = false;
  _busy  = false;
}

void RenderStateCollector::add(const RenderState& state) {
  const RenderState_Type type = state._type;
  if (type == RENDER_ERROR) {
    _error = true;

    const std::vector<std::string> errors = state.getErrors();
#ifdef C_CODE
    _errors.insert(_errors.end(),
                   errors.begin(),
                   errors.end());
#endif
#ifdef JAVA_CODE
    _errors.addAll(errors);
#endif
  }
  else if (type == RENDER_BUSY) {
    _busy = true;
  }
}

RenderState RenderStateCollector::getState() const {
  if (_error) {
    return RenderState::error(_errors);
  }
  else if (_busy) {
    return RenderState::busy();
  }
  else {
    return RenderState::ready();
  }
}
