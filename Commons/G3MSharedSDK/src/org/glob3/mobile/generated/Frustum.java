package org.glob3.mobile.generated;
//
//  Frustum.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 15/07/12.
//

//
//  Frustum.h
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 15/07/12.
//



//class BoundingVolume;
//class Box;
//class FrustumData;

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
     _ltn = that._ltn;
     _rtn = that._rtn;
     _lbn = that._lbn;
     _rbn = that._rbn;
     _ltf = that._ltf;
     _rtf = that._rtf;
     _lbf = that._lbf;
     _rbf = that._rbf;
     _boundingVolume = null;

  }

  public Frustum(double left, double right, double bottom, double top, double zNear, double zFar)
  {
     _ltn = new Vector3D(new Vector3D(left, top, -zNear));
     _rtn = new Vector3D(new Vector3D(right, top, -zNear));
     _lbn = new Vector3D(new Vector3D(left, bottom, -zNear));
     _rbn = new Vector3D(new Vector3D(right, bottom, -zNear));
     _ltf = new Vector3D(new Vector3D(zFar/zNear *left, zFar/zNear *top, -zFar));
     _rtf = new Vector3D(new Vector3D(zFar/zNear *right, zFar/zNear *top, -zFar));
     _lbf = new Vector3D(new Vector3D(zFar/zNear *left, zFar/zNear *bottom, -zFar));
     _rbf = new Vector3D(new Vector3D(zFar/zNear *right, zFar/zNear *bottom, -zFar));
     _leftPlane = Plane.fromPoints(Vector3D.ZERO, new Vector3D(left, top, -zNear), new Vector3D(left, bottom, -zNear));
     _bottomPlane = Plane.fromPoints(Vector3D.ZERO, new Vector3D(left, bottom, -zNear), new Vector3D(right, bottom, -zNear));
     _rightPlane = Plane.fromPoints(Vector3D.ZERO, new Vector3D(right, bottom, -zNear), new Vector3D(right, top, -zNear));
     _topPlane = Plane.fromPoints(Vector3D.ZERO, new Vector3D(right, top, -zNear), new Vector3D(left, top, -zNear));
     _nearPlane = new Plane(new Vector3D(0, 0, 1), zNear);
     _farPlane = new Plane(new Vector3D(0, 0, -1), -zFar);
     _boundingVolume = null;
  }

  public Frustum(FrustumData data)
  {
     _ltn = new Vector3D(new Vector3D(data._left, data._top, -data._zNear));
     _rtn = new Vector3D(new Vector3D(data._right, data._top, -data._zNear));
     _lbn = new Vector3D(new Vector3D(data._left, data._bottom, -data._zNear));
     _rbn = new Vector3D(new Vector3D(data._right, data._bottom, -data._zNear));
     _ltf = new Vector3D(new Vector3D(data._zFar/data._zNear *data._left, data._zFar/data._zNear *data._top, -data._zFar));
     _rtf = new Vector3D(new Vector3D(data._zFar/data._zNear *data._right, data._zFar/data._zNear *data._top, -data._zFar));
     _lbf = new Vector3D(new Vector3D(data._zFar/data._zNear *data._left, data._zFar/data._zNear *data._bottom, -data._zFar));
     _rbf = new Vector3D(new Vector3D(data._zFar/data._zNear *data._right, data._zFar/data._zNear *data._bottom, -data._zFar));
     _leftPlane = Plane.fromPoints(Vector3D.ZERO, new Vector3D(data._left, data._top, -data._zNear), new Vector3D(data._left, data._bottom, -data._zNear));
     _bottomPlane = Plane.fromPoints(Vector3D.ZERO, new Vector3D(data._left, data._bottom, -data._zNear), new Vector3D(data._right, data._bottom, -data._zNear));
     _rightPlane = Plane.fromPoints(Vector3D.ZERO, new Vector3D(data._right, data._bottom, -data._zNear), new Vector3D(data._right, data._top, -data._zNear));
     _topPlane = Plane.fromPoints(Vector3D.ZERO, new Vector3D(data._right, data._top, -data._zNear), new Vector3D(data._left, data._top, -data._zNear));
     _nearPlane = new Plane(new Vector3D(0, 0, 1), data._zNear);
     _farPlane = new Plane(new Vector3D(0, 0, -1), -data._zFar);
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning This implementation could gives false positives
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

}
//#define testAllCornersInside(plane, corners) ( (plane.signedDistance(corners[0]) >= 0) && (plane.signedDistance(corners[1]) >= 0) && (plane.signedDistance(corners[2]) >= 0) && (plane.signedDistance(corners[3]) >= 0) && (plane.signedDistance(corners[4]) >= 0) && (plane.signedDistance(corners[5]) >= 0) && (plane.signedDistance(corners[6]) >= 0) && (plane.signedDistance(corners[7]) >= 0) )

