

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IIntBuffer;

import com.google.gwt.core.client.JavaScriptObject;


public class IntBuffer_WebGL
         extends
            IIntBuffer {

   private final JavaScriptObject _buffer;
   private int                    _timestamp = 0;


   public IntBuffer_WebGL(final JavaScriptObject data) {
      _buffer = jsCreateBuffer(data);
   }


   public IntBuffer_WebGL(final int size) {
      _buffer = jsCreateBuffer(size);
   }


   IntBuffer_WebGL(final byte[] data) {
      //      TODO needed??
      throw new RuntimeException("ByteBuffer_WebGL(final byte[] data) IS NOT IMPLEMENTED");
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
   public int get(final int i) {
      return jsGet(i);
   }


   @Override
   public void put(final int i,
                   final int value) {
      jsPut(i, value);
   }


   public JavaScriptObject getBuffer() {
      return _buffer;
   }


   private void incTimestamp() {
      _timestamp++;
   }


   private native JavaScriptObject jsCreateBuffer(final JavaScriptObject data) /*-{
		return new Int32Array(data);
   }-*/;


   private native JavaScriptObject jsCreateBuffer(final int size) /*-{
		return new Int32Array(size);
   }-*/;


   private native int jsSize() /*-{
		return this.@org.glob3.mobile.specific.IntBuffer_WebGL::_buffer.length;
   }-*/;


   private native byte jsGet(int i) /*-{
		this.@org.glob3.mobile.specific.IntBuffer_WebGL::_buffer[i];
   }-*/;


   private native void jsPut(int i,
                             int value) /*-{
		var thisInstance = this;
		if (thisInstance.@org.glob3.mobile.specific.IntBuffer_WebGL::_buffer[i] != value) {
			thisInstance.@org.glob3.mobile.specific.IntBuffer_WebGL::_buffer[i] = value;
			thisInstance.@org.glob3.mobile.specific.IntBuffer_WebGL::incTimestamp()();
		}
   }-*/;

}
