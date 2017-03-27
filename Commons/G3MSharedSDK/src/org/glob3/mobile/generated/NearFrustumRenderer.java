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



//class Camera;


public abstract class NearFrustumRenderer implements Renderer
{


  public abstract void render(Camera currentCamera, G3MRenderContext rc, GLState glState);

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
