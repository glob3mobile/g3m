package org.glob3.mobile.generated; 
//
//  CompositeElevationData.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/04/13.
//
//

//
//  CompositeElevationData.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/04/13.
//
//




public abstract class CompositeElevationData extends ElevationData
{
  private java.util.ArrayList<ElevationData> _data = new java.util.ArrayList<ElevationData>();
  private boolean _hasNoData;

  private final Interpolator _interpolator;

  /** ElevationData's are giving in order. First the most significant one.
   */
  public CompositeElevationData(ElevationData data)
  {
     super(data.getSector(), data.getResolution());
     _hasNoData = data.hasNoData();
     _interpolator = new BilinearInterpolator();
    if (data == null)
    {
      ILogger.instance().logError("Invalid Elevation Data in Composite");
    }
    _data.add(data);
  }

  public final void addElevationData(ElevationData data)
  {
  
    ElevationData d0 = _data.get(0);
  
    if (data.getExtent()._x != _width || data.getExtent()._y != _height)
    {
      ILogger.instance().logError("Extents don't match.");
    }
  
    Sector s = data.getSector();
    Sector s2 = d0.getSector();
  
    if (!data.getSector().isEqualsTo(getSector()))
    {
      ILogger.instance().logError("Sectors don't match.");
    }
  
  
    final IMathUtils mu = IMathUtils.instance();
    _data.add(data);
  
  
    //Checking NoData
    int type;
    for (int i = 0; i < _width; i++)
    {
      for (int j = 0; j < _height; j++)
      {
        double height = getElevationAt(i, j, type);
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

  public final Vector2I getExtent()
  {
    return _data.get(0).getExtent();
  }

  public final int getExtentWidth()
  {
    return _data.get(0).getExtent()._x;
  }

  public final int getExtentHeight()
  {
    return _data.get(0).getExtent()._y;
  }

  public final double getElevationAt(int x, int y, int type)
  {
     return getElevationAt(x, y, type, IMathUtils.instance().NanD());
  }
  public final double getElevationAt(int x, int y, int type, double valueForNoData)
  {
    IMathUtils mu = IMathUtils.instance();
    int s = _data.size();
    for (int i = 0; i < s; i++)
    {
      double h = _data.get(i).getElevationAt(x, y, type);
      if (!mu.isNan(h))
      {
        return h;
      }
    }
  
    return valueForNoData;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  double getElevationAt(Angle latitude, Angle longitude, int type, double valueForNoData);

  public final String description(boolean detailed)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(CompositeElevationData extent=");
    isb.addInt(_width);
    isb.addString("x");
    isb.addInt(_height);
    isb.addString(" sector=");
    isb.addString(_sector.description());
    int unusedType = -1;
    if (detailed)
    {
      isb.addString("\n");
      for (int row = 0; row < _width; row++)
      {
        //isb->addString("   ");
        for (int col = 0; col < _height; col++)
        {
          isb.addDouble(getElevationAt(col, row, unusedType));
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

  public final Vector3D getMinMaxAverageHeights()
  {
  
    final IMathUtils mu = IMathUtils.instance();
    double minHeight = mu.maxDouble();
    double maxHeight = mu.minDouble();
    double sumHeight = 0.0;
  
    int type;
    for (int i = 0; i < _width; i++)
    {
      for (int j = 0; j < _height; j++)
      {
        final double height = getElevationAt(i, j, type);
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

  public final Mesh createMesh(Ellipsoid ellipsoid, float verticalExaggeration, Geodetic3D positionOffset, float pointSize)
  {
    return null;
  }

  public final Sector getSector()
  {
    return _sector;
  }

  public final boolean hasNoData()
  {
    return _hasNoData;
  }


}