package org.glob3.mobile.generated; 
//
//  TextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//class IImage;
//class IFactory;
//class RectangleI;
//class IImageListener;
//class RectangleF;
//class Vector2I;

public abstract class TextureBuilder
{
  public abstract void createTextureFromImages(Vector2I textureExtent, java.util.ArrayList<IImage> images, java.util.ArrayList<RectangleF> srcRectangles, java.util.ArrayList<RectangleF> destRectangles, IImageListener listener, boolean autodelete);

  public void dispose()
  {
  }

}