//
//  ITessellatorData.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/09/13.
//
//

#ifndef G3MiOSSDK_ITessellatorData_hpp
#define G3MiOSSDK_ITessellatorData_hpp

#ifdef C_CODE

class ITessellatorData {
public:
  virtual ~ITessellatorData() { }
};

#endif
#ifdef JAVA_CODE

public interface ITessellatorData
{
  public void dispose();
}
#endif

#endif
