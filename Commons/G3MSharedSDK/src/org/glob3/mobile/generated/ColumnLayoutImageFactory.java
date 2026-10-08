package org.glob3.mobile.generated;
//
//  ColumnLayoutImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

//
//  ColumnLayoutImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//




public class ColumnLayoutImageFactory extends LayoutImageFactory
{
  private final int _childrenSeparation;
  private final HorizontalAlignment _childrenAlignment;

  public void dispose()
  {
    super.dispose();
  }

  protected final void doLayout(G3MContext context, IImageFactoryListener listener, boolean deleteListener, java.util.ArrayList<ChildResult> results)
  {
    boolean anyError = false;
    String error = "";
    // the textures are cached by image name: each alignment needs its own
    String imageName;
    switch (_childrenAlignment)
    {
      case Left:
        imageName = "ColLeft";
        break;
      case Right:
        imageName = "ColRight";
        break;
      default:
        imageName = "Col";
        break;
    }
  
    int maxWidth = 0;
    int accumulatedHeight = 0;
  
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
        accumulatedHeight += image.getHeight();
        if (image.getWidth() > maxWidth)
        {
          maxWidth = image.getWidth();
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
      final float contentHeight = accumulatedHeight + ((resultsSize - 1) * _childrenSeparation);
  
      ICanvas canvas = context.getFactory().createCanvas(false);
  
      final Vector2F contentPos = _background.initializeCanvas(canvas, contentWidth, contentHeight);
  
      float cursorTop = contentPos._y;
      for (int i = 0; i < resultsSize; i++)
      {
        ChildResult result = results.get(i);
        final IImage image = result._image;
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
  
        float left;
        switch (_childrenAlignment)
        {
          case Left:
            left = contentPos._x;
            break;
          case Right:
            left = contentPos._x + (contentWidth - imageWidth);
            break;
          default:
            left = contentPos._x + ((contentWidth - imageWidth) / 2.0f);
            break;
        }
        canvas.drawImage(image, left, cursorTop);
        cursorTop += imageHeight + _childrenSeparation;
      }
  
      canvas.createImage(new CanvasOwnerImageListenerWrapper(canvas, new ColumnLayoutImageFactory_ImageListener(imageName, listener, deleteListener), true), true);
    }
  
    for (int i = 0; i < resultsSize; i++)
    {
      ChildResult result = results.get(i);
      result.dispose();
    }
  
  }


  public ColumnLayoutImageFactory(java.util.ArrayList<IImageFactory> children, ImageBackground background, int childrenSeparation)
  {
     this(children, background, childrenSeparation, HorizontalAlignment.Center);
  }
  public ColumnLayoutImageFactory(java.util.ArrayList<IImageFactory> children, ImageBackground background)
  {
     this(children, background, 0, HorizontalAlignment.Center);
  }
  public ColumnLayoutImageFactory(java.util.ArrayList<IImageFactory> children)
  {
     this(children, null, 0, HorizontalAlignment.Center);
  }
  public ColumnLayoutImageFactory(java.util.ArrayList<IImageFactory> children, ImageBackground background, int childrenSeparation, HorizontalAlignment childrenAlignment)
  {
     super(children, background);
     _childrenSeparation = childrenSeparation;
     _childrenAlignment = childrenAlignment;
  
  }

  public ColumnLayoutImageFactory(IImageFactory child0, IImageFactory child1, ImageBackground background, int childrenSeparation)
  {
     this(child0, child1, background, childrenSeparation, HorizontalAlignment.Center);
  }
  public ColumnLayoutImageFactory(IImageFactory child0, IImageFactory child1, ImageBackground background)
  {
     this(child0, child1, background, 0, HorizontalAlignment.Center);
  }
  public ColumnLayoutImageFactory(IImageFactory child0, IImageFactory child1)
  {
     this(child0, child1, null, 0, HorizontalAlignment.Center);
  }
  public ColumnLayoutImageFactory(IImageFactory child0, IImageFactory child1, ImageBackground background, int childrenSeparation, HorizontalAlignment childrenAlignment)
  {
     super(child0, child1, background);
     _childrenSeparation = childrenSeparation;
     _childrenAlignment = childrenAlignment;
  
  }

  public ColumnLayoutImageFactory(IImageFactory child0, ImageBackground background, int childrenSeparation)
  {
     this(child0, background, childrenSeparation, HorizontalAlignment.Center);
  }
  public ColumnLayoutImageFactory(IImageFactory child0, ImageBackground background)
  {
     this(child0, background, 0, HorizontalAlignment.Center);
  }
  public ColumnLayoutImageFactory(IImageFactory child0)
  {
     this(child0, null, 0, HorizontalAlignment.Center);
  }
  public ColumnLayoutImageFactory(IImageFactory child0, ImageBackground background, int childrenSeparation, HorizontalAlignment childrenAlignment)
  {
     super(child0, background);
     _childrenSeparation = childrenSeparation;
     _childrenAlignment = childrenAlignment;
  
  }

}