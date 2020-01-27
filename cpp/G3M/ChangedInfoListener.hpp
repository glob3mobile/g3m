//
//  ChangedInfoListener.hpp
//  G3M
//
//  Created by Vidal Toboso on 03/04/14.
//
//

#ifndef __G3M__ChangedInfoListener__
#define __G3M__ChangedInfoListener__

#include <vector>
class Info;

class ChangedInfoListener {
 
public:
#ifdef C_CODE
  virtual ~ChangedInfoListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
  
  virtual void changedInfo(const std::vector<const Info*>& info) = 0;
};




#endif /* defined(__G3M__ChangedInfoListener__) */
