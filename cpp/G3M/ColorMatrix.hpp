//
//  ColorMatrix.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#ifndef ColorMatrix_hpp
#define ColorMatrix_hpp

class Matrix44D;
class Color;
class Angle;


// Homogeneous color matrices for PlanetRenderer::setColorMatrix: color' = M * (r, g, b, 1)
class ColorMatrix {
private:
  static Matrix44D* createAffine(double m00, double m01, double m02, double offset0,
                                 double m10, double m11, double m12, double offset1,
                                 double m20, double m21, double m22, double offset2);

public:
  static Matrix44D* createSaturation(double saturation);

  static Matrix44D* createBrightness(double brightness);

  static Matrix44D* createContrast(double contrast);

  static Matrix44D* createHueRotation(const Angle& angle);

  static Matrix44D* createTint(const Color& tint);

  static Matrix44D* createInversion();

  static Matrix44D* createDuotone(const Color& shadows,
                                  const Color& highlights);

  static Matrix44D* createSequence(const Matrix44D* first,
                                   const Matrix44D* second);

};

#endif
