package org.glob3.mobile.generated;
//
//  RowLayoutImageFactory.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/20/19.
//

//
//  RowLayoutImageFactory.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/20/19.
//




public class RowLayoutImageFactory extends LayoutImageFactory
{
  private final int _childrenSeparation;

  public void dispose()
  {
    super.dispose();
  }

  protected final void doLayout(G3MContext context, IImageFactoryListener listener, boolean deleteListener, java.util.ArrayList<ChildResult> results)
  {
    boolean anyError = false;
    String error = "";
    String imageName = "Row";
  
    // the children are measured in points, as the retina canvas below draws in points
    final float pixelRatio = context.getFactory().getDeviceInfo().getDevicePixelRatio();
  
    float maxHeight = 0F;
    float accumulatedWidth = 0F;
  
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
        accumulatedWidth += image.getWidth() / pixelRatio;
        if ((image.getHeight() / pixelRatio) > maxHeight)
        {
          maxHeight = image.getHeight() / pixelRatio;
        }
        imageName += result._imageName + "/";
      }
    }
  
    // the separation changes the pixels, so it must be part of the texture name
    imageName += "sep=" + IStringUtils.instance().toString(_childrenSeparation) + "/";
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
      final float contentWidth = accumulatedWidth + ((resultsSize - 1) * _childrenSeparation);
      final float contentHeight = maxHeight;
  
      ICanvas canvas = context.getFactory().createCanvas(true);
  
      final Vector2F contentPos = _background.initializeCanvas(canvas, contentWidth, contentHeight);
  
      float cursorLeft = contentPos._x;
      for (int i = 0; i < resultsSize; i++)
      {
        ChildResult result = results.get(i);
        final IImage image = result._image;
        final float imageWidth = image.getWidth() / pixelRatio;
        final float imageHeight = image.getHeight() / pixelRatio;
  
        final float top = contentPos._y + ((contentHeight - imageHeight) / 2.0f);
        canvas.drawImage(image, cursorLeft, top, imageWidth, imageHeight);
        cursorLeft += imageWidth + _childrenSeparation;
      }
  
      canvas.createImage(new CanvasOwnerImageListenerWrapper(canvas, new RowLayoutImageFactory_ImageListener(imageName, listener, deleteListener), true), true);
    }
  
    for (int i = 0; i < resultsSize; i++)
    {
      ChildResult result = results.get(i);
      result.dispose();
    }
  
  }


  public RowLayoutImageFactory(java.util.ArrayList<IImageFactory> children, ImageBackground background)
  {
     this(children, background, 0);
  }
  public RowLayoutImageFactory(java.util.ArrayList<IImageFactory> children)
  {
     this(children, null, 0);
  }
  public RowLayoutImageFactory(java.util.ArrayList<IImageFactory> children, ImageBackground background, int childrenSeparation)
  {
     super(children, background);
     _childrenSeparation = childrenSeparation;
  
  }

  public RowLayoutImageFactory(IImageFactory child0, IImageFactory child1, ImageBackground background)
  {
     this(child0, child1, background, 0);
  }
  public RowLayoutImageFactory(IImageFactory child0, IImageFactory child1)
  {
     this(child0, child1, null, 0);
  }
  public RowLayoutImageFactory(IImageFactory child0, IImageFactory child1, ImageBackground background, int childrenSeparation)
  {
     super(child0, child1, background);
     _childrenSeparation = childrenSeparation;
  
  }

  public RowLayoutImageFactory(IImageFactory child0, ImageBackground background)
  {
     this(child0, background, 0);
  }
  public RowLayoutImageFactory(IImageFactory child0)
  {
     this(child0, null, 0);
  }
  public RowLayoutImageFactory(IImageFactory child0, ImageBackground background, int childrenSeparation)
  {
     super(child0, background);
     _childrenSeparation = childrenSeparation;
  
  }

}