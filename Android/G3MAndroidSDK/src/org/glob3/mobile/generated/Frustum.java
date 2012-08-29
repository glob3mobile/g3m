package org.glob3.mobile.generated; 
public class Frustum
{
  private Plane _leftPlane = new Plane();
  private Plane _rightPlane = new Plane();
  private Plane _bottomPlane = new Plane();
  private Plane _topPlane = new Plane();
  private Plane _nearPlane = new Plane();
  private Plane _farPlane = new Plane();

  private Frustum(Plane leftPlane, Plane rightPlane, Plane bottomPlane, Plane topPlane, Plane nearPlane, Plane farPlane)
  {
	  _leftPlane = new Plane(leftPlane);
	  _rightPlane = new Plane(rightPlane);
	  _bottomPlane = new Plane(bottomPlane);
	  _topPlane = new Plane(topPlane);
	  _nearPlane = new Plane(nearPlane);
	  _farPlane = new Plane(farPlane);

  }

  public Frustum(Frustum that)
  {
	  _leftPlane = new Plane(that._leftPlane);
	  _rightPlane = new Plane(that._rightPlane);
	  _bottomPlane = new Plane(that._bottomPlane);
	  _topPlane = new Plane(that._topPlane);
	  _nearPlane = new Plane(that._nearPlane);
	  _farPlane = new Plane(that._farPlane);

  }

  public Frustum(double left, double right, double bottom, double top, double znear, double zfar)
  {
	  _leftPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(left, top, -znear), new Vector3D(left, bottom, -znear)));
	  _bottomPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(left, bottom, -znear), new Vector3D(right, bottom, -znear)));
	  _rightPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(right, bottom, -znear), new Vector3D(right, top, -znear)));
	  _topPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(right, top, -znear), new Vector3D(left, top, -znear)));
	  _nearPlane = new Plane(new Plane(new Vector3D(0, 0, 1), znear));
	  _farPlane = new Plane(new Plane(new Vector3D(0, 0, -1), -zfar));
  }

  public Frustum (FrustumData data)
  {
	  _leftPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(data._left, data._top, -data._znear), new Vector3D(data._left, data._bottom, -data._znear)));
	  _bottomPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(data._left, data._bottom, -data._znear), new Vector3D(data._right, data._bottom, -data._znear)));
	  _rightPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(data._right, data._bottom, -data._znear), new Vector3D(data._right, data._top, -data._znear)));
	  _topPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(data._right, data._top, -data._znear), new Vector3D(data._left, data._top, -data._znear)));
	  _nearPlane = new Plane(new Plane(new Vector3D(0, 0, 1), data._znear));
	  _farPlane = new Plane(new Plane(new Vector3D(0, 0, -1), -data._zfar));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const Vector3D& point) const
  public final boolean contains(Vector3D point)
  {
	if (_leftPlane.signedDistance(point) > 0)
		return false;
	if (_rightPlane.signedDistance(point) > 0)
		return false;
	if (_bottomPlane.signedDistance(point) > 0)
		return false;
	if (_topPlane.signedDistance(point) > 0)
		return false;
	if (_nearPlane.signedDistance(point) > 0)
		return false;
	if (_farPlane.signedDistance(point) > 0)
		return false;
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesWithBox(const Box *box) const
  public final boolean touchesWithBox(Box box)
  {
	boolean outside;
  
	// create an array with the 8 corners of the box
	final Vector3D min = box.getLower();
	final Vector3D max = box.getUpper();
	Vector3D[] corners = { new Vector3D(min.x(), min.y(), min.z()), new Vector3D(min.x(), min.y(), max.z()), new Vector3D(min.x(), max.y(), min.z()), new Vector3D(min.x(), max.y(), max.z()), new Vector3D(max.x(), min.y(), min.z()), new Vector3D(max.x(), min.y(), max.z()), new Vector3D(max.x(), max.y(), min.z()), new Vector3D(max.x(), max.y(), max.z()) };
  
  //  std::vector<Vector3D> corners = box->getCorners();
  
	int __ASK_agustin;
	/* http: //www.flipcode.com/archives/Frustum_Culling.shtml */
  
  
	// test with left plane
	outside = true;
	for (int i = 0; i<8; i++)
	  if (_leftPlane.signedDistance(corners[i])<0)
	  {
		outside = false;
		break;
	  }
	if (outside)
		return false;
  
	// test with bottom plane
	outside = true;
	for (int i = 0; i<8; i++)
	  if (_bottomPlane.signedDistance(corners[i])<0)
	  {
		outside = false;
		break;
	  }
	if (outside)
		return false;
  
	// test with right plane
	outside = true;
	for (int i = 0; i<8; i++)
	  if (_rightPlane.signedDistance(corners[i])<0)
	  {
		outside = false;
		break;
	  }
	if (outside)
		return false;
  
	// test with top plane
	outside = true;
	for (int i = 0; i<8; i++)
	  if (_topPlane.signedDistance(corners[i])<0)
	  {
		outside = false;
		break;
	  }
	if (outside)
		return false;
  
	// test with near plane
	outside = true;
	for (int i = 0; i<8; i++)
	  if (_nearPlane.signedDistance(corners[i])<0)
	  {
		outside = false;
		break;
	  }
	if (outside)
		return false;
  
	// test with far plane
	outside = true;
	for (int i = 0; i<8; i++)
	  if (_farPlane.signedDistance(corners[i])<0)
	  {
		outside = false;
		break;
	  }
	if (outside)
		return false;
  
	return true;
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Frustum transformedBy(const MutableMatrix44D& matrix) const
  public final Frustum transformedBy(MutableMatrix44D matrix)
  {
	return new Frustum(_leftPlane.transformedBy(matrix), _rightPlane.transformedBy(matrix), _bottomPlane.transformedBy(matrix), _topPlane.transformedBy(matrix), _nearPlane.transformedBy(matrix), _farPlane.transformedBy(matrix));
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Frustum* transformedBy_P(const MutableMatrix44D& matrix) const
  public final Frustum transformedBy_P(MutableMatrix44D matrix)
  {
	return new Frustum(_leftPlane.transformedBy(matrix), _rightPlane.transformedBy(matrix), _bottomPlane.transformedBy(matrix), _topPlane.transformedBy(matrix), _nearPlane.transformedBy(matrix), _farPlane.transformedBy(matrix));
  }

  public void dispose()
  {
  }
}