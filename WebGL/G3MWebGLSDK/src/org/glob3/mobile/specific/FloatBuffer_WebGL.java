

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


   public FloatBuffer_WebGL(final float f0,
                            final float f1,
                            final float f2,
                            final float f3,
                            final float f4,
                            final float f5,
                            final float f6,
                            final float f7,
                            final float f8,
                            final float f9,
                            final float f10,
                            final float f11,
                            final float f12,
                            final float f13,
                            final float f14,
                            final float f15) {
      _buffer = jsCreateBuffer(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
   }


   private native JavaScriptObject jsCreateBuffer(final float f0,
                                                  final float f1,
                                                  final float f2,
                                                  final float f3,
                                                  final float f4,
                                                  final float f5,
                                                  final float f6,
                                                  final float f7,
                                                  final float f8,
                                                  final float f9,
                                                  final float f10,
                                                  final float f11,
                                                  final float f12,
                                                  final float f13,
                                                  final float f14,
                                                  final float f15) /*-{
		var buffer = new Float32Array(16);
		buffer[0] = f0;
		buffer[1] = f1;
		buffer[2] = f2;
		buffer[3] = f3;
		buffer[4] = f4;
		buffer[5] = f5;
		buffer[6] = f6;
		buffer[7] = f7;
		buffer[8] = f8;
		buffer[9] = f9;
		buffer[10] = f10;
		buffer[11] = f11;
		buffer[12] = f12;
		buffer[13] = f13;
		buffer[14] = f14;
		buffer[15] = f15;

		return buffer;
   }-*/;


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


   @Override
   public native void put(final int i,
                          final float value) /*-{
		if (this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] != value) {
			this.@org.glob3.mobile.specific.FloatBuffer_WebGL::_buffer[i] = value;
			this.@org.glob3.mobile.specific.FloatBuffer_WebGL::incTimestamp();
		}
   }-*/;


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
