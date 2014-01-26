//
//  RenderState.cpp
//  G3MiOSSDK
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
