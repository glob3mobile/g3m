package org.glob3.mobile.generated; 
//
//  SGGeometryNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGGeometryNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//




//class IFloatBuffer;
//class IShortBuffer;
//class GPUProgramState;

public class SGGeometryNode extends SGNode
{
  private final int _primitive;
  private IFloatBuffer _vertices;
  private IFloatBuffer _colors;
  private IFloatBuffer _uv;
  private IFloatBuffer _normals;
  private IShortBuffer _indices;

  private GLState _glState = new GLState();

  //void SGGeometryNode::rawRender(const G3MRenderContext* rc,
  //                               const GLGlobalState& parentState, const GPUProgramState* parentProgramState) {
  ////  GL* gl = rc->getGL();
  ////  gl->drawElements(_primitive, _indices, parentState, *rc->getGPUProgramManager(), parentProgramState);
  //}
  
  private void createGLState()
  {
  
  //  GPUProgramState& progState = *_glState.getGPUProgramState();
  
  //  progState.setAttributeValue(POSITION,
  //                              _vertices, 4, //The attribute is a float vector of 4 elements
  //                              3,            //Our buffer contains elements of 3
  //                              0,            //Index 0
  //                              false,        //Not normalized
  //                              0);           //Stride 0
  
    _glState.addGLFeature(new GeometryGLFeature(_vertices, 3, 0, false, 0, true, false, 0, false, (float)0.0, (float)0.0, (float)1.0, false, (float)1.0), false); //Depth test - Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
  
    //TODO:....
    int WARNING_TODO_SG;
  
  //  if (_colors != NULL){
  ////    progState.setUniformValue(EnableColorPerVertex, true);
  //    progState.setAttributeValue(COLOR,
  //                                _colors, 4,   //The attribute is a float vector of 4 elements RGBA
  //                                4,            //Our buffer contains elements of 4
  //                                0,            //Index 0
  //                                false,        //Not normalized
  //                                0);           //Stride 0
  ////    const float colorsIntensity = 1;
  ////    progState.setUniformValue(FlatColorIntensity, colorsIntensity);
  //  }
  //
  //  if (_uv != NULL){
  //    progState.setAttributeValue(TEXTURE_COORDS,
  //                                _uv, 2,
  //                                2,
  //                                0,
  //                                false,
  //                                0);
  
  //    progState.setUniformValue(SCALE_TEXTURE_COORDS, Vector2D(1.0, 1.0));
  //    progState.setUniformValue(TRANSLATION_TEXTURE_COORDS, Vector2D(0.0, 0.0));
  //  }
  }


  public SGGeometryNode(String id, String sId, int primitive, IFloatBuffer vertices, IFloatBuffer colors, IFloatBuffer uv, IFloatBuffer normals, IShortBuffer indices)
  {
     super(id, sId);
     _primitive = primitive;
     _vertices = vertices;
     _colors = colors;
     _uv = uv;
     _normals = normals;
     _indices = indices;
    createGLState();
  }


  ///#include "GPUProgramState.hpp"
  
  
  public void dispose()
  {
    if (_vertices != null)
       _vertices.dispose();
    if (_colors != null)
       _colors.dispose();
    if (_uv != null)
       _uv.dispose();
    if (_normals != null)
       _normals.dispose();
    if (_indices != null)
       _indices.dispose();
  }


//  void rawRender(const G3MRenderContext* rc,
//                 const GLGlobalState& parentState, const GPUProgramState* parentProgramState);

  public final void rawRender(G3MRenderContext rc, GLState glState)
  {
    GL gl = rc.getGL();
    gl.drawElements(_primitive, _indices, glState, rc.getGPUProgramManager());
  }

  public final GLState getGLState(GLState parentGLState)
  {
    _glState.setParent(parentGLState);
//    _glState.getGLGlobalState()->enableDepthTest();
    return _glState;
  }

  public final String description()
  {
    return "SGGeometryNode";
  }

}