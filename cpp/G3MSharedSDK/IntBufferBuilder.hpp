//
//  IntBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#ifndef __G3MiOSSDK__IntBufferBuilder__
#define __G3MiOSSDK__IntBufferBuilder__

#include <vector>

class IIntBuffer;

class IntBufferBuilder {
private:
  std::vector<int> _values;

  IntBufferBuilder(const IntBufferBuilder& that);

public:
  
  void add(int value) {
    _values.push_back(value);
  }
  
  IIntBuffer* create() const;
  
};

#endif
