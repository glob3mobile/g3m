package org.glob3.mobile.generated; 
public class Frustum
{
  private Plane _leftPlane = new Plane();
  private Plane _rightPlane = new Plane();
  private Plane _bottomPlane = new Plane();
  private Plane _topPlane = new Plane();
  private Plane _nearPlane = new Plane();
  private Plane _farPlane = new Plane();

  // the eight vertices of the frustum, i.e: ltn = left,top,near
  private final Vector3D _ltn ;
  private final Vector3D _rtn ;
  private final Vector3D _lbn ;
  private final Vector3D _rbn ;
  private final Vector3D _ltf ;
  private final Vector3D _rtf ;
  private final Vector3D _lbf ;
  private final Vector3D _rbf ;

  private Extent _extent;

  /*  Frustum(const Plane& leftPlane,
   const Plane& rightPlane,
   const Plane& bottomPlane,
   const Plane& topPlane,
   const Plane& nearPlane,
   const Plane& farPlane) :
   _leftPlane(leftPlane),
   _rightPlane(rightPlane),
   _bottomPlane(bottomPlane),
   _topPlane(topPlane),
   _nearPlane(nearPlane),
   _farPlane(farPlane)
   {
   
   }*/

  private Frustum(Frustum that, MutableMatrix44D matrix, MutableMatrix44D inverse)
  {
	  _ltn = new Vector3D(that._ltn.transformedBy(inverse, 1));
	  _rtn = new Vector3D(that._rtn.transformedBy(inverse, 1));
	  _lbn = new Vector3D(that._lbn.transformedBy(inverse, 1));
	  _rbn = new Vector3D(that._rbn.transformedBy(inverse, 1));
	  _ltf = new Vector3D(that._ltf.transformedBy(inverse, 1));
	  _rtf = new Vector3D(that._rtf.transformedBy(inverse, 1));
	  _lbf = new Vector3D(that._lbf.transformedBy(inverse, 1));
	  _rbf = new Vector3D(that._rbf.transformedBy(inverse, 1));
	  _leftPlane = new Plane(that._leftPlane.transformedByTranspose(matrix));
	  _rightPlane = new Plane(that._rightPlane.transformedByTranspose(matrix));
	  _bottomPlane = new Plane(that._bottomPlane.transformedByTranspose(matrix));
	  _topPlane = new Plane(that._topPlane.transformedByTranspose(matrix));
	  _nearPlane = new Plane(that._nearPlane.transformedByTranspose(matrix));
	  _farPlane = new Plane(that._farPlane.transformedByTranspose(matrix));
	_extent = computeExtent();
  }

