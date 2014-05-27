//
//  InfoDisplay.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 21/04/14.
//
//

#ifndef __G3MiOSSDK__InfoDisplay__
#define __G3MiOSSDK__InfoDisplay__

#include "ChangedInfoListener.hpp"

class InfoDisplay : public ChangedInfoListener {
public:
  
#ifdef C_CODE
  virtual ~InfoDisplay() { }
#endif
  
  virtual void showDisplay() = 0;
  
  virtual void hideDisplay() = 0;
  
  virtual bool isShowing() = 0;
};



#endif /* defined(__G3MiOSSDK__InfoDisplay__) */
