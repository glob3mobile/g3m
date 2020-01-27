package org.glob3.mobile.generated;
//
//  NullImageBackground.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//

//
//  NullImageBackground.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//




public class NullImageBackground extends ImageBackground
{

  public final Vector2F initializeCanvas(ICanvas canvas, float contentWidth, float contentHeight)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    canvas.initialize((int) mu.ceil(contentWidth), (int) mu.ceil(contentHeight));
  
    return Vector2F.ZERO;
  }

  public final String description()
  {
    return "NULL";
  }

  public final NullImageBackground copy()
  {
    return new NullImageBackground();
  }

}
