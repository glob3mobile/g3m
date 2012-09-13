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
//ORIGINAL LINE: virtual const GLImage* createTextureFromImage(GL * gl, const IFactory* factory, GLFormat format, const IImage* image, int width, int height) const = 0;
  public abstract GLImage createTextureFromImage(GL gl, IFactory factory, GLFormat format, IImage image, int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const GLImage* createTextureFromImages(GL * gl, const IFactory* factory, GLFormat format, const java.util.ArrayList<const IImage*> images, int width, int height) const = 0;
  public abstract GLImage createTextureFromImages(GL gl, IFactory factory, GLFormat format, java.util.ArrayList<IImage> images, int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const GLImage* createTextureFromImages(GL * gl, const IFactory* factory, GLFormat format, const java.util.ArrayList<const IImage*> images, const java.util.ArrayList<const Rectangle*> rectangles, int width, int height) const = 0;
  public abstract GLImage createTextureFromImages(GL gl, IFactory factory, GLFormat format, java.util.ArrayList<IImage> images, java.util.ArrayList<Rectangle> rectangles, int width, int height);
  public void dispose()
  {
  }
}