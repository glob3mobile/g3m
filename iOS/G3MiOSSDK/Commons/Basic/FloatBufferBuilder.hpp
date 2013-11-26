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
#include "Vector2D.hpp"
#include "Vector3D.hpp"

class IFloatBuffer;

class FloatBufferBuilder {
private:
  FloatBufferBuilder(const FloatBufferBuilder& that);

protected:
  enum CenterStrategy{
    NO_CENTER,FIRST_VERTEX,GIVEN_CENTER
  };


#ifdef C_CODE
  std::vector<float> _values;
#endif
#ifdef JAVA_CODE

  public final class FloatArrayList {
    private float[] _array;
    private int     _size;

    public FloatArrayList() {
      this(1024);
//      _array = IFactory.instance().getThreadLocalFloatArray();
//      _size = 0;
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
        //IFactory.instance().setThreadLocalFloatArray(_array);
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

  FloatBufferBuilder() {

  }

public:
  IFloatBuffer* create() const;

  virtual ~FloatBufferBuilder() {
  }

  int size() const{
    return _values.size();
  }

  Vector2D getVector2D(int i) const{
    int pos = i * 2;
#ifdef C_CODE
    return Vector2D(_values[pos], _values[pos + 1]);
#endif
#ifdef JAVA_CODE
    return new Vector2D(_values.get(pos), _values.get(pos + 1));
#endif
  }

  Vector3D getVector3D(int i) const{
    int pos = i * 3;
#ifdef C_CODE
    return Vector3D(_values[pos], _values[pos + 1], _values[pos+2]);
#endif
#ifdef JAVA_CODE
    return new Vector3D(_values.get(pos), _values.get(pos + 1), _values.get(pos + 2));
#endif
  }
};

#endif
