package org.glob3.mobile.generated;//
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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ICanvas;


public class DefaultChessCanvasImageBuilder extends CanvasImageBuilder
{

  private final Color _backgroundColor = new Color();
  private final Color _boxColor = new Color();
  private final int _splits;

  protected final void buildOnCanvas(G3MContext context, ICanvas canvas)
  {
	final float width = canvas.getWidth();
	final float height = canvas.getHeight();
  
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setFillColor(_backgroundColor);
	canvas.setFillColor(new Color(_backgroundColor));
	canvas.fillRectangle(0, 0, width, height);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setFillColor(_boxColor);
	canvas.setFillColor(new Color(_boxColor));
  
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getImageName(const G3MContext* context) const
  protected final String getImageName(G3MContext context)
  {
	final IStringUtils su = context.getStringUtils();
  
	return "_DefaultChessCanvasImage_" + su.toString(_width) + "_" + su.toString(_height) + "_" + _backgroundColor.toID() + "_" + _boxColor.toID() + "_" + su.toString(_splits);
  }

  public DefaultChessCanvasImageBuilder(int width, int height, Color backgroundColor, Color boxColor, int splits)
  {
	  super(width, height, false);
	  _backgroundColor = new Color(backgroundColor);
	  _boxColor = new Color(boxColor);
	  _splits = splits;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isMutable() const
  public final boolean isMutable()
  {
	return false;
  }
}
