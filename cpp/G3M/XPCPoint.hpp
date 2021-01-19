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


  const double _x;
  const double _y;
  const double _z;

  XPCPoint(const double x,
           const double y,
           const double z);

  ~XPCPoint();

};

#endif
