package org.glob3.mobile.generated; 
//
//  SGTranslateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGTranslateNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//



public class SGTranslateNode extends SGNode
{
  private final double _x;
  private final double _y;
  private final double _z;

  private MutableMatrix44D _translationMatrix = new MutableMatrix44D();


  public SGTranslateNode(String id, String sId, double x, double y, double z)
  {
     super(id, sId);
     _x = x;
     _y = y;
     _z = z;
     _translationMatrix = new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(_x, _y, _z));

  }

//  GLGlobalState* createState(const G3MRenderContext* rc,
//                       const GLGlobalState& parentState);
//  
//  GPUProgramState* createGPUProgramState(const G3MRenderContext* rc,
//                                         const GPUProgramState* parentState);


  //GLGlobalState* SGTranslateNode::createState(const G3MRenderContext* rc,
  //                     const GLGlobalState& parentState) {
  //  return NULL;
  //}
  //
  //GPUProgramState* SGTranslateNode::createGPUProgramState(const G3MRenderContext* rc,
  //                                               const GPUProgramState* parentState){
  //  
  //  GPUProgramState* progState = new GPUProgramState(parentState);
  //  progState->multiplyUniformValue("Modelview", MutableMatrix44D::createTranslationMatrix(_x, _y, _z));
  //  return progState;
  //  
  //}
  
  public final void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
  
  }

  public final void modifyGPUProgramState(GPUProgramState progState)
  {
    progState.multiplyUniformValue("Modelview", _translationMatrix);
  }

}