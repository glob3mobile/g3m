package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IByteBuffer;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;

public final class ByteBuffer_WebGL extends IByteBuffer {

	private final JavaScriptObject _buffer;
	private int _timestamp = 0;

	private static int _nextID = 0;
	private final int _id = _nextID++;

	private JavaScriptObject _webGLBuffer = null;
	private JavaScriptObject _gl = null;

	public ByteBuffer_WebGL(final JavaScriptObject data) {
		_buffer = jsCreateBuffer(data);
	}

	public ByteBuffer_WebGL(final int size) {
		_buffer = jsCreateBuffer(size);
	}

	// public ByteBuffer_WebGL(final byte[] data,
	// final int length) {
	// _buffer = jsCreateBuffer(length);
	//
	// for (int i = 0; i < length; i++) {
	// put(i, data[i]);
	// }
	// }

	public ByteBuffer_WebGL(final byte[] data, final int dataLength) {
		final JsArrayNumber array = JavaScriptObject.createArray()
				.<JsArrayNumber> cast();
		array.setLength(dataLength);
		for (int i = 0; i < dataLength; i++) {
			array.set(i, data[i]);
		}
		_buffer = jsCreateBuffer(array);
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
		var result = "";
		var buffer = this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer;
		for ( var i = 0; i < buffer.byteLength; i++) {
			result += String.fromCharCode(buffer[i]);
		}
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

	@Override
	public long getID() {
		return _id;
	}

	public JavaScriptObject getWebGLBuffer(final JavaScriptObject gl) {
		if (_webGLBuffer == null) {
			_gl = gl;
			_webGLBuffer = jsCreateWebGLBuffer();
		}
		return _webGLBuffer;
	}
	
	private native JavaScriptObject jsCreateWebGLBuffer() /*-{
		return this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_gl.createBuffer();
	}-*/;
}
