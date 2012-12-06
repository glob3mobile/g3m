package org.glob3.mobile.generated; 
//
//  TextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


///#include "IImage.hpp"
///#include "GL.hpp"
///#include "IFactory.hpp"
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFactory;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RectangleI;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageListener;


public abstract class TextureBuilder
{

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const void createTextureFromImage(GL* gl, const IFactory* factory, const IImage* image, int width, int height, IImageListener* listener, boolean autodelete) const = 0;
  public abstract void createTextureFromImage(GL gl, IFactory factory, IImage image, int width, int height, IImageListener listener, boolean autodelete);

//  virtual const void createTextureFromImages(GL* gl,
//                                             const IFactory* factory,
//                                             const std::vector<const IImage*>& images,
//                                             int width, int height,
//                                             IImageListener* listener,
//                                             bool autodelete) const = 0;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const void createTextureFromImages(GL* gl, const IFactory* factory, const java.util.ArrayList<const IImage*>& images, const java.util.ArrayList<const RectangleI*>& rectangles, int width, int height, IImageListener* listener, boolean autodelete) const = 0;
  public abstract void createTextureFromImages(GL gl, IFactory factory, java.util.ArrayList<IImage> images, java.util.ArrayList<const RectangleI> rectangles, int width, int height, IImageListener listener, boolean autodelete);

  public void dispose()
  {
  }

}