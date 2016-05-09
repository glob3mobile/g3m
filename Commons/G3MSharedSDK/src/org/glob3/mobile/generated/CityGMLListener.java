package org.glob3.mobile.generated; 
//
//  CityGMLParser.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/3/16.
//
//

//
//  CityGMLParser.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/3/16.
//
//




public abstract class CityGMLListener
{
  public void dispose()
  {
  }

  public abstract void onBuildingsCreated(java.util.ArrayList<CityGMLBuilding> buildings);

  public abstract void onError();
}