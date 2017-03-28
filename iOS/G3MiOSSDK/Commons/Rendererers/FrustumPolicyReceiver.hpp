//
//  FrustumPolicyReceiver.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 28/03/2017.
//
//

#ifndef FrustumPolicyReceiver_hpp
#define FrustumPolicyReceiver_hpp

class Renderer;
class Info;
class FrustumPolicy;

#include <vector>

class FrustumPolicyReceiver {
  
public:
#ifdef C_CODE
  virtual ~FrustumPolicyReceiver() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
  
  virtual void changeToFixedFrustum(double zNear, double zFar) = 0;
  virtual void resetFrustumPolicy() = 0;
  
};

#endif /* FrustumPolicyReceiver_hpp */
