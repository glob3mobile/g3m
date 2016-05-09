package org.glob3.mobile.generated; 
public class CityGMLParsingListener extends CityGMLListener
{

  private CityGMLRenderer _demo;
  private final IThreadUtils _threadUtils;
  private CityGMLRendererListener _listener;
  private boolean _autoDelete;

  public CityGMLParsingListener(CityGMLRenderer demo, CityGMLRendererListener listener, boolean autoDelete)
  {
     _demo = demo;
     _listener = listener;
     _autoDelete = autoDelete;

  }

  public static class MyLis extends CityGMLRendererListener
  {
    public void onBuildingsLoaded(java.util.ArrayList<CityGMLBuilding> buildings)
    {
    }

  }

  public void onBuildingsCreated(java.util.ArrayList<CityGMLBuilding> buildings)
  {
    _demo.addBuildings(buildings, _listener, _autoDelete);
  }

  public void onError()
  {

  }
}