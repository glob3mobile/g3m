

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IFloatBuffer;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;


public final class FloatBuffer_WebGL
         extends
            IFloatBuffer {

   private final JavaScriptObject _buffer;
   private int                    _timestamp = 0;


   public FloatBuffer_WebGL(final JavaScriptObject data) {
      _buffer = jsCreateBuffer(data);
   }


   public FloatBuffer_WebGL(final int size) {
      _buffer = jsCreateBuffer(size);
   }


   public FloatBuffer_WebGL(final float[] data) {
      final int dataLength = data.length;
      final JsArrayNumber array = JavaScriptObject.createArray().<JsArrayNumber> cast();
      array.setLength(dataLength);
      for (int i = 0; i < dataLength; i++) {
         array.set(i, data[i]);
      }
      _buffer = jsCreateBuffer(array);
   }


   //   private native void plainPut(final int i,
   //                                final float value) /*-{
   //		this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] = value;
   //   }-*/;


   //   private native void plainPut(final float[] data) /*-{
   //		this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer.set(data);
   //   }-*/;


   //   @Override
   //   public int size() {
   //      return jsSize();
   //   }

   @Override
   public native int size() /*-{
		return this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer.length;
   }-*/;


   @Override
   public int timestamp() {
      return _timestamp;
   }


   //   @Override
   //   public float get(final int i) {
   //      return jsGet(i);
   //   }

   @Override
   public native float get(int i) /*-{
		return this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i];
   }-*/;


   //   @Override
   //   public void put(final int i,
   //                   final float value) {
   //      jsPut(i, value);
   //   }

   @Override
   public native void put(final int i,
                          final float value) /*-{
		//var thisInstance = this;
		//if (thisInstance.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] != value) {
		//	thisInstance.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] = value;
		//	thisInstance.@org.glob3.mobile.specific.FloatBuffer_WebGL::incTimestamp()();
		//}

		if (this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] != value) {
			this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] = value;
			this.@org.glob3.mobile.specific.FloatBuffer_WebGL::incTimestamp();
		}
   }-*/;


   public JavaScriptObject getBuffer() {
      return _buffer;
   }


   private void incTimestamp() {
      _timestamp++;
   }


   private native JavaScriptObject jsCreateBuffer(final JavaScriptObject data) /*-{
		return new Float32Array(data);
   }-*/;


   private native JavaScriptObject jsCreateBuffer(final int size) /*-{
		return new Float32Array(size);
   }-*/;


   //   private native JavaScriptObject jsCreateBuffer(final float[] data) /*-{
   //		var result = new Float32Array(data.length);
   //		result.set(data);
   //		return result;
   //   }-*/;

   //   private native int jsSize() /*-{
   //		return this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer.length;
   //   }-*/;


   //   private native float jsGet(int i) /*-{
   //		return this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i];
   //   }-*/;


   //   private native void jsPut(int i,
   //                             float value) /*-{
   //		var thisInstance = this;
   //		if (thisInstance.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] != value) {
   //			thisInstance.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] = value;
   //			thisInstance.@org.glob3.mobile.specific.FloatBuffer_WebGL::incTimestamp()();
   //		}
   //   }-*/;

}
