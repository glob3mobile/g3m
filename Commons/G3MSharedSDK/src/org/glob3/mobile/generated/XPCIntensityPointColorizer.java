package org.glob3.mobile.generated;
//
//  XPCIntensityPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/21/21.
//

//
//  XPCIntensityPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/21/21.
//





public class XPCIntensityPointColorizer extends XPCFixedAlphaPointColorizer
{
  private final String _intensityDimensionName;

  private int _intensityDimensionIndex;

  private boolean _ok;


  public XPCIntensityPointColorizer(float alpha)
  {
     super(alpha);
     _intensityDimensionName = "Intensity";
     _intensityDimensionIndex = -1;
     _ok = false;
  
  }

  public XPCIntensityPointColorizer(String intensityDimensionName, float alpha)
  {
     super(alpha);
     _intensityDimensionName = intensityDimensionName;
     _intensityDimensionIndex = -1;
     _ok = false;
  
  }

  public void dispose()
  {
  
  }

  public final IIntBuffer initialize(XPCMetadata metadata)
  {
    final int dimensionsCount = metadata.getDimensionsCount();
  
    for (int i = 0; i < dimensionsCount; i++)
    {
      final String dimensionName = metadata.getDimension(i)._name;
  
      if (_intensityDimensionName.equals(dimensionName))
      {
        _intensityDimensionIndex = i;
      }
    }
  
    _ok = (_intensityDimensionIndex >= 0);
  
    if (!_ok)
    {
      ILogger.instance().logError("Can't find Intensity dimensions");
      return null;
    }
  
    IIntBuffer requiredDimensionIndices = IFactory.instance().createIntBuffer(1);
    requiredDimensionIndices.put(0, _intensityDimensionIndex);
    return requiredDimensionIndices;
  }

  public final void colorize(XPCMetadata metadata, double[] heights, java.util.ArrayList<IByteBuffer> dimensionsValues, int i, MutableColor color)
  {
    if (!_ok)
    {
      color.set(1, 0, 0, 1);
      return;
    }
  
    final float intensity = metadata.getDimension(_intensityDimensionIndex).getNormalizedValue(dimensionsValues.get(0), i);
  
    color.set(intensity, intensity, intensity, _alpha);
  }

}