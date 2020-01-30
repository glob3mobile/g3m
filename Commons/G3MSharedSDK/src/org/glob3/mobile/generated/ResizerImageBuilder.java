package org.glob3.mobile.generated;
//
//  ResizerImageBuilder.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//

//
//  ResizerImageBuilder.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//





//class ImageSizer;
//class IImage;


public class ResizerImageBuilder extends AbstractImageBuilder
{
  private IImageBuilder _imageBuilder;
  private ImageSizer _widthSizer;
  private ImageSizer _heightSizer;

  public void dispose()
  {
    if (_imageBuilder != null)
       _imageBuilder.dispose();
  
    if (_widthSizer != null)
       _widthSizer.dispose();
    if (_heightSizer != null)
       _heightSizer.dispose();
  
    super.dispose();
  }

  public ResizerImageBuilder(IImageBuilder imageBuilder, ImageSizer widthSizer, ImageSizer heightSizer)
  {
     _imageBuilder = imageBuilder;
     _widthSizer = widthSizer;
     _heightSizer = heightSizer;
    if (_imageBuilder.isMutable())
    {
      throw new RuntimeException("Mutable imageBuilder is not supported!");
    }
  }

  public final boolean isMutable()
  {
    return false;
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
    _imageBuilder.build(context, new ResizerImageBuilder_IImageBuilderListener(context, this, listener, deleteListener), true);
  }

  public final void onError(String error, IImageBuilderListener listener, boolean deleteListener)
  {
    listener.onError(error);
    if (deleteListener)
    {
      if (listener != null)
         listener.dispose();
    }
  }

  public final void imageCreated(IImage image, String imageName, G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
    final int sourceWidth = image.getWidth();
    final int sourceHeight = image.getHeight();
  
    final int resultWidth = _widthSizer.calculate();
    final int resultHeight = _heightSizer.calculate();
  
    if ((sourceWidth == resultWidth) && (sourceHeight == resultHeight))
    {
      listener.imageCreated(image, imageName);
      if (deleteListener)
      {
        if (listener != null)
           listener.dispose();
      }
    }
    else
    {
      final IStringUtils su = context.getStringUtils();
  
      final String resizedImageName = imageName + "/" + su.toString(resultWidth) + "x" + su.toString(resultHeight);
  
      ICanvas canvas = context.getFactory().createCanvas(true);
  
      canvas.initialize(resultWidth, resultHeight);
  
      final float ratioWidth = (float) resultWidth / sourceWidth;
      final float ratioHeight = (float) resultHeight / sourceHeight;
  
      final float destWidth = (ratioHeight > ratioWidth) ? resultWidth : (sourceWidth * ratioHeight);
      final float destHeight = (ratioHeight > ratioWidth) ? (sourceHeight * ratioWidth) : resultHeight;
  
      final float destLeft = (resultWidth - destWidth) / 2.0f;
      final float destTop = (resultHeight - destHeight) / 2.0f;
  
      canvas.drawImage(image, destLeft, destTop, destWidth, destHeight);
  
      canvas.createImage(new ResizerImageBuilder_ImageListener(canvas, resizedImageName, listener, deleteListener), true); // transfer canvas to be deleted AFTER the image creation
  
      if (image != null)
         image.dispose();
    }
  
  }

}
