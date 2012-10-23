package org.glob3.mobile.generated; 
//
//  TextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public abstract class TextureBuilder
{

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const IImage* createTextureFromImage(GL * gl, const IFactory* factory, const IImage* image, int width, int height) const = 0;
  public abstract IImage createTextureFromImage(GL gl, IFactory factory, IImage image, int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const IImage* createTextureFromImages(GL * gl, const IFactory* factory, const java.util.ArrayList<const IImage*> images, int width, int height) const = 0;
  public abstract IImage createTextureFromImages(GL gl, IFactory factory, java.util.ArrayList<IImage> images, int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const IImage* createTextureFromImages(GL * gl, const IFactory* factory, const java.util.ArrayList<const IImage*> images, const java.util.ArrayList<const RectangleD*> rectangles, int width, int height) const = 0;
  public abstract IImage createTextureFromImages(GL gl, IFactory factory, java.util.ArrayList<IImage> images, java.util.ArrayList<RectangleD> rectangles, int width, int height);
  public void dispose()
  {
  }
}