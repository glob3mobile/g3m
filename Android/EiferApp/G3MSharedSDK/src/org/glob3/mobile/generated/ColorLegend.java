package org.glob3.mobile.generated; 
//
//  ColorLegend.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/4/16.
//
//

//
//  ColorLegend.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/4/16.
//
//



public class ColorLegend
{



  public static class ColorAndValue
  {
    public double _value;
    public Color _color ;
    public ColorAndValue(Color color, double value)
    {
       _value = value;
       _color = new Color(color);
    }
    public void dispose(){}
  }

  public ColorLegend(java.util.ArrayList<ColorAndValue> legend)
  {
     _legend = legend;

    if (_legend.size() == 0)
    {
      return;
    }

    for (int i = 0; i < _legend.size() -1; i++)
    {
      if (_legend.get(i)._value >= _legend.get(i+1)._value)
      {
        throw new RuntimeException("ColorLegend -> List of colors must be passed in ascendant order.");
      }
    }

  }

  public void dispose()
  {
    for (int i = 0; i < _legend.size(); i++)
    {
      if (_legend.get(i) != null)
         _legend.get(i).dispose();
    }
  }


  public final Color getColor(double value)
  {

    ColorAndValue inf = null;
    ColorAndValue sup = null;

    for (int i = 0; i < _legend.size(); i++)
    {
      if (_legend.get(i)._value == value)
      {
        return _legend.get(i)._color;
      }
      if (_legend.get(i)._value <= value)
      {
        inf = _legend.get(i);
        if (i < _legend.size() -1)
        {
          sup = _legend.get(i+1);
        }
      }
    }

    if (inf == null)
    {
      return _legend.get(0)._color;
    }
    if (sup == null)
    {
      return inf._color;
    }

    double x = (value - inf._value) / (sup._value - inf._value);
    return Color.interpolateColor(inf._color, sup._color, (float)x);
  }


  private java.util.ArrayList<ColorAndValue> _legend = new java.util.ArrayList<ColorAndValue>();

}