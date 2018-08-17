package org.glob3.mobile.generated;//
//  Box.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 17/07/12.
//

//
//  Box.h
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 16/07/12.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;

public class Box extends BoundingVolume
{

  private Mesh _mesh;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createMesh(const Color& color) const
  private Mesh createMesh(Color color)
  {
	double[] v = { _lower._x, _lower._y, _lower._z, _lower._x, _upper._y, _lower._z, _lower._x, _upper._y, _upper._z, _lower._x, _lower._y, _upper._z, _upper._x, _lower._y, _lower._z, _upper._x, _upper._y, _lower._z, _upper._x, _upper._y, _upper._z, _upper._x, _lower._y, _upper._z };
  
	short[] i = { 0, 1, 1, 2, 2, 3, 3, 0, 1, 5, 5, 6, 6, 2, 2, 1, 5, 4, 4, 7, 7, 6, 6, 5, 4, 0, 0, 3, 3, 7, 7, 4, 3, 2, 2, 6, 6, 7, 7, 3, 0, 1, 1, 5, 5, 4, 4, 0 };
  
	FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
	final int numVertices = 8;
	for (int n = 0; n < numVertices; n++)
	{
	  vertices.add(v[n *3], v[n *3+1], v[n *3+2]);
	}
  
	ShortBufferBuilder indices = new ShortBufferBuilder();
	final int numIndices = 48;
	for (int n = 0; n < numIndices; n++)
	{
	  indices.add(i[n]);
	}
  
	Mesh mesh = new IndexedMesh(GLPrimitive.lines(), vertices.getCenter(), vertices.create(), true, indices.create(), true, 2, 1, new Color(color));
  
	if (vertices != null)
		vertices.dispose();
  
	return mesh;
  }

  public final Vector3D _lower = new Vector3D();
  public final Vector3D _upper = new Vector3D();

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public java.util.ArrayList<Vector3D> _cornersD = null; // cache for getCorners() method
  public java.util.ArrayList<Vector3F> _cornersF = null; // cache for getCornersF() method
//#endif

  public Box(Vector3D lower, Vector3D upper)
  {
	  _lower = new Vector3D(lower);
	  _upper = new Vector3D(upper);
	  _mesh = null;
  }

