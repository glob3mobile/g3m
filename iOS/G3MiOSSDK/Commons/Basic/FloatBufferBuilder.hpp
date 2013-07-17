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
  
  public final class FloatArrayList {
    private float[] _array;
    private int     _size;
    
    public FloatArrayList() {
      this(256);
    }

    public FloatArrayList(final int initialCapacity) {
      if (initialCapacity < 0) {
        throw new IllegalArgumentException("Capacity can't be negative: " + initialCapacity);
      }
      _array = new float[initialCapacity];
      _size = 0;
    }

    public int size() {
      return _size;
    }

    public float get(final int index) {
      return _array[index];
    }

    public void push_back(final float element) {
      ensureCapacity(_size + 1);
      _array[_size++] = element;
    }

    private void ensureCapacity(final int mincap) {
      if (mincap > _array.length) {
        final int newcap = ((_array.length * 3) >> 1) + 1;
        final float[] olddata = _array;
        _array = new float[newcap < mincap ? mincap : newcap];
        System.arraycopy(olddata, 0, _array, 0, _size);
      }
    }
    
    public float[] toArray() {
      final float[] result = new float[_size];
      System.arraycopy(_array, 0, result, 0, _size);
      return result;
    }

  }

  protected final FloatArrayList _values = new FloatArrayList();
#endif

public:
  IFloatBuffer* create() const;
};

#endif
