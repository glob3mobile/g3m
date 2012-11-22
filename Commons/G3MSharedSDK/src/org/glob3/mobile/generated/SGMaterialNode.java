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

  private double _shine;
  private double _specular;

//  baseColor { r: 0.0, g: 0.0, b: 0.0 }
//  specularColor { r: 0.0, g: 0.0, b: 0.0 }
//
//  specular 1
//  shine 10
//  alpha 1.0
//  emit 0.0


  protected final void prepareRender(G3MRenderContext rc)
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
  
	super.prepareRender(rc);
  }

  protected final void cleanUpRender(G3MRenderContext rc)
  {
	GL gl = rc.getGL();
  
	gl.disableVertexFlatColor();
  
	super.cleanUpRender(rc);
  }


  public SGMaterialNode()
  {
	  _specularColor = null;
	  _shine = 0;
	  _specular = 0;

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

  public final void setShine(double shine)
  {
	_shine = shine;
  }

  public final void setSpecular(double specular)
  {
	_specular = specular;
  }

}