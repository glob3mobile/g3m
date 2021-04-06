package org.glob3.mobile.generated;
//
//  XPCFixedAlphaPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 2/10/21.
//

//
//  XPCFixedAlphaPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 2/10/21.
//




public abstract class XPCFixedAlphaPointColorizer extends XPCPointColorizer
{
  protected final float _alpha;


  protected XPCFixedAlphaPointColorizer(float alpha)
  {
     _alpha = alpha;
  
  }

  public void dispose()
  {
    super.dispose();
  }


  public final float getAlpha()
  {
    return _alpha;
  }

}