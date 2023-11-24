
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;
import com.google.gwt.core.client.*;

public final class ByteBuffer_WebGL extends IByteBuffer {

   private JavaScriptObject _buffer;
   private int              _timestamp = 0;

   public ByteBuffer_WebGL(final JavaScriptObject data) {
      _buffer = jsCreateBuffer(data);
   }

   public ByteBuffer_WebGL(final int size) {
      _buffer = jsCreateBuffer(size);
   }

   @Override
   public void dispose() {
      _buffer = null;
      super.dispose();
   }

   @Override
   public native int size() /*-{
    return this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer.length;
   }-*/;

   @Override
   public int timestamp() {
      return _timestamp;
   }

   @Override
   public native byte get(final int i) /*-{
    return this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer[i];
   }-*/;

   @Override
   public native void put(final int i, final byte value) /*-{
    var buffer = this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer;
    if (buffer[i] != value) {
      buffer[i] = value;
      this.@org.glob3.mobile.specific.ByteBuffer_WebGL::incTimestamp()();
    }
   }-*/;

   @Override
   public native void rawPut(final int i, final byte value) /*-{
    this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer[i] = value;
   }-*/;

   @Override
   public String description() {
      return "ByteBuffer_WebGL (size=" + size() + ")";
   }

   @Override
   public native String getAsString() /*-{
    var buffer = this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer.buffer;
    var array = new Uint8Array(buffer);
    var result = this.@org.glob3.mobile.specific.ByteBuffer_WebGL::utf8ArrayToStr(Lcom/google/gwt/core/client/JavaScriptObject;)(array);
    return result;
   }-*/;

   public JavaScriptObject getBuffer() {
      return _buffer;
   }

   private void incTimestamp() {
      _timestamp++;
   }

   private native JavaScriptObject jsCreateBuffer(final JavaScriptObject data) /*-{
    return new Int8Array(data);
   }-*/;

   private native JavaScriptObject jsCreateBuffer(final int size) /*-{
    return new Int8Array(size);
   }-*/;

   private native String utf8ArrayToStr(final JavaScriptObject array) /*-{
    var out, i, len, c;
    var char2, char3;

    out = "";
    len = array.length;
    i = 0;
    while (i < len) {
      c = array[i++];
      switch (c >> 4) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        // 0xxxxxxx
        out += String.fromCharCode(c);
        break;
      case 12:
      case 13:
        // 110x xxxx   10xx xxxx
        char2 = array[i++];
        out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
        break;
      case 14:
        // 1110 xxxx  10xx xxxx  10xx xxxx
        char2 = array[i++];
        char3 = array[i++];
        out += String.fromCharCode(((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
        break;
      }
    }

    return out;
   }-*/;

   @Override
   public ByteBuffer_WebGL copy(final int from, final int length) {
      return new ByteBuffer_WebGL(jsBufferSlice(from, length));
   }

   native private JavaScriptObject jsBufferSlice(final int from, final int length) /*-{
    var buffer = this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer.buffer;
    return buffer.slice(from, from + length);
   }-*/;

}
