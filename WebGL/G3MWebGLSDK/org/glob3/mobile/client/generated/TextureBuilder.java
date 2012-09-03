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
//ORIGINAL LINE: virtual const GLTextureId createTextureFromImages(GL * gl, const java.util.ArrayList<const IImage*> images, int width, int height, boolean generateMipmap) const = 0;
  public abstract GLTextureId createTextureFromImages(GL gl, java.util.ArrayList<IImage> images, int width, int height, boolean generateMipmap);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const GLTextureId createTextureFromImages(GL * gl, const IFactory* factory, const java.util.ArrayList<const IImage*> images, const java.util.ArrayList<const Rectangle*> rectangles, int width, int height, boolean generateMipmap) const = 0;
  public abstract GLTextureId createTextureFromImages(GL gl, IFactory factory, java.util.ArrayList<IImage> images, java.util.ArrayList<Rectangle> rectangles, int width, int height, boolean generateMipmap);
  public void dispose()
  {
  }
}