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


    public static Vector2I getResolution(IByteBuffer buffer){
        JSONObject data = IJSONParser.instance().parse(buffer.getAsString()).asObject();
        return new Vector2I((int) data.getAsNumber("width",0),(int) data.getAsNumber("height",0));
    }

    public static ShortBufferElevationData parseJSONDemElevationData(Sector sector, Vector2I extent, IByteBuffer buffer, short noData)
    {
       return parseJSONDemElevationData(sector, extent, buffer, noData, 0);
    }
    public static ShortBufferElevationData parseJSONDemElevationData(Sector sector, Vector2I extent, IByteBuffer buffer, short noData, double deltaHeight)
    {
        final JSONBaseObject data = IJSONParser.instance().parse(buffer.getAsString());
        final short minValue = IMathUtils.instance().minInt16();
        final int size = extent._x * extent._y;
        final JSONArray dataArray = data.asObject().getAsArray("data");
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
    
        short max = (short) data.asObject().getAsNumber("max", IMathUtils.instance().minInt16());
        short min = (short) data.asObject().getAsNumber("min", IMathUtils.instance().maxInt16());
        short hasChildren = (short) data.asObject().getAsNumber("withChildren", 0);
        double geomError = data.asObject().getAsNumber("similarity", 0);
    
        if (data != null)
           data.dispose();
    
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
    
        res.add(5 *array.size() +1);
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