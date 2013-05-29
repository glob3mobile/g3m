package org.glob3.mobile.generated; 
//
//  SGRotateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGRotateNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//




public class SGRotateNode extends SGNode
{
  private final double _x;
  private final double _y;
  private final double _z;
  private final double _angle;

  private MutableMatrix44D _rotationMatrix = new MutableMatrix44D();

  public SGRotateNode(String id, String sId, double x, double y, double z, double angle)
  {
     super(id, sId);
     _x = x;
     _y = y;
     _z = z;
     _angle = angle;
     _rotationMatrix = new MutableMatrix44D(MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_angle), new Vector3D(_x, _y, _z)));
  }

//  GLGlobalState* createState(const G3MRenderContext* rc,
//                       const GLGlobalState& parentState);
//  
//  GPUProgramState* createGPUProgramState(const G3MRenderContext* rc,
//                                         const GPUProgramState* parentState);


  //GLGlobalState* SGRotateNode::createState(const G3MRenderContext* rc,
  //                     const GLGlobalState& parentState) {
  //  return NULL;
  //}
  //
  //GPUProgramState* SGRotateNode::createGPUProgramState(const G3MRenderContext* rc,
  //                                                        const GPUProgramState* parentState){
  //  
  //  GPUProgramState* progState = new GPUProgramState(parentState);
  //  progState->multiplyUniformValue("Modelview", MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_angle),
  //                                                                                      Vector3D(_x, _y, _z)));
  //  return progState;
  //  
  //}
  
  public final void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
  
  }
  public final void modifyGPUProgramState(GPUProgramState progState)
  {
    progState.multiplyUniformValue("Modelview", _rotationMatrix);
  }

}