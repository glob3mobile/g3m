//
//  ChangedInfoListener.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 03/04/14.
//
//

#ifndef __G3MiOSSDK__ChangedInfoListener__
#define __G3MiOSSDK__ChangedInfoListener__

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




#endif /* defined(__G3MiOSSDK__ChangedInfoListener__) */
