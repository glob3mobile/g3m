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
  static XPCPoint* fromByteBufferIterator(ByteBufferIterator& it);


  const double _latitudeDegrees;
  const double _longitueDegrees;
  const double _height;

  XPCPoint(const double latitudeDegrees,
           const double longitueDegrees,
           const double height);

  ~XPCPoint();

};

#endif
