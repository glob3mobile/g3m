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
#include "IStringBuilder.hpp"

class IShortBuffer;

class ShortBufferBuilder {
private:
  ShortBufferBuilder(const ShortBufferBuilder& that);

#ifdef C_CODE
  std::vector<short> _values;
#endif
#ifdef JAVA_CODE

  public final class ShortArrayList {
    private short[] _array;
    private int     _size;

    public ShortArrayList() {
      this(1024);
    }

    public ShortArrayList(final int initialCapacity) {
      if (initialCapacity < 0) {
        throw new IllegalArgumentException("Capacity can't be negative: " + initialCapacity);
      }
      _array = new short[initialCapacity];
      _size = 0;
    }

    public int size() {
      return _size;
    }

    public short get(final int index) {
      return _array[index];
    }

    public void push_back(final short element) {
      ensureCapacity(_size + 1);
      _array[_size++] = element;
    }

    private void ensureCapacity(final int mincap) {
      if (mincap > _array.length) {
        final int newcap = ((_array.length * 3) >> 1) + 1;
        final short[] olddata = _array;
        _array = new short[newcap < mincap ? mincap : newcap];
        System.arraycopy(olddata, 0, _array, 0, _size);
      }
    }

    public short[] toArray() {
      final short[] result = new short[_size];
      System.arraycopy(_array, 0, result, 0, _size);
      return result;
    }

  }

  protected final ShortArrayList _values = new ShortArrayList();
#endif

public:
  ShortBufferBuilder() {
  }

  void add(short value) {
    _values.push_back(value);
  }

  IShortBuffer* create() const;

  std::string description() const{
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("ShortBufferBuilder: ");
    for (int i = 0; i < (int)_values.size(); i++) {

#ifdef C_CODE
      short v = _values[i];
#endif
#ifdef JAVA_CODE
      short v = _values.get(i);
#endif
      isb->addInt(v);
      isb->addString(", ");
    }
    const std::string s = isb->getString();
    delete isb;
    return s;
  }
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

#endif
