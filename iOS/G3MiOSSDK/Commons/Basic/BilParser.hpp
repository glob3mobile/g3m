//
//  BilParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/19/13.
//
//

#ifndef __G3MiOSSDK__BilParser__
#define __G3MiOSSDK__BilParser__

class ShortBufferElevationData;
class IByteBuffer;
class Sector;
class Vector2I;


class BilParser {
private:
  BilParser();

public:

  static ShortBufferElevationData* parseBil16(const Sector& sector,
                                              const Vector2I& extent,
                                              const IByteBuffer* buffer,
                                              double deltaHeight = 0);
    
  static ShortBufferElevationData* parseBil16MaxMin(const Sector& sector, const Vector2I &extent, IByteBuffer *buffer, double deltaHeight = 0);
    
  static ShortBufferElevationData* parseBil16Redim (const Sector& sector, IByteBuffer *buffer, double deltaHeight);
};

#endif
