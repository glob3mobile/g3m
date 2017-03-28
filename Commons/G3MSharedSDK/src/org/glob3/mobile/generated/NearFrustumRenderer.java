package org.glob3.mobile.generated;
//
//  NearFrustumRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/03/2017.
//
//

//
//  NearFrustumRenderer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/03/2017.
//
//



//class FrustumData;
//class FrustumPolicyHandler;


public abstract class NearFrustumRenderer implements Renderer
{


  public abstract void render(G3MRenderContext rc, GLState glState);

  public abstract void render(FrustumData currentFrustumData, FrustumPolicyHandler handler, G3MRenderContext rc, GLState glState);

  public final SurfaceElevationProvider getSurfaceElevationProvider()
  {
    return null;
  }

  public final PlanetRenderer getPlanetRenderer()
  {
    return null;
  }

  public final boolean isPlanetRenderer()
  {
    return false;
  }

}
