//
//  TriangleSetParser.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/10/14.
//
//

#ifndef __G3MiOSSDK__TriangleSetParser__
#define __G3MiOSSDK__TriangleSetParser__

#include <stdio.h>
#include <string>

class Mesh;
class IByteBuffer;
class JSONObject;
class Planet;

class TriangleSetParser{
public:
  static Mesh* parseJSON(const JSONObject& json, const Planet* planet);
  static Mesh* parseJSONString(const std::string& json, const Planet* planet);
  static Mesh* parseBSON(const IByteBuffer& bb, const Planet* planet);
};

#endif /* defined(__G3MiOSSDK__TriangleSetParser__) */
