//
//  RendererState.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

#include "RendererState.hpp"

RendererState RendererState::ready() {
  return RendererState(READY);
}

RendererState RendererState::busy() {
  return RendererState(BUSY);
}

RendererState RendererState::error(const std::vector<std::string>& errors) {
  return RendererState(errors);
}
