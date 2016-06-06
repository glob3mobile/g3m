//
//  IPrePostRenderTasks.hpp
//  G3MiOSSDK
//
//  Created by Premecz Matyas on 12/05/16.
//
//

#ifndef IPrePostRenderTasks_hpp
#define IPrePostRenderTasks_hpp

#include <stdio.h>


class IPrePostRenderTasks {
public:

  virtual void preRenderTask() = 0;
  virtual void postRenderTask() = 0;
  
  virtual ~IPrePostRenderTasks() {
  }
  
};



#endif /* IPrePostRenderTasks_hpp */
