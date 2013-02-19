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

public abstract class IElevationDataListener
{
  public void dispose()
  {
  }

  public abstract void onData(Sector sector, Vector2I resolution, ElevationData elevationData);

  public abstract void onError(Sector sector, Vector2I resolution);

}