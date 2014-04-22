//
//  InfoDisplay.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 21/04/14.
//
//

#ifndef __G3MiOSSDK__InfoDisplay__
#define __G3MiOSSDK__InfoDisplay__

#include "ChangedRendererInfoListener.hpp"

class InfoDisplay : public ChangedRendererInfoListener {
  
public:
  
#ifdef C_CODE
  virtual ~InfoDisplay() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
  
  virtual void showDisplay() = 0;
  
  virtual void hideDisplay() = 0;
  
  virtual bool isShowing() = 0;
};



#endif /* defined(__G3MiOSSDK__InfoDisplay__) */
