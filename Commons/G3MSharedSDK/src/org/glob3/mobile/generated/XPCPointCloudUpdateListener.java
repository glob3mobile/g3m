package org.glob3.mobile.generated;
//
//  XPCPointCloudUpdateListener.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 3/13/21.
//

//
//  XPCPointCloudUpdateListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 3/13/21.
//




public abstract class XPCPointCloudUpdateListener
{

  public void dispose()
  {

  }

  public abstract void onPointCloudUpdateSuccess(long updatedPoints);

  public abstract void onPointCloudUpdateFail(String errorMessage);

}