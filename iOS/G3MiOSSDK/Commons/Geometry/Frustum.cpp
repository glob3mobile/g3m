//
//  Frustum.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "Frustum.hpp"
#include "Box.hpp"


Frustum::Frustum (const FrustumData& data):
_ltn(Vector3D(data._left, data._top, -data._znear)),
_rtn(Vector3D(data._right, data._top, -data._znear)),
_lbn(Vector3D(data._left, data._bottom, -data._znear)),
_rbn(Vector3D(data._right, data._bottom, -data._znear)),
_ltf(Vector3D(data._zfar/data._znear*data._left,  data._zfar/data._znear*data._top,     -data._zfar)),
_rtf(Vector3D(data._zfar/data._znear*data._right, data._zfar/data._znear*data._top,     -data._zfar)),
_lbf(Vector3D(data._zfar/data._znear*data._left,  data._zfar/data._znear*data._bottom,  -data._zfar)),
_rbf(Vector3D(data._zfar/data._znear*data._right, data._zfar/data._znear*data._bottom,  -data._zfar)),
_leftPlane(Plane(Vector3D(0, 0, 0), 
                 Vector3D(data._left, data._top, -data._znear), 
                 Vector3D(data._left, data._bottom, -data._znear))),
_bottomPlane(Plane(Vector3D(0, 0, 0), 
                   Vector3D(data._left, data._bottom, -data._znear), 
                   Vector3D(data._right, data._bottom, -data._znear))),
_rightPlane(Plane(Vector3D(0, 0, 0), 
                  Vector3D(data._right, data._bottom, -data._znear), 
                  Vector3D(data._right, data._top, -data._znear))),
_topPlane(Plane(Vector3D(0, 0, 0), 
                Vector3D(data._right, data._top, -data._znear), 
                Vector3D(data._left, data._top, -data._znear))),
_nearPlane(Plane(Vector3D(0, 0, 1), data._znear)),
_farPlane(Plane(Vector3D(0, 0, -1), -data._zfar)),
_extent(NULL)
{
}


bool Frustum::contains(const Vector3D& point) const {
  if (_leftPlane.signedDistance(point)   > 0) return false;
  if (_rightPlane.signedDistance(point)  > 0) return false;
  if (_bottomPlane.signedDistance(point) > 0) return false;
  if (_topPlane.signedDistance(point)    > 0) return false;
  if (_nearPlane.signedDistance(point)   > 0) return false;
  if (_farPlane.signedDistance(point)    > 0) return false;
  return true;
}


bool Frustum::touchesWithBox(const Box *box) const
{
  bool outside;
  
  // test first if frustum extent intersect with box
  if (!getExtent()->touchesBox(box)) return false;
  
  // create an array with the 8 corners of the box
  const Vector3D min = box->getLower();
  const Vector3D max = box->getUpper();
  Vector3D corners[8] = {
    Vector3D(min.x(), min.y(), min.z()),
    Vector3D(min.x(), min.y(), max.z()),
    Vector3D(min.x(), max.y(), min.z()),
    Vector3D(min.x(), max.y(), max.z()),
    Vector3D(max.x(), min.y(), min.z()),
    Vector3D(max.x(), min.y(), max.z()),
    Vector3D(max.x(), max.y(), min.z()),
    Vector3D(max.x(), max.y(), max.z())
  };
  
//  std::vector<Vector3D> corners = box->getCorners();
  
  int __ASK_agustin;
  /* http://www.flipcode.com/archives/Frustum_Culling.shtml */
  
  
  // test with left plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_leftPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with bottom plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_bottomPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with right plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_rightPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with top plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_topPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with near plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_nearPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with far plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_farPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  return true;
}


Extent* Frustum::computeExtent() 
{
  double minx=1e10, miny=1e10, minz=1e10;
  double maxx=-1e10, maxy=-1e10, maxz=-1e10;
  
  if (_ltn.x()<minx) minx=_ltn.x();     if (_ltn.x()>maxx) maxx=_ltn.x();
  if (_ltn.y()<miny) miny=_ltn.y();     if (_ltn.y()>maxy) maxy=_ltn.y();
  if (_ltn.z()<minz) minz=_ltn.z();     if (_ltn.z()>maxz) maxz=_ltn.z();
  
  if (_rtn.x()<minx) minx=_rtn.x();     if (_rtn.x()>maxx) maxx=_rtn.x();
  if (_rtn.y()<miny) miny=_rtn.y();     if (_rtn.y()>maxy) maxy=_rtn.y();
  if (_rtn.z()<minz) minz=_rtn.z();     if (_rtn.z()>maxz) maxz=_rtn.z();
  
  if (_lbn.x()<minx) minx=_lbn.x();     if (_lbn.x()>maxx) maxx=_lbn.x();
  if (_lbn.y()<miny) miny=_lbn.y();     if (_lbn.y()>maxy) maxy=_lbn.y();
  if (_lbn.z()<minz) minz=_lbn.z();     if (_lbn.z()>maxz) maxz=_lbn.z();
  
  if (_rbn.x()<minx) minx=_rbn.x();     if (_rbn.x()>maxx) maxx=_rbn.x();
  if (_rbn.y()<miny) miny=_rbn.y();     if (_rbn.y()>maxy) maxy=_rbn.y();
  if (_rbn.z()<minz) minz=_rbn.z();     if (_rbn.z()>maxz) maxz=_rbn.z();
  
  if (_ltf.x()<minx) minx=_ltn.x();     if (_ltn.x()>maxx) maxx=_ltn.x();
  if (_ltf.y()<miny) miny=_ltn.y();     if (_ltn.y()>maxy) maxy=_ltn.y();
  if (_ltf.z()<minz) minz=_ltn.z();     if (_ltn.z()>maxz) maxz=_ltn.z();
  
  if (_rtf.x()<minx) minx=_rtf.x();     if (_rtf.x()>maxx) maxx=_rtf.x();
  if (_rtf.y()<miny) miny=_rtf.y();     if (_rtf.y()>maxy) maxy=_rtf.y();
  if (_rtf.z()<minz) minz=_rtf.z();     if (_rtf.z()>maxz) maxz=_rtf.z();

  if (_lbf.x()<minx) minx=_lbf.x();     if (_lbf.x()>maxx) maxx=_lbf.x();
  if (_lbf.y()<miny) miny=_lbf.y();     if (_lbf.y()>maxy) maxy=_lbf.y();
  if (_lbf.z()<minz) minz=_lbf.z();     if (_lbf.z()>maxz) maxz=_lbf.z();
  
  if (_rbf.x()<minx) minx=_rbf.x();     if (_rbf.x()>maxx) maxx=_rbf.x();
  if (_rbf.y()<miny) miny=_rbf.y();     if (_rbf.y()>maxy) maxy=_rbf.y();
  if (_rbf.z()<minz) minz=_rbf.z();     if (_rbf.z()>maxz) maxz=_rbf.z();
  
  
  return new Box(Vector3D(minx, miny, minz), Vector3D(maxx, maxy, maxz));
}

