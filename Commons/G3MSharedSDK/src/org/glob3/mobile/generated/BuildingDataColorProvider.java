package org.glob3.mobile.generated; 
public class BuildingDataColorProvider extends CityGMLBuildingColorProvider
{

  private final ColorLegend _legend;
  private final String _propertyName;




  public BuildingDataColorProvider(String propertyName, ColorLegend colorLegend)
  {
     _propertyName = propertyName;
     _legend = colorLegend;
  }

  public final Color getColor(CityGMLBuilding building)
  {
    double value = building.getNumericProperty(_propertyName);
    if ((value != value))
    {
      return Color.gray();
    }
    return _legend.getColor(value);
  }

}