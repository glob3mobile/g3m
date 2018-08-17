package org.glob3.mobile.generated;import java.util.*;

///////////////////////////////////////////////////////////////////////////////////////////



public class Geometry2DGLFeature extends GLFeature
{
  //Position + cull + depth + polygonoffset + linewidth
  private GPUAttributeValueVec2Float _position;

  private final float _lineWidth;

  public void dispose()
  {
	//  _position->_release();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  private GPUUniformValueVec2FloatMutable _translation;



  ///////////////////////////////
  public Geometry2DGLFeature(IFloatBuffer buffer, int arrayElementSize, int index, boolean normalized, int stride, float lineWidth, boolean needsPointSize, float pointSize, Vector2F translation)
  {
	  super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_GEOMETRY);
	  _lineWidth = lineWidth;
	_position = new GPUAttributeValueVec2Float(buffer, arrayElementSize, index, stride, normalized);
	_values.addAttributeValue(GPUAttributeKey.POSITION_2D, _position, false);
  
	_translation = new GPUUniformValueVec2FloatMutable(translation._x, translation._y);
	_values.addUniformValue(GPUUniformKey.TRANSLATION_2D, _translation, false);
  
	if (needsPointSize)
	{
	  _values.addUniformValue(GPUUniformKey.POINT_SIZE, new GPUUniformValueFloat(pointSize), false);
	}
  }

  public final void setTranslation(float x, float y)
  {
	_translation.changeValue(x, y);
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
	state.enableCullFace(GLCullFace.back());
	state.setLineWidth(_lineWidth);
  }

}
