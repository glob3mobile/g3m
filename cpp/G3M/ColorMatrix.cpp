//
//  ColorMatrix.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#include "ColorMatrix.hpp"

#include "Matrix44D.hpp"
#include "Color.hpp"
#include "Angle.hpp"
#include "IMathUtils.hpp"
#include "Vector3D.hpp"


// Rec. 709 luma weights
static const double RED_LUMA   = 0.2126;
static const double GREEN_LUMA = 0.7152;
static const double BLUE_LUMA  = 0.0722;


Matrix44D* ColorMatrix::createAffine(double m00, double m01, double m02, double offset0,
                                     double m10, double m11, double m12, double offset1,
                                     double m20, double m21, double m22, double offset2) {
  return new Matrix44D(m00,     m10,     m20,     0,
                       m01,     m11,     m21,     0,
                       m02,     m12,     m22,     0,
                       offset0, offset1, offset2, 1);
}

Matrix44D* ColorMatrix::createSaturation(double saturation) {
  const double gray = 1 - saturation;
  return createAffine(saturation + gray * RED_LUMA, gray * GREEN_LUMA,              gray * BLUE_LUMA,              0,
                      gray * RED_LUMA,              saturation + gray * GREEN_LUMA, gray * BLUE_LUMA,              0,
                      gray * RED_LUMA,              gray * GREEN_LUMA,              saturation + gray * BLUE_LUMA, 0);
}

// Pivots on middle gray
Matrix44D* ColorMatrix::createContrast(double contrast) {
  const double offset = 0.5 * (1 - contrast);
  return createAffine(contrast, 0,        0,        offset,
                      0,        contrast, 0,        offset,
                      0,        0,        contrast, offset);
}

// Luma-preserving rotation around the gray axis, as SVG feColorMatrix hueRotate
// (whose middle-row sine terms 0.143, 0.140, -0.283 are kept as published)
Matrix44D* ColorMatrix::createHueRotation(const Angle& angle) {
  const IMathUtils* mu = IMathUtils::instance();
  const double cosine = mu->cos(angle._radians);
  const double sine   = mu->sin(angle._radians);
  return createAffine(RED_LUMA + cosine * (1 - RED_LUMA) - sine * RED_LUMA,
                      GREEN_LUMA - cosine * GREEN_LUMA - sine * GREEN_LUMA,
                      BLUE_LUMA - cosine * BLUE_LUMA + sine * (1 - BLUE_LUMA),
                      0,
                      RED_LUMA - cosine * RED_LUMA + sine * 0.143,
                      GREEN_LUMA + cosine * (1 - GREEN_LUMA) + sine * 0.140,
                      BLUE_LUMA - cosine * BLUE_LUMA - sine * 0.283,
                      0,
                      RED_LUMA - cosine * RED_LUMA - sine * (1 - RED_LUMA),
                      GREEN_LUMA - cosine * GREEN_LUMA + sine * GREEN_LUMA,
                      BLUE_LUMA + cosine * (1 - BLUE_LUMA) + sine * BLUE_LUMA,
                      0);
}

// Planckian locus chromaticity by Kim et al. (2002) cubic splines, Y = 1,
// then XYZ to linear sRGB (IEC 61966-2-1)
Vector3D ColorMatrix::linearRGBOfBlackBody(double kelvin) {
  const double t  = kelvin;
  const double t2 = t * t;
  const double t3 = t2 * t;

  const double x = (t <= 4000)
  ? (-0.2661239e9 / t3) - (0.2343589e6 / t2) + (0.8776956e3 / t) + 0.179910
  : (-3.0258469e9 / t3) + (2.1070379e6 / t2) + (0.2226347e3 / t) + 0.240390;

  const double x2 = x * x;
  const double x3 = x2 * x;
  const double y = (t <= 2222)
  ? (-1.1063814 * x3) - (1.34811020 * x2) + (2.18555832 * x) - 0.20219683
  : (t <= 4000)
  ? (-0.9549476 * x3) - (1.37418593 * x2) + (2.09137015 * x) - 0.16748867
  : ( 3.0817580 * x3) - (5.87338670 * x2) + (3.75112997 * x) - 0.37001483;

  const double X = x / y;
  const double Y = 1;
  const double Z = (1 - x - y) / y;

  return Vector3D(( 3.2404542 * X) - (1.5371385 * Y) - (0.4985314 * Z),
                  (-0.9692660 * X) + (1.8760108 * Y) + (0.0415560 * Z),
                  ( 0.0556434 * X) - (0.2040259 * Y) + (1.0572252 * Z));
}

// Gains are computed in linear light keeping gray's luminance, then raised to 1/2.2
// because the tiles hold gamma-encoded values
Matrix44D* ColorMatrix::createWhiteBalance(double kelvin) {
  const Vector3D white   = linearRGBOfBlackBody(kelvin);
  const Vector3D neutral = linearRGBOfBlackBody(6500);

  const double redGain   = white._x / neutral._x;
  const double greenGain = white._y / neutral._y;
  const double blueGain  = white._z / neutral._z;
  const double grayGain  = (RED_LUMA * redGain) + (GREEN_LUMA * greenGain) + (BLUE_LUMA * blueGain);

  const IMathUtils* mu = IMathUtils::instance();
  const double encodingExponent = 1 / 2.2;
  return createAffine(mu->pow(redGain / grayGain, encodingExponent), 0, 0, 0,
                      0, mu->pow(greenGain / grayGain, encodingExponent), 0, 0,
                      0, 0, mu->pow(blueGain / grayGain, encodingExponent), 0);
}

Matrix44D* ColorMatrix::createTint(const Color& tint) {
  return createAffine(tint._red, 0,           0,          0,
                      0,         tint._green, 0,          0,
                      0,         0,           tint._blue, 0);
}

Matrix44D* ColorMatrix::createBrightness(double brightness) {
  return createAffine(brightness, 0,          0,          0,
                      0,          brightness, 0,          0,
                      0,          0,          brightness, 0);
}

Matrix44D* ColorMatrix::createInversion() {
  return createAffine(-1,  0,  0, 1,
                       0, -1,  0, 1,
                       0,  0, -1, 1);
}

Matrix44D* ColorMatrix::createDuotone(const Color& shadows,
                                      const Color& highlights) {
  const double redRange   = highlights._red   - shadows._red;
  const double greenRange = highlights._green - shadows._green;
  const double blueRange  = highlights._blue  - shadows._blue;
  return createAffine(redRange   * RED_LUMA, redRange   * GREEN_LUMA, redRange   * BLUE_LUMA, shadows._red,
                      greenRange * RED_LUMA, greenRange * GREEN_LUMA, greenRange * BLUE_LUMA, shadows._green,
                      blueRange  * RED_LUMA, blueRange  * GREEN_LUMA, blueRange  * BLUE_LUMA, shadows._blue);
}

Matrix44D* ColorMatrix::createSequence(const Matrix44D* first,
                                       const Matrix44D* second) {
  return second->createMultiplication(*first);
}
