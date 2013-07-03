package org.glob3.mobile.generated; 
//
//  AbstractMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

//
//  AbstractMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//




//class MutableMatrix44D;
//class IFloatBuffer;
//class Color;

public abstract class AbstractMesh extends Mesh
{
  protected final int _primitive;
  protected final boolean _owner;
  protected Vector3D _center ;
  protected final MutableMatrix44D _translationMatrix;
  protected IFloatBuffer _vertices;
  protected Color _flatColor;
  protected IFloatBuffer _colors;
  protected final float _colorsIntensity;
  protected final float _lineWidth;
  protected final float _pointSize;
  protected final boolean _depthTest;

  protected Extent _extent;
  protected final Extent computeExtent()
  {
    final int vertexCount = getVertexCount();
  
    if (vertexCount <= 0)
    {
      return null;
    }
  
    double minX = 1e12;
    double minY = 1e12;
    double minZ = 1e12;
  
    double maxX = -1e12;
    double maxY = -1e12;
    double maxZ = -1e12;
  
    for (int i = 0; i < vertexCount; i++)
    {
      final int i3 = i * 3;
  
      final double x = _vertices.get(i3) + _center._x;
      final double y = _vertices.get(i3 + 1) + _center._y;
      final double z = _vertices.get(i3 + 2) + _center._z;
  
      if (x < minX)
         minX = x;
      if (x > maxX)
         maxX = x;
  
      if (y < minY)
         minY = y;
      if (y > maxY)
         maxY = y;
  
      if (z < minZ)
         minZ = z;
      if (z > maxZ)
         maxZ = z;
    }
  
    return new Box(new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ));
  }

  protected AbstractMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest)
  {
     _primitive = primitive;
     _owner = owner;
     _vertices = vertices;
     _flatColor = flatColor;
     _colors = colors;
     _colorsIntensity = colorsIntensity;
     _extent = null;
     _center = new Vector3D(center);
     _translationMatrix = (center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(center));
     _lineWidth = lineWidth;
     _pointSize = pointSize;
     _depthTest = depthTest;
    createGLState();
  }

  protected abstract void rawRender(G3MRenderContext rc);
//  virtual void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const = 0;

  protected GLState _glState = new GLState();

  protected final void createGLState()
  {
  
    GLGlobalState globalState = _glState.getGLGlobalState();
  
    globalState.setLineWidth(_lineWidth);
    if (_depthTest)
    {
      globalState.enableDepthTest();
    }
    else
    {
      globalState.disableDepthTest();
    }
  
    if (_flatColor != null && _flatColor.isTransparent())
    {
      globalState.enableBlend();
      globalState.setBlendFactors(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
    }
  
    GPUProgramState progState = _glState.getGPUProgramState();
  
    if (_flatColor != null && _colors == null) //FlatColorMesh Shader
    {
      progState.setAttributeValue(GPUAttributeKey.POSITION, _vertices, 4, 3, 0, false, 0); //Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
      progState.setUniformValue(GPUUniformKey.FLAT_COLOR, (double)_flatColor.getRed(), (double)_flatColor.getGreen(), (double) _flatColor.getBlue(), (double) _flatColor.getAlpha());
      if (_translationMatrix != null)
      {
        //progState.setUniformMatrixValue(MODELVIEW, *_translationMatrix, true);
        _glState.setModelView(_translationMatrix.asMatrix44D(), true);
      }
      return;
    }
  
  
    progState.setUniformValue(GPUUniformKey.POINT_SIZE, _pointSize);
  
    progState.setAttributeValue(GPUAttributeKey.POSITION, _vertices, 4, 3, 0, false, 0); //Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
  
    if (_colors != null)
    {
  //    progState.setUniformValue(EnableColorPerVertex, true);
      progState.setAttributeValue(GPUAttributeKey.COLOR, _colors, 4, 4, 0, false, 0); //Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 4 - The attribute is a float vector of 4 elements RGBA
  
  //    progState.setUniformValue(ColorPerVertexIntensity, _colorsIntensity);
    }
    else
    {
  //    progState.setAttributeDisabled(COLOR);
  //    progState.setUniformValue(EnableColorPerVertex, false);
  //    progState.setUniformValue(ColorPerVertexIntensity, (float)0.0);
    }
  
  //  if (_flatColor != NULL){
  //    progState.setUniformValue(EnableFlatColor, true);
  //    progState.setUniformValue(FLAT_COLOR,
  //                              (double)_flatColor->getRed(),
  //                              (double)_flatColor->getGreen(),
  //                              (double) _flatColor->getBlue(),
  //                              (double) _flatColor->getAlpha());
  //
  //    progState.setUniformValue(FlatColorIntensity, _colorsIntensity);
  //  } else{
  //    progState.setUniformValue(EnableFlatColor, false);
  //    progState.setUniformValue(ColorPerVertexIntensity, (float)0.0);
  //    progState.setUniformValue(FLAT_COLOR, (float)0.0, (float)0.0, (float)0.0, (float)0.0);
  //    progState.setUniformValue(FlatColorIntensity, (float)0.0);
  //  }
  
    if (_translationMatrix != null)
    {
      //progState.setUniformMatrixValue(MODELVIEW, *_translationMatrix, true);
      _glState.setModelView(_translationMatrix.asMatrix44D(), true);
    }
  }

  public void dispose()
  {
    if (_owner)
    {
      if (_vertices != null)
         _vertices.dispose();
      if (_colors != null)
         _colors.dispose();
      if (_flatColor != null)
         _flatColor.dispose();
    }
  
    if (_extent != null)
       _extent.dispose();
    if (_translationMatrix != null)
       _translationMatrix.dispose();
  }

  public final void render(G3MRenderContext rc)
  {
    rawRender(rc);
  }

  public final Extent getExtent()
  {
    if (_extent == null)
    {
      _extent = computeExtent();
    }
    return _extent;
  }

  public final int getVertexCount()
  {
    return _vertices.size() / 3;
  }

  public final Vector3D getVertex(int i)
  {
    final int p = i * 3;
    return new Vector3D(_vertices.get(p) + _center._x, _vertices.get(p+1) + _center._y, _vertices.get(p+2) + _center._z);
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    if (_flatColor == null)
    {
      return false;
    }
    return _flatColor.isTransparent();
  }

  public final void render(G3MRenderContext rc, GLState parentGLState)
  {
  
    _glState.setParent(parentGLState);
    rawRender(rc);
  }

}