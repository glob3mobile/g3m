//
//  InfoDisplay.hpp
//  G3M
//
//  Created by Vidal Toboso on 21/04/14.
//
//

#ifndef __G3M__InfoDisplay__
#define __G3M__InfoDisplay__

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



#endif /* defined(__G3M__InfoDisplay__) */
