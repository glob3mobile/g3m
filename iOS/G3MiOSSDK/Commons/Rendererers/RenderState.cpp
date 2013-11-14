//
//  RenderState.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

#include "RenderState.hpp"

RenderState RenderState::ready() {
  return RenderState(RENDER_READY);
}

RenderState RenderState::busy() {
  return RenderState(RENDER_BUSY);
}

RenderState RenderState::error(const std::vector<std::string>& errors) {
  return RenderState(errors);
}
