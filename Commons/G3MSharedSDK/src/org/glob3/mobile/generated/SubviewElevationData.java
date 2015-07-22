package org.glob3.mobile.generated; 
//
//  SubviewElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

//
//  SubviewElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//



//class IFloatBuffer;

public class SubviewElevationData extends ElevationData
{
  protected float[] _buffer;
  protected boolean _hasNoData;

  protected SubviewElevationData(ElevationData elevationData, Sector sector, Vector2I extent)
  {
     super(sector, extent);
     _buffer = new float[_width * _height];
    if ((elevationData == null) || (elevationData.getExtentWidth() < 1) || (elevationData.getExtentHeight() < 1))
    {
      ILogger.instance().logError("SubviewElevationData can't subview given elevation data.");
      _buffer = null;
      return;
    }
  
    _hasNoData = false;
  }


  public void dispose()
  {
    _buffer = null;
  
    super.dispose();
  
  }

  public final double getElevationAt(int x, int y)
  {
  
    final int index = ((_height-1-y) * _width) + x;
  
    return _buffer[index];
  }

  public final String description(boolean detailed)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(SubviewElevationData extent=");
    isb.addInt(_width);
    isb.addString("x");
    isb.addInt(_height);
    isb.addString(" sector=");
    isb.addString(_sector.description());
    if (detailed)
    {
      isb.addString("\n");
      for (int row = 0; row < _width; row++)
      {
        //isb->addString("   ");
        for (int col = 0; col < _height; col++)
        {
          isb.addDouble(getElevationAt(col, row));
          isb.addString(",");
        }
        isb.addString("\n");
      }
    }
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  
  }

  public final Vector3D getMinMaxAverageElevations()
  {
    final IMathUtils mu = IMathUtils.instance();
  
    double minHeight = mu.maxDouble();
    double maxHeight = mu.minDouble();
    double sumHeight = 0.0;
  
    for (int x = 0; x < _width; x++)
    {
      for (int y = 0; y < _height; y++)
      {
        final double height = getElevationAt(x, y);
        if (!(height != height))
        {
          if (height < minHeight)
          {
            minHeight = height;
          }
          if (height > maxHeight)
          {
            maxHeight = height;
          }
          sumHeight += height;
        }
      }
    }
  
    if (minHeight == mu.maxDouble())
    {
      minHeight = 0;
    }
    if (maxHeight == mu.minDouble())
    {
      maxHeight = 0;
    }
  
    return new Vector3D(minHeight, maxHeight, sumHeight / (_width * _height));
  }

  public final boolean hasNoData()
  {
     return _hasNoData;
  }

}