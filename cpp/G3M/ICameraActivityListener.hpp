//
//  ICameraActivityListener.hpp
//  G3M
//
//  Created by Vidal Toboso on 28/05/13.
//
//

#ifndef __G3M__ICameraActivityListener__
#define __G3M__ICameraActivityListener__


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
