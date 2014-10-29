package org.glob3.mobile.generated; 
//
//  TriangleSetParser.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/10/14.
//
//

//
//  TriangleSetParser.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/10/14.
//
//



//class Mesh;
//class IByteBuffer;
//class JSONObject;
//class Planet;

public class TriangleSetParser
{
  public static Mesh parseJSON(JSONObject json, Planet planet)
  {
  
    final JSONArray tss = json.get("triangleSets").asArray();
  
    CompositeMesh cm = new CompositeMesh();
  
    for(int i = 0; i < tss.size(); i++)
    {
      JSONObject ts = (JSONObject) tss.get(i);
      JSONArray coord = (JSONArray) ts.get("coordinates");
      JSONArray indices = (JSONArray) ts.get("indices");
      JSONArray color = (JSONArray) ts.get("color");
  
      FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
      for (int j = 0; j < coord.size(); j+=2)
      {
        double lat = coord.get(j).asNumber().value();
        double lon = coord.get(j+1).asNumber().value();
        fbb.add(Angle.fromDegrees(lon), Angle.fromDegrees(lat), 0); //LON - LAT
      }
  
      short maxIndex = 0;
      ShortBufferBuilder sbb = new ShortBufferBuilder();
      for (int j = 0; j < indices.size(); j++)
      {
        short index = (short) indices.get(j).asNumber().value();
  
        if (index > maxIndex)
        {
          maxIndex = index;
        }
  
        sbb.add(index);
      }
  
  //    if (maxIndex * 3 != fbb->size()){
  //      printf("OH OH");
  //    }
  
      double r = color.get(0).asNumber().value() / 255.0;
      double g = color.get(1).asNumber().value() / 255.0;
      double b = color.get(2).asNumber().value() / 255.0;
  
  
      IndexedMesh im = new IndexedMesh(GLPrimitive.triangles(), true, fbb.getCenter(), fbb.create(), sbb.create(), 10.0, 1, Color.newFromRGBA((float)r, (float)g, (float)b, 0.5), null, 0.0, false);
  
      cm.addMesh(im);
  
    }
  
    return cm;
  
  }
  public static Mesh parseJSONString(String json, Planet planet)
  {
  
    IJSONParser jsonParser = IJSONParser.instance();
    final JSONObject obj = (JSONObject)jsonParser.parse(json);
  
    return parseJSON(obj, planet);
  }
  public static Mesh parseBSON(IByteBuffer bb, Planet planet)
  {
  
    final JSONObject obj = (JSONObject) BSONParser.parse(bb);
    return parseJSON(obj, planet);
  }
}