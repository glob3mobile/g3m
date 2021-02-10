package org.glob3.mobile.generated;
//
//  XPCHeightPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 2/10/21.
//

//
//  XPCHeightPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 2/10/21.
//



//class RampColorizer;


public class XPCHeightPointColorizer extends XPCPointColorizer
{
  private final RampColorizer _ramp;
  private final boolean _deleteRamp;
  private final float _alpha;

  private double _minHeight;
  private double _maxHeight;
  private double _deltaHeight;


  public XPCHeightPointColorizer(RampColorizer ramp, boolean deleteRamp, float alpha)
  {
     _ramp = ramp;
     _deleteRamp = deleteRamp;
     _alpha = alpha;
     _minHeight = Double.NaN;
     _maxHeight = Double.NaN;
     _deltaHeight = Double.NaN;
  
  }

  public void dispose()
  {
    if (_deleteRamp)
    {
      if (_ramp != null)
         _ramp.dispose();
    }
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
    super.dispose();
//#endif
  }

  public final IIntBuffer initialize(XPCMetadata metadata)
  {
    _minHeight = metadata._minHeight;
    _maxHeight = metadata._maxHeight;
    _deltaHeight = _maxHeight - _minHeight;
  
    return null; // no dimension needed
  }

  public final void colorize(XPCMetadata metadata, double[] heights, java.util.ArrayList<IByteBuffer> dimensionsValues, int i, MutableColor color)
  {
    if ((_minHeight != _minHeight) || (_ramp == null))
    {
      color.set(1, 0, 0, 1);
      return;
    }
  
    final double height = heights[i];
  
    final double alpha = (height - _minHeight) / _deltaHeight;
  
    _ramp.getColor((float) alpha, color);
  }

}