package org.glob3.mobile.generated; 
//
//  FloatBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

//
//  FloatBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//



//class IFloatBuffer;

public class FloatBufferBuilder
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  FloatBufferBuilder(FloatBufferBuilder that);

  protected enum CenterStrategy
  {
    NO_CENTER,
    FIRST_VERTEX,
    GIVEN_CENTER;

     public int getValue()
     {
        return this.ordinal();
     }

     public static CenterStrategy forValue(int value)
     {
        return values()[value];
     }
  }



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

  protected FloatBufferBuilder()
  {

  }

  public final IFloatBuffer create()
  {
    //return IFactory.instance().createFloatBuffer( _values.toArray() );
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
    int pos = i * 2;
    return new Vector2D(_values.get(pos), _values.get(pos + 1));
  }

  public final Vector3D getVector3D(int i)
  {
    int pos = i * 3;
    return new Vector3D(_values.get(pos), _values.get(pos + 1), _values.get(pos + 2));
  }
}