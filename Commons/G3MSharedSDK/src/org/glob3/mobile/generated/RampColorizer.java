package org.glob3.mobile.generated;
//
//  RampColorizer.cpp
//  G3MiOSSDK
//
//  Created by Nico on 21/02/2019.
//

//
//  RampColorizer.hpp
//  G3MiOSSDK
//
//  Created by Nico on 21/02/2019.
//





public class RampColorizer
{
  private final java.util.ArrayList<Color> _colors = new java.util.ArrayList<Color>();
  private final int _colorsLength;
  private final java.util.ArrayList<Float> _steps = new java.util.ArrayList<Float>();

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
    if(colors.isEmpty())
    {
      throw new RuntimeException("Colors is empty.")
    }
    if(steps.size() != colors.size())
    {
      throw new RuntimeException("Steps size is not equal as colors size.")
    }
    //size steps = size colors
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  RampColorizer(java.util.ArrayList<Color> colors);

  public static RampColorizer initializeRampColorizer(java.util.ArrayList<Color> colors, java.util.ArrayList<Float> steps)
  {
    return new RampColorizer(colors, steps);
  }

  public static RampColorizer initializeRampColorizer(java.util.ArrayList<Color> colors)
  {
    return new RampColorizer(colors, createDefaultSteps(colors.size()));
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

  public void dispose()
  {
  
  }
}
