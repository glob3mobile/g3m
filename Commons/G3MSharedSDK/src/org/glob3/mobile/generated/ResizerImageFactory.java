package org.glob3.mobile.generated;
//
//  ResizerImageFactory.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//

//
//  ResizerImageFactory.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//





//class ImageSizer;
//class IImage;


public class ResizerImageFactory extends AbstractImageFactory
{
  private IImageFactory _imageFactory;
  private ImageSizer _widthSizer;
  private ImageSizer _heightSizer;

  public void dispose()
  {
    if (_imageFactory != null)
       _imageFactory.dispose();
  
    if (_widthSizer != null)
       _widthSizer.dispose();
    if (_heightSizer != null)
       _heightSizer.dispose();
  
    super.dispose();
  }

  public ResizerImageFactory(IImageFactory imageFactory, ImageSizer widthSizer, ImageSizer heightSizer)
  {
     _imageFactory = imageFactory;
     _widthSizer = widthSizer;
     _heightSizer = heightSizer;
    if (_imageFactory.isMutable())
    {
      throw new RuntimeException("Mutable imageFactory is not supported!");
    }
  }

  public final boolean isMutable()
  {
    return false;
  }

  public final void create(G3MContext context, IImageFactoryListener listener, boolean deleteListener)
  {
    _imageFactory.create(context, new ResizerImageFactory_ImageFactoryListener(context, this, listener, deleteListener), true);
  }

  public final void onError(String error, IImageFactoryListener listener, boolean deleteListener)
  {
    listener.onError(error);
    if (deleteListener)
    {
      if (listener != null)
         listener.dispose();
    }
  }

  public final void imageCreated(IImage image, String imageName, G3MContext context, IImageFactoryListener listener, boolean deleteListener)
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
  
      canvas.createImage(new CanvasOwnerImageListenerWrapper(canvas, new ResizerImageFactory_ImageListener(resizedImageName, listener, deleteListener), true), true);
  
      if (image != null)
         image.dispose();
    }
  
  }

}