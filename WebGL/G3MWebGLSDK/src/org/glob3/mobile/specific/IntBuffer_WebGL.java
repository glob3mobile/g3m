

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IIntBuffer;

import com.google.gwt.core.client.JavaScriptObject;


public final class IntBuffer_WebGL
         extends
            IIntBuffer {

   private final JavaScriptObject _buffer;
   private int                    _timestamp   = 0;

   private JavaScriptObject       _webGLBuffer = null;
   private JavaScriptObject       _gl          = null;
   
   //ID
   private static long _nextID = 0;
   private final long _id = _nextID++;


   public JavaScriptObject getWebGLBuffer(final JavaScriptObject gl) {
      if (_webGLBuffer == null) {
         _gl = gl;
         _webGLBuffer = jsCreateWebGLBuffer();
      }
      return _webGLBuffer;
   }


   @Override
   public void dispose() {
      if (_webGLBuffer != null) {
         jsDeleteWebGLBuffer();
         _webGLBuffer = null;
         _gl = null;
      }
      super.dispose();
   }


   private native JavaScriptObject jsCreateWebGLBuffer() /*-{
		return this.@org.glob3.mobile.specific.IntBuffer_WebGL::_gl
				.createBuffer();
   }-*/;


   private native JavaScriptObject jsDeleteWebGLBuffer() /*-{
		return this.@org.glob3.mobile.specific.IntBuffer_WebGL::_gl
				.deleteBuffer(this.@org.glob3.mobile.specific.IntBuffer_WebGL::_webGLBuffer);
   }-*/;


   public IntBuffer_WebGL(final JavaScriptObject data) {
      _buffer = jsCreateBuffer(data);
   }


   public IntBuffer_WebGL(final int size) {
      _buffer = jsCreateBuffer(size);
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


   private native void jsPut(int i,
                             int value) /*-{

		if (value < 0 || value > 65535) {
			alert("EXCEDING SHORT RANGE IN UINT16 JAVASCRIPT BUFFER");
		}

		var thisInstance = this;
		if (thisInstance.@org.glob3.mobile.specific.IntBuffer_WebGL::_buffer[i] != value) {
			thisInstance.@org.glob3.mobile.specific.IntBuffer_WebGL::_buffer[i] = value;
			thisInstance.@org.glob3.mobile.specific.IntBuffer_WebGL::incTimestamp()();
		}
   }-*/;


   @Override
   public native void rawPut(final int i,
                             final int value) /*-{

		if (value < 0 || value > 65535) {
			alert("EXCEDING SHORT RANGE IN UINT16 JAVASCRIPT BUFFER");
		}

		this.@org.glob3.mobile.specific.IntBuffer_WebGL::_buffer[i] = value;
   }-*/;


   public JavaScriptObject getBuffer() {
      return _buffer;
   }


   private void incTimestamp() {
      _timestamp++;
   }


   private native JavaScriptObject jsCreateBuffer(final JavaScriptObject data) /*-{
		return new Uint16Array(data);
   }-*/;


   private native JavaScriptObject jsCreateBuffer(final int size) /*-{
		return new Uint16Array(size);
   }-*/;


   private native int jsSize() /*-{
		return this.@org.glob3.mobile.specific.IntBuffer_WebGL::_buffer.length;
   }-*/;


   private native int jsGet(int i) /*-{
		return this.@org.glob3.mobile.specific.IntBuffer_WebGL::_buffer[i];
   }-*/;


   @Override
   public String description() {
      return "IntBuffer_WebGL(timestamp=" + _timestamp + ", buffer=" + _buffer + ")";
   }


@Override
public long getID() {
	return _id;
}


}
