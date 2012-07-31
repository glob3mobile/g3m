package org.glob3.mobile.generated; 
//
//  Box.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 17/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

//
//  Box.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2D;


public class Box extends Extent
{

  public Box(Vector3D lower, Vector3D upper)
  {
	  _lower = new Vector3D(lower);
	  _upper = new Vector3D(upper);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touches(const Frustum* frustum) const
  public final boolean touches(Frustum frustum)
  {
	return frustum.touchesWithBox(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getLower() const
  public final Vector3D getLower()
  {
	  return _lower;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getUpper() const
  public final Vector3D getUpper()
  {
	  return _upper;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Vector3D> getCorners() const
  public final java.util.ArrayList<Vector3D> getCorners()
  {
  	final java.util.ArrayList<Vector3D> c = new java.util.ArrayList<Vector3D>(8);
  
  	c.add(new Vector3D(_lower.x(), _lower.y(), _lower.z()));
  	c.add(new Vector3D(_lower.x(), _lower.y(), _upper.z()));
  	c.add(new Vector3D(_lower.x(), _upper.y(), _lower.z()));
  	c.add(new Vector3D(_lower.x(), _upper.y(), _upper.z()));
  	c.add(new Vector3D(_upper.x(), _lower.y(), _lower.z()));
  	c.add(new Vector3D(_upper.x(), _lower.y(), _upper.z()));
  	c.add(new Vector3D(_upper.x(), _upper.y(), _lower.z()));
  	c.add(new Vector3D(_upper.x(), _upper.y(), _upper.z()));
  
  	return c;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double squaredProjectedArea(const RenderContext* rc) const
  public final double squaredProjectedArea(RenderContext rc)
  {
	final Vector2D extent = projectedExtent(rc);
	return extent.x() * extent.y();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D projectedExtent(const RenderContext *rc) const
  public final Vector2D projectedExtent(RenderContext rc)
  {
	final java.util.ArrayList<Vector3D> corners = getCorners();
  
	final Vector2D pixel0 = rc.getCamera().point2Pixel(corners.get(0));
  
	double lowerX = pixel0.x();
	double upperX = pixel0.x();
	double lowerY = pixel0.y();
	double upperY = pixel0.y();
  
	final int cornersSize = corners.size();
	for (int i = 1; i < cornersSize; i++)
	{
	  final Vector2D pixel = rc.getCamera().point2Pixel(corners.get(i));
  
	  final double x = pixel.x();
	  final double y = pixel.y();
  
	  if (x < lowerX)
	  {
		  lowerX = x;
	  }
	  if (y < lowerY)
	  {
		  lowerY = y;
	  }
  
	  if (x > upperX)
	  {
		  upperX = x;
	  }
	  if (y > upperY)
	  {
		  upperY = y;
	  }
	}
  
	final double width = upperX - lowerX;
	final double height = upperY - lowerY;
  
	return new Vector2D(width, height);
  }


  private final Vector3D _lower ;
  private final Vector3D _upper ;
}