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



//class RenderState;
//class G3MContext;


public abstract class TerrainElevationProvider extends RCObject
{
  public void dispose()
  {
    super.dispose();
  }


  public abstract RenderState getRenderState();

  public abstract void initialize(G3MContext context);

}