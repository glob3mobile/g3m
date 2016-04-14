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




//class MarksRenderer;
//class MeshRenderer;

public interface CityGMLListener
{
  void onBuildingsCreated(java.util.ArrayList<CityGMLBuilding> buildings);

  void onError();
}