//
//  JSONDemParser.cpp
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 14/3/16.
//
//

#include "JSONDemParser.hpp"

#include "IMathUtils.hpp"
#include "JSONBaseObject.hpp"
#include "JSONArray.hpp"
#include "JSONNumber.hpp"
#include "JSONInteger.hpp"
#include "ShortBufferElevationData.hpp"

ShortBufferElevationData* JSONDemParser::parseJSONDemElevationData(const Sector& sector,
                                              const Vector2I& extent,
                                              const IByteBuffer* buffer,
                                              const short noData,
                                              double deltaHeight){
    const JSONBaseObject *data = IJSONParser::instance()->parse(buffer->getAsString());
    const short minValue = IMathUtils::instance()->minInt16();
    const int size = extent._x * extent._y;
    const JSONArray *dataArray = data->asObject()->getAsArray("data");
    short *shortBuffer = new short[size];
    for (int i = 0; i < size; i++)
    {
        short height = (short) dataArray->getAsNumber(i, minValue);
        if (height == noData)
        {
            height = ShortBufferElevationData::NO_DATA_VALUE;
        }
        else if (height == minValue)
        {
            height = ShortBufferElevationData::NO_DATA_VALUE;
        }
        
        shortBuffer[i] = height;
    }
    
    short max = (short) data->asObject()->getAsNumber("max",IMathUtils::instance()->minInt16());
    short min = (short) data->asObject()->getAsNumber("min",IMathUtils::instance()->maxInt16());
    short hasChildren = (short) data->asObject()->getAsNumber("withChildren",0);
    double geomError = data->asObject()->getAsNumber("similarity",0);
    
    delete data;
    
    return new ShortBufferElevationData(sector, extent, sector, extent, shortBuffer,
                                        size, deltaHeight,max,min,hasChildren,geomError);
}

double * JSONDemParser::parseDemMetadata (const IByteBuffer *buffer){
    
    const JSONBaseObject* parser = IJSONParser::instance()->parse(buffer->getAsString());
    if (parser == NULL) return NULL;
    const JSONArray* array = parser->asObject()->getAsArray("sectors");
    if (array == NULL || array->size() == 0) {
        delete parser;
        return NULL;
    }
    double * res = new double[5*array->size() +1];
    res[0] = 5*array->size() +1;
    for (size_t i=0; i < array->size(); i++){
        res[i*5 + 1] = array->getAsObject(i)->getAsObject("sector")->getAsObject("lower")->getAsNumber("lat")->value();
        res[i*5 + 2] = array->getAsObject(i)->getAsObject("sector")->getAsObject("lower")->getAsNumber("lon")->value();
        res[i*5 + 3] = array->getAsObject(i)->getAsObject("sector")->getAsObject("upper")->getAsNumber("lat")->value();
        res[i*5 + 4] = array->getAsObject(i)->getAsObject("sector")->getAsObject("upper")->getAsNumber("lon")->value();
        res[i*5 + 5] = array->getAsObject(i)->getAsNumber("pyrLevel")->value();
    }
    delete parser;
    return res;
}