  private Extent computeExtent()
  {
	double minx = 1e10;
	double miny = 1e10;
	double minz = 1e10;
	double maxx = -1e10;
	double maxy = -1e10;
	double maxz = -1e10;
  
	if (_ltn._x<minx)
		minx = _ltn._x;
		if (_ltn._x>maxx)
			maxx = _ltn._x;
	if (_ltn._y<miny)
		miny = _ltn._y;
		if (_ltn._y>maxy)
			maxy = _ltn._y;
	if (_ltn._z<minz)
		minz = _ltn._z;
		if (_ltn._z>maxz)
			maxz = _ltn._z;
  
	if (_rtn._x<minx)
		minx = _rtn._x;
		if (_rtn._x>maxx)
			maxx = _rtn._x;
	if (_rtn._y<miny)
		miny = _rtn._y;
		if (_rtn._y>maxy)
			maxy = _rtn._y;
	if (_rtn._z<minz)
		minz = _rtn._z;
		if (_rtn._z>maxz)
			maxz = _rtn._z;
  
	if (_lbn._x<minx)
		minx = _lbn._x;
		if (_lbn._x>maxx)
			maxx = _lbn._x;
	if (_lbn._y<miny)
		miny = _lbn._y;
		if (_lbn._y>maxy)
			maxy = _lbn._y;
	if (_lbn._z<minz)
		minz = _lbn._z;
		if (_lbn._z>maxz)
			maxz = _lbn._z;
  
	if (_rbn._x<minx)
		minx = _rbn._x;
		if (_rbn._x>maxx)
			maxx = _rbn._x;
	if (_rbn._y<miny)
		miny = _rbn._y;
		if (_rbn._y>maxy)
			maxy = _rbn._y;
	if (_rbn._z<minz)
		minz = _rbn._z;
		if (_rbn._z>maxz)
			maxz = _rbn._z;
  
	if (_ltf._x<minx)
		minx = _ltf._x;
		if (_ltf._x>maxx)
			maxx = _ltf._x;
	if (_ltf._y<miny)
		miny = _ltf._y;
		if (_ltf._y>maxy)
			maxy = _ltf._y;
	if (_ltf._z<minz)
		minz = _ltf._z;
		if (_ltf._z>maxz)
			maxz = _ltf._z;
  
	if (_rtf._x<minx)
		minx = _rtf._x;
		if (_rtf._x>maxx)
			maxx = _rtf._x;
	if (_rtf._y<miny)
		miny = _rtf._y;
		if (_rtf._y>maxy)
			maxy = _rtf._y;
	if (_rtf._z<minz)
		minz = _rtf._z;
		if (_rtf._z>maxz)
			maxz = _rtf._z;
  
	if (_lbf._x<minx)
		minx = _lbf._x;
		if (_lbf._x>maxx)
			maxx = _lbf._x;
	if (_lbf._y<miny)
		miny = _lbf._y;
		if (_lbf._y>maxy)
			maxy = _lbf._y;
	if (_lbf._z<minz)
		minz = _lbf._z;
		if (_lbf._z>maxz)
			maxz = _lbf._z;
  
	if (_rbf._x<minx)
		minx = _rbf._x;
		if (_rbf._x>maxx)
			maxx = _rbf._x;
	if (_rbf._y<miny)
		miny = _rbf._y;
		if (_rbf._y>maxy)
			maxy = _rbf._y;
	if (_rbf._z<minz)
		minz = _rbf._z;
		if (_rbf._z>maxz)
			maxz = _rbf._z;
  
	return new Box(new Vector3D(minx, miny, minz), new Vector3D(maxx, maxy, maxz));
  }


  public Frustum(Frustum that)
  {
	  _leftPlane = new Plane(that._leftPlane);
	  _rightPlane = new Plane(that._rightPlane);
	  _bottomPlane = new Plane(that._bottomPlane);
	  _topPlane = new Plane(that._topPlane);
	  _nearPlane = new Plane(that._nearPlane);
	  _farPlane = new Plane(that._farPlane);
	  _ltn = new Vector3D(that._ltn);
	  _rtn = new Vector3D(that._rtn);
	  _lbn = new Vector3D(that._lbn);
	  _rbn = new Vector3D(that._rbn);
	  _ltf = new Vector3D(that._ltf);
	  _rtf = new Vector3D(that._rtf);
	  _lbf = new Vector3D(that._lbf);
	  _rbf = new Vector3D(that._rbf);
	  _extent = null;

  }

  public Frustum(double left, double right, double bottom, double top, double znear, double zfar)
  {
	  _ltn = new Vector3D(new Vector3D(left, top, -znear));
	  _rtn = new Vector3D(new Vector3D(right, top, -znear));
	  _lbn = new Vector3D(new Vector3D(left, bottom, -znear));
	  _rbn = new Vector3D(new Vector3D(right, bottom, -znear));
	  _ltf = new Vector3D(new Vector3D(zfar/znear *left, zfar/znear *top, -zfar));
	  _rtf = new Vector3D(new Vector3D(zfar/znear *right, zfar/znear *top, -zfar));
	  _lbf = new Vector3D(new Vector3D(zfar/znear *left, zfar/znear *bottom, -zfar));
	  _rbf = new Vector3D(new Vector3D(zfar/znear *right, zfar/znear *bottom, -zfar));
	  _leftPlane = new Plane(new Plane(Vector3D.zero(), new Vector3D(left, top, -znear), new Vector3D(left, bottom, -znear)));
	  _bottomPlane = new Plane(new Plane(Vector3D.zero(), new Vector3D(left, bottom, -znear), new Vector3D(right, bottom, -znear)));
	  _rightPlane = new Plane(new Plane(Vector3D.zero(), new Vector3D(right, bottom, -znear), new Vector3D(right, top, -znear)));
	  _topPlane = new Plane(new Plane(Vector3D.zero(), new Vector3D(right, top, -znear), new Vector3D(left, top, -znear)));
	  _nearPlane = new Plane(new Plane(new Vector3D(0, 0, 1), znear));
	  _farPlane = new Plane(new Plane(new Vector3D(0, 0, -1), -zfar));
	  _extent = null;
  }

