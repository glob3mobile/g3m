//
//  ChangedRendererInfoListener.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 21/04/14.
//
//

#ifndef __G3MiOSSDK__ChangedRendererInfoListener__
#define __G3MiOSSDK__ChangedRendererInfoListener__

class Renderer;

#include <vector>

class ChangedRendererInfoListener {
  
public:
#ifdef C_CODE
  virtual ~ChangedRendererInfoListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
  
  virtual void changedRendererInfo(const int rendererIdentifier, const std::vector<std::string>& info) = 0;
  
 };


#endif /* defined(__G3MiOSSDK__ChangedRendererInfoListener__) */
