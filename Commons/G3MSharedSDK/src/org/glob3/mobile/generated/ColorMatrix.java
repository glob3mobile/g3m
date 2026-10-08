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
//class Vector3D;


// Homogeneous color matrices for PlanetRenderer::setColorMatrix: color' = M * (r, g, b, 1)
public class ColorMatrix
{
  private static Matrix44D createAffine(double m00, double m01, double m02, double offset0, double m10, double m11, double m12, double offset1, double m20, double m21, double m22, double offset2)
  {
    return new Matrix44D(m00, m10, m20, 0, m01, m11, m21, 0, m02, m12, m22, 0, offset0, offset1, offset2, 1);
  }


  // Planckian locus chromaticity by Kim et al. (2002) cubic splines, Y = 1,
  // then XYZ to linear sRGB (IEC 61966-2-1)
  private static Vector3D linearRGBOfBlackBody(double kelvin)
  {
    final double t = kelvin;
    final double t2 = t * t;
    final double t3 = t2 * t;
  
    final double x = (t <= 4000) ? (-0.2661239e9 / t3) - (0.2343589e6 / t2) + (0.8776956e3 / t) + 0.179910 : (-3.0258469e9 / t3) + (2.1070379e6 / t2) + (0.2226347e3 / t) + 0.240390;
  
    final double x2 = x * x;
    final double x3 = x2 * x;
    final double y = (t <= 2222) ? (-1.1063814 * x3) - (1.34811020 * x2) + (2.18555832 * x) - 0.20219683 : (t <= 4000) ? (-0.9549476 * x3) - (1.37418593 * x2) + (2.09137015 * x) - 0.16748867 : (3.0817580 * x3) - (5.87338670 * x2) + (3.75112997 * x) - 0.37001483;
  
    final double X = x / y;
    final double Y = 1;
    final double Z = (1 - x - y) / y;
  
    return new Vector3D((3.2404542 * X) - (1.5371385 * Y) - (0.4985314 * Z), (-0.9692660 * X) + (1.8760108 * Y) + (0.0415560 * Z), (0.0556434 * X) - (0.2040259 * Y) + (1.0572252 * Z));
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

  // Lit as by a black body of that temperature: lower is warmer, 6500 is neutral
  // (valid 1900-25000: below ~1900 K the sRGB blue of the black body is negative)

  // Gains are computed in linear light keeping gray's luminance, then raised to 1/2.2
  // because the tiles hold gamma-encoded values
  public static Matrix44D createWhiteBalance(double kelvin)
  {
    final Vector3D white = linearRGBOfBlackBody(kelvin);
    final Vector3D neutral = linearRGBOfBlackBody(6500);
  
    final double redGain = white._x / neutral._x;
    final double greenGain = white._y / neutral._y;
    final double blueGain = white._z / neutral._z;
    final double grayGain = (GlobalMembersColorMatrix.RED_LUMA * redGain) + (GlobalMembersColorMatrix.GREEN_LUMA * greenGain) + (GlobalMembersColorMatrix.BLUE_LUMA * blueGain);
  
    final IMathUtils mu = IMathUtils.instance();
    final double encodingExponent = 1 / 2.2;
    return createAffine(mu.pow(redGain / grayGain, encodingExponent), 0, 0, 0, 0, mu.pow(greenGain / grayGain, encodingExponent), 0, 0, 0, 0, mu.pow(blueGain / grayGain, encodingExponent), 0);
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