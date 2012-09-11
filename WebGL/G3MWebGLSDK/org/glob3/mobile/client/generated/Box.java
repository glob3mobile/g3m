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
	  _mesh = null;
  }

  public void dispose()
  {
	if (_mesh != null)
		if (_mesh != null)
			_mesh.dispose();
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
//ORIGINAL LINE: const java.util.ArrayList<Vector3D> getCorners() const
  public final java.util.ArrayList<Vector3D> getCorners()
  {
	if (_corners == null) {
  	_corners = new java.util.ArrayList<Vector3D>(8);
  
  	_corners.add(_lower);
  	_corners.add(new Vector3D(_lower.x(), _lower.y(), _upper.z()));
  	_corners.add(new Vector3D(_lower.x(), _upper.y(), _lower.z()));
  	_corners.add(new Vector3D(_lower.x(), _upper.y(), _upper.z()));
  	_corners.add(new Vector3D(_upper.x(), _lower.y(), _lower.z()));
  	_corners.add(new Vector3D(_upper.x(), _lower.y(), _upper.z()));
  	_corners.add(new Vector3D(_upper.x(), _upper.y(), _lower.z()));
  	_corners.add(_upper);
	}
	return _corners;
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
  
	final Vector2D pixel0 = rc.getCurrentCamera().point2Pixel(corners.get(0));
  
	double lowerX = pixel0.x();
	double upperX = pixel0.x();
	double lowerY = pixel0.y();
	double upperY = pixel0.y();
  
	final int cornersSize = corners.size();
	for (int i = 1; i < cornersSize; i++)
	{
	  final Vector2D pixel = rc.getCurrentCamera().point2Pixel(corners.get(i));
  
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const Vector3D& p) const
  public final boolean contains(Vector3D p)
  {
	final double margin = 1e-3;
	if (p.x() < _lower.x() - margin)
		return false;
	if (p.x() > _upper.x() + margin)
		return false;
  
	if (p.y() < _lower.y() - margin)
		return false;
	if (p.y() > _upper.y() + margin)
		return false;
  
	if (p.z() < _lower.z() - margin)
		return false;
	if (p.z() > _upper.z() + margin)
		return false;
  
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const
  public final Vector3D intersectionWithRay(Vector3D origin, Vector3D direction)
  {
  
	//MIN X
	{
	  Plane p = new Plane(new Vector3D(1.0, 0.0, 0.0), _lower.x());
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	//MAX X
	{
	  Plane p = new Plane(new Vector3D(1.0, 0.0, 0.0), _upper.x());
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	//MIN Y
	{
	  Plane p = new Plane(new Vector3D(0.0, 1.0, 0.0), _lower.y());
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	//MAX Y
	{
	  Plane p = new Plane(new Vector3D(0.0, 1.0, 0.0), _upper.y());
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	//MIN Z
	{
	  Plane p = new Plane(new Vector3D(0.0, 0.0, 1.0), _lower.z());
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	//MAX Z
	{
	  Plane p = new Plane(new Vector3D(0.0, 0.0, 1.0), _upper.z());
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	return Vector3D.nan();
  }

  public final void render(RenderContext rc)
  {
	if (_mesh == null)
		createMesh();
	_mesh.render(rc);
  }


  private final Vector3D _lower ;
  private final Vector3D _upper ;

  java.util.ArrayList<Vector3D> _corners = null; // cache for getCorners() method

  private Mesh _mesh;
  private void createMesh()
  {
	int numVertices = 8;
	int numIndices = 48;
  
	float[] v = { (float) _lower.x(), (float) _lower.y(), (float) _lower.z(), (float) _lower.x(), (float) _upper.y(), (float) _lower.z(), (float) _lower.x(), (float) _upper.y(), (float) _upper.z(), (float) _lower.x(), (float) _lower.y(), (float) _upper.z(), (float) _upper.x(), (float) _lower.y(), (float) _lower.z(), (float) _upper.x(), (float) _upper.y(), (float) _lower.z(), (float) _upper.x(), (float) _upper.y(), (float) _upper.z(), (float) _upper.x(), (float) _lower.y(), (float) _upper.z()};
  
	int[] i = { 0, 1, 1, 2, 2, 3, 3, 0, 1, 5, 5, 6, 6, 2, 2, 1, 5, 4, 4, 7, 7, 6, 6, 5, 4, 0, 0, 3, 3, 7, 7, 4, 3, 2, 2, 6, 6, 7, 7, 3, 0, 1, 1, 5, 5, 4, 4, 0 };
  
	FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.NoCenter, Vector3D.zero());
	IntBufferBuilder indices = new IntBufferBuilder();
  
	for (int n = 0; n<numVertices; n++)
	  vertices.add(v[n *3], v[n *3+1], v[n *3+2]);
  
	for (int n = 0; n<numIndices; n++)
	  indices.add(i[n]);
  
	Color flatColor = new Color(Color.fromRGBA((float)1.0, (float)1.0, (float)0.0, (float)1.0));
  
	// create mesh
	_mesh = new IndexedMesh(GLPrimitive.TriangleStrip, true, vertices.getCenter(), vertices.create(), indices.create(), flatColor);
  }

}