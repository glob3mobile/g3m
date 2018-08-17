package org.glob3.mobile.generated;public class Frustum
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Plane _leftPlane = new Plane();
  private final Plane _rightPlane = new Plane();
  private final Plane _bottomPlane = new Plane();
  private final Plane _topPlane = new Plane();
  private final Plane _nearPlane = new Plane();
  private final Plane _farPlane = new Plane();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Plane _leftPlane = new internal();
  public final Plane _rightPlane = new internal();
  public final Plane _bottomPlane = new internal();
  public final Plane _topPlane = new internal();
  public final Plane _nearPlane = new internal();
  public final Plane _farPlane = new internal();
//#endif

  // the eight vertices of the frustum, i.e: ltn = left,top,near
  private final Vector3D _ltn = new Vector3D();
  private final Vector3D _rtn = new Vector3D();
  private final Vector3D _lbn = new Vector3D();
  private final Vector3D _rbn = new Vector3D();
  private final Vector3D _ltf = new Vector3D();
  private final Vector3D _rtf = new Vector3D();
  private final Vector3D _lbf = new Vector3D();
  private final Vector3D _rbf = new Vector3D();

  private BoundingVolume _boundingVolume;

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
	  _boundingVolume = null;
	//_boundingVolume = computeBoundingVolume();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: BoundingVolume* computeBoundingVolume() const
  private BoundingVolume computeBoundingVolume()
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
	  _boundingVolume = null;

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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _leftPlane = new Plane(Plane::fromPoints(Vector3D::zero, Vector3D(left, top, -znear), Vector3D(left, bottom, -znear)));
	  _leftPlane = new Plane(Plane.fromPoints(new Vector3D(Vector3D.zero), new Vector3D(left, top, -znear), new Vector3D(left, bottom, -znear)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _bottomPlane = new Plane(Plane::fromPoints(Vector3D::zero, Vector3D(left, bottom, -znear), Vector3D(right, bottom, -znear)));
	  _bottomPlane = new Plane(Plane.fromPoints(new Vector3D(Vector3D.zero), new Vector3D(left, bottom, -znear), new Vector3D(right, bottom, -znear)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _rightPlane = new Plane(Plane::fromPoints(Vector3D::zero, Vector3D(right, bottom, -znear), Vector3D(right, top, -znear)));
	  _rightPlane = new Plane(Plane.fromPoints(new Vector3D(Vector3D.zero), new Vector3D(right, bottom, -znear), new Vector3D(right, top, -znear)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _topPlane = new Plane(Plane::fromPoints(Vector3D::zero, Vector3D(right, top, -znear), Vector3D(left, top, -znear)));
	  _topPlane = new Plane(Plane.fromPoints(new Vector3D(Vector3D.zero), new Vector3D(right, top, -znear), new Vector3D(left, top, -znear)));
	  _nearPlane = new Plane(new Plane(new Vector3D(0, 0, 1), znear));
	  _farPlane = new Plane(new Plane(new Vector3D(0, 0, -1), -zfar));
	  _boundingVolume = null;
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _leftPlane = new Plane(Plane::fromPoints(Vector3D::zero, Vector3D(data._left, data._top, -data._znear), Vector3D(data._left, data._bottom, -data._znear)));
	  _leftPlane = new Plane(Plane.fromPoints(new Vector3D(Vector3D.zero), new Vector3D(data._left, data._top, -data._znear), new Vector3D(data._left, data._bottom, -data._znear)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _bottomPlane = new Plane(Plane::fromPoints(Vector3D::zero, Vector3D(data._left, data._bottom, -data._znear), Vector3D(data._right, data._bottom, -data._znear)));
	  _bottomPlane = new Plane(Plane.fromPoints(new Vector3D(Vector3D.zero), new Vector3D(data._left, data._bottom, -data._znear), new Vector3D(data._right, data._bottom, -data._znear)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _rightPlane = new Plane(Plane::fromPoints(Vector3D::zero, Vector3D(data._right, data._bottom, -data._znear), Vector3D(data._right, data._top, -data._znear)));
	  _rightPlane = new Plane(Plane.fromPoints(new Vector3D(Vector3D.zero), new Vector3D(data._right, data._bottom, -data._znear), new Vector3D(data._right, data._top, -data._znear)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _topPlane = new Plane(Plane::fromPoints(Vector3D::zero, Vector3D(data._right, data._top, -data._znear), Vector3D(data._left, data._top, -data._znear)));
	  _topPlane = new Plane(Plane.fromPoints(new Vector3D(Vector3D.zero), new Vector3D(data._right, data._top, -data._znear), new Vector3D(data._left, data._top, -data._znear)));
	  _nearPlane = new Plane(new Plane(new Vector3D(0, 0, 1), data._znear));
	  _farPlane = new Plane(new Plane(new Vector3D(0, 0, -1), -data._zfar));
	  _boundingVolume = null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const Vector3D& point) const
  public final boolean contains(Vector3D point)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (_leftPlane.signedDistance(point) > 0)
	if (_leftPlane.signedDistance(new Vector3D(point)) > 0)
		return false;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (_rightPlane.signedDistance(point) > 0)
	if (_rightPlane.signedDistance(new Vector3D(point)) > 0)
		return false;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (_bottomPlane.signedDistance(point) > 0)
	if (_bottomPlane.signedDistance(new Vector3D(point)) > 0)
		return false;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (_topPlane.signedDistance(point) > 0)
	if (_topPlane.signedDistance(new Vector3D(point)) > 0)
		return false;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (_nearPlane.signedDistance(point) > 0)
	if (_nearPlane.signedDistance(new Vector3D(point)) > 0)
		return false;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (_farPlane.signedDistance(point) > 0)
	if (_farPlane.signedDistance(new Vector3D(point)) > 0)
		return false;
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesWithBox(const Box* that) const
  public final boolean touchesWithBox(Box that)
  {
	// test first if frustum extent intersect with box
	if (!getBoundingVolume().touchesBox(that))
	{
	  return false;
	}
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning This implementation could gives false positives
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	// create an array with the 8 corners of the box
	final Vector3D min = that.getLower();
	final Vector3D max = that.getUpper();
  
	Vector3F[] corners = { new Vector3F((float) min._x, (float) min._y, (float) min._z), new Vector3F((float) min._x, (float) min._y, (float) max._z), new Vector3F((float) min._x, (float) max._y, (float) min._z), new Vector3F((float) min._x, (float) max._y, (float) max._z), new Vector3F((float) max._x, (float) min._y, (float) min._z), new Vector3F((float) max._x, (float) min._y, (float) max._z), new Vector3F((float) max._x, (float) max._y, (float) min._z), new Vector3F((float) max._x, (float) max._y, (float) max._z) };
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return (!((_leftPlane.signedDistance(corners[0]) >= 0) && (_leftPlane.signedDistance(corners[1]) >= 0) && (_leftPlane.signedDistance(corners[2]) >= 0) && (_leftPlane.signedDistance(corners[3]) >= 0) && (_leftPlane.signedDistance(corners[4]) >= 0) && (_leftPlane.signedDistance(corners[5]) >= 0) && (_leftPlane.signedDistance(corners[6]) >= 0) && (_leftPlane.signedDistance(corners[7]) >= 0)) && !((_bottomPlane.signedDistance(corners[0]) >= 0) && (_bottomPlane.signedDistance(corners[1]) >= 0) && (_bottomPlane.signedDistance(corners[2]) >= 0) && (_bottomPlane.signedDistance(corners[3]) >= 0) && (_bottomPlane.signedDistance(corners[4]) >= 0) && (_bottomPlane.signedDistance(corners[5]) >= 0) && (_bottomPlane.signedDistance(corners[6]) >= 0) && (_bottomPlane.signedDistance(corners[7]) >= 0)) && !((_rightPlane.signedDistance(corners[0]) >= 0) && (_rightPlane.signedDistance(corners[1]) >= 0) && (_rightPlane.signedDistance(corners[2]) >= 0) && (_rightPlane.signedDistance(corners[3]) >= 0) && (_rightPlane.signedDistance(corners[4]) >= 0) && (_rightPlane.signedDistance(corners[5]) >= 0) && (_rightPlane.signedDistance(corners[6]) >= 0) && (_rightPlane.signedDistance(corners[7]) >= 0)) && !((_topPlane.signedDistance(corners[0]) >= 0) && (_topPlane.signedDistance(corners[1]) >= 0) && (_topPlane.signedDistance(corners[2]) >= 0) && (_topPlane.signedDistance(corners[3]) >= 0) && (_topPlane.signedDistance(corners[4]) >= 0) && (_topPlane.signedDistance(corners[5]) >= 0) && (_topPlane.signedDistance(corners[6]) >= 0) && (_topPlane.signedDistance(corners[7]) >= 0)) && !((_nearPlane.signedDistance(corners[0]) >= 0) && (_nearPlane.signedDistance(corners[1]) >= 0) && (_nearPlane.signedDistance(corners[2]) >= 0) && (_nearPlane.signedDistance(corners[3]) >= 0) && (_nearPlane.signedDistance(corners[4]) >= 0) && (_nearPlane.signedDistance(corners[5]) >= 0) && (_nearPlane.signedDistance(corners[6]) >= 0) && (_nearPlane.signedDistance(corners[7]) >= 0)) && !((_farPlane.signedDistance(corners[0]) >= 0) && (_farPlane.signedDistance(corners[1]) >= 0) && (_farPlane.signedDistance(corners[2]) >= 0) && (_farPlane.signedDistance(corners[3]) >= 0) && (_farPlane.signedDistance(corners[4]) >= 0) && (_farPlane.signedDistance(corners[5]) >= 0) && (_farPlane.signedDistance(corners[6]) >= 0) && (_farPlane.signedDistance(corners[7]) >= 0)));
	return (!((_leftPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[7])) >= 0)) && !((_bottomPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[7])) >= 0)) && !((_rightPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[7])) >= 0)) && !((_topPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[7])) >= 0)) && !((_nearPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[7])) >= 0)) && !((_farPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[7])) >= 0)));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_final[] Vector3F corners = that.getCornersArray();
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return !((_leftPlane.signedDistance(corners[0]) >= 0) && (_leftPlane.signedDistance(corners[1]) >= 0) && (_leftPlane.signedDistance(corners[2]) >= 0) && (_leftPlane.signedDistance(corners[3]) >= 0) && (_leftPlane.signedDistance(corners[4]) >= 0) && (_leftPlane.signedDistance(corners[5]) >= 0) && (_leftPlane.signedDistance(corners[6]) >= 0) && (_leftPlane.signedDistance(corners[7]) >= 0)) && !((_bottomPlane.signedDistance(corners[0]) >= 0) && (_bottomPlane.signedDistance(corners[1]) >= 0) && (_bottomPlane.signedDistance(corners[2]) >= 0) && (_bottomPlane.signedDistance(corners[3]) >= 0) && (_bottomPlane.signedDistance(corners[4]) >= 0) && (_bottomPlane.signedDistance(corners[5]) >= 0) && (_bottomPlane.signedDistance(corners[6]) >= 0) && (_bottomPlane.signedDistance(corners[7]) >= 0)) && !((_rightPlane.signedDistance(corners[0]) >= 0) && (_rightPlane.signedDistance(corners[1]) >= 0) && (_rightPlane.signedDistance(corners[2]) >= 0) && (_rightPlane.signedDistance(corners[3]) >= 0) && (_rightPlane.signedDistance(corners[4]) >= 0) && (_rightPlane.signedDistance(corners[5]) >= 0) && (_rightPlane.signedDistance(corners[6]) >= 0) && (_rightPlane.signedDistance(corners[7]) >= 0)) && !((_topPlane.signedDistance(corners[0]) >= 0) && (_topPlane.signedDistance(corners[1]) >= 0) && (_topPlane.signedDistance(corners[2]) >= 0) && (_topPlane.signedDistance(corners[3]) >= 0) && (_topPlane.signedDistance(corners[4]) >= 0) && (_topPlane.signedDistance(corners[5]) >= 0) && (_topPlane.signedDistance(corners[6]) >= 0) && (_topPlane.signedDistance(corners[7]) >= 0)) && !((_nearPlane.signedDistance(corners[0]) >= 0) && (_nearPlane.signedDistance(corners[1]) >= 0) && (_nearPlane.signedDistance(corners[2]) >= 0) && (_nearPlane.signedDistance(corners[3]) >= 0) && (_nearPlane.signedDistance(corners[4]) >= 0) && (_nearPlane.signedDistance(corners[5]) >= 0) && (_nearPlane.signedDistance(corners[6]) >= 0) && (_nearPlane.signedDistance(corners[7]) >= 0)) && !((_farPlane.signedDistance(corners[0]) >= 0) && (_farPlane.signedDistance(corners[1]) >= 0) && (_farPlane.signedDistance(corners[2]) >= 0) && (_farPlane.signedDistance(corners[3]) >= 0) && (_farPlane.signedDistance(corners[4]) >= 0) && (_farPlane.signedDistance(corners[5]) >= 0) && (_farPlane.signedDistance(corners[6]) >= 0) && (_farPlane.signedDistance(corners[7]) >= 0));
	return !((_leftPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_leftPlane.signedDistance(new Vector3F(corners[7])) >= 0)) && !((_bottomPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_bottomPlane.signedDistance(new Vector3F(corners[7])) >= 0)) && !((_rightPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_rightPlane.signedDistance(new Vector3F(corners[7])) >= 0)) && !((_topPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_topPlane.signedDistance(new Vector3F(corners[7])) >= 0)) && !((_nearPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_nearPlane.signedDistance(new Vector3F(corners[7])) >= 0)) && !((_farPlane.signedDistance(new Vector3F(corners[0])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[1])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[2])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[3])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[4])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[5])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[6])) >= 0) && (_farPlane.signedDistance(new Vector3F(corners[7])) >= 0));
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Frustum* transformedBy_P(const MutableMatrix44D& matrix) const
  public final Frustum transformedBy_P(MutableMatrix44D matrix)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new Frustum(this, matrix, matrix.inversed());
	return new Frustum(this, new MutableMatrix44D(matrix), matrix.inversed());
  }

  public void dispose()
  {
	if (_boundingVolume != null)
		if (_boundingVolume != null)
			_boundingVolume.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: BoundingVolume* getBoundingVolume() const
  public final BoundingVolume getBoundingVolume()
  {
	if (_boundingVolume == null)
		_boundingVolume = computeBoundingVolume();
	return _boundingVolume;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Plane getTopPlane() const
  public final Plane getTopPlane()
  {
	  return _topPlane;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Plane getBottomPlane() const
  public final Plane getBottomPlane()
  {
	  return _bottomPlane;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Plane getLeftPlane() const
  public final Plane getLeftPlane()
  {
	  return _leftPlane;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Plane getRightPlane() const
  public final Plane getRightPlane()
  {
	  return _rightPlane;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Plane getNearPlane() const
  public final Plane getNearPlane()
  {
	  return _nearPlane;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Plane getFarPlane() const
  public final Plane getFarPlane()
  {
	  return _farPlane;
  }

}
//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//#define testAllCornersInside(plane, corners) ( (plane.signedDistance(corners[0]) >= 0) && (plane.signedDistance(corners[1]) >= 0) && (plane.signedDistance(corners[2]) >= 0) && (plane.signedDistance(corners[3]) >= 0) && (plane.signedDistance(corners[4]) >= 0) && (plane.signedDistance(corners[5]) >= 0) && (plane.signedDistance(corners[6]) >= 0) && (plane.signedDistance(corners[7]) >= 0) )

