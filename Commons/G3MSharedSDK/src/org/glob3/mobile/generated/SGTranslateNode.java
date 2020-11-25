package org.glob3.mobile.generated;
//
//  SGTranslateNode.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGTranslateNode.hpp
//  G3M
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

  private GLState _glState;


  public SGTranslateNode(String id, String sID, double x, double y, double z)
  {
     super(id, sID);
     _x = x;
     _y = y;
     _z = z;
     _translationMatrix = new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(_x, _y, _z));
     _glState = new GLState();
    _glState.addGLFeature(new ModelTransformGLFeature(_translationMatrix.asMatrix44D()), false);
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
    _glState.setParent(parentState);
    return _glState;
  }

  public final String description()
  {
    return "SGTranslateNode";
  }

  public void dispose()
  {
    _glState._release();
    super.dispose();
  }

}