package org.glob3.mobile.generated; 
//
//  JSONDemParser.cpp
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 14/3/16.
//
//

//
//  JSONDemParser.hpp
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 14/3/16.
//
//



//class ShortBufferElevationData;
//class IByteBuffer;
//class Sector;
//class Vector2I;


public class JSONDemParser
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    JSONDemParser();
    private final JSONBaseObject _data;

    public JSONDemParser (String message)
    {
         _data = IJSONParser.instance().parse(message);
    }

    public void dispose()
    {
        if (_data != null)
           _data.dispose();
    }

    public Vector2I getResolution(){
        JSONObject data = _data.asObject();
        return new Vector2I((int) data.getAsNumber("width",0),(int) data.getAsNumber("height",0));
    }

    public final ShortBufferElevationData parseJSONDemElevationData(Sector sector, Vector2I extent, IByteBuffer buffer, short noData)
    {
       return parseJSONDemElevationData(sector, extent, buffer, noData, 0);
    }
    public final ShortBufferElevationData parseJSONDemElevationData(Sector sector, Vector2I extent, IByteBuffer buffer, short noData, double deltaHeight)
    {
    
        final short minValue = IMathUtils.instance().minInt16();
        final int size = extent._x * extent._y;
        final JSONArray dataArray = _data.asObject().getAsArray("data");
        short[] shortBuffer = new short[size];
        for (int i = 0; i < size; i++)
        {
            short height = (short) dataArray.getAsNumber(i, minValue);
            if (height == noData)
            {
                height = ShortBufferElevationData.NO_DATA_VALUE;
            }
            else if (height == minValue)
            {
                height = ShortBufferElevationData.NO_DATA_VALUE;
            }
    
            shortBuffer[i] = height;
        }
    
        short max = (short) _data.asObject().getAsNumber("max", IMathUtils.instance().minInt16());
        short min = (short) _data.asObject().getAsNumber("min", IMathUtils.instance().maxInt16());
        short hasChildren = (short) _data.asObject().getAsNumber("withChildren", 0);
        double geomError = _data.asObject().getAsNumber("similarity", 0);
    
        return new ShortBufferElevationData(sector, extent, sector, extent, shortBuffer, size, deltaHeight,max,min,hasChildren,geomError);
    }

    public static java.util.ArrayList<Double> parseDemMetadata (IByteBuffer buffer)
    {
    
        java.util.ArrayList<Double> res = new java.util.ArrayList<Double>();
    
        final JSONBaseObject parser = IJSONParser.instance().parse(buffer.getAsString());
        if (parser == null)
           return res;
        final JSONArray array = parser.asObject().getAsArray("sectors");
        if (array == null || array.size() == 0)
        {
            if (parser != null)
               parser.dispose();
            return res;
        }
    
        res.add(5 *array.size() +1.0);
        for (int i = 0; i < array.size(); i++)
        {
            res.add(array.getAsObject(i).getAsObject("sector").getAsObject("lower").getAsNumber("lat").value());
            res.add(array.getAsObject(i).getAsObject("sector").getAsObject("lower").getAsNumber("lon").value());
            res.add(array.getAsObject(i).getAsObject("sector").getAsObject("upper").getAsNumber("lat").value());
            res.add(array.getAsObject(i).getAsObject("sector").getAsObject("upper").getAsNumber("lon").value());
            res.add(array.getAsObject(i).getAsNumber("pyrLevel").value());
        }
        if (parser != null)
           parser.dispose();
        return res;
    }
}