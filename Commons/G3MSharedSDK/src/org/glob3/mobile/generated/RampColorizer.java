package org.glob3.mobile.generated;
//
//  RampColorizer.cpp
//  G3M
//
//  Created by Nico on 21/02/2019.
//



//
//  RampColorizer.hpp
//  G3M
//
//  Created by Nico on 21/02/2019.
//




//class MutableColor;


public class RampColorizer
{
  private java.util.ArrayList<Color> _colors = new java.util.ArrayList<Color>();
  private final int _colorsLength;
  private java.util.ArrayList<Float> _steps = new java.util.ArrayList<Float>();

  private static java.util.ArrayList<Float> createDefaultSteps(int length)
  {
    java.util.ArrayList<Float> result = new java.util.ArrayList<Float>();
    final float step = 1.0f / (length - 1);
    for (int i = 0; i < length; i++)
    {
      result.add(step * i);
    }
    return result;
  }

  private RampColorizer(java.util.ArrayList<Color> colors, java.util.ArrayList<Float> steps)
  {
     _colors = colors;
     _colorsLength = _colors.size();
     _steps = steps;
    if (colors.isEmpty())
    {
      throw new RuntimeException("Colors is empty.");
    }
    if (steps.size() != colors.size())
    {
      throw new RuntimeException("Steps size is not equal as colors size.");
    }
  }

  public static RampColorizer colorSpectrum()
  {
    java.util.ArrayList<Color> colors = new java.util.ArrayList<Color>();
    colors.add(Color.NAVY);
    colors.add(Color.BLUE);
    colors.add(Color.GREEN);
    colors.add(Color.YELLOW);
    colors.add(Color.RED);
    return createRampColorizer(colors);
  }
  public static RampColorizer visibleSpectrum()
  {
    java.util.ArrayList<Color> colors = new java.util.ArrayList<Color>();
    colors.add(Color.fromRGBA255(255, 0, 255));
    colors.add(Color.fromRGBA255(0, 0, 255));
    colors.add(Color.fromRGBA255(0, 255, 0));
    colors.add(Color.fromRGBA255(255, 255, 0));
    colors.add(Color.fromRGBA255(255, 0, 0));
    return createRampColorizer(colors);
  }
  public static RampColorizer sunrise()
  {
    java.util.ArrayList<Color> colors = new java.util.ArrayList<Color>();
    colors.add(Color.WHITE);
    colors.add(Color.YELLOW);
    colors.add(Color.RED);
    return createRampColorizer(colors);
  }
  public static RampColorizer reds()
  {
    java.util.ArrayList<Color> colors = new java.util.ArrayList<Color>();
    colors.add(Color.fromRGBA255(255, 255, 255));
    colors.add(Color.fromRGBA255(254, 224, 210));
    colors.add(Color.fromRGBA255(252, 146, 114));
    colors.add(Color.fromRGBA255(222, 45, 38));
    return createRampColorizer(colors);
  }

  public static RampColorizer createRampColorizer(java.util.ArrayList<Color> colors, java.util.ArrayList<Float> steps)
  {
    return new RampColorizer(colors, steps);
  }

  public static RampColorizer createRampColorizer(java.util.ArrayList<Color> colors)
  {
    return new RampColorizer(colors, createDefaultSteps(colors.size()));
  }

  public void dispose()
  {
  
  }

  public final Color getColor(float alpha)
  {
    if (_colorsLength == 1)
    {
      return _colors.get(0);
    }
  
    if (alpha <= 0)
    {
      return _colors.get(0);
    }
    if (alpha >= 1)
    {
      return _colors.get(_colorsLength - 1);
    }
  
    int baseColorIndex = -1;
    float baseStep = 0F;
  
    for (int i = _steps.size() - 1; i >= 0; i--)
    {
      final float step = _steps.get(i);
      if (alpha <= step)
      {
        baseStep = step;
        baseColorIndex = i;
      }
    }
    final float deltaStep = baseStep - alpha;
  
    if (deltaStep <= 0.0001)
    {
      return _colors.get(baseColorIndex);
    }
  
    final float localAlpha = 1 - (deltaStep * (_colorsLength - 1));
  
    return _colors.get(baseColorIndex - 1).mixedWith((_colors.get(baseColorIndex)), localAlpha);
  }

  public final void getColor(float alpha, MutableColor color)
  {
    color.set(getColor(alpha));
  }

}