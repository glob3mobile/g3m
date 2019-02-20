package org.glob3.mobile.generated;
//
//  RowLayoutImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/20/19.
//

//
//  RowLayoutImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/20/19.
//




public class RowLayoutImageBuilder extends LayoutImageBuilder
{
  private final int _childrenSeparation;

  protected final void doLayout(G3MContext context, IImageBuilderListener listener, boolean deleteListener, java.util.ArrayList<ChildResult> results)
  {
    boolean anyError = false;
    String error = "";
    String imageName = "Row";
  
    int maxHeight = 0;
    int accumulatedWidth = 0;
  
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
        accumulatedWidth += image.getWidth();
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
      final float contentWidth = accumulatedWidth + ((resultsSize - 1) * _childrenSeparation);
      final float contentHeight = maxHeight;
  
      ICanvas canvas = context.getFactory().createCanvas(false);
  
      final Vector2F contentPos = _background.initializeCanvas(canvas, contentWidth, contentHeight);
  
      float cursorLeft = contentPos._x;
      for (int i = 0; i < resultsSize; i++)
      {
        ChildResult result = results.get(i);
        final IImage image = result._image;
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
  
        final float top = contentPos._y + ((contentHeight - imageHeight) / 2.0f);
        canvas.drawImage(image, cursorLeft, top);
        cursorLeft += imageWidth + _childrenSeparation;
      }
  
      canvas.createImage(new RowLayoutImageBuilder_IImageListener(imageName, listener, deleteListener), true);
      if (canvas != null)
         canvas.dispose();
    }
  
    for (int i = 0; i < resultsSize; i++)
    {
      ChildResult result = results.get(i);
      result.dispose();
    }
  
  }


  public RowLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, ImageBackground background)
  {
     this(children, background, 0);
  }
  public RowLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children)
  {
     this(children, null, 0);
  }
  public RowLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, ImageBackground background, int childrenSeparation)
  {
     super(children, background);
     _childrenSeparation = childrenSeparation;
  
  }

  public RowLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, ImageBackground background)
  {
     this(child0, child1, background, 0);
  }
  public RowLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1)
  {
     this(child0, child1, null, 0);
  }
  public RowLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, ImageBackground background, int childrenSeparation)
  {
     super(child0, child1, background);
     _childrenSeparation = childrenSeparation;
  
  }

  public RowLayoutImageBuilder(IImageBuilder child0, ImageBackground background)
  {
     this(child0, background, 0);
  }
  public RowLayoutImageBuilder(IImageBuilder child0)
  {
     this(child0, null, 0);
  }
  public RowLayoutImageBuilder(IImageBuilder child0, ImageBackground background, int childrenSeparation)
  {
     super(child0, background);
     _childrenSeparation = childrenSeparation;
  
  }

}
