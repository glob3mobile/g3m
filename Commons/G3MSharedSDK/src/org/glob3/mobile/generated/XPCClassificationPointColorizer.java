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






public class XPCClassificationPointColorizer extends XPCFixedAlphaPointColorizer
{

  private final String _classificationDimensionName;

  private int _classificationDimensionIndex;

  private boolean _ok;

  private final java.util.ArrayList<Color> _colors = new java.util.ArrayList<Color>();

  private static void initializeColors(java.util.ArrayList<Color> colors, float alpha)
  {
    final int alpha255 = IMathUtils.instance().round(alpha * 255.0f);
  
    colors.add(Color.fromRGBA255(215, 30, 30, alpha255)); // 0 - Never Classified
    colors.add(Color.fromRGBA255(219, 61, 61, alpha255)); // 1 - Unclassified
    colors.add(Color.fromRGBA255(255, 0, 255, alpha255)); // 2 - Ground
    colors.add(Color.fromRGBA255(0, 143, 0, alpha255)); // 3 - Low Vegetation
    colors.add(Color.fromRGBA255(0, 191, 0, alpha255)); // 4 - Medium Vegetation
    colors.add(Color.fromRGBA255(0, 255, 0, alpha255)); // 5 - High Vegetation
    colors.add(Color.fromRGBA255(255, 0, 0, alpha255)); // 6 - Building
    colors.add(Color.fromRGBA255(207, 44, 44, alpha255)); // 7 - Low Point / Noise
    colors.add(Color.fromRGBA255(255, 255, 0, alpha255)); // 8 - Key Point / Reserved
    colors.add(Color.fromRGBA255(0, 0, 255, alpha255)); // 9 - Water
    colors.add(Color.fromRGBA255(0, 224, 224, alpha255)); // 10 - Rail
    colors.add(Color.fromRGBA255(224, 0, 224, alpha255)); // 11 - Road Surface
    colors.add(Color.fromRGBA255(66, 249, 63, alpha255)); // 12 - Reserved
    colors.add(Color.fromRGBA255(189, 158, 3, alpha255)); // 13 - Wire Guard / Shield
    colors.add(Color.fromRGBA255(89, 214, 125, alpha255)); // 14 - Wire Conductor / Phase
    colors.add(Color.fromRGBA255(147, 163, 120, alpha255)); // 15 - Transmission Tower
    colors.add(Color.fromRGBA255(123, 50, 42, alpha255)); // 16 - Wire Structure Connection
    colors.add(Color.fromRGBA255(36, 120, 60, alpha255)); // 17 - Bridge Deck
    colors.add(Color.fromRGBA255(87, 38, 65, alpha255)); // 18 - High Noise
  }


  public XPCClassificationPointColorizer(float alpha)
  {
     super(alpha);
     _classificationDimensionName = "Classification";
     _classificationDimensionIndex = -1;
     _ok = false;
    initializeColors(_colors, _alpha);
  }

  public XPCClassificationPointColorizer(String classificationDimensionName, float alpha)
  {
     super(alpha);
     _classificationDimensionName = classificationDimensionName;
     _classificationDimensionIndex = -1;
     _ok = false;
    initializeColors(_colors, _alpha);
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

  public final void colorize(XPCMetadata metadata, double[] heights, java.util.ArrayList<IByteBuffer> dimensionsValues, int i, MutableColor color)
  {
    if (!_ok)
    {
      color.set(1, 0, 0, 1);
      return;
    }
  
    final byte classification = dimensionsValues.get(0).get(i);
  
    if (classification >= _colors.size())
    {
      color.set(1, 0, 0, 1);
      return;
    }
  
    color.set(_colors.get(classification));
  }

}