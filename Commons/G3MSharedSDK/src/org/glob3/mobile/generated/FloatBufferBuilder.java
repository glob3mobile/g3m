package org.glob3.mobile.generated;
//
//  FloatBufferBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

//
//  FloatBufferBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//



//class Vector2D;
//class Vector3D;
//class IFloatBuffer;


public class FloatBufferBuilder
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  FloatBufferBuilder(FloatBufferBuilder that);


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

  protected FloatBufferBuilder()
  {
  }

  public final IFloatBuffer create()
  {
    return IFactory.instance().createFloatBuffer( _values._array, _values._size );
  }

  public void dispose()
  {
  }

  public final int size()
  {
    return _values.size();
  }

  public final Vector2D getVector2D(int i)
  {
    final int i2 = i * 2;
    return new Vector2D(_values.get(i2), _values.get(i2 + 1));
  }

  public final Vector3D getVector3D(int i)
  {
    final int i3 = i * 3;
    return new Vector3D(_values.get(i3), _values.get(i3 + 1), _values.get(i3 + 2));
  }

}