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
  private Color _specularColor;

  protected final void prepareRender(RenderContext rc)
  {
	GL gl = rc.getGL();
  
	if (_specularColor == null)
	{
	  gl.disableVertexFlatColor();
	}
	else
	{
	  final float colorsIntensity = 1F;
	  gl.enableVertexFlatColor(_specularColor, colorsIntensity);
	}
  
  }

  protected final void cleanUpRender(RenderContext rc)
  {
	GL gl = rc.getGL();
  
	gl.disableVertexFlatColor();
  }


  public SGMaterialNode()
  {
	  _specularColor = null;

  }

  public void dispose()
  {
	_specularColor = null;
  }

  public final void setSpecularColor(Color color)
  {
	_specularColor = null;
	_specularColor = color;
  }
}