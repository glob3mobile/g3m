package org.glob3.mobile.generated; 
//
//  SGLayerNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/9/12.
//
//

//
//  SGLayerNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/9/12.
//
//



public class SGLayerNode extends SGNode
{
  private String _uri;
  private String _applyTo;
  private String _blendMode;
  private boolean _flipY;

  private String _magFilter;
  private String _minFilter;
  private String _wrapS;
  private String _wrapT;

  protected void prepareRender(RenderContext rc)
  {
  
	super.prepareRender(rc);
  }

  protected void cleanUpRender(RenderContext rc)
  {
  
	super.cleanUpRender(rc);
  }


  public final void setUri(String uri)
  {
	_uri = uri;
  }

  public final void setApplyTo(String applyTo)
  {
	_applyTo = applyTo;
  }

  public final void setBlendMode(String blendMode)
  {
	_blendMode = blendMode;
  }

  public final void setFlipY(boolean flipY)
  {
	_flipY = flipY;
  }

  public final void setMagFilter(String magFilter)
  {
	_magFilter = magFilter;
  }

  public final void setMinFilter(String minFilter)
  {
	_minFilter = minFilter;
  }

  public final void setWrapS(String wrapS)
  {
	_wrapS = wrapS;
  }

  public final void setWrapT(String wrapT)
  {
	_wrapT = wrapT;
  }

}