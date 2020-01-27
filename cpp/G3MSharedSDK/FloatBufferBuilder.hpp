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

class Vector2D;
class Vector3D;
class IFloatBuffer;


class FloatBufferBuilder {
private:
  FloatBufferBuilder(const FloatBufferBuilder& that);

protected:
#ifdef C_CODE
  std::vector<float> _values;
#endif
#ifdef JAVA_CODE

  public final class FloatArrayList {
    private float[] _array;
    private int     _size;

    public FloatArrayList() {
      this(1024);
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

  FloatBufferBuilder();

public:
  IFloatBuffer* create() const;

  virtual ~FloatBufferBuilder();

  const size_t size() const;

  Vector2D getVector2D(int i) const;
  
  Vector3D getVector3D(int i) const;

};

#endif
