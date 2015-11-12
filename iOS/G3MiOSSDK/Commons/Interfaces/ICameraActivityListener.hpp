//
//  ICameraActivityListener.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 28/05/13.
//
//

#ifndef __G3MiOSSDK__ICameraActivityListener__
#define __G3MiOSSDK__ICameraActivityListener__


class ICameraActivityListener {
public:
#ifdef C_CODE
  virtual ~ICameraActivityListener() {
  }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
  
  virtual void touchEventHandled() = 0;
  
};


#endif
