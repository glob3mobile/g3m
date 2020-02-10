package org.glob3.mobile.generated;
//
//  StackLayoutImageBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

//
//  StackLayoutImageBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//



public class StackLayoutImageBuilder extends LayoutImageBuilder
{
  public void dispose()
  {
    super.dispose();
  }

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
  
    imageName += _background.description();
  
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
      final float contentWidth = maxWidth;
      final float contentHeight = maxHeight;
  
      ICanvas canvas = context.getFactory().createCanvas(false);
      final Vector2F contentPos = _background.initializeCanvas(canvas, contentWidth, contentHeight);
  
      for (int i = 0; i < resultsSize; i++)
      {
        ChildResult result = results.get(i);
        final IImage image = result._image;
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
  
        final float top = contentPos._y + ((contentHeight - imageHeight) / 2.0f);
        final float left = contentPos._x + ((contentWidth - imageWidth) / 2.0f);
        canvas.drawImage(image, left, top);
      }
  
      canvas.createImage(new CanvasOwnerImageListenerWrapper(canvas, new StackLayoutImageBuilder_ImageListener(imageName, listener, deleteListener), true), true);
    }
  
    for (int i = 0; i < resultsSize; i++)
    {
      ChildResult result = results.get(i);
      result.dispose();
    }
  
  }


  public StackLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children)
  {
     this(children, null);
  }
  public StackLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, ImageBackground background)
  {
     super(children, background);
  
  }

  public StackLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1)
  {
     this(child0, child1, null);
  }
  public StackLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, ImageBackground background)
  {
     super(child0, child1, background);
  
  }

}
