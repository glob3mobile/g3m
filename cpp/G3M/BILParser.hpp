//
//  BILParser.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/19/13.
//
//

#ifndef __G3M__BILParser__
#define __G3M__BILParser__

class ShortBufferElevationData;
class IByteBuffer;
class Sector;
class Vector2I;
class ShortBufferDEMGrid;

class BILParser {
private:
  BILParser() {}

public:

  static ShortBufferElevationData* oldParseBIL16(const Sector&      sector,
                                                 const Vector2I&    extent,
                                                 const IByteBuffer* buffer,
                                                 const double       deltaHeight = 0);

  static ShortBufferDEMGrid* parseBIL16(const Sector&      sector,
                                        const Vector2I&    extent,
                                        const IByteBuffer* buffer,
                                        const short        noDataValue,
                                        const double       deltaHeight = 0);

};

#endif
