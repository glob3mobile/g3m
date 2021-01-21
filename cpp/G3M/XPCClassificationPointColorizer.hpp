//
//  XPCClassificationPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/21/21.
//

#ifndef XPCClassificationPointColorizer_hpp
#define XPCClassificationPointColorizer_hpp

#include "XPCPointColorizer.hpp"


class XPCClassificationPointColorizer : public XPCPointColorizer {
private:

  const static Color COLORS[];


  const std::string _classificationDimensionName;
  
  int _classificationDimensionIndex;
  
  bool _ok;
  
public:
  
  XPCClassificationPointColorizer();
  
  XPCClassificationPointColorizer(const std::string& classificationDimensionName);
  
  ~XPCClassificationPointColorizer();
  
  IIntBuffer* initialize(const XPCMetadata* metadata);
  
  Color colorize(const XPCMetadata* metadata,
                 const std::vector<XPCPoint*>* points,
                 const std::vector<const IByteBuffer*>* dimensionsValues,
                 const size_t i);
  
};

#endif
