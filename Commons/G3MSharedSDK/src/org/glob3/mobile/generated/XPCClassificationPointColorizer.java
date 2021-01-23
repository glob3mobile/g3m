package org.glob3.mobile.generated;
//
//  XPCClassificationPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/21/21.
//

//
//  XPCClassificationPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/21/21.
//




public class XPCClassificationPointColorizer extends XPCPointColorizer
{

  private static final Color[] COLORS = { Color.fromRGBA255(215, 30, 30), Color.fromRGBA255(219, 61, 61), Color.fromRGBA255(255, 0, 255), Color.fromRGBA255(0, 143, 0), Color.fromRGBA255(0, 191, 0), Color.fromRGBA255(0, 255, 0), Color.fromRGBA255(255, 0, 0), Color.fromRGBA255(207, 44, 44), Color.fromRGBA255(255, 255, 0), Color.fromRGBA255(0, 0, 255), Color.fromRGBA255(0, 224, 224), Color.fromRGBA255(224, 0, 224), Color.fromRGBA255(66, 249, 63), Color.fromRGBA255(189, 158, 3), Color.fromRGBA255(89, 214, 125), Color.fromRGBA255(147, 163, 120), Color.fromRGBA255(123, 50, 42), Color.fromRGBA255(36, 120, 60), Color.fromRGBA255(87, 38, 65) };


  private final String _classificationDimensionName;

  private int _classificationDimensionIndex;

  private boolean _ok;


  public XPCClassificationPointColorizer()
  {
     _classificationDimensionName = "Classification";
     _classificationDimensionIndex = -1;
     _ok = false;
  
  }

  public XPCClassificationPointColorizer(String classificationDimensionName)
  {
     _classificationDimensionName = classificationDimensionName;
     _classificationDimensionIndex = -1;
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
  
      if (_classificationDimensionName.equals(dimensionName))
      {
        _classificationDimensionIndex = i;
      }
    }
  
    _ok = (_classificationDimensionIndex >= 0);
  
    if (!_ok)
    {
      ILogger.instance().logError("Can't find Classification dimensions");
      return null;
    }
  
    IIntBuffer requiredDimensionIndices = IFactory.instance().createIntBuffer(1);
    requiredDimensionIndices.put(0, _classificationDimensionIndex);
    return requiredDimensionIndices;
  }

  public final Color colorize(XPCMetadata metadata, java.util.ArrayList<IByteBuffer> dimensionsValues, int i)
  {
    if (!_ok)
    {
      return Color.RED;
    }
  
    byte classification = dimensionsValues.get(0).get(i);
    return (classification > 18) ? Color.RED : COLORS[classification];
  }

}