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
//class IImage;
//class GL;
//class IFactory;
//class RectangleI;
//class IImageListener;


public abstract class TextureBuilder {

  public abstract void createTextureFromImage(GL gl, IFactory factory, IImage image, int width, int height, IImageListener listener, boolean autodelete);

  public abstract void createTextureFromImages(GL gl, IFactory factory, java.util.ArrayList<IImage> images, java.util.ArrayList<RectangleI> rectangles, int width, int height, IImageListener listener, boolean autodelete);

  public void dispose() {
  }

}