package org.glob3.mobile.generated; 
//
//  SGMaterialNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGMaterialNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//



public class SGMaterialNode extends SGNode
{
  private Color _baseColor;
  private final Color _specularColor;

//  const double _specular;
//  const double _shine;
//  const double _alpha;
//  const double _emit;


  private GLState _glState = new GLState();


  public SGMaterialNode(String id, String sId, Color baseColor, Color specularColor, double specular, double shine, double alpha, double emit)
//  _specular(specular),
//  _shine(shine),
//  _alpha(alpha),
//  _emit(emit)
  {
     super(id, sId);
     _baseColor = baseColor;
     _specularColor = specularColor;
    _glState.addGLFeatureAndRelease(new FlatColorGLFeature(_baseColor, false, 0, 0));
  }

  public final GLState getGLState(GLState parentGLState)
  {
    _glState.setParent(parentGLState);
    return _glState;
  }

  public final void setBaseColor(Color baseColor)
  {
    if (baseColor != _baseColor)
    {
      _baseColor = null;
      _baseColor = baseColor;
    }
  }

  public void dispose()
  {
    _baseColor = null;
    if (_specularColor != null)
       _specularColor.dispose();
  }

//  void modifyGLGlobalState(GLGlobalState& GLGlobalState) const;
//  void modifyGPUProgramState(GPUProgramState& progState) const;

  public final String description()
  {
    return "SGMaterialNode";
  }

}
///#include "GPUProgramState.hpp"

//GLGlobalState* SGMaterialNode::createState(const G3MRenderContext* rc,
//                                     const GLGlobalState& parentState) {
//  return NULL;
//}
//
//GPUProgramState* SGMaterialNode::createGPUProgramState(const G3MRenderContext* rc,
//                                                       const GPUProgramState* parentState){
//
//
//  //  GPUProgramState* progState = new GPUProgramState(parentState);
//
//  //  if (_baseColor != NULL){
//  //    progState->setUniformValue(EnableFlatColor, true);
//  //    progState->setUniformValue(FLAT_COLOR,
//  //                               (double)_baseColor->getRed(),
//  //                               (double)_baseColor->getBlue(),
//  //                               (double) _baseColor->getGreen(),
//  //                               (double) _baseColor->getAlpha());
//  //    const float colorsIntensity = 1;
//  //    progState->setUniformValue(FlatColorIntensity, colorsIntensity);
//  //  }
//  //
//  //  return progState;
//}


//void SGMaterialNode::modifyGLGlobalState(GLGlobalState& GLGlobalState) const{
//
//}
//void SGMaterialNode::modifyGPUProgramState(GPUProgramState& progState) const{
//
//  if (_baseColor != NULL){
//    //    progState.setUniformValue(EnableFlatColor, true);
//    progState.setUniformValue(FLAT_COLOR, _baseColor->getRed(),
//                              _baseColor->getGreen(),
//                              _baseColor->getBlue(),
//                              _baseColor->getAlpha());
//    //    const float colorsIntensity = 1;
//    //    progState.setUniformValue(FlatColorIntensity, colorsIntensity);
//  }
//}
