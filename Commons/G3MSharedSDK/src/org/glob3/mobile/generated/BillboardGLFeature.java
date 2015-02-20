package org.glob3.mobile.generated; 
public class BillboardGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  public BillboardGLFeature(Vector3D position, int textureWidth, int textureHeight)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_BILLBOARD);
    _values.addUniformValue(GPUUniformKey.TEXTURE_EXTENT, new GPUUniformValueVec2Float(textureWidth, textureHeight), false);
  
    _values.addUniformValue(GPUUniformKey.BILLBOARD_POSITION, new GPUUniformValueVec4Float((float) position._x, (float) position._y, (float) position._z, 1), false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    state.disableDepthTest();
    state.disableCullFace();
    state.disablePolygonOffsetFill();
  }
}