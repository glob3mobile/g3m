//
//  TriangleSetParser.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/10/14.
//
//

#include "TriangleSetParser.hpp"

#include "IJSONParser.hpp"
#include "JSONArray.hpp"
#include "JSONObject.hpp"
#include "JSONNumber.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"
#include "CompositeMesh.hpp"
#include "BSONParser.hpp"

Mesh* TriangleSetParser::parseJSON(const JSONObject& json, const Planet* planet){
  
  const JSONArray* tss = json.get("triangleSets")->asArray();
  
  CompositeMesh* cm = new CompositeMesh();
  
  for(int i = 0; i < tss->size(); i++){
    JSONObject* ts = (JSONObject*) tss->get(i);
    JSONArray* coord = (JSONArray*) ts->get("coordinates");
    JSONArray* indices = (JSONArray*) ts->get("indices");
    JSONArray* color = (JSONArray*) ts->get("color");
    
    FloatBufferBuilderFromGeodetic* fbb = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);
    for (int j = 0; j < coord->size(); j+=2) {
      double lat = coord->get(j)->asNumber()->value();
      double lon = coord->get(j+1)->asNumber()->value();
      fbb->add(Angle::fromDegrees(lon), //LAT
               Angle::fromDegrees(lat), //LON
               0);
    }
    
    short maxIndex = 0;
    ShortBufferBuilder sbb;
    for (int j = 0; j < indices->size(); j++) {
      short index = (short) indices->get(j)->asNumber()->value();
      
      if (index > maxIndex){
        maxIndex = index;
      }
      
      sbb.add(index);
    }
    
//    if (maxIndex * 3 != fbb->size()){
//      printf("OH OH");
//    }
    
    double r = color->get(0)->asNumber()->value() / 255.0;
    double g = color->get(1)->asNumber()->value() / 255.0;
    double b = color->get(2)->asNumber()->value() / 255.0;
    
    
    IndexedMesh* im = new IndexedMesh(GLPrimitive::triangles(),
                                      true,
                                      fbb->getCenter(),
                                      fbb->create(),
                                      sbb.create(),
                                      (float)10.0,
                                      (float)1,
                                      Color::newFromRGBA((float)r,(float)g,(float)b, (float)0.5),
                                      NULL,
                                      (float)0.0,
                                      false);
    
    cm->addMesh(im);
    
  }
  
  return cm;

}

Mesh* TriangleSetParser::parseJSONString(const std::string& json, const Planet* planet){
  
  IJSONParser* jsonParser = IJSONParser::instance();
  const JSONObject* obj = (JSONObject*)jsonParser->parse(json);
  
  return parseJSON(*obj, planet);
}

Mesh* TriangleSetParser::parseBSON(const IByteBuffer& bb, const Planet* planet){
  
  const JSONObject* obj = (JSONObject*) BSONParser::parse(&bb);
  return parseJSON(*obj, planet);
}