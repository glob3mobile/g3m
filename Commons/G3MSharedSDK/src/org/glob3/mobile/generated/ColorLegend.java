package org.glob3.mobile.generated;//
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
	public Color _color = new Color();
	public ColorAndValue(Color color, double value)
	{
		_value = value;
		_color = new Color(color);
	}
  }

  public ColorLegend(java.util.ArrayList<ColorAndValue*> legend)
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
		THROW_EXCEPTION("ColorLegend -> List of colors must be passed in ascendant order.");
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


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color getColor(double value) const
  public final Color getColor(double value)
  {
	if (value <= _legend.get(0)._value)
	{
	  return _legend.get(0)._color;
	}
	else
	{

	  for (int i = 0; i < _legend.size(); i++)
	  {
		if (i == _legend.size()-1 || _legend.get(i)._value == value)
		{
		  return _legend.get(i)._color;
		}
		else
		{

		  if (_legend.get(i)._value > value)
		  {
			ColorAndValue inf = _legend.get(i-1);
			ColorAndValue sup = _legend.get(i);

			double x = (value - inf._value) / (sup._value - inf._value);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return Color::interpolateColor(inf->_color, sup->_color, (float)x);
			return Color.interpolateColor(new Color(inf._color), new Color(sup._color), (float)x);
		  }
		}
	  }
	}

	return Color.transparent();
  }


  private java.util.ArrayList<ColorAndValue> _legend = new java.util.ArrayList<ColorAndValue>();

}
