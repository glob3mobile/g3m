package org.glob3.mobile.generated; 
//
//  TerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

//
//  TerrainElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//


//class G3MRenderContext;
//class G3MContext;


public abstract class TerrainElevationProvider
{
  public void dispose()
  {
  }

  public abstract boolean isReadyToRender(G3MRenderContext rc);

  public abstract void initialize(G3MContext context);

}