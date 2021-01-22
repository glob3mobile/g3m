//
//  XPCPoint.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/19/21.
//

#ifndef XPCPoint_hpp
#define XPCPoint_hpp

class ByteBufferIterator;


class XPCPoint {
public:
  static XPCPoint* fromByteBufferIterator(ByteBufferIterator& it,
                                          const float centerLatitudeDegrees,
                                          const float centerLongitudeDegrees,
                                          const float centerHeight);


  const double _latitudeDegrees;
  const double _longitudeDegrees;
  const double _height;

  XPCPoint(const double latitudeDegrees,
           const double longitudeDegrees,
           const double height);

  ~XPCPoint();

};

#endif
