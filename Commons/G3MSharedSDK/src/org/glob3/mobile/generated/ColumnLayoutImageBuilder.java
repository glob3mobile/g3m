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
  protected final void doLayout(G3MContext context, IImageBuilderListener listener, boolean deleteListener, java.util.ArrayList<ChildResult> results)
  {
    boolean anyError = false;
    String error = "";
    String imageName = "";
  
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
      ICanvas canvas = context.getFactory().createCanvas();
  
      final int width = maxWidth;
      final int height = accumulatedHeight;
      canvas.initialize(width, height);
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning remove debug code
      canvas.setFillColor(Color.red());
      canvas.fillRectangle(0, 0, width, height);
  
      float cursorTop = height;
      for (int i = 0; i < resultsSize; i++)
      {
        ChildResult result = results.get(i);
        final IImage image = result._image;
  
        final float destLeft = ((float)(width - image.getWidth()) / 2.0f);
        cursorTop -= image.getHeight();
        canvas.drawImage(image, destLeft, cursorTop);
      }
  
      canvas.createImage(new ColumnLayoutImageBuilder_IImageListener(imageName, listener, deleteListener), true);
      if (canvas != null)
         canvas.dispose();
    }
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning delete images??
    for (int i = 0; i < resultsSize; i++)
    {
      ChildResult result = results.get(i);
      result = null;
    }
  
  }


  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, int margin, int borderWidth, Color borderColor)
  {
     this(children, margin, borderWidth, borderColor, 2);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, int margin, int borderWidth)
  {
     this(children, margin, borderWidth, Color.transparent(), 2);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, int margin)
  {
     this(children, margin, 0, Color.transparent(), 2);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children)
  {
     this(children, 4, 0, Color.transparent(), 2);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, int margin, int borderWidth, Color borderColor, int padding)
  {
     super(children, margin, borderWidth, borderColor, padding);
  
  }

  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, int margin, int borderWidth, Color borderColor)
  {
     this(child0, child1, margin, borderWidth, borderColor, 2);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, int margin, int borderWidth)
  {
     this(child0, child1, margin, borderWidth, Color.transparent(), 2);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, int margin)
  {
     this(child0, child1, margin, 0, Color.transparent(), 2);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1)
  {
     this(child0, child1, 4, 0, Color.transparent(), 2);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, int margin, int borderWidth, Color borderColor, int padding)
  {
     super(child0, child1, margin, borderWidth, borderColor, padding);
  
  }

}