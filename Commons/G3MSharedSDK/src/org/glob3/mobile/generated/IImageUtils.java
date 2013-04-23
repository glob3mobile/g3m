package org.glob3.mobile.generated; 
//
//  IImageUtils.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//

//
//  IImageUtils.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//




public class IImageUtils
{
  public static void scale(IImage image, Vector2I size, IImageListener listener, boolean autodelete)
  {
    ICanvas canvas = IFactory.instance().createCanvas();
    canvas.initialize(size._x, size._y);
    canvas.drawImage(image, (float)0.0, (float)0.0, (float)size._x, (float)size._y);
  
    canvas.createImage(listener, autodelete);
    if (canvas != null)
       canvas.dispose();
  }

  public static void subImage(IImage image, RectangleF rect, IImageListener listener, boolean autodelete)
  {
  
    ICanvas canvas = IFactory.instance().createCanvas();
    canvas.initialize((int)rect._width, (int)rect._height);
    canvas.drawImage(image, (float)rect._x, (float)rect._y, (float)rect._width, (float)rect._height, (float)0, (float)0, (float)rect._width, (float)rect._height);
  
    canvas.createImage(listener, autodelete);
    if (canvas != null)
       canvas.dispose();
  }

  public static void combine(java.util.ArrayList<IImage> images, java.util.ArrayList<RectangleF> sourceRects, java.util.ArrayList<RectangleF> destRects, Vector2I size, IImageListener listener, boolean autodelete)
  {
  
    final int imagesSize = images.size();
    if (imagesSize == 0 || imagesSize != sourceRects.size() || imagesSize != destRects.size())
    {
      ILogger.instance().logError("Failure at combine images.");
      return;
    }
  
    if (imagesSize == 1)
    {
      int im0Width = images.get(0).getWidth();
      int im0Height = images.get(0).getHeight();
  
      if (im0Width == size._x && im0Height == size._y && sourceRects.get(0)._x == 0 && sourceRects.get(0)._y == 0 && sourceRects.get(0)._width == im0Width && sourceRects.get(0)._height == im0Height)
      {
        listener.imageCreated(images.get(0).shallowCopy());
        if (autodelete)
        {
          if (listener != null)
             listener.dispose();
        }
      }
      else
      {
        scale(images.get(0), size, listener, autodelete);
      }
    }
    else
    {
  
  
      ICanvas canvas = IFactory.instance().createCanvas();
      canvas.initialize((int)size._x, (int)size._y);
  
      for (int i = 0; i < imagesSize ; i++)
      {
        IImage image = images.get(i);
        RectangleF srcRect = sourceRects.get(i);
        RectangleF dstRect = destRects.get(i);
  
        canvas.drawImage(image, srcRect._x, srcRect._y, srcRect._width, srcRect._height, dstRect._x, dstRect._y, dstRect._width, dstRect._height);
      }
  
  
      canvas.createImage(listener, autodelete);
      if (canvas != null)
         canvas.dispose();
    }
  }

}