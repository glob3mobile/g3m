package org.glob3.mobile.generated;
//
//  XPCRGBPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/20/21.
//

//
//  XPCRGBPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/20/21.
//




public class XPCRGBPointColorizer extends XPCPointColorizer
{
  private final String _redDimensionName;
  private final String _greenDimensionName;
  private final String _blueDimensionName;

  private int _redDimensionIndex;
  private int _greenDimensionIndex;
  private int _blueDimensionIndex;
  private boolean _ok;


  public XPCRGBPointColorizer()
  {
     _redDimensionName = "Red";
     _greenDimensionName = "Green";
     _blueDimensionName = "Blue";
     _redDimensionIndex = -1;
     _greenDimensionIndex = -1;
     _blueDimensionIndex = -1;
     _ok = false;
  
  }

  public XPCRGBPointColorizer(String redDimensionName, String greenDimensionName, String blueDimensionName)
  {
     _redDimensionName = redDimensionName;
     _greenDimensionName = greenDimensionName;
     _blueDimensionName = blueDimensionName;
     _redDimensionIndex = -1;
     _greenDimensionIndex = -1;
     _blueDimensionIndex = -1;
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
  
      if (_redDimensionName.equals(dimensionName))
      {
        _redDimensionIndex = i;
      }
      else if (_greenDimensionName.equals(dimensionName))
      {
        _greenDimensionIndex = i;
      }
      else if (_blueDimensionName.equals(dimensionName))
      {
        _blueDimensionIndex = i;
      }
    }
  
    _ok = (_redDimensionIndex >= 0) && (_greenDimensionIndex >= 0) && (_blueDimensionIndex >= 0);
  
    if (!_ok)
    {
      ILogger.instance().logError("Can't find Red, Green and Blue dimensions");
      return null;
    }
  
    IIntBuffer requiredDimensionIndices = IFactory.instance().createIntBuffer(3);
    requiredDimensionIndices.put(0, _redDimensionIndex);
    requiredDimensionIndices.put(1, _greenDimensionIndex);
    requiredDimensionIndices.put(2, _blueDimensionIndex);
  
    return requiredDimensionIndices;
  }

  public final Color colorize(XPCMetadata metadata, XPCPoint point)
  {
    if (!_ok)
    {
      return Color.RED;
    }
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning DIEGO AT WORK
    return Color.YELLOW;
  }

}