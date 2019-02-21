package org.glob3.mobile.generated;
//
//  FrameImageBackground.cpp
//  G3MiOSSDK
//
//  Created by Nico on 21/02/2019.
//

//
//  FrameImageBackground.hpp
//  G3MiOSSDK
//
//  Created by Nico on 21/02/2019.
//





public class FrameImageBackground extends ImageBackground
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  FrameImageBackground(FrameImageBackground that);

  private final float _topFrameHeight;
  private final float _bottomFrameHeight;
  private final float _leftFrameWidth;
  private final float _rightFrameWidth;

  private final Color _color ;


  ///#include "Vector2I.hpp"
  
  
  public FrameImageBackground(float topFrameHeight, float bottomFrameHeight, float leftFrameWidth, float rightFrameWidth, Color color)
  {
     _topFrameHeight = topFrameHeight;
     _bottomFrameHeight = bottomFrameHeight;
     _leftFrameWidth = leftFrameWidth;
     _rightFrameWidth = rightFrameWidth;
     _color = new Color(color);
  
  }

  public final Vector2F initializeCanvas(ICanvas canvas, float contentWidth, float contentHeight)
  {
  
  
    final IMathUtils mu = IMathUtils.instance();
  
    final float canvasWidth = contentWidth + _leftFrameWidth + _rightFrameWidth;
    final float canvasHeight = contentHeight + _topFrameHeight + _bottomFrameHeight;
  
    canvas.initialize((int) mu.ceil(canvasWidth), (int) mu.ceil(canvasHeight));
  
  
    if (!_color.isTransparent())
    {
      canvas.setFillColor(_color);
      if (_topFrameHeight > 0)
      {
        canvas.fillRectangle(0, 0, canvasWidth, _topFrameHeight);
      }
      if (_bottomFrameHeight > 0)
      {
        canvas.fillRectangle(0, canvasHeight - _bottomFrameHeight, canvasWidth, _bottomFrameHeight);
      }
  
      if (_leftFrameWidth > 0)
      {
        canvas.fillRectangle(0, _topFrameHeight, _leftFrameWidth, canvasHeight - _topFrameHeight - _bottomFrameHeight);
  
      }
      if (_rightFrameWidth > 0)
      {
        canvas.fillRectangle(canvasWidth - _rightFrameWidth, _topFrameHeight, _rightFrameWidth, canvasHeight - _topFrameHeight - _bottomFrameHeight);
      }
    }
  
  
    final Vector2F contentPosition = new Vector2F(_leftFrameWidth, _topFrameHeight);
    return contentPosition;
  }

  public final String description()
  {
    final IStringUtils su = IStringUtils.instance();
    return ("Frame/" + su.toString(_topFrameHeight) + "/" + su.toString(_bottomFrameHeight) + "/" + su.toString(_leftFrameWidth) + "/" + su.toString(_rightFrameWidth) + "/" + _color.id());
  }

  public final FrameImageBackground copy()
  {
    return new FrameImageBackground(_topFrameHeight, _bottomFrameHeight, _leftFrameWidth, _rightFrameWidth, _color);
  
  }

}
