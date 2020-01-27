//
//  FrustumData.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/25/17.
//
//

#ifndef FrustumData_hpp
#define FrustumData_hpp

class FrustumData {
private:
  FrustumData& operator=(const FrustumData& that);

public:
  const double _left;
  const double _right;
  const double _bottom;
  const double _top;
  const double _zNear;
  const double _zFar;

  FrustumData(double left,
              double right,
              double bottom,
              double top,
              double zNear,
              double zFar);

  explicit FrustumData(const FrustumData& fd);

  ~FrustumData();

};

#endif
