package org.glob3.mobile.generated; 
//
//  PyramidElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 4/3/16.
//
//

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#import "PyramidElevationDataProvider.hpp"

public class MetadataListener extends IBufferDownloadListener
{
  public MetadataListener(java.util.ArrayList<PyramidComposition> itself)
  {
     _itself = itself;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {

    final String str = buffer.getAsString();

    IJSONParser parser = IJSONParser.instance();
    final JSONArray array = parser.parse(str).asObject().getAsArray("sectors");
    if (array == null)
    {
      throw new RuntimeException("Problem parsing at PyramidElevationDataProvider::MetadataListener::onDownload().");
    }

    for (int i = 0; i<array.size(); i++)
    {
      _itself.add(PyramidComposition(getLowerLat(array, i),getLowerLon(array, i),getUpperLat(array, i),getUpperLon(array, i),getLevel(array, i)));
    }
  }
  public final void onError(URL url)
  {
  }
  public final void onCancel(URL url)
  {
  }

  public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
  {
  }

  private java.util.ArrayList<PyramidComposition> _itself;
  private final G3MContext _context;

  private double getUpperLat(JSONArray array, int index)
  {
    JSONDouble doble = (JSONDouble) array.getAsObject(index).getAsObject("sector").getAsObject("upper").getAsNumber("lat");
    return doble.value();
  }

  private double getLowerLat(JSONArray array, int index)
  {
    JSONDouble doble = (JSONDouble) array.getAsObject(index).getAsObject("sector").getAsObject("lower").getAsNumber("lat");
    return doble.value();
  }

  private double getUpperLon(JSONArray array, int index)
  {
    JSONDouble doble = (JSONDouble) array.getAsObject(index).getAsObject("sector").getAsObject("upper").getAsNumber("lon");
    return doble.value();
  }

  private double getLowerLon(JSONArray array, int index)
  {
    JSONDouble doble = (JSONDouble)array.getAsObject(index).getAsObject("sector").getAsObject("lower").getAsNumber("lon");
    return doble.value();
  }

  private int getLevel(JSONArray array, int index)
  {
    JSONInteger integer = (JSONInteger) array.getAsObject(index).getAsNumber("pyrLevel");
    return integer.intValue();
  }
}