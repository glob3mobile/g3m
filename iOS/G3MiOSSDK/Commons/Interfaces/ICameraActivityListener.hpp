//
//  ICameraActivityListener.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 28/05/13.
//
//

#ifndef __G3MiOSSDK__ICameraActivityListener__
#define __G3MiOSSDK__ICameraActivityListener__

#include "Disposable.hpp"


class ICameraActivityListener : public Disposable {
public:
  virtual ~ICameraActivityListener() {
    JAVA_POST_DISPOSE
  }
  
  virtual void touchEventHandled() = 0;
  
};


#endif
