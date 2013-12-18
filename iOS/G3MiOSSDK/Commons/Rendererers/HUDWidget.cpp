//
//  HUDWidget.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#include "HUDWidget.hpp"

void HUDWidget::render(const G3MRenderContext* rc,
                       GLState* glState) {
  if (_enable) {
    rawRender(rc, glState);
  }
}
