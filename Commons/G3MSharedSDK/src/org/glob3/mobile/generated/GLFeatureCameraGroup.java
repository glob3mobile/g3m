package org.glob3.mobile.generated;import java.util.*;

public class GLFeatureCameraGroup extends GLFeatureGroup
{
  public final void apply(GLFeatureSet features, tangible.RefObject<GPUVariableValueSet> vs, tangible.RefObject<GLGlobalState> state)
  {
	  Matrix44DMultiplicationHolderBuilder modelViewHolderBuilder = new Matrix44DMultiplicationHolderBuilder(); //Model + View + Projection
	  Matrix44DMultiplicationHolderBuilder modelTransformHolderBuilder = new Matrix44DMultiplicationHolderBuilder(); //Model
	  Matrix44DMultiplicationHolderBuilder modelHolderBuilder = new Matrix44DMultiplicationHolderBuilder(); //View
  
	  boolean modelRequired = false;
  
	  final int featuresSize = features.size();
	  for (int i = 0; i < featuresSize; i++)
	  {
		  final GLFeature f = features.get(i);
		  final GLFeatureGroupName group = f._group;
		  final GLFeatureID id = f._id;
		  if (group == GLFeatureGroupName.CAMERA_GROUP)
		  {
			  GLCameraGroupFeature cf = ((GLCameraGroupFeature) f);
  
			  switch (id)
			  {
				  case GLF_MODEL_TRANSFORM:
					  modelTransformHolderBuilder.add(cf.getMatrixHolder());
					  break;
				  case GLF_MODEL:
					  modelHolderBuilder.add(cf.getMatrixHolder());
					  break;
				  case GLF_MODEL_VIEW:
				  case GLF_PROJECTION:
					  modelViewHolderBuilder.add(cf.getMatrixHolder());
					  break;
				  default:
					  ILogger.instance().logError("Problem while applying GLFeatureCameraGroup.");
					  break;
			  }
  
			  //      if (id == GLF_MODEL_TRANSFORM) {
			  //        modelTransformHolderBuilder.add(cf->getMatrixHolder());
			  //      }
			  //      else {
			  //        modelViewHolderBuilder.add(cf->getMatrixHolder());
			  //      }
		  }
		  else
		  {
			  if (group == GLFeatureGroupName.LIGHTING_GROUP)
			  {
				  if (id == GLFeatureID.GLF_VERTEX_NORMAL)
				  {
					  modelRequired = true;
				  }
			  }
  
			  if (id == GLFeatureID.GLF_TRANSPARENCY_DISTANCE_THRESHOLD)
			  {
				  modelRequired = true;
			  }
		  }
	  }
  
	  if (modelTransformHolderBuilder.size() > 0)
	  {
		  Matrix44DProvider prov = modelTransformHolderBuilder.create();
		  modelViewHolderBuilder.add(prov);
		  modelHolderBuilder.add(prov);
		  prov._release();
	  }
  
	  if (modelRequired && modelHolderBuilder.size() > 0)
	  {
		  Matrix44DProvider prov = modelHolderBuilder.create();
		  vs.argvalue.addUniformValue(GPUUniformKey.MODEL, new GPUUniformValueMatrix4(prov), false); //FOR LIGHTING
	  }
  
	  if (modelViewHolderBuilder.size() > 0)
	  {
		  Matrix44DProvider modelViewProvider = modelViewHolderBuilder.create();
  
		  vs.argvalue.addUniformValue(GPUUniformKey.MODELVIEW, new GPUUniformValueMatrix4(modelViewProvider), false);
  
		  modelViewProvider._release();
	  }
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  modelViewHolderBuilder.dispose();
	  modelTransformHolderBuilder.dispose();
//#endif
  }
}
