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

  private GLState _glState = new GLState();


  public SGTranslateNode(String id, String sId, double x, double y, double z)
  {
     super(id, sId);
     _x = x;
     _y = y;
     _z = z;
     _translationMatrix = new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(_x, _y, _z));
    //_glState.getGPUProgramState()->setUniformMatrixValue(MODELVIEW, _translationMatrix, true);
    _glState.setModelView(_translationMatrix.asMatrix44D(), true);
  }

  public final GLState getGLState(GLState parentGLState)
  {
    _glState.setParent(parentGLState);
    return _glState;
  }

}