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
//class StarsIntensityGLFeature;


// The stars of the Yale Bright Star Catalogue, fixed on the planet's cartesian axes,
// drawn as round discs; the light above white makes a star bigger
public class StarsRenderer extends DefaultRenderer
{
  private final float _starPointSize;
  private DirectMesh _starsMesh;
  private GLState _glState;
  private StarsIntensityGLFeature _starsIntensityGLFeature;

  private DirectMesh createStarsMesh()
  {
    final int starsCount = YaleBrightStars.starsCount();
    IFloatBuffer directions = IFactory.instance().createFloatBuffer(starsCount * 3);
    IFloatBuffer colors = IFactory.instance().createFloatBuffer(starsCount * 4);
    YaleBrightStars.putStars(directions, colors);
  
    return new DirectMesh(GLPrimitive.points(), true, Vector3D.ZERO, directions, 1, _starPointSize, null, colors, false);
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

  // starPointSize: diameter in pixels of a star as bright as white;
  // starsIntensity: multiplies the brightness of every star (1 keeps the catalogue brightness)
  public StarsRenderer(float starPointSize, float starsIntensity)
  {
     _starPointSize = starPointSize;
     _starsMesh = null;
     _glState = new GLState();
     _starsIntensityGLFeature = new StarsIntensityGLFeature(starsIntensity);
    _glState.addGLFeature(_starsIntensityGLFeature, true);
  }

  public final void setStarsIntensity(float starsIntensity)
  {
    _starsIntensityGLFeature.changeIntensity(starsIntensity);
  }

  public void dispose()
  {
    if (_starsMesh != null)
       _starsMesh.dispose();
    _starsIntensityGLFeature._release();
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