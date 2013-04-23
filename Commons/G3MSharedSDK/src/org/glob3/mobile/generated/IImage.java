package org.glob3.mobile.generated; 
//
//  IImage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


///#include <vector>
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

//  
//  virtual void subImage(const RectangleI& rect,
//                        IImageListener* listener,
//                        bool autodelete) const = 0;
//  
//  virtual void scale(int width, int height,
//                     IImageListener* listener,
//                     bool autodelete) const = 0;

  public abstract String description();

  public abstract IImage shallowCopy();

//  
//  virtual void combineWith(const RectangleI& thisSourceRect,
//                           const IImage& other,
//                           const RectangleI& sourceRect,
//                           const RectangleI& destRect,
//                           const Vector2I& destSize,
//                           IImageListener* listener,
//                           bool autodelete) const = 0;
//  
//  virtual void combineWith(const RectangleI& thisSourceRect,
//                           const std::vector<const IImage*>& images,
//                           const std::vector<RectangleI*>& sourceRects,
//                           const std::vector<RectangleI*>& destRects,
//                           const Vector2I& size,
//                           IImageListener* listener,
//                           bool autodelete) const = 0;

}