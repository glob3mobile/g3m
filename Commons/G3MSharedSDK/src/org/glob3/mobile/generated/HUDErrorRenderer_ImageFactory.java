package org.glob3.mobile.generated; 
//
//  HUDErrorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

//
//  HUDErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//




public class HUDErrorRenderer_ImageFactory extends HUDImageRenderer.ImageFactory
{
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  public final void create(G3MRenderContext rc, int width, int height, IImageListener listener, boolean deleteListener)
  {
    int _DGD_AtWork;
  }

  public void dispose()
  {

  }

  public final void setErrors(java.util.ArrayList<String> errors)
  {
    _errors.clear();
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
    _errors.insert(_errors.end(), errors.iterator(), errors.end());
  }
}