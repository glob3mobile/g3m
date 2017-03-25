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


//class MeshRenderer;


public class NearFrustumRenderer extends DefaultRenderer
{
  private MeshRenderer _mr;

  public NearFrustumRenderer()
  {
     _mr = new MeshRenderer();
  }

  public void dispose()
  {
    super.dispose();
    if (_mr != null)
       _mr.dispose();
  }

  public final void onChangedContext()
  {
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    return RenderState.ready();
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
  
    final Camera cam = rc.getCurrentCamera();
    CoordinateSystem camCS = cam.getCameraCoordinateSystem();
  
    final Vector3D up = rc.getPlanet().geodeticSurfaceNormal(cam.getGeodeticPosition());
    final Vector3D right = camCS._y.cross(up);
    final Vector3D front = up.cross(right);
  
    final Vector3D controllerDisp = front.scaleToLength(0.7).sub(up.scaleToLength(0.5)).add(right.scaleToLength(0.1));
  
    final Vector3D origin = cam.getCartesianPosition().add(controllerDisp);
  
    final CoordinateSystem camCS2 = camCS.changeOrigin(origin);
  
    Mesh mesh = camCS2.createMesh(1000000, Color.RED, Color.GREEN, Color.BLUE);
  
    _mr.clearMeshes();
    _mr.addMesh(mesh);
    _mr.render(rc, glState);
  
    ILogger.instance().logInfo("Frustum %f - %f. Model at: %f meters.", cam.getFrustumData()._zNear, cam.getFrustumData()._zFar, camCS._origin.distanceTo(origin));
  
  }

}
