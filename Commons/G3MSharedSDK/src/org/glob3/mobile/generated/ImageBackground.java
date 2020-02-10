package org.glob3.mobile.generated;
//
//  ImageBackground.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//

//
//  ImageBackground.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//


//class ICanvas;


public abstract class ImageBackground
{
  protected ImageBackground()
  {

  }

  public void dispose()
  {

  }

  /**
   Initialize the canvas sized to fits the content and the background.
   Answer the content position.
   */
  public abstract Vector2F initializeCanvas(ICanvas canvas, float contentWidth, float contentHeight);

  public Vector2F initializeCanvas(ICanvas canvas, Vector2F contentSize)
  {
    return initializeCanvas(canvas, contentSize._x, contentSize._y);
  }

  public abstract String description();

  public abstract ImageBackground copy();

}
