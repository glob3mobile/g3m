//
//  BilParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/19/13.
//
//

#ifndef __G3MiOSSDK__BilParser__
#define __G3MiOSSDK__BilParser__

class ElevationData;
class IByteBuffer;
#include "Vector2I.hpp"

class BilParser {
private:
  BilParser();
  
public:

  static ElevationData* parseBil16(const IByteBuffer* buffer,
                                   const Vector2I& extent);

};

#endif
