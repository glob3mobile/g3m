package org.glob3.mobile.generated; 
//
//  DefaultChessCanvasImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 21/08/14.
//
//

//
//  DefaultChessCanvasImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 21/08/14.
//
//


//class Color;
//class G3MContext;
//class ICanvas;


public class DefaultChessCanvasImageBuilder extends CanvasImageBuilder
{

  private final Color _backgroundColor ;
  private final Color _boxColor ;
  private final int _splits;

  protected final void buildOnCanvas(G3MContext context, ICanvas canvas)
  {
    final float width = canvas.getWidth();
    final float height = canvas.getHeight();
  
  
    canvas.setFillColor(_backgroundColor);
    canvas.fillRectangle(0, 0, width, height);
  
    canvas.setFillColor(_boxColor);
  
    final float xInterval = (float) width / _splits;
    final float yInterval = (float) height / _splits;
  
    for (int col = 0; col < _splits; col += 2)
    {
      final float x = col * xInterval;
      final float x2 = (col + 1) * xInterval;
      for (int row = 0; row < _splits; row += 2)
      {
        final float y = row * yInterval;
        final float y2 = (row + 1) * yInterval;
  
        canvas.fillRoundedRectangle(x + 2, y + 2, xInterval - 4, yInterval - 4, 4);
        canvas.fillRoundedRectangle(x2 + 2, y2 + 2, xInterval - 4, yInterval - 4, 4);
      }
    }
  }

  protected final String getImageName(G3MContext context)
  {
    final IStringUtils su = context.getStringUtils();
  
    return "_DefaultChessCanvasImage_" + su.toString(_width) + "_" + su.toString(_height) + "_" + _backgroundColor.toID() + "_" + _boxColor.toID() + "_" + su.toString(_splits);
  }

  public DefaultChessCanvasImageBuilder(int width, int height, Color backgroundColor, Color boxColor, int splits)
  {
     super(width, height);
     _backgroundColor = new Color(backgroundColor);
     _boxColor = new Color(boxColor);
     _splits = splits;
  }

  public final boolean isMutable()
  {
    return false;
  }
}