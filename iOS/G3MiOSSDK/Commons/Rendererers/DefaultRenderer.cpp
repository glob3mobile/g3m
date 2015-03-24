//
//  DefaultRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

#include "DefaultRenderer.hpp"
#include "ILogger.hpp"

void DefaultRenderer::setEnable(bool enable) {
  if(enable != _enable){
    _enable = enable;
    if(_changedInfoListener!= NULL){
      if(isEnable()){
        notifyChangedInfo(_info);
      }
      else {
        const std::vector<const Info*> info;
        _changedInfoListener->changedRendererInfo(_rendererIdentifier, info);
      }
    }
  }
}

void DefaultRenderer::setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener, const int rendererIdentifier) {
  if (_changedInfoListener != NULL) {
    ILogger::instance()->logError("Changed Renderer Info Listener of DefaultRenderer already set");
  }
  else {
    _changedInfoListener = changedInfoListener;
    _rendererIdentifier = rendererIdentifier;
    notifyChangedInfo(_info);
  }
}

void DefaultRenderer::setInfo(const std::vector<const Info*>& info){
  _info.clear();
#ifdef C_CODE
  _info.insert(_info.end(),
               info.begin(),
               info.end());
#endif
#ifdef JAVA_CODE
  _info.addAll(info);
#endif
  notifyChangedInfo(_info);
}

void DefaultRenderer::addInfo(const std::vector<const Info*>& info){
#ifdef C_CODE
  _info.insert(_info.end(),
               info.begin(),
               info.end());
#endif
#ifdef JAVA_CODE
  _info.addAll(info);
#endif
  notifyChangedInfo(_info);
}

void DefaultRenderer::addInfo(const Info* info){
#ifdef C_CODE
  _info.insert(_info.end(), info);
#endif
#ifdef JAVA_CODE
  _info.add(info);
#endif
  notifyChangedInfo(_info);
}

void DefaultRenderer::notifyChangedInfo(const std::vector<const Info*>& info) {
  if(_changedInfoListener!= NULL){
    if(isEnable()){
      _changedInfoListener->changedRendererInfo(_rendererIdentifier, info);
    }
  }
}
