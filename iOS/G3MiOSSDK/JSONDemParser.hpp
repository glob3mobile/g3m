//
//  JSONDemParser.hpp
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 14/3/16.
//
//

#ifndef __G3MiOSSDK__JSONDemParser_hpp
#define __G3MiOSSDK__JSONDemParser_hpp

#include "IJSONParser.hpp"
#include "JSONObject.hpp"
#include "Vector2I.hpp"
#include <stdio.h>
#include <sstream>

class ShortBufferElevationData;
class IByteBuffer;
class Sector;
class Vector2I;


class JSONDemParser {
private:
    JSONDemParser();
    
public:
    
#ifdef C_CODE
    static const Vector2I* getResolution(const IByteBuffer *buffer){
        const JSONObject *data = IJSONParser::instance()->parse(buffer->getAsString())->asObject();
        return new Vector2I((int) data->getAsNumber("width",0),(int) data->getAsNumber("height",0));
    }
#endif
#ifdef JAVA_CODE
    public static Vector2I getResolution(IByteBuffer buffer){
        JSONObject data = IJSONParser.instance().parse(buffer.getAsString()).asObject();
        return new Vector2I((int) data.getAsNumber("width",0),(int) data.getAsNumber("height",0));
    }
#endif
    
    static ShortBufferElevationData* parseJSONDemElevationData(const Sector& sector,
                                                const Vector2I& extent,
                                                const IByteBuffer* buffer,
                                                const short noData,
                                                double deltaHeight = 0);
    
    static double* parseDemMetadata( const IByteBuffer *buffer);
};


#endif /* JSONDemParser_hpp */
