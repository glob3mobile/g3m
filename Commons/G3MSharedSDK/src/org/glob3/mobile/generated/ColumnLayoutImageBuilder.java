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
  
      final int margin2 = _margin *2;
      final int padding2 = _padding *2;
  
      final int width = maxWidth + margin2 + padding2;
      final int height = accumulatedHeight + margin2 + padding2 + (resultsSize-1)*_childrenSeparation;
  
      ICanvas canvas = context.getFactory().createCanvas();
      canvas.initialize(width, height);
  
  ///#warning remove debug code
  //    canvas->setFillColor(Color::red());
  //    canvas->fillRectangle(0, 0, width, height);
  
      if (!_backgroundColor.isFullTransparent())
      {
        canvas.setFillColor(_backgroundColor);
        if (_cornerRadius > 0)
        {
          canvas.fillRoundedRectangle(_margin, _margin, width-margin2, height-margin2, _cornerRadius);
        }
        else
        {
          canvas.fillRectangle(_margin, _margin, width-margin2, height-margin2);
        }
      }
  
      if (_borderWidth > 0 && !_borderColor.isFullTransparent())
      {
        canvas.setLineColor(_borderColor);
        canvas.setLineWidth(_borderWidth);
        if (_cornerRadius > 0)
        {
          canvas.strokeRoundedRectangle(_margin, _margin, width-margin2, height-margin2, _cornerRadius);
        }
        else
        {
          canvas.strokeRectangle(_margin, _margin, width-margin2, height-margin2);
        }
      }
  
      float cursorTop = height - _margin - _padding;
      for (int i = 0; i < resultsSize; i++)
      {
        ChildResult result = results.get(i);
        final IImage image = result._image;
  
        final float destLeft = ((float)(width - image.getWidth()) / 2.0f);
        cursorTop -= image.getHeight();
        canvas.drawImage(image, destLeft, cursorTop);
        cursorTop -= _childrenSeparation;
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
      result.dispose();
    }
  
  }


  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, int margin, float borderWidth, Color borderColor, int padding, Color backgroundColor, float cornerRadius)
  {
     this(children, margin, borderWidth, borderColor, padding, backgroundColor, cornerRadius, 0.0f);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, int margin, float borderWidth, Color borderColor, int padding, Color backgroundColor)
  {
     this(children, margin, borderWidth, borderColor, padding, backgroundColor, 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, int margin, float borderWidth, Color borderColor, int padding)
  {
     this(children, margin, borderWidth, borderColor, padding, Color.transparent(), 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, int margin, float borderWidth, Color borderColor)
  {
     this(children, margin, borderWidth, borderColor, 0, Color.transparent(), 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, int margin, float borderWidth)
  {
     this(children, margin, borderWidth, Color.transparent(), 0, Color.transparent(), 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, int margin)
  {
     this(children, margin, 0.0f, Color.transparent(), 0, Color.transparent(), 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children)
  {
     this(children, 0, 0.0f, Color.transparent(), 0, Color.transparent(), 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, int margin, float borderWidth, Color borderColor, int padding, Color backgroundColor, float cornerRadius, int childrenSeparation)
  {
     super(children, margin, borderWidth, borderColor, padding, backgroundColor, cornerRadius, childrenSeparation);
  
  }

  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, int margin, float borderWidth, Color borderColor, int padding, Color backgroundColor, float cornerRadius)
  {
     this(child0, child1, margin, borderWidth, borderColor, padding, backgroundColor, cornerRadius, 0.0f);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, int margin, float borderWidth, Color borderColor, int padding, Color backgroundColor)
  {
     this(child0, child1, margin, borderWidth, borderColor, padding, backgroundColor, 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, int margin, float borderWidth, Color borderColor, int padding)
  {
     this(child0, child1, margin, borderWidth, borderColor, padding, Color.transparent(), 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, int margin, float borderWidth, Color borderColor)
  {
     this(child0, child1, margin, borderWidth, borderColor, 0, Color.transparent(), 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, int margin, float borderWidth)
  {
     this(child0, child1, margin, borderWidth, Color.transparent(), 0, Color.transparent(), 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, int margin)
  {
     this(child0, child1, margin, 0.0f, Color.transparent(), 0, Color.transparent(), 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1)
  {
     this(child0, child1, 0, 0.0f, Color.transparent(), 0, Color.transparent(), 0.0f, 0.0f);
  }
  public ColumnLayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, int margin, float borderWidth, Color borderColor, int padding, Color backgroundColor, float cornerRadius, int childrenSeparation)
  {
     super(child0, child1, margin, borderWidth, borderColor, padding, backgroundColor, cornerRadius, childrenSeparation);
  
  }

}