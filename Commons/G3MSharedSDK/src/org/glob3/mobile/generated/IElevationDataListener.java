package org.glob3.mobile.generated; 
//
//  ElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  ElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//


//class Sector;
//class Vector2I;
//class ElevationData;
//class G3MContext;

public interface IElevationDataListener
{
  public void dispose();

  void onData(Sector sector, Vector2I resolution, ElevationData elevationData);

  void onError(Sector sector, Vector2I resolution);

}