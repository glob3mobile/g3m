package org.glob3.mobile.generated;
//
//  StarsRenderer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/7/26.
//

//
//  StarsRenderer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/7/26.
//



//class DirectMesh;
//class GLState;
//class Camera;
//class StarsGLFeature;


// The stars of the Yale Bright Star Catalogue, fixed on the planet's cartesian axes, drawn as round discs
// from their real light (as Stellarium): the faint stars fade at the smallest diameter, the bright ones grow
public class StarsRenderer extends DefaultRenderer
{
  private final float _smallestStarDiameter;
  private DirectMesh _starsMesh;
  private GLState _glState;
  private StarsGLFeature _starsGLFeature;

  private DirectMesh createStarsMesh()
  {
    final int starsCount = YaleBrightStars.starsCount();
    IFloatBuffer directions = IFactory.instance().createFloatBuffer(starsCount * 3);
    IFloatBuffer colors = IFactory.instance().createFloatBuffer(starsCount * 4);
    YaleBrightStars.putStars(directions, colors);
  
    return new DirectMesh(GLPrimitive.points(), true, Vector3D.ZERO, directions, 1, _smallestStarDiameter, null, colors, false);
  }

  private void updateGLState(Camera camera)
  {
    ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glState.addGLFeature(new ModelViewGLFeature(camera), true);
    }
    else
    {
      f.setMatrix(camera.getModelViewMatrix44D());
    }
  
    // The stars are infinitely far: they are drawn on a sphere centred on the camera,
    // at a distance the frustum never clips (between zNear and zFar)
    final FrustumData frustumData = camera.getFrustumData();
    final double distance = IMathUtils.instance().sqrt(frustumData._zNear * frustumData._zFar);
    final MutableMatrix44D aroundCamera = MutableMatrix44D.createTranslationMatrix(camera.getCartesianPosition()).multiply(MutableMatrix44D.createScaleMatrix(distance, distance, distance));
    _starsMesh.setUserTransformMatrix(new MutableMatrix44D(aroundCamera));
  }

  // smallestStarDiameter: in pixels, the diameter of the faint stars;
  // fullStarMagnitude: a star of this magnitude is drawn opaque at the smallest diameter (fainter ones fade);
  // starSizeExponent: the diameter of the brighter stars grows as their light to this power
  public StarsRenderer(float smallestStarDiameter, float fullStarMagnitude, float starSizeExponent)
  {
     _smallestStarDiameter = smallestStarDiameter;
     _starsMesh = null;
     _glState = new GLState();
     _starsGLFeature = new StarsGLFeature(fullStarMagnitude, starSizeExponent);
    _glState.addGLFeature(_starsGLFeature, true);
  }

  public final void setFullStarMagnitude(float fullStarMagnitude)
  {
    _starsGLFeature.changeFullStarMagnitude(fullStarMagnitude);
  }

  public void dispose()
  {
    if (_starsMesh != null)
       _starsMesh.dispose();
    _starsGLFeature._release();
    _glState._release();
    super.dispose();
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    if (_starsMesh == null)
    {
      _starsMesh = createStarsMesh();
    }
  
    updateGLState(rc.getCurrentCamera());
    _glState.setParent(glState);
  
    _starsMesh.render(rc, _glState);
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }

}