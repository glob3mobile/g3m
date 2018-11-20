package org.glob3.mobile.generated;
//
//  FrustumPolicyHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 28/03/2017.
//
//

//
//  FrustumPolicyHandler.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 28/03/2017.
//
//



public interface FrustumPolicyHandler
{

  void dispose();

  void changeToFixedFrustum(double zNear, double zFar);

  void resetFrustumPolicy();

}
