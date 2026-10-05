package org.glob3.mobile.generated;
//
//  StackLayoutImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

//
//  StackLayoutImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//



public class StackLayoutImageFactory extends LayoutImageFactory
{
  public void dispose()
  {
    super.dispose();
  }

  protected final void doLayout(G3MContext context, IImageFactoryListener listener, boolean deleteListener, java.util.ArrayList<ChildResult> results)
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
  
      canvas.createImage(new CanvasOwnerImageListenerWrapper(canvas, new StackLayoutImageFactory_ImageListener(imageName, listener, deleteListener), true), true);
    }
  
    for (int i = 0; i < resultsSize; i++)
    {
      ChildResult result = results.get(i);
      result.dispose();
    }
  
  }


  public StackLayoutImageFactory(java.util.ArrayList<IImageFactory> children)
  {
     this(children, null);
  }
  public StackLayoutImageFactory(java.util.ArrayList<IImageFactory> children, ImageBackground background)
  {
     super(children, background);
  
  }

  public StackLayoutImageFactory(IImageFactory child0, IImageFactory child1)
  {
     this(child0, child1, null);
  }
  public StackLayoutImageFactory(IImageFactory child0, IImageFactory child1, ImageBackground background)
  {
     super(child0, child1, background);
  
  }

}