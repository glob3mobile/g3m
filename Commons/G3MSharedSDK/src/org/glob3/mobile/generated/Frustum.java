package org.glob3.mobile.generated; 
public class Frustum
{
  private final Plane _leftPlane;
  private final Plane _rightPlane;
  private final Plane _bottomPlane;
  private final Plane _topPlane;
  private final Plane _nearPlane;
  private final Plane _farPlane;

  // the eight vertices of the frustum, i.e: ltn = left,top,near
  private final Vector3D _ltn ;
  private final Vector3D _rtn ;
  private final Vector3D _lbn ;
  private final Vector3D _rbn ;
  private final Vector3D _ltf ;
  private final Vector3D _rtf ;
  private final Vector3D _lbf ;
  private final Vector3D _rbf ;

  // the center of projection for the frustum
  private final double _znear;

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
     _znear = that._znear;
     _leftPlane = that._leftPlane.transformedByTranspose(matrix);
     _rightPlane = that._rightPlane.transformedByTranspose(matrix);
     _bottomPlane = that._bottomPlane.transformedByTranspose(matrix);
     _topPlane = that._topPlane.transformedByTranspose(matrix);
     _nearPlane = that._nearPlane.transformedByTranspose(matrix);
     _farPlane = that._farPlane.transformedByTranspose(matrix);
     _boundingVolume = null;
    //_boundingVolume = computeBoundingVolume();
  }

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
     _leftPlane = that._leftPlane;
     _rightPlane = that._rightPlane;
     _bottomPlane = that._bottomPlane;
     _topPlane = that._topPlane;
     _nearPlane = that._nearPlane;
     _farPlane = that._farPlane;
     _ltn = new Vector3D(that._ltn);
     _rtn = new Vector3D(that._rtn);
     _lbn = new Vector3D(that._lbn);
     _rbn = new Vector3D(that._rbn);
     _ltf = new Vector3D(that._ltf);
     _rtf = new Vector3D(that._rtf);
     _lbf = new Vector3D(that._lbf);
     _rbf = new Vector3D(that._rbf);
     _znear = that._znear;
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
     _znear = znear;
     _leftPlane = Plane.fromPoints(Vector3D.zero, new Vector3D(left, top, -znear), new Vector3D(left, bottom, -znear));
     _bottomPlane = Plane.fromPoints(Vector3D.zero, new Vector3D(left, bottom, -znear), new Vector3D(right, bottom, -znear));
     _rightPlane = Plane.fromPoints(Vector3D.zero, new Vector3D(right, bottom, -znear), new Vector3D(right, top, -znear));
     _topPlane = Plane.fromPoints(Vector3D.zero, new Vector3D(right, top, -znear), new Vector3D(left, top, -znear));
     _nearPlane = new Plane(new Vector3D(0, 0, 1), znear);
     _farPlane = new Plane(new Vector3D(0, 0, -1), -zfar);
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
     _znear = data._znear;
     _leftPlane = Plane.fromPoints(Vector3D.zero, new Vector3D(data._left, data._top, -data._znear), new Vector3D(data._left, data._bottom, -data._znear));
     _bottomPlane = Plane.fromPoints(Vector3D.zero, new Vector3D(data._left, data._bottom, -data._znear), new Vector3D(data._right, data._bottom, -data._znear));
     _rightPlane = Plane.fromPoints(Vector3D.zero, new Vector3D(data._right, data._bottom, -data._znear), new Vector3D(data._right, data._top, -data._znear));
     _topPlane = Plane.fromPoints(Vector3D.zero, new Vector3D(data._right, data._top, -data._znear), new Vector3D(data._left, data._top, -data._znear));
     _nearPlane = new Plane(new Vector3D(0, 0, 1), data._znear);
     _farPlane = new Plane(new Vector3D(0, 0, -1), -data._zfar);
     _boundingVolume = null;
  }

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

  public final boolean touchesWithBox(Box that)
  {
    // test first if frustum extent intersect with box
    if (!getBoundingVolume().touchesBox(that))
    {
      return false;
    }
  
    final Vector3F[] corners = that.getCornersArray();
  
    return !((_leftPlane.signedDistance(corners[0]) >= 0) && (_leftPlane.signedDistance(corners[1]) >= 0)
             && (_leftPlane.signedDistance(corners[2]) >= 0) && (_leftPlane.signedDistance(corners[3]) >= 0)
             && (_leftPlane.signedDistance(corners[4]) >= 0) && (_leftPlane.signedDistance(corners[5]) >= 0)
             && (_leftPlane.signedDistance(corners[6]) >= 0) && (_leftPlane.signedDistance(corners[7]) >= 0))
             && !((_bottomPlane.signedDistance(corners[0]) >= 0) && (_bottomPlane.signedDistance(corners[1]) >= 0)
                  && (_bottomPlane.signedDistance(corners[2]) >= 0) && (_bottomPlane.signedDistance(corners[3]) >= 0)
                  && (_bottomPlane.signedDistance(corners[4]) >= 0) && (_bottomPlane.signedDistance(corners[5]) >= 0)
                  && (_bottomPlane.signedDistance(corners[6]) >= 0) && (_bottomPlane.signedDistance(corners[7]) >= 0))
             && !((_rightPlane.signedDistance(corners[0]) >= 0) && (_rightPlane.signedDistance(corners[1]) >= 0)
                  && (_rightPlane.signedDistance(corners[2]) >= 0) && (_rightPlane.signedDistance(corners[3]) >= 0)
                  && (_rightPlane.signedDistance(corners[4]) >= 0) && (_rightPlane.signedDistance(corners[5]) >= 0)
                  && (_rightPlane.signedDistance(corners[6]) >= 0) && (_rightPlane.signedDistance(corners[7]) >= 0))
             && !((_topPlane.signedDistance(corners[0]) >= 0) && (_topPlane.signedDistance(corners[1]) >= 0)
                  && (_topPlane.signedDistance(corners[2]) >= 0) && (_topPlane.signedDistance(corners[3]) >= 0)
                  && (_topPlane.signedDistance(corners[4]) >= 0) && (_topPlane.signedDistance(corners[5]) >= 0)
                  && (_topPlane.signedDistance(corners[6]) >= 0) && (_topPlane.signedDistance(corners[7]) >= 0))
             && !((_nearPlane.signedDistance(corners[0]) >= 0) && (_nearPlane.signedDistance(corners[1]) >= 0)
                  && (_nearPlane.signedDistance(corners[2]) >= 0) && (_nearPlane.signedDistance(corners[3]) >= 0)
                  && (_nearPlane.signedDistance(corners[4]) >= 0) && (_nearPlane.signedDistance(corners[5]) >= 0)
                  && (_nearPlane.signedDistance(corners[6]) >= 0) && (_nearPlane.signedDistance(corners[7]) >= 0))
             && !((_farPlane.signedDistance(corners[0]) >= 0) && (_farPlane.signedDistance(corners[1]) >= 0)
                  && (_farPlane.signedDistance(corners[2]) >= 0) && (_farPlane.signedDistance(corners[3]) >= 0)
                  && (_farPlane.signedDistance(corners[4]) >= 0) && (_farPlane.signedDistance(corners[5]) >= 0)
                  && (_farPlane.signedDistance(corners[6]) >= 0) && (_farPlane.signedDistance(corners[7]) >= 0));
  }
  public final boolean touchesWithSphere(Sphere sphere)
  {
    // this implementation is right exact, but slower than touchesFrustumApprox()
    int numOutsiders = 0;
  
    // compute distances to near and far planes
    double nearDistance = _nearPlane.signedDistance(sphere._center);
    if (nearDistance > sphere._radius)
       return false;
    if (nearDistance > 0)
       numOutsiders++;
    double farDistance = _farPlane.signedDistance(sphere._center);
    if (farDistance > sphere._radius)
       return false;
    if (farDistance > 0)
       numOutsiders++;
  
    // test if sphere center is behind center of projection, to invert sign of lateral sides
    double invSign = 1;
    if (nearDistance > _znear)
       invSign = -1;
    double leftDistance = _leftPlane.signedDistance(sphere._center) * invSign;
    if (leftDistance > sphere._radius)
       return false;
    if (leftDistance > 0)
       numOutsiders++;
    double rightDistance = _rightPlane.signedDistance(sphere._center) * invSign;
    if (rightDistance > sphere._radius)
       return false;
    if (rightDistance > 0)
       numOutsiders++;
    double topDistance = _topPlane.signedDistance(sphere._center) * invSign;
    if (topDistance > sphere._radius)
       return false;
    if (topDistance > 0)
       numOutsiders++;
    double bottomDistance = _bottomPlane.signedDistance(sphere._center) * invSign;
    if (bottomDistance > sphere._radius)
       return false;
    if (bottomDistance > 0)
       numOutsiders++;
  
    // numOutsiders always between 0 and 3
    double squareDistance;
    double distance;
    switch (numOutsiders)
    {
  
      //case 0: // sphere center inside the frustum
      //return true;
  
      //case 1: // need to compute distance from sphere center to frustum plane
      //return true;
  
      case 2: // need to compute distance from sphere center to frustum edge
        if (leftDistance > 0)
        {
          if (topDistance > 0)
            distance = sphere._center.distanceToLine(_ltn, _ltf.sub(_ltn));
          else if (bottomDistance > 0)
            distance = sphere._center.distanceToLine(_lbn, _lbf.sub(_lbn));
          else if (nearDistance > 0)
            distance = sphere._center.distanceToLine(_ltn, _lbn.sub(_ltn));
          else
            distance = sphere._center.distanceToLine(_ltf, _lbf.sub(_ltf));
        }
        else if (rightDistance > 0)
        {
          if (topDistance > 0)
            distance = sphere._center.distanceToLine(_rtn, _rtf.sub(_rtn));
          else if (bottomDistance > 0)
            distance = sphere._center.distanceToLine(_rbn, _rbf.sub(_rbn));
          else if (nearDistance > 0)
            distance = sphere._center.distanceToLine(_rtn, _rbn.sub(_rtn));
          else
            distance = sphere._center.distanceToLine(_rtf, _rbf.sub(_rtf));
        }
        else if (nearDistance > 0)
        {
          if (topDistance > 0)
            distance = sphere._center.distanceToLine(_ltn, _rtn.sub(_ltn));
          else
            distance = sphere._center.distanceToLine(_lbn, _rbn.sub(_lbn));
        }
        else
        {
          if (topDistance > 0)
            distance = sphere._center.distanceToLine(_ltf, _rtf.sub(_ltf));
          else
            distance = sphere._center.distanceToLine(_lbf, _rbf.sub(_lbf));
        }
        if (distance > sphere._radius)
          return false;
        else
          return true;
  
      case 3: // need to compute distance from sphere center to frustum vertex
        if (leftDistance > 0)
        {
          if (topDistance > 0)
          {
            if (nearDistance > 0)
              squareDistance = sphere._center.squaredDistanceTo(_ltn);
            else
              squareDistance = sphere._center.squaredDistanceTo(_ltf);
          }
          else
          {
            if (nearDistance > 0)
              squareDistance = sphere._center.squaredDistanceTo(_lbn);
            else
              squareDistance = sphere._center.squaredDistanceTo(_lbf);
          }
        }
        else
        {
          if (topDistance > 0)
          {
            if (nearDistance > 0)
              squareDistance = sphere._center.squaredDistanceTo(_rtn);
            else
              squareDistance = sphere._center.squaredDistanceTo(_rtf);
          }
          else
          {
            if (nearDistance > 0)
              squareDistance = sphere._center.squaredDistanceTo(_rbn);
            else
              squareDistance = sphere._center.squaredDistanceTo(_rbf);
          }
        }
        if (squareDistance > sphere._radius * sphere._radius)
          return false;
        else
          return true;
    }
    return true;
  }


  public final Frustum transformedBy_P(MutableMatrix44D matrix)
  {
    return new Frustum(this, matrix, matrix.inversed());
  }

  public void dispose()
  {
    if (_boundingVolume != null)
       if (_boundingVolume != null)
          _boundingVolume.dispose();
  }

  public final BoundingVolume getBoundingVolume()
  {
    if (_boundingVolume == null)
       _boundingVolume = computeBoundingVolume();
    return _boundingVolume;
  }

  public final Plane getTopPlane()
  {
     return _topPlane;
  }
  public final Plane getBottomPlane()
  {
     return _bottomPlane;
  }
  public final Plane getLeftPlane()
  {
     return _leftPlane;
  }
  public final Plane getRightPlane()
  {
     return _rightPlane;
  }
  public final Plane getNearPlane()
  {
     return _nearPlane;
  }
  public final Plane getFarPlane()
  {
     return _farPlane;
  }

  public final Mesh createWireFrameMesh()
  {
    FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
    fbb.add(_ltn);
    fbb.add(_ltf);
    fbb.add(_rtn);
    fbb.add(_rtf);
    fbb.add(_lbn);
    fbb.add(_lbf);
    fbb.add(_rbn);
    fbb.add(_rbf);
  
    fbb.add(_ltn);
    fbb.add(_rtn);
    fbb.add(_rtn);
    fbb.add(_rbn);
    fbb.add(_rbn);
    fbb.add(_lbn);
    fbb.add(_lbn);
    fbb.add(_ltn);
  
    fbb.add(_ltf);
    fbb.add(_rtf);
    fbb.add(_rtf);
    fbb.add(_rbf);
    fbb.add(_rbf);
    fbb.add(_lbf);
    fbb.add(_lbf);
    fbb.add(_ltf);
  
    IFloatBuffer edges = fbb.create();
    if (fbb != null)
       fbb.dispose();
    return new DirectMesh(GLPrimitive.lines(), true, new Vector3D(0,0,0), edges, (float)2.0, (float)1.0, new Color(Color.blue()));
  }

}
//#define testAllCornersInside(plane, corners) ( (plane.signedDistance(corners[0]) >= 0) && (plane.signedDistance(corners[1]) >= 0) && (plane.signedDistance(corners[2]) >= 0) && (plane.signedDistance(corners[3]) >= 0) && (plane.signedDistance(corners[4]) >= 0) && (plane.signedDistance(corners[5]) >= 0) && (plane.signedDistance(corners[6]) >= 0) && (plane.signedDistance(corners[7]) >= 0) )


