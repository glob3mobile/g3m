package org.glob3.mobile.generated; 
//
//  IImage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IImage* combineWith(const IImage& other, int width, int height) const = 0;
  public abstract IImage combineWith(IImage other, int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IImage* combineWith(const IImage& other, const RectangleD& rect, int width, int height) const = 0;
  public abstract IImage combineWith(IImage other, RectangleD rect, int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IImage* subImage(const RectangleD& rect) const = 0;
  public abstract IImage subImage(RectangleD rect);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IImage* scale(int width, int height) const = 0;
  public abstract IImage scale(int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String description() const = 0;
  public abstract String description();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IImage* shallowCopy() const = 0;
  public abstract IImage shallowCopy();
}