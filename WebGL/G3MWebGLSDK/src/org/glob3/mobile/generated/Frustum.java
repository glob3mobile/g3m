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
  
	if (_ltn.x()<minx)
		minx = _ltn.x();
		if (_ltn.x()>maxx)
			maxx = _ltn.x();
	if (_ltn.y()<miny)
		miny = _ltn.y();
		if (_ltn.y()>maxy)
			maxy = _ltn.y();
	if (_ltn.z()<minz)
		minz = _ltn.z();
		if (_ltn.z()>maxz)
			maxz = _ltn.z();
  
	if (_rtn.x()<minx)
		minx = _rtn.x();
		if (_rtn.x()>maxx)
			maxx = _rtn.x();
	if (_rtn.y()<miny)
		miny = _rtn.y();
		if (_rtn.y()>maxy)
			maxy = _rtn.y();
	if (_rtn.z()<minz)
		minz = _rtn.z();
		if (_rtn.z()>maxz)
			maxz = _rtn.z();
  
	if (_lbn.x()<minx)
		minx = _lbn.x();
		if (_lbn.x()>maxx)
			maxx = _lbn.x();
	if (_lbn.y()<miny)
		miny = _lbn.y();
		if (_lbn.y()>maxy)
			maxy = _lbn.y();
	if (_lbn.z()<minz)
		minz = _lbn.z();
		if (_lbn.z()>maxz)
			maxz = _lbn.z();
  
	if (_rbn.x()<minx)
		minx = _rbn.x();
		if (_rbn.x()>maxx)
			maxx = _rbn.x();
	if (_rbn.y()<miny)
		miny = _rbn.y();
		if (_rbn.y()>maxy)
			maxy = _rbn.y();
	if (_rbn.z()<minz)
		minz = _rbn.z();
		if (_rbn.z()>maxz)
			maxz = _rbn.z();
  
	if (_ltf.x()<minx)
		minx = _ltn.x();
		if (_ltn.x()>maxx)
			maxx = _ltn.x();
	if (_ltf.y()<miny)
		miny = _ltn.y();
		if (_ltn.y()>maxy)
			maxy = _ltn.y();
	if (_ltf.z()<minz)
		minz = _ltn.z();
		if (_ltn.z()>maxz)
			maxz = _ltn.z();
  
	if (_rtf.x()<minx)
		minx = _rtf.x();
		if (_rtf.x()>maxx)
			maxx = _rtf.x();
	if (_rtf.y()<miny)
		miny = _rtf.y();
		if (_rtf.y()>maxy)
			maxy = _rtf.y();
	if (_rtf.z()<minz)
		minz = _rtf.z();
		if (_rtf.z()>maxz)
			maxz = _rtf.z();
  
	if (_lbf.x()<minx)
		minx = _lbf.x();
		if (_lbf.x()>maxx)
			maxx = _lbf.x();
	if (_lbf.y()<miny)
		miny = _lbf.y();
		if (_lbf.y()>maxy)
			maxy = _lbf.y();
	if (_lbf.z()<minz)
		minz = _lbf.z();
		if (_lbf.z()>maxz)
			maxz = _lbf.z();
  
	if (_rbf.x()<minx)
		minx = _rbf.x();
		if (_rbf.x()>maxx)
			maxx = _rbf.x();
	if (_rbf.y()<miny)
		miny = _rbf.y();
		if (_rbf.y()>maxy)
			maxy = _rbf.y();
	if (_rbf.z()<minz)
		minz = _rbf.z();
		if (_rbf.z()>maxz)
			maxz = _rbf.z();
  
  
	return new Box(new Vector3D(minx, miny, minz), new Vector3D(maxx, maxy, maxz));
  }


  /*Frustum(const Frustum& that) :
  _leftPlane(that._leftPlane),
  _rightPlane(that._rightPlane),
  _bottomPlane(that._bottomPlane),
  _topPlane(that._topPlane),
  _nearPlane(that._nearPlane),
  _farPlane(that._farPlane)
  {
    
  }*/

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
	  _leftPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(left, top, -znear), new Vector3D(left, bottom, -znear)));
	  _bottomPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(left, bottom, -znear), new Vector3D(right, bottom, -znear)));
	  _rightPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(right, bottom, -znear), new Vector3D(right, top, -znear)));
	  _topPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(right, top, -znear), new Vector3D(left, top, -znear)));
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
	  _leftPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(data._left, data._top, -data._znear), new Vector3D(data._left, data._bottom, -data._znear)));
	  _bottomPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(data._left, data._bottom, -data._znear), new Vector3D(data._right, data._bottom, -data._znear)));
	  _rightPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(data._right, data._bottom, -data._znear), new Vector3D(data._right, data._top, -data._znear)));
	  _topPlane = new Plane(new Plane(new Vector3D(0, 0, 0), new Vector3D(data._right, data._top, -data._znear), new Vector3D(data._left, data._top, -data._znear)));
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
	  if (_extent != null)
		  _extent = null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent *getExtent() const
  public final Extent getExtent()
  {
	  return _extent;
  }
}