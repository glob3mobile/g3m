package org.glob3.mobile.generated;
//
//  SGMaterialNode.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGMaterialNode.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//



//class Color;


public class SGMaterialNode extends SGNode
{
  private Color _baseColor;
  private final Color _specularColor;

  //  const double _specular;
  //  const double _shine;
  //  const double _alpha;
  //  const double _emit;

  private GLState _glState;


  public SGMaterialNode(String id, String sID, Color baseColor, Color specularColor, double specular, double shine, double alpha, double emit)
  //  _specular(specular),
  //  _shine(shine),
  //  _alpha(alpha),
  //  _emit(emit)
  {
     super(id, sID);
     _baseColor = baseColor;
     _specularColor = specularColor;
     _glState = new GLState();
    _glState.addGLFeature(new FlatColorGLFeature(_baseColor, false, 0, 0), false);
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
    _glState.setParent(parentState);
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
  
    _glState._release();
    super.dispose();
  }

  public final String description()
  {
    return "SGMaterialNode";
  }

}