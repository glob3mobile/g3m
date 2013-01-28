//
//  ShortBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/13.
//
//

#ifndef __G3MiOSSDK__ShortBufferBuilder__
#define __G3MiOSSDK__ShortBufferBuilder__

#include <vector>

class IShortBuffer;

class ShortBufferBuilder {
private:
  std::vector<short> _values;

public:

  void add(short value) {
    _values.push_back(value);
  }

  IShortBuffer* create() const;

};

#endif
