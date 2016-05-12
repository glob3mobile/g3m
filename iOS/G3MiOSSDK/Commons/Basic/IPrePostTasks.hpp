//
//  IPrePostTasks.hpp
//  G3MiOSSDK
//
//  Created by Premecz Matyas on 12/05/16.
//
//

#ifndef IPrePostTasks_hpp
#define IPrePostTasks_hpp

#include <stdio.h>


class IPrePostTasks {
public:

  virtual void preRenderTask() = 0;
  virtual void postRenderTask() = 0;
  
  virtual ~IPrePostTasks() {
  }
  
};



#endif /* IPrePostTasks_hpp */
