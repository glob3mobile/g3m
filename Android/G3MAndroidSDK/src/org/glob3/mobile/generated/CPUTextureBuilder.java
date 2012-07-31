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
//ORIGINAL LINE: int createTextureFromImages(IGL * gl, const java.util.ArrayList<const IImage*>& vImages, int width, int height) const
  public final int createTextureFromImages(IGL gl, java.util.ArrayList<IImage> vImages, int width, int height)
  {
	if (vImages.size() > 0)
	{
  
	  IImage im = vImages.get(0);
	  IImage im2 = null;
	  for (int i = 1; i < vImages.size(); i++)
	  {
		IImage imTrans = vImages.get(i);
		im2 = im.combineWith(imTrans, width, height);
		if (i > 1)
			if (im != null)
				im.dispose();
		im = im2;
	  }
  
	  int texID = gl.uploadTexture(im, width, height);
  
	  if (1 < vImages.size())
	  {
		if (im != null)
			im.dispose();
	  }
	  return texID;
	}
	else
	{
	  return -1;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int createTextureFromImages(IGL * gl, const IFactory* factory, const java.util.ArrayList<const IImage*>& vImages, const java.util.ArrayList<const Rectangle*>& vRectangles, int width, int height) const
  public final int createTextureFromImages(IGL gl, IFactory factory, java.util.ArrayList<IImage> vImages, java.util.ArrayList<Rectangle> vRectangles, int width, int height)
  {
  
	int todo_JM = 0;
	IImage base;
	if (vRectangles.get(0)._width == width && vRectangles.get(0)._height == height)
	{
	  base = vImages.get(0);
	}
	else
	{
	  base = factory.createImageFromSize(width, height);
	}
  
	for (int i = 1; i < vImages.size(); i++)
	{
  	  IImage im2 = base.combineWith(vImages.get(i), vRectangles.get(i), width, height);
	  if (base != null)
		  base.dispose();
	  base = im2;
	}
  
	return todo_JM;
  
  }

}