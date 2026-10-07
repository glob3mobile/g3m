package org.glob3.mobile.generated;
//
//  ColorMatrix.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/7/26.
//

//
//  ColorMatrix.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/7/26.
//


//class Matrix44D;
//class Color;
//class Angle;


// Homogeneous color matrices for PlanetRenderer::setColorMatrix: color' = M * (r, g, b, 1)
public class ColorMatrix
{
  private static Matrix44D createAffine(double m00, double m01, double m02, double offset0, double m10, double m11, double m12, double offset1, double m20, double m21, double m22, double offset2)
  {
    return new Matrix44D(m00, m10, m20, 0, m01, m11, m21, 0, m02, m12, m22, 0, offset0, offset1, offset2, 1);
  }

  public static Matrix44D createSaturation(double saturation)
  {
    final double gray = 1 - saturation;
    return createAffine(saturation + gray * GlobalMembersColorMatrix.RED_LUMA, gray * GlobalMembersColorMatrix.GREEN_LUMA, gray * GlobalMembersColorMatrix.BLUE_LUMA, 0, gray * GlobalMembersColorMatrix.RED_LUMA, saturation + gray * GlobalMembersColorMatrix.GREEN_LUMA, gray * GlobalMembersColorMatrix.BLUE_LUMA, 0, gray * GlobalMembersColorMatrix.RED_LUMA, gray * GlobalMembersColorMatrix.GREEN_LUMA, saturation + gray * GlobalMembersColorMatrix.BLUE_LUMA, 0);
  }

  public static Matrix44D createBrightness(double brightness)
  {
    return createAffine(brightness, 0, 0, 0, 0, brightness, 0, 0, 0, 0, brightness, 0);
  }


  // Pivots on middle gray
  public static Matrix44D createContrast(double contrast)
  {
    final double offset = 0.5 * (1 - contrast);
    return createAffine(contrast, 0, 0, offset, 0, contrast, 0, offset, 0, 0, contrast, offset);
  }


  // Luma-preserving rotation around the gray axis, as SVG feColorMatrix hueRotate
  // (whose middle-row sine terms 0.143, 0.140, -0.283 are kept as published)
  public static Matrix44D createHueRotation(Angle angle)
  {
    final IMathUtils mu = IMathUtils.instance();
    final double cosine = mu.cos(angle._radians);
    final double sine = mu.sin(angle._radians);
    return createAffine(GlobalMembersColorMatrix.RED_LUMA + cosine * (1 - GlobalMembersColorMatrix.RED_LUMA) - sine * GlobalMembersColorMatrix.RED_LUMA, GlobalMembersColorMatrix.GREEN_LUMA - cosine * GlobalMembersColorMatrix.GREEN_LUMA - sine * GlobalMembersColorMatrix.GREEN_LUMA, GlobalMembersColorMatrix.BLUE_LUMA - cosine * GlobalMembersColorMatrix.BLUE_LUMA + sine * (1 - GlobalMembersColorMatrix.BLUE_LUMA), 0, GlobalMembersColorMatrix.RED_LUMA - cosine * GlobalMembersColorMatrix.RED_LUMA + sine * 0.143, GlobalMembersColorMatrix.GREEN_LUMA + cosine * (1 - GlobalMembersColorMatrix.GREEN_LUMA) + sine * 0.140, GlobalMembersColorMatrix.BLUE_LUMA - cosine * GlobalMembersColorMatrix.BLUE_LUMA - sine * 0.283, 0, GlobalMembersColorMatrix.RED_LUMA - cosine * GlobalMembersColorMatrix.RED_LUMA - sine * (1 - GlobalMembersColorMatrix.RED_LUMA), GlobalMembersColorMatrix.GREEN_LUMA - cosine * GlobalMembersColorMatrix.GREEN_LUMA + sine * GlobalMembersColorMatrix.GREEN_LUMA, GlobalMembersColorMatrix.BLUE_LUMA + cosine * (1 - GlobalMembersColorMatrix.BLUE_LUMA) + sine * GlobalMembersColorMatrix.BLUE_LUMA, 0);
  }

  public static Matrix44D createTint(Color tint)
  {
    return createAffine(tint._red, 0, 0, 0, 0, tint._green, 0, 0, 0, 0, tint._blue, 0);
  }

  public static Matrix44D createInversion()
  {
    return createAffine(-1, 0, 0, 1, 0, -1, 0, 1, 0, 0, -1, 1);
  }

  public static Matrix44D createDuotone(Color shadows, Color highlights)
  {
    final double redRange = highlights._red - shadows._red;
    final double greenRange = highlights._green - shadows._green;
    final double blueRange = highlights._blue - shadows._blue;
    return createAffine(redRange * GlobalMembersColorMatrix.RED_LUMA, redRange * GlobalMembersColorMatrix.GREEN_LUMA, redRange * GlobalMembersColorMatrix.BLUE_LUMA, shadows._red, greenRange * GlobalMembersColorMatrix.RED_LUMA, greenRange * GlobalMembersColorMatrix.GREEN_LUMA, greenRange * GlobalMembersColorMatrix.BLUE_LUMA, shadows._green, blueRange * GlobalMembersColorMatrix.RED_LUMA, blueRange * GlobalMembersColorMatrix.GREEN_LUMA, blueRange * GlobalMembersColorMatrix.BLUE_LUMA, shadows._blue);
  }

  public static Matrix44D createSequence(Matrix44D first, Matrix44D second)
  {
    return second.createMultiplication(first);
  }

}