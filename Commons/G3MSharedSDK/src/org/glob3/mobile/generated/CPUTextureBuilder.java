package org.glob3.mobile.generated; 
//
//  CPUTextureBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  CPUTextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class CPUTextureBuilder extends TextureBuilder
{

  //Scales but may return the same image
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IImage* createTextureFromImage(GL* gl, const IFactory* factory, const IImage* image, int width, int height) const
  public final IImage createTextureFromImage(GL gl, IFactory factory, IImage image, int width, int height)
  {
	if (image == null)
	{
	  ILogger.instance().logWarning("Creating blank Image");
	  return factory.createImageFromSize(width, height);
	}
	else
	{
	  if (image.getHeight() == height && image.getWidth() == width)
	  {
		return image.shallowCopy();
	  }
	  else
	  {
		return image.scale(width, height);
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IImage* createTextureFromImages(GL* gl, const IFactory* factory, const java.util.ArrayList<const IImage*> images, int width, int height) const
  public final IImage createTextureFromImages(GL gl, IFactory factory, java.util.ArrayList<IImage> images, int width, int height)
  {
  
	final int imagesSize = images.size();
  
	if (imagesSize == 0)
	{
	  ILogger.instance().logWarning("Creating blank Image");
	  return factory.createImageFromSize(width, height);
	}
	else
	{
	  IImage im = images.get(0).shallowCopy();
	  IImage im2 = null;
	  for (int i = 1; i < imagesSize; i++)
	  {
		IImage imTrans = images.get(i);
		im2 = im.combineWith(imTrans, width, height);
		if (im != null)
			im.dispose();
		im = im2;
	  }
	  return im;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IImage* createTextureFromImages(GL* gl, const IFactory* factory, const java.util.ArrayList<const IImage*> images, const java.util.ArrayList<const RectangleD*> rectangles, int width, int height) const
  public final IImage createTextureFromImages(GL gl, IFactory factory, java.util.ArrayList<IImage> images, java.util.ArrayList<RectangleD> rectangles, int width, int height)
  {
  
	final int imagesSize = images.size();
  
	if (imagesSize == 0 || images.size() != rectangles.size())
	{
	  ILogger.instance().logWarning("Creating blank Image");
	  return factory.createImageFromSize(width, height);
	}
	else
	{
	  IImage base;
	  int i;
	  final RectangleD baseRec = new RectangleD(0,0, width, height);
	  if (rectangles.size() > 0 && rectangles.get(0).equalTo(baseRec))
	  {
		base = images.get(0).shallowCopy();
		i = 1;
	  }
	  else
	  {
		base = factory.createImageFromSize(width, height);
		i = 0;
	  }
  
	  for (; i < images.size(); i++)
	  {
		final IImage newIm = images.get(i);
		final RectangleD newRect = rectangles.get(i);
  
		IImage im2 = base.combineWith(newIm, newRect, width, height);
		if (base != null)
			base.dispose();
		base = im2;
	  }
	  return base;
	}
  }

}