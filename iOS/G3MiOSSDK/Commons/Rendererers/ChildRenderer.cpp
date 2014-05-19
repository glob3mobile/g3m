//
//  ChildRenderer.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 22/04/14.
//
//

#include "ChildRenderer.hpp"


void ChildRenderer::addInfo(std::string& inf) {
  _info.push_back(inf);
}


void ChildRenderer::setInfo(const std::vector<std::string>& info) {
  _info.clear();
#ifdef C_CODE
  _info.insert(_info.end(),
               info.begin(),
               info.end());
#endif
#ifdef JAVA_CODE
  _info.addAll(info);
#endif
}
