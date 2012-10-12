

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IFloatBuffer;

import com.google.gwt.core.client.JavaScriptObject;


public final class FloatBuffer_WebGL
         extends
            IFloatBuffer {

   private final JavaScriptObject _buffer;
   private int                    _timestamp   = 0;


   private JavaScriptObject       _webGLBuffer = null;
   private JavaScriptObject       _gl          = null;


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
		return this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_gl
				.createBuffer();
   }-*/;


   private native JavaScriptObject jsDeleteWebGLBuffer() /*-{
		return this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_gl
				.deleteBuffer(this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_webGLBuffer);
   }-*/;


   public FloatBuffer_WebGL(final JavaScriptObject data) {
      _buffer = jsCreateBuffer(data);
   }


   public FloatBuffer_WebGL(final int size) {
      _buffer = jsCreateBuffer(size);
   }


   @Override
   public native int size() /*-{
		return this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer.length;
   }-*/;


   @Override
   public int timestamp() {
      return _timestamp;
   }


   @Override
   public native float get(int i) /*-{
		return this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i];
   }-*/;


   //   @Override
   //   public native void put(final int i,
   //                          final float value) /*-{
   //		if (this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] != value) {
   //			this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] = value;
   //			this.@org.glob3.mobile.specific.FloatBuffer_WebGL::incTimestamp();
   //		}
   //   }-*/;

   @Override
   public native void rawPut(final int i,
                             final float value) /*-{
		this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] = value;
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

}
