package org.glob3.mobile.generated; 
//
//  CPUTextureBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  CPUTextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class CPUTextureBuilder extends TextureBuilder
{

  public final void createTextureFromImage(GL gl, IFactory factory, IImage image, Vector2I textureResolution, IImageListener listener, boolean autodelete)
  {
    final int width = textureResolution._x;
    final int height = textureResolution._y;
    if (image == null)
    {
      ILogger.instance().logWarning("Creating blank Image");
      factory.createImageFromSize(width, height, listener, autodelete);
    }
    else if (image.getHeight() == height && image.getWidth() == width)
    {
      listener.imageCreated(image.shallowCopy());
      if (autodelete)
      {
        if (listener != null)
           listener.dispose();
      }
    }
    else
    {
      image.scale(width, height, listener, autodelete);
    }
  }

  public final void createTextureFromImages(GL gl, IFactory factory, java.util.ArrayList<IImage> images, java.util.ArrayList<RectangleI> srcRectangles, java.util.ArrayList<RectangleI> destRectangles, Vector2I textureResolution, IImageListener listener, boolean autodelete)
  {
    final int width = textureResolution._x;
    final int height = textureResolution._y;
  
    final int imagesSize = images.size();
  
    if (imagesSize == 0 || imagesSize != destRectangles.size() || imagesSize != srcRectangles.size())
    {
      ILogger.instance().logWarning("Creating blank Image");
      //return factory->createImageFromSize(width, height);
      factory.createImageFromSize(width, height, listener, autodelete);
    }
    /*else if (imagesSize == 1) {
     RectangleI* rectangle = destRectangles[0];
     images[0]->subImage(*rectangle,
     new CPUTextureBuilderSubImageImageLister(width, height,
     listener, autodelete),
     true);
     }*/
    else
    {
      final java.util.ArrayList<IImage> tailImages = new java.util.ArrayList<IImage>();
      java.util.ArrayList<RectangleI> tailSourceRectangles = new java.util.ArrayList<RectangleI>();
      java.util.ArrayList<RectangleI> tailDestRectangles = new java.util.ArrayList<RectangleI>();
      for (int i = 1; i < imagesSize; i++)
      {
        tailImages.add(images.get(i));
        tailSourceRectangles.add(srcRectangles.get(i));
        tailDestRectangles.add(destRectangles.get(i));
      }
  
      RectangleI image0SrcRect = srcRectangles.get(0);
  
      images.get(0).combineWith(image0SrcRect, tailImages, tailSourceRectangles, tailDestRectangles, new Vector2I(width, height), listener, autodelete);
    }
  }

}