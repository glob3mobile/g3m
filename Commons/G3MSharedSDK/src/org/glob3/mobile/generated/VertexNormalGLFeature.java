package org.glob3.mobile.generated; 
public class VertexNormalGLFeature extends GLFeature
{
  public VertexNormalGLFeature(IFloatBuffer buffer, int arrayElementSize, int index, boolean normalized, int stride)
  {
     super(GLFeatureGroupName.LIGHTING_GROUP);
    _values.addAttributeValue(GPUAttributeKey.NORMAL, new GPUAttributeValueVec3Float(buffer, arrayElementSize, index, stride, normalized), false);
  
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}