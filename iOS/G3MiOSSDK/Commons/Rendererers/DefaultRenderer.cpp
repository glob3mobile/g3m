//
//  DefaultRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

#include "DefaultRenderer.hpp"
#include "ILogger.hpp"


void DefaultRenderer::setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener, const int rendererIdentifier) {
  if (_changedInfoListener != NULL) {
    ILogger::instance()->logError("Changed Renderer Info Listener of DefaultRenderer already set");
  }
  _changedInfoListener = changedInfoListener;
  _rendererIdentifier = rendererIdentifier;
  ILogger::instance()->logInfo("Changed Renderer Info Listener of DefaultRenderer set ok");
}