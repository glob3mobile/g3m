//
//  XPCRGBPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/20/21.
//

#ifndef XPCRGBPointColorizer_hpp
#define XPCRGBPointColorizer_hpp

#include "XPCPointColorizer.hpp"


class XPCRGBPointColorizer : public XPCPointColorizer {
private:
  const std::string _redDimensionName;
  const std::string _greenDimensionName;
  const std::string _blueDimensionName;

  int _redDimensionIndex;
  int _greenDimensionIndex;
  int _blueDimensionIndex;
  bool _ok;

public:

  XPCRGBPointColorizer();

  XPCRGBPointColorizer(const std::string& redDimensionName,
                       const std::string& greenDimensionName,
                       const std::string& blueDimensionName);

  ~XPCRGBPointColorizer();

  void initialize(const XPCMetadata* metadata);

  Color colorize(const XPCMetadata* metadata,
                 const XPCPoint* point);

};

#endif
