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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GLImage* createTextureFromImage(GL * gl, const IFactory* factory, GLFormat format, const IImage* image, int width, int height) const
  public final GLImage createTextureFromImage(GL gl, IFactory factory, GLFormat format, IImage image, int width, int height)
  {
	if (image == null)
	{
	  ILogger.instance().logWarning("Creating blank GLImage");
	  int imageBytes = 4* width *height;
	  byte[] data = new byte[imageBytes];
	  for (int i = 0; i < imageBytes; i++)
	  {
		data[i] = (byte) 255; //WHITE
	  }
  
	  IByteBuffer bb = factory.createByteBuffer(data, imageBytes);
  
	  GLImage glImage = new GLImage(GLFormat.RGBA, bb, width, height);
  
	  return glImage;
	}
	else
	{
  
	  IByteBuffer bb = image.createByteBufferRGBA8888(width, height);
	  GLImage glImage = new GLImage(GLFormat.RGBA, bb, width, height);
  
	  return glImage;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GLImage* createTextureFromImages(GL * gl, const IFactory* factory, GLFormat format, const java.util.ArrayList<const IImage*> images, int width, int height) const
  public final GLImage createTextureFromImages(GL gl, IFactory factory, GLFormat format, java.util.ArrayList<IImage> images, int width, int height)
  {
  
	final int imagesSize = images.size();
  
	if (imagesSize == 0)
	{
  
	  ILogger.instance().logWarning("Creating blank GLImage");
	  int imageBytes = 4* width *height;
	  byte[] data = new byte[imageBytes];
	  for (int i = 0; i < imageBytes; i++)
	  {
		data[i] = (byte) 255; //WHITE
	  }
  
	  IByteBuffer bb = factory.createByteBuffer(data, imageBytes);
	  GLImage glImage = new GLImage(GLFormat.RGBA, bb, width, height);
  
	  return glImage;
	}
	else
	{
	  IImage im = images.get(0);
	  IImage im2 = null;
	  for (int i = 1; i < imagesSize; i++)
	  {
		IImage imTrans = images.get(i);
		im2 = im.combineWith(imTrans, width, height);
		if (i > 1)
		{
		  if (im != null)
			  im.dispose();
		}
		im = im2;
	  }
  
	  IByteBuffer bb = im.createByteBufferRGBA8888(width, height);
  
	  GLImage glImage = new GLImage(GLFormat.RGBA, bb, width, height);
  
	  if (imagesSize > 1)
	  {
		if (im != null)
			im.dispose();
	  }
  
	  return glImage;
  
	}
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GLImage* createTextureFromImages(GL * gl, const IFactory* factory, GLFormat format, const java.util.ArrayList<const IImage*> images, const java.util.ArrayList<const Rectangle*> rectangles, int width, int height) const
  public final GLImage createTextureFromImages(GL gl, IFactory factory, GLFormat format, java.util.ArrayList<IImage> images, java.util.ArrayList<Rectangle> rectangles, int width, int height)
  {
  
  
	final int imagesSize = images.size();
  
	if (imagesSize == 0 || images.size() != rectangles.size())
	{
  
	  ILogger.instance().logWarning("Creating blank GLImage");
	  int imageBytes = 4* width *height;
	  byte[] data = new byte[imageBytes];
	  for (int i = 0; i < imageBytes; i++)
	  {
		data[i] = (byte) 255; //WHITE
	  }
  
	  IByteBuffer bb = factory.createByteBuffer(data, imageBytes);
	  GLImage glImage = new GLImage(GLFormat.RGBA, bb, width, height);
  
	  return glImage;
	}
	else
	{
  
	  IImage base;
	  int i = 0; //First image to merge
	  Rectangle baseRec = new Rectangle(0,0, width, height);
	  if (rectangles.size() > 0 && rectangles.get(0).equalTo(baseRec))
	  {
		base = images.get(0);
		i = 1;
	  }
	  else
	  {
		base = factory.createImageFromSize(width, height);
	  }
  
	  for (; i < images.size(); i++)
	  {
		final IImage newIm = images.get(i);
		final Rectangle newRect = rectangles.get(i);
  
		IImage im2 = base.combineWith(newIm, newRect, width, height);
		if (base != images.get(0))
		{
		  if (base != null)
			  base.dispose();
		}
		base = im2;
	  }
  
	  IByteBuffer bb = base.createByteBufferRGBA8888(width, height);
  
	  GLImage glImage = new GLImage(GLFormat.RGBA, bb, width, height);
  
	  if (rectangles.size() > 0 && base != images.get(0))
	  {
		if (base != null)
			base.dispose();
	  }
  
	  return glImage;
  
	}
  }

}