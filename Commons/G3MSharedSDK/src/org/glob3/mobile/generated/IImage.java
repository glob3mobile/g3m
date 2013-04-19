package org.glob3.mobile.generated; 
//
//  IImage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


///#include "RectangleI.hpp"
//class RectangleI;
//class IImageListener;

public abstract class IImage
{
  public void dispose()
  {
  }

  public abstract int getWidth();
  public abstract int getHeight();
  public abstract Vector2I getExtent();


  public abstract void subImage(RectangleI rect, IImageListener listener, boolean autodelete);

  public abstract void scale(int width, int height, IImageListener listener, boolean autodelete);

  public abstract String description();

  public abstract IImage shallowCopy();


  public abstract void combineWith(RectangleI thisSourceRect, IImage other, RectangleI sourceRect, RectangleI destRect, Vector2I destSize, IImageListener listener, boolean autodelete);

  public abstract void combineWith(RectangleI thisSourceRect, java.util.ArrayList<IImage> images, java.util.ArrayList<RectangleI> sourceRects, java.util.ArrayList<RectangleI> destRects, Vector2I size, IImageListener listener, boolean autodelete);

}