

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IByteBuffer;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;


public final class ByteBuffer_WebGL
         extends
            IByteBuffer {

   private final JavaScriptObject _buffer;
   private int                    _timestamp = 0;


   public ByteBuffer_WebGL(final JavaScriptObject data) {
      _buffer = jsCreateBuffer(data);
   }


   public ByteBuffer_WebGL(final int size) {
      _buffer = jsCreateBuffer(size);
   }


   //   public ByteBuffer_WebGL(final byte[] data,
   //                           final int length) {
   //      _buffer = jsCreateBuffer(length);
   //
   //      for (int i = 0; i < length; i++) {
   //         put(i, data[i]);
   //      }
   //   }

   public ByteBuffer_WebGL(final byte[] data,
                           final int dataLength) {
      final JsArrayNumber array = JavaScriptObject.createArray().<JsArrayNumber> cast();
      array.setLength(dataLength);
      for (int i = 0; i < dataLength; i++) {
         array.set(i, data[i]);
      }
      _buffer = jsCreateBuffer(array);
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
   public byte get(final int i) {
      return jsGet(i);
   }


   @Override
   public void put(final int i,
                   final byte value) {
      jsPut(i, value);
   }


   @Override
   public String description() {
      return "ByteBuffer_WebGL (size=" + size() + ")";
   }


   @Override
   public String getAsString() {
      return jsGetAsString();
   }


   public JavaScriptObject getBuffer() {
      return _buffer;
   }


   private void incTimestamp() {
      _timestamp++;
   }


   private native JavaScriptObject jsCreateBuffer(final JavaScriptObject data) /*-{
		return new Uint8Array(data);
   }-*/;


   private native JavaScriptObject jsCreateBuffer(final int size) /*-{
		return new Uint8Array(size);
   }-*/;


   private native int jsSize() /*-{
		return this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer.length;
   }-*/;


   private native byte jsGet(int i) /*-{
		this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer.get(i);
   }-*/;


   private native void jsPut(int i,
                             byte value) /*-{
		var thisInstance = this;
		if (thisInstance.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer
				.get(i) != value) {
			thisInstance.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer
					.set(i, value);
			thisInstance.@org.glob3.mobile.specific.ByteBuffer_WebGL::incTimestamp()();
		}
   }-*/;


   private native String jsGetAsString() /*-{
		var thisInstance = this;
		return String.fromCharCode
				.apply(
						null,
						thisInstance.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer);
   }-*/;
}
