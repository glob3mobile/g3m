//
//  XPCDimension.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#ifndef XPCDimension_hpp
#define XPCDimension_hpp

#include <vector>
#include <string>

class JSONArray;
class JSONObject;


class XPCDimension {

  static XPCDimension* fromJSON(const JSONObject* jsonObject);

public:

  static const std::vector<XPCDimension*>* fromJSON(const JSONArray* jsonArray);

  ~XPCDimension() {

  }

private:
  const std::string _name;
  const std::string _type;


  XPCDimension(const std::string& name,
               const std::string& type) :
  _name(name),
  _type(type)
  {

  }


};

#endif
