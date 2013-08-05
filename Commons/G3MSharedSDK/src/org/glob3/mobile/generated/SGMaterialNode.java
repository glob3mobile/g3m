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


  public SGMaterialNode(String id, String sId, Color baseColor, Color specularColor, double specular, double shine, double alpha, double emit)
//  _specular(specular),
//  _shine(shine),
//  _alpha(alpha),
//  _emit(emit)
  {
     super(id, sId);
     _baseColor = baseColor;
     _specularColor = specularColor;

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

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
    if (_baseColor == null)
    {
      return null;
    }
  
    GLState state = new GLState(parentState);
    final float colorsIntensity = 1F;
    state.enableFlatColor(_baseColor, colorsIntensity);
  
    return state;
  }

}