package org.glob3.mobile.specific;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.ILogger;

import android.opengl.GLES20;

public final class ByteBuffer_Android extends IByteBuffer {

	// private final ByteBuffer _buffer;
	private byte[] _buffer;
	private int _timestamp = 0;

	// ID
	final long _id;
	static long _nextID = 0;

	ByteBuffer _byteBuffer = null;
	private boolean _vertexBufferCreated = false;
	// private boolean _disposed = false;
	private int _vertexBuffer = -1;
	private int _vertexBufferTimeStamp = -1;

	NativeGL2_Android _nativeGL = null;

	ByteBuffer_Android(final byte[] data) {
		// _buffer = ByteBuffer.wrap(data);
		_buffer = data;
		_id = _nextID++;

		// _buffer = ByteBuffer.allocateDirect(data.length);
		// _buffer.put(data);
		// _buffer.rewind();
	}

	public ByteBuffer_Android(final int size) {
		// _buffer = ByteBuffer.allocate(size);
		// _buffer = ByteBuffer.wrap(new byte[size]);
		_buffer = new byte[size];
		_id = _nextID++;
	}

	@Override
	public int size() {
		
		if (_byteBuffer != null){
			return _byteBuffer.capacity();
		}
		
		// return _buffer.capacity();
		return _buffer.length;
	}

	@Override
	public int timestamp() {
		return _timestamp;
	}

	@Override
	public byte get(final int i) {
		
		if (_byteBuffer != null){
			return _byteBuffer.get(i);
		}
		
		// return _buffer.get(i);
		return _buffer[i];
	}

	@Override
	public void put(final int i, final byte value) {
		
		if (_byteBuffer != null){
			if (_byteBuffer.get(i) != value){
				_byteBuffer.put(i, value);
				_timestamp++;
			}
			return;
		}
		
		// if (_buffer.get(i) != value) {
		// _buffer.put(i, value);
		// _timestamp++;
		// }
		if (_buffer[i] != value) {
			_buffer[i] = value;
			_timestamp++;
		}
	}

	@Override
	public void rawPut(final int i, final byte value) {
		
		if (_byteBuffer != null){
			_byteBuffer.put(i, value);
			return;
		}
		
		// _buffer.put(i, value);
		_buffer[i] = value;
	}

	// public ByteBuffer getBuffer() {
	// return _buffer;
	// }

	public byte[] getBuffer() {
		
		if (_byteBuffer != null){
			return _byteBuffer.array();
		}
		
		return _buffer;
	}

	public ByteBuffer getByteBuffer() {
		if (_byteBuffer == null) {
			_byteBuffer = ByteBuffer.allocateDirect(_buffer.length);
			_byteBuffer.put(_buffer);
			_byteBuffer.position(0);
			
			_buffer = null; //DELETING PREVIOUS BUFFER WHEN THE BYTEBUFFER
		}
		return _byteBuffer;
	}

	@Override
	public String description() {
		// return "ByteBuffer_iOS (size=" + _buffer.capacity() + ")";
		return "ByteBuffer_iOS (size=" + size() + ")";
	}

	@Override
	public String getAsString() {
		// final byte[] bytes = _buffer.array();
		// return new String(bytes);
		try {
			return new String(getBuffer(), "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long getID() {
		return _id;
	}

	public int bindAsVBOToGPU(NativeGL2_Android gl) {

		_nativeGL = gl;
		if (_vertexBuffer < 0) {
			_vertexBuffer = _nativeGL.genBuffer();
		}

		_nativeGL.bindVBO(_vertexBuffer);

		if (_vertexBufferTimeStamp != _timestamp) {
			_vertexBufferTimeStamp = _timestamp;

			final ByteBuffer buffer = getByteBuffer();
			final int vboSize = size();

			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vboSize, buffer,
					GLES20.GL_STATIC_DRAW);
		}

		if (GLES20.glGetError() != GLES20.GL_NO_ERROR) {
			ILogger.instance().logError("Error at ByteBuffer::bindAsVBOToGPU()");
		}

		return _vertexBuffer;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (_vertexBufferCreated && _nativeGL != null) {
			_nativeGL.deleteVBO(_vertexBuffer);
			_vertexBufferCreated = false;
		}
	}

}