  public void dispose()
  {
	if (_mesh != null)
		_mesh.dispose();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesFrustum(const Frustum* frustum) const
  public final boolean touchesFrustum(Frustum frustum)
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	Vector3D[] c = { _lower, new Vector3D(_lower._x, _lower._y, _upper._z), new Vector3D(_lower._x, _upper._y, _lower._z), new Vector3D(_lower._x, _upper._y, _upper._z), new Vector3D(_upper._x, _lower._y, _lower._z), new Vector3D(_upper._x, _lower._y, _upper._z), new Vector3D(_upper._x, _upper._y, _lower._z), _upper };
  
	return new java.util.ArrayList<Vector3D>(c, c+8);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	if (_cornersD == null)
	{
	  _cornersD = new java.util.ArrayList<Vector3D>(8);
  
	  _cornersD.add(_lower);
	  _cornersD.add(new Vector3D(_lower._x, _lower._y, _upper._z));
	  _cornersD.add(new Vector3D(_lower._x, _upper._y, _lower._z));
	  _cornersD.add(new Vector3D(_lower._x, _upper._y, _upper._z));
	  _cornersD.add(new Vector3D(_upper._x, _lower._y, _lower._z));
	  _cornersD.add(new Vector3D(_upper._x, _lower._y, _upper._z));
	  _cornersD.add(new Vector3D(_upper._x, _upper._y, _lower._z));
	  _cornersD.add(_upper);
	}
	return _cornersD;
//#endif
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<Vector3F> getCornersF() const
  public final java.util.ArrayList<Vector3F> getCornersF()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	Vector3F[] c = { new Vector3F((float) _lower._x, (float) _lower._y, (float) _lower._z), new Vector3F((float) _lower._x, (float) _lower._y, (float) _upper._z), new Vector3F((float) _lower._x, (float) _upper._y, (float) _lower._z), new Vector3F((float) _lower._x, (float) _upper._y, (float) _upper._z), new Vector3F((float) _upper._x, (float) _lower._y, (float) _lower._z), new Vector3F((float) _upper._x, (float) _lower._y, (float) _upper._z), new Vector3F((float) _upper._x, (float) _upper._y, (float) _lower._z), new Vector3F((float) _upper._x, (float) _upper._y, (float) _upper._z) };
  
	return new java.util.ArrayList<Vector3F>(c, c+8);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	if (_cornersF == null)
	{
	  _cornersF = new java.util.ArrayList<Vector3F>(8);
  
	  _cornersF.add(new Vector3F((float) _lower._x, (float) _lower._y, (float) _lower._z));
	  _cornersF.add(new Vector3F((float) _lower._x, (float) _lower._y, (float) _upper._z));
	  _cornersF.add(new Vector3F((float) _lower._x, (float) _upper._y, (float) _lower._z));
	  _cornersF.add(new Vector3F((float) _lower._x, (float) _upper._y, (float) _upper._z));
	  _cornersF.add(new Vector3F((float) _upper._x, (float) _lower._y, (float) _lower._z));
	  _cornersF.add(new Vector3F((float) _upper._x, (float) _lower._y, (float) _upper._z));
	  _cornersF.add(new Vector3F((float) _upper._x, (float) _upper._y, (float) _lower._z));
	  _cornersF.add(new Vector3F((float) _upper._x, (float) _upper._y, (float) _upper._z));
	}
	return _cornersF;
//#endif
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double projectedArea(const G3MRenderContext* rc) const
  public final double projectedArea(G3MRenderContext rc)
  {
  //  const Vector2I extent = projectedExtent(rc);
  //  return extent._x * extent._y;
  
	final java.util.ArrayList<Vector3F> corners = getCornersF();
  
	final Camera currentCamera = rc.getCurrentCamera();
  
	final Vector2F pixel0 = currentCamera.point2Pixel(corners.get(0));
  
	float lowerX = pixel0._x;
	float upperX = pixel0._x;
	float lowerY = pixel0._y;
	float upperY = pixel0._y;
  
	final int cornersSize = corners.size();
	for (int i = 1; i < cornersSize; i++)
	{
	  final Vector2F pixel = currentCamera.point2Pixel(corners.get(i));
  
	  final float x = pixel._x;
	  final float y = pixel._y;
  
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
  
	final float width = upperX - lowerX;
	final float height = upperY - lowerY;
  
	return width * height;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2F projectedExtent(const G3MRenderContext* rc) const
  public final Vector2F projectedExtent(G3MRenderContext rc)
  {
	final java.util.ArrayList<Vector3F> corners = getCornersF();
  
	final Camera currentCamera = rc.getCurrentCamera();
  
	final Vector2F pixel0 = currentCamera.point2Pixel(corners.get(0));
  
	float lowerX = pixel0._x;
	float upperX = pixel0._x;
	float lowerY = pixel0._y;
	float upperY = pixel0._y;
  
	final int cornersSize = corners.size();
	for (int i = 1; i < cornersSize; i++)
	{
	  final Vector2F pixel = currentCamera.point2Pixel(corners.get(i));
  
	  final float x = pixel._x;
	  final float y = pixel._y;
  
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
  
	final float width = upperX - lowerX;
	final float height = upperY - lowerY;
  
	return new Vector2F(width, height);
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const
  public final Vector3D intersectionWithRay(Vector3D origin, Vector3D direction)
  {
	//MIN X
	{
	  Plane p = new Plane(new Vector3D(1.0, 0.0, 0.0), _lower._x);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D inter = p.intersectionWithRay(origin, direction);
	  Vector3D inter = p.intersectionWithRay(new Vector3D(origin), new Vector3D(direction));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!inter.isNan() && contains(inter))
	  if (!inter.isNan() && contains(new Vector3D(inter)))
		  return inter;
	}
  
	//MAX X
	{
	  Plane p = new Plane(new Vector3D(1.0, 0.0, 0.0), _upper._x);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D inter = p.intersectionWithRay(origin, direction);
	  Vector3D inter = p.intersectionWithRay(new Vector3D(origin), new Vector3D(direction));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!inter.isNan() && contains(inter))
	  if (!inter.isNan() && contains(new Vector3D(inter)))
		  return inter;
	}
  
	//MIN Y
	{
	  Plane p = new Plane(new Vector3D(0.0, 1.0, 0.0), _lower._y);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D inter = p.intersectionWithRay(origin, direction);
	  Vector3D inter = p.intersectionWithRay(new Vector3D(origin), new Vector3D(direction));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!inter.isNan() && contains(inter))
	  if (!inter.isNan() && contains(new Vector3D(inter)))
		  return inter;
	}
  
	//MAX Y
	{
	  Plane p = new Plane(new Vector3D(0.0, 1.0, 0.0), _upper._y);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D inter = p.intersectionWithRay(origin, direction);
	  Vector3D inter = p.intersectionWithRay(new Vector3D(origin), new Vector3D(direction));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!inter.isNan() && contains(inter))
	  if (!inter.isNan() && contains(new Vector3D(inter)))
		  return inter;
	}
  
	//MIN Z
	{
	  Plane p = new Plane(new Vector3D(0.0, 0.0, 1.0), _lower._z);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D inter = p.intersectionWithRay(origin, direction);
	  Vector3D inter = p.intersectionWithRay(new Vector3D(origin), new Vector3D(direction));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!inter.isNan() && contains(inter))
	  if (!inter.isNan() && contains(new Vector3D(inter)))
		  return inter;
	}
  
	//MAX Z
	{
	  Plane p = new Plane(new Vector3D(0.0, 0.0, 1.0), _upper._z);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D inter = p.intersectionWithRay(origin, direction);
	  Vector3D inter = p.intersectionWithRay(new Vector3D(origin), new Vector3D(direction));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!inter.isNan() && contains(inter))
	  if (!inter.isNan() && contains(new Vector3D(inter)))
		  return inter;
	}
  
