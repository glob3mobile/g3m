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
      IImageUtils.scale(image, new Vector2I(width, height), listener, autodelete);
  
      //image->scale(width, height, listener, autodelete);
    }
  }

  public final void createTextureFromImages(GL gl, IFactory factory, java.util.ArrayList<IImage> images, java.util.ArrayList<RectangleF> srcRectangles, java.util.ArrayList<RectangleF> destRectangles, Vector2I textureResolution, IImageListener listener, boolean autodelete)
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
      /*
      std::vector<const IImage*> tailImages;
      std::vector<RectangleI*> tailSourceRectangles;
      std::vector<RectangleI*> tailDestRectangles;
      for (int i = 1; i < imagesSize; i++) {
        tailImages.push_back( images[i] );
        tailSourceRectangles.push_back(srcRectangles[i]);
        tailDestRectangles.push_back( destRectangles[i] );
      }
      
      RectangleI* image0SrcRect = srcRectangles[0];
      
      images[0]->combineWith(*image0SrcRect,
                             tailImages,
                             tailSourceRectangles,
                             tailDestRectangles,
                             Vector2I(width, height),
                             listener,
                             autodelete);
       */
  
      //TODO: Check!!!!!!!!!!!!!!
  //    std::vector<const IImage*> images2;
  //    std::vector<RectangleF*> srf;
  //    std::vector<RectangleF*> drf;
  //    for (int i = 0; i < imagesSize; i++) {
  //      images2.push_back( images[i] );
  //      srf.push_back(new RectangleF(srcRectangles[i]->_x,
  //                                                    srcRectangles[i]->_y,
  //                                                    srcRectangles[i]->_width,
  //                                                    srcRectangles[i]->_height));
  //      drf.push_back(new RectangleF(destRectangles[i]->_x,
  //                                   destRectangles[i]->_y,
  //                                   destRectangles[i]->_width,
  //                                   destRectangles[i]->_height));
  //
  //      delete srcRectangles[i];
  //      delete destRectangles[i];
  //    }
  //
  
      IImageUtils.combine(images, srcRectangles, destRectangles, new Vector2I(width, height), listener, autodelete);
    }
  }

}