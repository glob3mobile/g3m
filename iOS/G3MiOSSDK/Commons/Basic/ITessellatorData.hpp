//
//  ITessellatorData.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/09/13.
//
//

#ifndef G3MiOSSDK_ITessellatorData_hpp
#define G3MiOSSDK_ITessellatorData_hpp

class ITessellatorData {
public:

#ifdef C_CODE
  virtual ~ITessellatorData() { }
#endif
#ifdef JAVA_CODE
  public void dispose();
#endif
};

#endif

