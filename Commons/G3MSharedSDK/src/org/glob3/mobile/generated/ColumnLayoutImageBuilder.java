package org.glob3.mobile.generated;
//
//  ColumnLayoutImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

//
//  ColumnLayoutImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//




public class ColumnLayoutImageBuilder extends LayoutImageBuilder
{
  private final int _childrenSeparation;

  protected final void doLayout(G3MContext context, IImageBuilderListener listener, boolean deleteListener, java.util.ArrayList<ChildResult> results)
  {
    boolean anyError = false;
    String error = "";
    String imageName = "Col";
  
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
  
  //    const Vector2I canvasSize = _background->getCanvasSize(contentWidth,
  //                                                           contentHeight);
  //
  //    ICanvas* canvas = context->getFactory()->createCanvas(false);
  //    canvas->initialize(canvasSize._x, canvasSize._y);
  //
  //    _background->drawOn(canvas, canvasSize);
  
  //    const Vector2I canvasSize = _background->getCanvasSize(contentWidth,
  //                                                           contentHeight);
  
      ICanvas canvas = context.getFactory().createCanvas(false);
  //    canvas->initialize(canvasSize._x, canvasSize._y);
  
  
  //    const Vector2F contentPos = _background->getContentPosition();
  
      final Vector2F contentPos = _background.initializeCanvas(canvas, contentWidth, contentHeight);
  
      float cursorTop = contentPos._y;
      for (int i = 0; i < resultsSize; i++)
      {
        ChildResult result = results.get(i);
        final IImage image = result._image;
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
  
        final float left = contentPos._x + ((contentWidth - imageWidth) / 2.0f);
        canvas.drawImage(image, left, cursorTop);
        cursorTop += imageHeight + _childrenSeparation;
      }
  
      canvas.createImage(new ColumnLayoutImageBuilder_IImageListener(imageName, listener, deleteListener), true);
      if (canvas != null)
         canvas.dispose();
    }
  
    for (int i = 0; i < resultsSize; i++)
    {
      ChildResult result = results.get(i);
      result.dispose();
    }
  
  }


  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, ImageBackground background)
  {
     this(children, background, 0);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children)
  {
     this(children, null, 0);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, ImageBackground background, int childrenSeparation)
  {
     super(children, background);
     _childrenSeparation = childrenSeparation;
  
  }

  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, ImageBackground background)
  {
     this(child0, child1, background, 0);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1)
  {
     this(child0, child1, null, 0);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, ImageBackground background, int childrenSeparation)
  {
     super(child0, child1, background);
     _childrenSeparation = childrenSeparation;
  
  }

  public ColumnLayoutImageBuilder(IImageBuilder child0, ImageBackground background)
  {
     this(child0, background, 0);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0)
  {
     this(child0, null, 0);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, ImageBackground background, int childrenSeparation)
  {
     super(child0, background);
     _childrenSeparation = childrenSeparation;
  
  }

}