	return Vector3D.nan();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void render(const G3MRenderContext* rc, const GLState* parentState, const Color& color) const
  public final void render(G3MRenderContext rc, GLState parentState, Color color)
  {
	if (_mesh == null)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _mesh = createMesh(color);
	  _mesh = createMesh(new Color(color));
	}
	_mesh.render(rc, parentState);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touches(const BoundingVolume* that) const
  public final boolean touches(BoundingVolume that)
  {
	if (that == null)
	{
	  return false;
	}
	return that.touchesBox(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesBox(const Box* that) const
  public final boolean touchesBox(Box that)
  {
	if (_lower._x > that._upper._x)
	{
		return false;
	}
	if (_upper._x < that._lower._x)
	{
		return false;
	}
	if (_lower._y > that._upper._y)
	{
		return false;
	}
	if (_upper._y < that._lower._y)
	{
		return false;
	}
	if (_lower._z > that._upper._z)
	{
		return false;
	}
	if (_upper._z < that._lower._z)
	{
		return false;
	}
	return true;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesSphere(const Sphere* that) const
  public final boolean touchesSphere(Sphere that)
  {
	return that.touchesBox(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: BoundingVolume* mergedWith(const BoundingVolume* that) const
  public final BoundingVolume mergedWith(BoundingVolume that)
  {
	if (that == null)
	{
	  return null;
	}
	return that.mergedWithBox(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Box* mergedWithBox(const Box* that) const
  public final Box mergedWithBox(Box that)
  {
	final IMathUtils mu = IMathUtils.instance();
  
	final double lowerX = mu.min(_lower._x, that._lower._x);
	final double lowerY = mu.min(_lower._y, that._lower._y);
	final double lowerZ = mu.min(_lower._z, that._lower._z);
  
	final double upperX = mu.max(_upper._x, that._upper._x);
	final double upperY = mu.max(_upper._y, that._upper._y);
	final double upperZ = mu.max(_upper._z, that._upper._z);
  
	return new Box(new Vector3D(lowerX, lowerY, lowerZ), new Vector3D(upperX, upperY, upperZ));
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: BoundingVolume* mergedWithSphere(const Sphere* that) const
  public final BoundingVolume mergedWithSphere(Sphere that)
  {
	return that.mergedWithBox(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D closestPoint(const Vector3D& point) const
  public final Vector3D closestPoint(Vector3D point)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return point.clamp(_lower, _upper);
	return point.clamp(new Vector3D(_lower), new Vector3D(_upper));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const Vector3D& p) const
  public final boolean contains(Vector3D p)
  {
	final double margin = 1e-3;
	if (p._x < _lower._x - margin)
		return false;
	if (p._x > _upper._x + margin)
		return false;
  
	if (p._y < _lower._y - margin)
		return false;
	if (p._y > _upper._y + margin)
		return false;
  
	if (p._z < _lower._z - margin)
		return false;
	if (p._z > _upper._z + margin)
		return false;
  
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean fullContains(const BoundingVolume* that) const
  public final boolean fullContains(BoundingVolume that)
  {
	return that.fullContainedInBox(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean fullContainedInBox(const Box* box) const
  public final boolean fullContainedInBox(Box box)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return box->contains(_upper) && box->contains(_lower);
	return box.contains(new Vector3D(_upper)) && box.contains(new Vector3D(_lower));
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean fullContainedInSphere(const Sphere* that) const
  public final boolean fullContainedInSphere(Sphere that)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return that->contains(_lower) && that->contains(_upper);
	return that.contains(new Vector3D(_lower)) && that.contains(new Vector3D(_upper));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sphere* createSphere() const
  public final Sphere createSphere()
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D center = _lower.add(_upper).div(2);
	final Vector3D center = _lower.add(new Vector3D(_upper)).div(2);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double radius = center.distanceTo(_upper);
	final double radius = center.distanceTo(new Vector3D(_upper));
	return new Sphere(center, radius);
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public internal[] Vector3F _cornersArray = null;
  public final Vector3F[] getCornersArray()
  {
	if (_cornersArray == null)
	{
	  _cornersArray = new Vector3F[8];

	  _cornersArray[0] = new Vector3F((float) _lower._x, (float) _lower._y, (float) _lower._z);
	  _cornersArray[1] = new Vector3F((float) _lower._x, (float) _lower._y, (float) _upper._z);
	  _cornersArray[2] = new Vector3F((float) _lower._x, (float) _upper._y, (float) _lower._z);
	  _cornersArray[3] = new Vector3F((float) _lower._x, (float) _upper._y, (float) _upper._z);
	  _cornersArray[4] = new Vector3F((float) _upper._x, (float) _lower._y, (float) _lower._z);
	  _cornersArray[5] = new Vector3F((float) _upper._x, (float) _lower._y, (float) _upper._z);
	  _cornersArray[6] = new Vector3F((float) _upper._x, (float) _upper._y, (float) _lower._z);
	  _cornersArray[7] = new Vector3F((float) _upper._x, (float) _upper._y, (float) _upper._z);
	}
	return _cornersArray;
  }
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("[Box ");
	isb.addString(_lower.description());
	isb.addString(" / ");
	isb.addString(_upper.description());
	isb.addString("]");
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}
