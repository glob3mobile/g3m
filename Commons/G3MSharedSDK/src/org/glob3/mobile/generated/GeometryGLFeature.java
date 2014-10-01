package org.glob3.mobile.generated; 
public class GeometryGLFeature extends GLFeature
{
  //Position + cull + depth + polygonoffset + linewidth
  private GPUAttributeValueVec4Float _position;

  private final boolean _depthTestEnabled;
  private final boolean _cullFace;
  private final int _culledFace;
  private final boolean _polygonOffsetFill;
  private final float _polygonOffsetFactor;
  private final float _polygonOffsetUnits;
  private final float _lineWidth;

  public void dispose()
  {
    //  _position->_release();
  
    super.dispose();
  }

  private GPUUniformValueFloatMutable _pointSizeUniformValue;



  public GeometryGLFeature(IFloatBuffer buffer, int arrayElementSize, int index, boolean normalized, int stride, boolean depthTestEnabled, boolean cullFace, int culledFace, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, float lineWidth, float pointSize)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_GEOMETRY);
     _depthTestEnabled = depthTestEnabled;
     _cullFace = cullFace;
     _culledFace = culledFace;
     _polygonOffsetFill = polygonOffsetFill;
     _polygonOffsetFactor = polygonOffsetFactor;
     _polygonOffsetUnits = polygonOffsetUnits;
     _lineWidth = lineWidth;
     _pointSizeUniformValue = null;
  
    _position = new GPUAttributeValueVec4Float(buffer, arrayElementSize, index, stride, normalized);
    _values.addAttributeValue(GPUAttributeKey.POSITION, _position, false);
  
    if (pointSize > 0.0)
    {
      _pointSizeUniformValue = new GPUUniformValueFloatMutable(pointSize);
      _values.addUniformValue(GPUUniformKey.POINT_SIZE, _pointSizeUniformValue, false);
    }
  }


  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    if (_depthTestEnabled)
    {
      state.enableDepthTest();
    }
    else
    {
      state.disableDepthTest();
    }
  
    if (_cullFace)
    {
      state.enableCullFace(_culledFace);
    }
    else
    {
      state.disableCullFace();
    }
  
    if (_polygonOffsetFill)
    {
      state.enablePolygonOffsetFill(_polygonOffsetFactor, _polygonOffsetUnits);
    }
    else
    {
      state.disPolygonOffsetFill();
    }
  
    state.setLineWidth(_lineWidth);
  }

  public final void setPointSize(float v)
  {
  
    if (v < 0.0)
    {
      v = 0.0F;
    }
  
    if (_pointSizeUniformValue == null)
    {
      _pointSizeUniformValue = new GPUUniformValueFloatMutable(v);
      _values.addUniformValue(GPUUniformKey.POINT_SIZE, _pointSizeUniformValue, false);
    }
    else
    {
      _pointSizeUniformValue.changeValue(v);
    }
  }

}