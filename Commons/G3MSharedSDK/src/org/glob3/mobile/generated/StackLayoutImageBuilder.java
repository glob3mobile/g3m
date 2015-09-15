package org.glob3.mobile.generated; 
//
//  StackLayoutImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

//
//  StackLayoutImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//



public class StackLayoutImageBuilder extends LayoutImageBuilder
{
  protected final void doLayout(G3MContext context, IImageBuilderListener listener, boolean deleteListener, java.util.ArrayList<ChildResult> results)
  {
    boolean anyError = false;
    String error = "";
    String imageName = "Stack";
  
    int maxWidth = 0;
    int maxHeight = 0;
  
    final int resultsSize = results.size();
    for (int i = 0; i < resultsSize; i++)
    {
      ChildResult result = results.get(i);
      final IImage image = result._image;
  
      if (image == null)
      {
        anyError = true;
        error += result._error + " ";
      }
      else
      {
        if (image.getWidth() > maxWidth)
        {
          maxWidth = image.getWidth();
        }
        if (image.getHeight() > maxHeight)
        {
          maxHeight = image.getHeight();
        }
        imageName += result._imageName + "/";
      }
    }
  
    if (anyError)
    {
      if (listener != null)
      {
        listener.onError(error);
        if (deleteListener)
        {
          if (listener != null)
             listener.dispose();
        }
      }
    }
    else
    {
      final int canvasWidth = maxWidth;
      final int canvasHeight = maxHeight;
  
      ICanvas canvas = context.getFactory().createCanvas();
      canvas.initialize(canvasWidth, canvasHeight);
  
      ///#warning remove debug code
      //    canvas->setFillColor(Color::red());
      //    canvas->fillRectangle(0, 0, width, height);
  
      for (int i = 0; i < resultsSize; i++)
      {
        ChildResult result = results.get(i);
        final IImage image = result._image;
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
  
        final float top = ((float)(canvasHeight - imageHeight) / 2.0f);
        final float left = ((float)(canvasWidth - imageWidth) / 2.0f);
        canvas.drawImage(image, left, top);
      }
  
      canvas.createImage(new StackLayoutImageBuilder_IImageListener(imageName, listener, deleteListener), true);
      if (canvas != null)
         canvas.dispose();
    }
  
    for (int i = 0; i < resultsSize; i++)
    {
      ChildResult result = results.get(i);
      result.dispose();
    }
  
  }


  public StackLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children)
  {
     super(children, 0, 0, Color.transparent(), 0, Color.transparent(), 0, 0);
  
  }

  public StackLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1)
  {
     super(child0, child1, 0, 0, Color.transparent(), 0, Color.transparent(), 0, 0);
  
  }

}