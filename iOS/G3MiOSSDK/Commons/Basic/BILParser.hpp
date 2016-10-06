//
//  BILParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/19/13.
//
//

#ifndef __G3MiOSSDK__BILParser__
#define __G3MiOSSDK__BILParser__

class ShortBufferElevationData;
class IByteBuffer;
class Sector;
class Vector2I;
class ShortBufferTerrainElevationGrid;

class BILParser {
private:
  BILParser() {}

  static short* pvtParse(const int          size,
                         const IByteBuffer* buffer,
                         const short        noDataValue);

public:

  static ShortBufferElevationData* oldParseBIL16(const Sector&      sector,
                                                 const Vector2I&    extent,
                                                 const IByteBuffer* buffer,
                                                 const double       deltaHeight = 0);

  static ShortBufferTerrainElevationGrid* parseBIL16(const Sector&      sector,
                                                     const Vector2I&    extent,
                                                     const IByteBuffer* buffer,
                                                     const short        noDataValue,
                                                     const double       deltaHeight = 0);
  
};

#endif
