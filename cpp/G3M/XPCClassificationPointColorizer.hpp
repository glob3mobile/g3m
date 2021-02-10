//
//  XPCClassificationPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/21/21.
//

#ifndef XPCClassificationPointColorizer_hpp
#define XPCClassificationPointColorizer_hpp

#include "XPCPointColorizer.hpp"

#include <vector>


class XPCClassificationPointColorizer : public XPCPointColorizer {
private:

  const std::string _classificationDimensionName;
  const float       _alpha;

  int _classificationDimensionIndex;
  
  bool _ok;

  std::vector<const Color> _colors;

  static void initializeColors(std::vector<const Color>& colors,
                               const float alpha);

public:
  
  XPCClassificationPointColorizer(const float alpha);
  
  XPCClassificationPointColorizer(const std::string& classificationDimensionName,
                                  const float alpha);
  
  ~XPCClassificationPointColorizer();
  
  IIntBuffer* initialize(const XPCMetadata* metadata);
  
  Color colorize(const XPCMetadata* metadata,
                 const std::vector<const IByteBuffer*>* dimensionsValues,
                 const size_t i);
  
};

#endif
