package org.glob3.mobile.generated; 
//
//  IImage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


///#include "RectangleI.hpp"
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RectangleI;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageListener;

public abstract class IImage
{
  // a virtual destructor is needed for conversion to Java
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getWidth() const = 0;
  public abstract int getWidth();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getHeight() const = 0;
  public abstract int getHeight();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector2I getExtent() const = 0;
  public abstract Vector2I getExtent();

//  virtual void combineWith(const IImage& other,
//                           int width, int height,
//                           IImageListener* listener,
//                           bool autodelete) const = 0;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void combineWith(const IImage& other, const RectangleI& rect, int width, int height, IImageListener* listener, boolean autodelete) const = 0;
  public abstract void combineWith(IImage other, RectangleI rect, int width, int height, IImageListener listener, boolean autodelete);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void combineWith(const java.util.ArrayList<const IImage*>& images, const java.util.ArrayList<const RectangleI*>& rectangles, int width, int height, IImageListener* listener, boolean autodelete) const = 0;
  public abstract void combineWith(java.util.ArrayList<IImage> images, java.util.ArrayList<const RectangleI> rectangles, int width, int height, IImageListener listener, boolean autodelete);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void subImage(const RectangleI& rect, IImageListener* listener, boolean autodelete) const = 0;
  public abstract void subImage(RectangleI rect, IImageListener listener, boolean autodelete);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void scale(int width, int height, IImageListener* listener, boolean autodelete) const = 0;
  public abstract void scale(int width, int height, IImageListener listener, boolean autodelete);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String description() const = 0;
  public abstract String description();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IImage* shallowCopy() const = 0;
  public abstract IImage shallowCopy();

}