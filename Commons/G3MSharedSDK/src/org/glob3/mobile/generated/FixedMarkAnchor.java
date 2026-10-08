package org.glob3.mobile.generated;
//
//  FixedMarkAnchor.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

//
//  FixedMarkAnchor.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//




public class FixedMarkAnchor extends MarkAnchor
{
  private final float _anchorU;
  private final float _anchorV;

  public FixedMarkAnchor(float anchorU, float anchorV)
  {
     _anchorU = anchorU;
     _anchorV = anchorV;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final Vector2F getAnchor(IImage image)
  {
    return new Vector2F(_anchorU, _anchorV);
  }

}