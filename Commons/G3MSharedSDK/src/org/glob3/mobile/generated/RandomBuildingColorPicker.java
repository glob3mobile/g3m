package org.glob3.mobile.generated; 
public class RandomBuildingColorPicker extends CityGMLBuildingColorProvider
{


  public RandomBuildingColorPicker()
  {
  }

  public final Color getColor(CityGMLBuilding building)
  {
    double x = (int)(IMathUtils.instance().nextRandomDouble() * 10.0e6);
  
    return Color.fromRGBA255((int)(IMathUtils.instance().nextRandomDouble() * 10.0e6) % 256, (int)(IMathUtils.instance().nextRandomDouble() * 10.0e6) % 256, (int)(IMathUtils.instance().nextRandomDouble() * 10.0e6) % 256, 255);
  }
}