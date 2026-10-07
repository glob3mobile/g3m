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
