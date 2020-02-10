//
//  ChangedRendererInfoListener.hpp
//  G3M
//
//  Created by Vidal Toboso on 21/04/14.
//
//

#ifndef __G3M__ChangedRendererInfoListener__
#define __G3M__ChangedRendererInfoListener__

class Renderer;
class Info;

#include <vector>

class ChangedRendererInfoListener {
  
public:
#ifdef C_CODE
  virtual ~ChangedRendererInfoListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
  
  virtual void changedRendererInfo(const size_t rendererID,
                                   const std::vector<const Info*>& info) = 0;
  
 };


#endif /* defined(__G3M__ChangedRendererInfoListener__) */
