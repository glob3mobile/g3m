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
  private final Color _baseColor;
  private final Color _specularColor;

  private final double _specular;
  private final double _shine;
  private final double _alpha;
  private final double _emit;


  public SGMaterialNode(String id, String sId, Color baseColor, Color specularColor, double specular, double shine, double alpha, double emit)
  {
	  super(id, sId);
	  _baseColor = baseColor;
	  _specularColor = specularColor;
	  _specular = specular;
	  _shine = shine;
	  _alpha = alpha;
	  _emit = emit;

  }

  public void dispose()
  {
	if (_baseColor != null)
		_baseColor.dispose();
	if (_specularColor != null)
		_specularColor.dispose();
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
	if (_specularColor == null)
	{
	  return null;
	}
  
	GLState state = new GLState(parentState);
	final float colorsIntensity = 1F;
	state.enableFlatColor(_specularColor, colorsIntensity);
  
	return state;
  }

}