package org.glob3.mobile.generated; 
public class GLFeatureLightingGroup extends GLFeatureGroup
{
//  void applyOnGlobalGLState(GLGlobalState* state);
//  void addToGPUVariableSet(GPUVariableValueSet* vs);
  public final void apply(GLFeatureSet features, GPUVariableValueSet vs, GLGlobalState state)
  {
    boolean normalsAvailable = false;
    for(int i = 0; i < features.size(); i++)
    {
      final GLFeature f = features.get(i);
      if (f.getID() == GLFeatureID.GLF_VERTEX_NORMAL)
      {
        normalsAvailable = true;
        break;
      }
    }
  
  
    if (normalsAvailable)
    {
  
      int modelTransformCount = 0;
  
      for(int i = 0; i < features.size(); i++)
      {
        final GLFeature f = features.get(i);
  
        if (f.getID() == GLFeatureID.GLF_MODEL_TRANSFORM)
        {
          modelTransformCount++;
        }
  
        if (f.getGroup() == GLFeatureGroupName.LIGHTING_GROUP)
        {
          f.applyOnGlobalGLState(state);
          vs.combineWith(f.getGPUVariableValueSet());
        }
      }
  
      /////////////////////////////////////////////////////////////////////////////////////////////
      Matrix44DProvider[] modelTransformHolders = new Matrix44DProvider[size];
  
      modelTransformCount = 0;
      for (int i = 0; i < features.size(); i++)
      {
        final GLFeature f = features.get(i);
        if (f.getID() == GLFeatureID.GLF_MODEL_TRANSFORM)
        {
          GLCameraGroupFeature cf = ((GLCameraGroupFeature) f);
          final Matrix44D m = cf.getMatrixHolder().getMatrix();
  
          if (!m.isScaleMatrix() && !m.isTranslationMatrix())
          {
            modelTransformHolders[modelTransformCount++] = cf.getMatrixHolder();
          }
        }
  
      }
  
      Matrix44DProvider modelProvider = null;
      if (modelTransformCount > 0)
      {
        modelProvider = new Matrix44DMultiplicationHolder(modelTransformHolders, modelTransformCount);
  
        vs.addUniformValue(GPUUniformKey.MODEL, new GPUUniformValueMatrix4(modelProvider, true), false);
      }
  
      modelTransformHolders = null;
  
  
    }
  }
}