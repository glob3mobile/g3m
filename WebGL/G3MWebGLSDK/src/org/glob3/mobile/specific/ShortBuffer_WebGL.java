
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;
import com.google.gwt.core.client.*;

public class ShortBuffer_WebGL extends IShortBuffer {

   // Indices are signed Java shorts; the Uint16Array stores their unsigned 16-bit value.
   private static final int UINT16_MASK = 0xFFFF;

   private final JavaScriptObject _buffer;
   private int                    _timestamp       = 0;
   private int                    _bufferTimeStamp = -1;

   private JavaScriptObject _webGLBuffer = null;
   private JavaScriptObject _gl          = null;

   // ID
   private static long _nextID = 0;
   private final long  _id     = _nextID++;

   public void bindIBO(final JavaScriptObject gl) {
      if (_webGLBuffer == null) {
         _gl          = gl;
         _webGLBuffer = jsCreateWebGLBuffer();
      }
      jsBindWebGLBuffer(gl);
      if (_bufferTimeStamp != _timestamp) {
         _bufferTimeStamp = _timestamp;
         jsDataToIBO(gl);
      }
   }

   @Override
   public void dispose() {
      if (_webGLBuffer != null) {
         jsDeleteWebGLBuffer();
         _webGLBuffer = null;
         _gl          = null;
      }
      super.dispose();
   }

   private native JavaScriptObject jsCreateWebGLBuffer() /*-{
    return this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_gl.createBuffer();
   }-*/;

   private native void jsBindWebGLBuffer(final JavaScriptObject gl) /*-{
    var buffer = this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_webGLBuffer;
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, buffer);
   }-*/;

   private native void jsDataToIBO(final JavaScriptObject gl) /*-{
    var buffer = this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_buffer;
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, buffer, gl.STATIC_DRAW);
   }-*/;

   private native void jsDeleteWebGLBuffer() /*-{
    this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_gl.deleteBuffer(this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_webGLBuffer);
   }-*/;

   public ShortBuffer_WebGL(final JavaScriptObject data) {
      _buffer = jsCreateBufferWithData(data);
   }

   public ShortBuffer_WebGL(final int size) {
      _buffer = jsCreateBufferWithSize(size);
   }

   public ShortBuffer_WebGL(final short[] array) {
      final int size = array.length;
      _buffer = jsCreateBufferWithSize(size);
      for (int i = 0; i < size; i++) {
         rawPut(i, array[i]);
      }
   }

   public ShortBuffer_WebGL(final short[] array,
                            final int length) {
      _buffer = jsCreateBufferWithSize(length);
      for (int i = 0; i < length; i++) {
         rawPut(i, array[i]);
      }
   }

   @Override
   public int size() {
      return jsSize();
   }

   @Override
   public int timestamp() {
      return _timestamp;
   }

   @Override
   public short get(final int i) {
      return (short) jsGet(i);
   }

   @Override
   public void put(final int i, final short value) {
      jsPut(i, value & UINT16_MASK);
   }

   private native void jsPut(int i, int uint16Value) /*-{
    var thisInstance = this;
    if (thisInstance.@org.glob3.mobile.specific.ShortBuffer_WebGL::_buffer[i] != uint16Value) {
      thisInstance.@org.glob3.mobile.specific.ShortBuffer_WebGL::_buffer[i] = uint16Value;
      thisInstance.@org.glob3.mobile.specific.ShortBuffer_WebGL::incTimestamp()();
    }
   }-*/;

   @Override
   public void rawPut(final int i, final short value) {
      jsRawPut(i, value & UINT16_MASK);
   }

   private native void jsRawPut(int i, int uint16Value) /*-{
    this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_buffer[i] = uint16Value;
   }-*/;

   public JavaScriptObject getBuffer() {
      return _buffer;
   }

   private void incTimestamp() {
      _timestamp++;
   }

   private native JavaScriptObject jsCreateBufferWithData(final JavaScriptObject data) /*-{
    return new Uint16Array(data);
   }-*/;

   private native JavaScriptObject jsCreateBufferWithSize(final int size) /*-{
    return new Uint16Array(size);
   }-*/;

   private native int jsSize() /*-{
    return this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_buffer.length;
   }-*/;

   private native int jsGet(int i) /*-{
    return this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_buffer[i];
   }-*/;

   @Override
   public String description() {
      return "ShortBuffer_WebGL(timestamp=" + _timestamp + ", buffer=" + _buffer + ")";
   }

   @Override
   public long getID() {
      return _id;
   }

}
