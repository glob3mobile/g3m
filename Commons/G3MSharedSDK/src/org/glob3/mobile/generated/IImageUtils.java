package org.glob3.mobile.generated; 
//
//  IImageUtils.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//

//
//  IImageUtils.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//



//class RectangleF;
//class IImage;
//class IImageListener;
//class Vector2I;

public class IImageUtils
{
  private IImageUtils()
  {

  }

  private static void createShallowCopy(IImage image, IImageListener listener, boolean autodelete)
  {
    listener.imageCreated(image.shallowCopy());
    if (autodelete)
    {
      if (listener != null)
         listener.dispose();
    }
  }


  public static void scale(int width, int height, IImage image, IImageListener listener, boolean autodelete)
  {
    if (width == image.getWidth() && height == image.getHeight())
    {
      createShallowCopy(image, listener, autodelete);
    }
    else
    {
      ICanvas canvas = IFactory.instance().createCanvas();
      canvas.initialize(width, height);
  
      canvas.drawImage(image, 0, 0, width, height);
  
      canvas.createImage(listener, autodelete);
      if (canvas != null)
         canvas.dispose();
    }
  }

  public static void scale(Vector2I extent, IImage image, IImageListener listener, boolean autodelete)
  {
    scale(extent._x, extent._y, image, listener, autodelete);
  }


  public static void subImage(IImage image, RectangleF rect, IImageListener listener, boolean autodelete)
  {
  
    if (rect._x == 0 && rect._y == 0 && rect._width == image.getWidth() && rect._height == image.getHeight())
    {
      createShallowCopy(image, listener, autodelete);
    }
    else
    {
      ICanvas canvas = IFactory.instance().createCanvas();
  
      final IMathUtils mu = IMathUtils.instance();
      canvas.initialize(mu.round(rect._width), mu.round(rect._height));
  
      canvas.drawImage(image, rect._x, rect._y, rect._width, rect._height, 0, 0, rect._width, rect._height);
  
      canvas.createImage(listener, autodelete);
      if (canvas != null)
         canvas.dispose();
    }
  }


  public static void combine(int width, int height, java.util.ArrayList<IImage> images, java.util.ArrayList<RectangleF> sourceRects, java.util.ArrayList<RectangleF> destRects, java.util.ArrayList<Float> transparencies, IImageListener listener, boolean autodelete)
  {
  
    final int imagesSize = images.size();
  
    if (imagesSize != sourceRects.size() || imagesSize != destRects.size())
    {
      ILogger.instance().logError("Failure at combine images.");
      return;
    }
  
    if (imagesSize == 1)
    {
      final IImage image = images.get(0);
      final RectangleF sourceRect = sourceRects.get(0);
      final RectangleF destRect = destRects.get(0);
  
      if ((sourceRect._x == 0) && (sourceRect._y == 0) && (sourceRect._width == image.getWidth()) && (sourceRect._height == image.getHeight()) && (destRect._x == 0) && (destRect._y == 0) && (destRect._width == width) && (destRect._height == height))
      {
        scale(width, height, image, listener, autodelete);
        return;
      }
    }
  
  
    ICanvas canvas = IFactory.instance().createCanvas();
    canvas.initialize(width, height);
  
    for (int i = 0; i < imagesSize ; i++)
    {
      final IImage image = images.get(i);
      final RectangleF srcRect = sourceRects.get(i);
      final RectangleF dstRect = destRects.get(i);
      final float transparency = transparencies.get(i);
  
      if (transparency == 1.0)
      {
        canvas.drawImage(image, srcRect._x, srcRect._y, srcRect._width, srcRect._height, dstRect._x, dstRect._y, dstRect._width, dstRect._height);
      }
      else
      {
        canvas.drawImage(image, srcRect._x, srcRect._y, srcRect._width, srcRect._height, dstRect._x, dstRect._y, dstRect._width, dstRect._height, transparency);
      }
    }
  
    canvas.createImage(listener, autodelete);
    if (canvas != null)
       canvas.dispose();
  }

  public static void combine(Vector2I extent, java.util.ArrayList<IImage> images, java.util.ArrayList<RectangleF> sourceRects, java.util.ArrayList<RectangleF> destRects, java.util.ArrayList<Float> transparencies, IImageListener listener, boolean autodelete)
  {
    combine(extent._x, extent._y, images, sourceRects, destRects, transparencies, listener, autodelete);
  }

}