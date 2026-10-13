package org.glob3.mobile.generated;
//
//  MarkAnchor.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

//
//  MarkAnchor.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//



//class IImage;


/** the point of a mark's image, in UV, that sits on the mark's position; known once the image exists */
public abstract class MarkAnchor
{
  public void dispose()
  {
  }

  public abstract Vector2F getAnchor(IImage image);

}