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

  public final void prepareRender(G3MRenderContext rc)
  {
	//GL* gl = rc->getGL();
  
	int TEMP_commented_by_Agustin_until_decision_about_glstate;
	/*
	if (_specularColor == NULL) {
	  gl->disableVertexFlatColor();
	}
	else {
	  const float colorsIntensity = 1;
	  gl->enableVertexFlatColor(*_specularColor, colorsIntensity);
	}
	 */
  
	super.prepareRender(rc);
  }

  public final void cleanUpRender(G3MRenderContext rc)
  {
	//GL* gl = rc->getGL();
  
	int TEMP_commented_by_Agustin_until_decision_about_glstate;
	/*
	gl->disableVertexFlatColor();
	 */
  
	super.cleanUpRender(rc);
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