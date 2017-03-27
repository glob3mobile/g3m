//
//  FrustumData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/17.
//
//

#include "FrustumData.hpp"


FrustumData::FrustumData(double left,
                         double right,
                         double bottom,
                         double top,
                         double zNear,
                         double zFar) :
_left(left),
_right(right),
_bottom(bottom),
_top(top),
_zNear(zNear),
_zFar(zFar)
{
}

FrustumData::FrustumData(const FrustumData& fd) :
_left(fd._left),
_right(fd._right),
_bottom(fd._bottom),
_top(fd._top),
_zNear(fd._zNear),
_zFar(fd._zFar)
{
}

FrustumData::~FrustumData() {

}
