package org.glob3.mobile.generated;
//
//  AtmosphereRenderer.cpp
//  G3M
//
//  Created by Jose Miguel SN on 07/10/2016.
//
//

//
//  AtmosphereRenderer.hpp
//  G3M
//
//  Created by Jose Miguel SN on 07/10/2016.
//
//




//class DirectMesh;
//class IFloatBuffer;
//class CameraPositionGLFeature;
//class Camera;
//class Color;
//class ColorLook;
//class Vector3F;
//class Matrix44D;

public class AtmosphereRenderer extends DefaultRenderer
{
  private final boolean _groundHazePass;
  private GLState _glState;
  private DirectMesh _directMesh;
  private IFloatBuffer _vertices;
  private CameraPositionGLFeature _camPosGLF;
  private ColorLook _colorLook;

  private void updateGLState(Camera camera, Color spaceColor)
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
  
    // The quad is placed relative to the camera, so the shader gets small numbers
    // and the ray directions keep their float precision at low altitude
    updateVerticesOfZNearPlaneRelativeToCamera(camera);
    _directMesh.setUserTransformMatrix(new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(camera.getCartesianPosition())));
  
    //CamPos
    _camPosGLF.update(camera, spaceColor);
  }

  private void updateVerticesOfZNearPlaneRelativeToCamera(Camera camera)
  {
    final FrustumData frustumData = camera.getFrustumData();
  
    final Vector3D viewDirection = camera.getViewDirection().normalized();
    final Vector3D center = viewDirection.times(frustumData._zNear);
    final Vector3D up = camera.getUp().normalized().times(frustumData._top * 2.0);
    final Vector3D right = viewDirection.cross(up).normalized().times(frustumData._right * 2.0);
  
    _vertices.putVector3D(0, center.sub(up).sub(right));
    _vertices.putVector3D(1, center.add(up).sub(right));
    _vertices.putVector3D(2, center.sub(up).add(right));
    _vertices.putVector3D(3, center.add(right).add(up));
  }

  private AtmosphereRenderer(boolean groundHazePass)
  {
     _groundHazePass = groundHazePass;
     _glState = null;
     _directMesh = null;
     _vertices = null;
     _camPosGLF = null;
     _colorLook = ColorLook.neutral();
  }

  // the horizon of Google Earth seen from the ground
  private static Color defaultHorizonColor()
  {
    return Color.fromRGBA255(201, 227, 242, 255);
  }

  // Rayleigh scattering at sea level for red, green and blue (680, 550, 440 nm), in 1e-3 / km
  private static Vector3F defaultSkyRayleighScattering()
  {
    return new Vector3F(5.802e-3f, 13.558e-3f, 33.1e-3f);
  }

  private static Color gradedHorizonColor(Matrix44D colorMatrix, Color horizonColor)
  {
    final IMathUtils mu = IMathUtils.instance();
    // as the tile shader: colorMatrix * (r, g, b, 1)
    final double red = (colorMatrix._m00 * horizonColor._red) + (colorMatrix._m01 * horizonColor._green) + (colorMatrix._m02 * horizonColor._blue) + colorMatrix._m03;
    final double green = (colorMatrix._m10 * horizonColor._red) + (colorMatrix._m11 * horizonColor._green) + (colorMatrix._m12 * horizonColor._blue) + colorMatrix._m13;
    final double blue = (colorMatrix._m20 * horizonColor._red) + (colorMatrix._m21 * horizonColor._green) + (colorMatrix._m22 * horizonColor._blue) + colorMatrix._m23;
    // the screen clamps to 1; the shader divides by it, so at least one 8-bit level
    final double minimum = 1.0 / 255.0;
    return Color.fromRGBA((float) mu.clamp(red, minimum, 1.0), (float) mu.clamp(green, minimum, 1.0), (float) mu.clamp(blue, minimum, 1.0), 1.0f);
  }

  private void applyColorLook()
  {
    if (_camPosGLF == null)
    {
      // start() applies it
      return;
    }
  
    final Color horizonColor = defaultHorizonColor();
    final Vector3F skyRayleighScattering = defaultSkyRayleighScattering();
    if (_colorLook.isEquals(ColorLook.neutral()))
    {
      _camPosGLF.changeAtmosphereColors(horizonColor, skyRayleighScattering);
      return;
    }
  
    // the haze is one flat colour blended over the graded tiles: grading that colour is exact
    Matrix44D colorMatrix = _colorLook.createColorMatrix();
    final Color gradedHorizon = gradedHorizonColor(colorMatrix, horizonColor);
    colorMatrix._release();
  
    // The sky light is not linear in its colours, so only the saturation goes into the air (hue, contrast,
    // brightness and tint reach the sky through the horizon colour it tends to): red and blue scatter more
    // like green as the saturation falls. Towards green, not the mean: the sky alpha is the transmittance
    // of green, so the halo keeps its opacity
    final IMathUtils mu = IMathUtils.instance();
    final float saturation = (float) _colorLook._saturation;
    final float green = skyRayleighScattering._y;
    final Vector3F gradedSkyRayleighScattering = new Vector3F(mu.max(0.0f, green + ((skyRayleighScattering._x - green) * saturation)), green, mu.max(0.0f, green + ((skyRayleighScattering._z - green) * saturation)));
  
    _camPosGLF.changeAtmosphereColors(gradedHorizon, gradedSkyRayleighScattering);
  }

  // the sky and the space, drawn before the PlanetRenderer
  public static AtmosphereRenderer createSky()
  {
    return new AtmosphereRenderer(false);
  }

  // the air in front of the ground, drawn after the PlanetRenderer
  public static AtmosphereRenderer createGroundHaze()
  {
    return new AtmosphereRenderer(true);
  }

  public void dispose()
  {
    if (_colorLook != null)
       _colorLook.dispose();
    if (_directMesh != null)
       _directMesh.dispose();
    _glState._release();
  }

  public final void start(G3MRenderContext rc)
  {
    if (_glState == null)
    {
      _glState = new GLState();
  
      FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
      fbb.add(0.0, 0.0, 0.0);
      fbb.add(0.0, 0.0, 0.0);
      fbb.add(0.0, 0.0, 0.0);
      fbb.add(0.0, 0.0, 0.0);
  
      _vertices = fbb.create();
  
      _directMesh = new DirectMesh(GLPrimitive.triangleStrip(), true, fbb.getCenter(), _vertices, 10.0f, 10.0f, null, null, false);
  
      if (fbb != null)
         fbb.dispose();
      fbb = null;
  
      //CamPos
      // the background colour is the colour of space, seen through the air
      _camPosGLF = new CameraPositionGLFeature(rc.getCurrentCamera(), _groundHazePass, rc.getWidget().getBackgroundColor(), defaultHorizonColor(), defaultSkyRayleighScattering());
      _glState.addGLFeature(_camPosGLF, false);
  
      applyColorLook();
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
  
    final Sector rSector = rc.getWidget().getPlanetRenderer().getRenderedSector();
    if (rc.getPlanet().getType().compareTo("Flat") == 0 || (rSector != null && !rSector.fullContains(Sector.fullSphere())))
    {
      return;
    }
  
    updateGLState(rc.getCurrentCamera(), rc.getWidget().getBackgroundColor());
    _glState.setParent(glState);
  
    _directMesh.render(rc, _glState);
  }

  // Grades the air like the tiles graded with the same look (PlanetRenderer::setColorMatrix):
  // the haze exactly, the sky approximately (its light is not linear in its colours)
  public final void setColorLook(ColorLook look)
  {
    if (_colorLook != null)
       _colorLook.dispose();
    _colorLook = look;
  
    applyColorLook();
  }

}