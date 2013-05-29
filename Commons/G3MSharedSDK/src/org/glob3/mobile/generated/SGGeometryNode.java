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

  private GLGlobalState _GLGlobalState = new GLGlobalState();
  private GPUProgramState _programState = new GPUProgramState();


  public SGGeometryNode(String id, String sId, int primitive, IFloatBuffer vertices, IFloatBuffer colors, IFloatBuffer uv, IFloatBuffer normals, IShortBuffer indices)
  {
     super(id, sId);
     _primitive = primitive;
     _vertices = vertices;
     _colors = colors;
     _uv = uv;
     _normals = normals;
     _indices = indices;

  }

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



  //GPUProgramState * SGGeometryNode::createGPUProgramState(const G3MRenderContext *rc, const GPUProgramState *parentState){
  //  GPUProgramState* progState = new GPUProgramState(parentState);
  //  
  //  progState->setAttributeEnabled("Position", true);
  //  progState->setAttributeValue("Position",
  //                               _vertices, 4, //The attribute is a float vector of 4 elements
  //                               3,            //Our buffer contains elements of 3
  //                               0,            //Index 0
  //                               false,        //Not normalized
  //                               0);           //Stride 0
  //  
  //  if (_colors != NULL){
  //    progState->setAttributeEnabled("Color", true);
  //    progState->setUniformValue("EnableColorPerVertex", true);
  //    progState->setAttributeValue("Color",
  //                                 _colors, 4,   //The attribute is a float vector of 4 elements RGBA
  //                                 4,            //Our buffer contains elements of 4
  //                                 0,            //Index 0
  //                                 false,        //Not normalized
  //                                 0);           //Stride 0
  //    const float colorsIntensity = 1;
  //    progState->setUniformValue("FlatColorIntensity", colorsIntensity);
  //  } else{
  //    progState->setAttributeEnabled("Color", false);
  //    progState->setUniformValue("EnableColorPerVertex", false);
  //  }
  //  
  //  if (_uv != NULL){
  //    progState->setAttributeValue("TextureCoord",
  //                                 _uv, 2,
  //                                 2,
  //                                 0,
  //                                 false,
  //                                 0);
  //    
  //    progState->setUniformValue("ScaleTexCoord", Vector2D(1.0, 1.0));
  //    progState->setUniformValue("TranslationTexCoord", Vector2D(0.0, 0.0));
  //  }
  //  
  //  return progState;
  //}
  
  public final void rawRender(G3MRenderContext rc, GLGlobalState parentState, GPUProgramState parentProgramState)
  {
    GL gl = rc.getGL();
    gl.drawElements(_primitive, _indices, parentState, rc.getGPUProgramManager(), parentProgramState);
  }

//  GLGlobalState* createState(const G3MRenderContext* rc,
//                             const GLGlobalState& parentState) {
//    return NULL;
//  }
//  
//  GPUProgramState * createGPUProgramState(const G3MRenderContext *rc, const GPUProgramState *parentState);

  //Idle if this is not a drawable client
//  void getGLGlobalStateAndGPUProgramState(GLGlobalState** GLGlobalState, GPUProgramState** progState){
//    _programState.clear();
////    (*GLGlobalState) = &_GLGlobalState;
////    (*progState) = &_programState;
//  }
  public final GLGlobalState getGLGlobalState()
  {
    return _GLGlobalState;
  }
  public final GPUProgramState getGPUProgramState()
  {
    _programState.clear();
    return _programState;
  }

  public final void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
  
  }
  public final void modifyGPUProgramState(GPUProgramState progState)
  {
  
    progState.setAttributeEnabled("Position", true);
    progState.setAttributeValue("Position", _vertices, 4, 3, 0, false, 0); //Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
  
    if (_colors != null)
    {
      progState.setAttributeEnabled("Color", true);
      progState.setUniformValue("EnableColorPerVertex", true);
      progState.setAttributeValue("Color", _colors, 4, 4, 0, false, 0); //Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 4 - The attribute is a float vector of 4 elements RGBA
      final float colorsIntensity = 1F;
      progState.setUniformValue("FlatColorIntensity", colorsIntensity);
    }
    else
    {
      progState.setAttributeEnabled("Color", false);
      progState.setUniformValue("EnableColorPerVertex", false);
    }
  
    if (_uv != null)
    {
      progState.setAttributeValue("TextureCoord", _uv, 2, 2, 0, false, 0);
  
      progState.setUniformValue("ScaleTexCoord", new Vector2D(1.0, 1.0));
      progState.setUniformValue("TranslationTexCoord", new Vector2D(0.0, 0.0));
    }
  }

}