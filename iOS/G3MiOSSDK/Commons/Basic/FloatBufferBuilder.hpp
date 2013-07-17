//
//  FloatBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#ifndef __G3MiOSSDK__FloatBufferBuilder__
#define __G3MiOSSDK__FloatBufferBuilder__

#include <vector>

class IFloatBuffer;


class CenterStrategy {
private:
  static const int _noCenter    = 0;
  static const int _firstVertex = 1;
  static const int _givenCenter = 2;

  CenterStrategy() {
  }

public:
  static int noCenter()    { return _noCenter;    }
  static int firstVertex() { return _firstVertex; }
  static int givenCenter() { return _givenCenter; }
};


class FloatBufferBuilder {
protected:
#ifdef C_CODE
  std::vector<float> _values;
#endif
#ifdef JAVA_CODE
  protected final FloatArrayList _values = new FloatArrayList();
#endif

public:
  IFloatBuffer* create() const;
};

#endif
