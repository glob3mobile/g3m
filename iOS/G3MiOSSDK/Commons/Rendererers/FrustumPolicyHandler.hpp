//
//  FrustumPolicyHandler.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 28/03/2017.
//
//

#ifndef FrustumPolicyHandler_hpp
#define FrustumPolicyHandler_hpp


class FrustumPolicyHandler {

public:
#ifdef C_CODE
  virtual ~FrustumPolicyHandler() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual void changeToFixedFrustum(double zNear,
                                    double zFar) = 0;

  virtual void resetFrustumPolicy() = 0;

};

#endif
