package org.glob3.mobile.generated; 
//
//  CompositeElevationData.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/04/13.
//
//

//
//  CompositeElevationData.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/04/13.
//
//





public class CompositeElevationData extends ElevationData
{
  private java.util.ArrayList<ElevationData> _data = new java.util.ArrayList<ElevationData>();
  private boolean _hasNoData;

  private final Interpolator _interpolator;

  /** ElevationData's are giving in order. First the most significant one.
   */
  public CompositeElevationData(ElevationData data)
  {
     super(data.getSector(), data.getExtent());
     _hasNoData = data.hasNoData();
     _interpolator = new BilinearInterpolator();
    _data.add(data);
  }

  public final void addElevationData(ElevationData data)
  {
  //  ElevationData* d0 = _data[0];
  
    if ((data.getExtentWidth() != _width) || (data.getExtentHeight() != _height))
    {
      ILogger.instance().logError("Extents don't match.");
    }
  
  //  Sector s = data->getSector();
  //  Sector s2 = d0->getSector();
  
    if (!data.getSector().isEqualsTo(getSector()))
    {
      ILogger.instance().logError("Sectors don't match.");
    }
  
  
    final IMathUtils mu = IMathUtils.instance();
    _data.add(data);
  
    //Checking NoData
    for (int i = 0; i < _width; i++)
    {
      for (int j = 0; j < _height; j++)
      {
        double height = getElevationAt(i, j);
        if (mu.isNan(height))
        {
          _hasNoData = true;
          return;
        }
      }
    }
  }

  public void dispose()
  {
    int s = _data.size();
    for (int i = 0; i < s; i++)
    {
      if (_data.get(i) != null)
         _data.get(i).dispose();
    }
    if (_interpolator != null)
       _interpolator.dispose();
  }

  public final double getElevationAt(int x, int y)
  {
    final IMathUtils mu = IMathUtils.instance();
    final int size = _data.size();
    for (int i = 0; i < size; i++)
    {
      final double h = _data.get(i).getElevationAt(x, y);
      if (!mu.isNan(h))
      {
        return h;
      }
    }
  
    return mu.NanD();
  }

  public final String description(boolean detailed)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(CompositeElevationData extent=");
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
  
    for (int i = 0; i < _width; i++)
    {
      for (int j = 0; j < _height; j++)
      {
        final double height = getElevationAt(i, j);
        if (!mu.isNan(height))
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

  public final Sector getSector()
  {
    return _sector;
  }

  public final boolean hasNoData()
  {
    return _hasNoData;
  }

//  const Geodetic2D getRealResolution() const;

}
//const Geodetic2D CompositeElevationData::getRealResolution() const {
//  int _TODO_complete;
//  aa;
//}