  public Frustum (FrustumData data)
  {
	  _ltn = new Vector3D(new Vector3D(data._left, data._top, -data._znear));
	  _rtn = new Vector3D(new Vector3D(data._right, data._top, -data._znear));
	  _lbn = new Vector3D(new Vector3D(data._left, data._bottom, -data._znear));
	  _rbn = new Vector3D(new Vector3D(data._right, data._bottom, -data._znear));
	  _ltf = new Vector3D(new Vector3D(data._zfar/data._znear *data._left, data._zfar/data._znear *data._top, -data._zfar));
	  _rtf = new Vector3D(new Vector3D(data._zfar/data._znear *data._right, data._zfar/data._znear *data._top, -data._zfar));
	  _lbf = new Vector3D(new Vector3D(data._zfar/data._znear *data._left, data._zfar/data._znear *data._bottom, -data._zfar));
	  _rbf = new Vector3D(new Vector3D(data._zfar/data._znear *data._right, data._zfar/data._znear *data._bottom, -data._zfar));
	  _leftPlane = new Plane(new Plane(Vector3D.zero(), new Vector3D(data._left, data._top, -data._znear), new Vector3D(data._left, data._bottom, -data._znear)));
	  _bottomPlane = new Plane(new Plane(Vector3D.zero(), new Vector3D(data._left, data._bottom, -data._znear), new Vector3D(data._right, data._bottom, -data._znear)));
	  _rightPlane = new Plane(new Plane(Vector3D.zero(), new Vector3D(data._right, data._bottom, -data._znear), new Vector3D(data._right, data._top, -data._znear)));
	  _topPlane = new Plane(new Plane(Vector3D.zero(), new Vector3D(data._right, data._top, -data._znear), new Vector3D(data._left, data._top, -data._znear)));
	  _nearPlane = new Plane(new Plane(new Vector3D(0, 0, 1), data._znear));
	  _farPlane = new Plane(new Plane(new Vector3D(0, 0, -1), -data._zfar));
	  _extent = null;
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
  
	// test first if frustum extent intersect with box
	if (!getExtent().touchesBox(box))
		return false;
  
	// create an array with the 8 corners of the box
	final Vector3D min = box.getLower();
	final Vector3D max = box.getUpper();
	Vector3D[] corners = { new Vector3D(min._x, min._y, min._z), new Vector3D(min._x, min._y, max._z), new Vector3D(min._x, max._y, min._z), new Vector3D(min._x, max._y, max._z), new Vector3D(max._x, min._y, min._z), new Vector3D(max._x, min._y, max._z), new Vector3D(max._x, max._y, min._z), new Vector3D(max._x, max._y, max._z) };
  
  //  std::vector<Vector3D> corners = box->getCorners();
  
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

  /*
   Frustum transformedBy(const MutableMatrix44D& matrix) const {
   return Frustum(_leftPlane.transformedBy(matrix),
   _rightPlane.transformedBy(matrix),
   _bottomPlane.transformedBy(matrix),
   _topPlane.transformedBy(matrix),
   _nearPlane.transformedBy(matrix),
   _farPlane.transformedBy(matrix));
   }
   
   
   Frustum* transformedBy_P(const MutableMatrix44D& matrix) const {
   return new Frustum(_leftPlane.transformedBy(matrix),
   _rightPlane.transformedBy(matrix),
   _bottomPlane.transformedBy(matrix),
   _topPlane.transformedBy(matrix),
   _nearPlane.transformedBy(matrix),
   _farPlane.transformedBy(matrix));
   }*/


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Frustum* transformedBy_P(const MutableMatrix44D& matrix) const
  public final Frustum transformedBy_P(MutableMatrix44D matrix)
  {
	return new Frustum(this, matrix, matrix.inversed());
  }

  public void dispose()
  {
	  _extent = null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent *getExtent() const
  public final Extent getExtent()
  {
	  return _extent;
  }
}