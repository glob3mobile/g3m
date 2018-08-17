package org.glob3.mobile.generated;import java.util.*;

//
//  GL.cpp
//  Glob3 Mobile
//
//  Created by Agustin Trujillo Pino on 02/05/11.
//


//
//  GL.hpp
//  Glob3 Mobile
//
//  Created by Agustin Trujillo Pino on 14/06/11.
//





//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLProgramId;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLUniformID;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgramManager;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgramState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;


public class GL
{
	private final INativeGL _nativeGL;


	/////////////////////////////////////////////////
	//CURRENT GL STATUS
	private GLGlobalState _currentGLGlobalState;
	private GPUProgram _currentGPUProgram;
	/////////////////////////////////////////////////

	private final java.util.LinkedList<IGLTextureId> _texturesIdBag = new java.util.LinkedList<IGLTextureId>();
	private int _texturesIdAllocationCounter;

	private IGLTextureId getGLTextureId()
	{
		//  if (_verbose) {
		//    ILogger::instance()->logInfo("GL::getGLTextureId()");
		//  }
    
		if (_texturesIdBag.size() == 0)
		{
			//const int bugdetSize = 256;
			final int bugdetSize = 1024;
			//const int bugdetSize = 10240;
    
			final java.util.ArrayList<IGLTextureId> ids = _nativeGL.genTextures(bugdetSize);
			final int idsCount = ids.size();
			for (int i = 0; i < idsCount; i++)
			{
				// ILogger::instance()->logInfo("  = Created textureId=%s", ids[i]->description().c_str());
				_texturesIdBag.addFirst(ids.get(i));
			}
    
			_texturesIdAllocationCounter += idsCount;
    
			ILogger.instance().logInfo("= Created %d texturesIds (accumulated %d).", idsCount, _texturesIdAllocationCounter);
		}
    
		//  _texturesIdGetCounter++;
    
		if (_texturesIdBag.size() == 0)
		{
			ILogger.instance().logError("TextureIds bag exhausted");
			return null;
		}
    
		final IGLTextureId result = _texturesIdBag.getLast();
		_texturesIdBag.removeLast();
    
		//  printf("   - Assigning 1 texturesId (#%d) from bag (bag size=%ld). Gets:%ld, Takes:%ld, Delta:%ld.\n",
		//         result.getGLTextureId(),
		//         _texturesIdBag.size(),
		//         _texturesIdGetCounter,
		//         _texturesIdTakeCounter,
		//         _texturesIdGetCounter - _texturesIdTakeCounter);
    
		return result;
	}

	private GLGlobalState _clearScreenState; //State used to clear screen with certain color

	private static boolean isPowerOfTwo(int x)
	{
		return ((x >= 0) && ((x == 1) || (x == 2) || (x == 4) || (x == 8) || (x == 16) || (x == 32) || (x == 64) || (x == 128) || (x == 256) || (x == 512) || (x == 1024) || (x == 2048) || (x == 4096) || (x == 8192) || (x == 16384) || (x == 32768) || (x == 65536) || (x == 131072) || (x == 262144) || (x == 524288) || (x == 1048576) || (x == 2097152) || (x == 4194304) || (x == 8388608) || (x == 16777216) || (x == 33554432) || (x == 67108864) || (x == 134217728) || (x == 268435456) || (x == 536870912) || (x == 1073741824)));
				 //(x == 2147483648)
	}




	public GL(INativeGL nativeGL)
	{
		_nativeGL = nativeGL;
		_texturesIdAllocationCounter = 0;
		_currentGPUProgram = null;
		_clearScreenState = null;
		//Init Constants
		GLCullFace.init(_nativeGL);
		GLBufferType.init(_nativeGL);
		GLStage.init(_nativeGL);
		GLType.init(_nativeGL);
		GLPrimitive.init(_nativeGL);
		GLBlendFactor.init(_nativeGL);
		GLTextureType.init(_nativeGL);
		GLTextureParameter.init(_nativeGL);
		GLTextureParameterValue.init(_nativeGL);
		GLAlignment.init(_nativeGL);
		GLFormat.init(_nativeGL);
		GLVariable.init(_nativeGL);
		GLError.init(_nativeGL);

		GLGlobalState.initializationAvailable();

		_currentGLGlobalState = new GLGlobalState();
		_clearScreenState = new GLGlobalState();

		//    _currentState = GLGlobalState::newDefault(); //Init after constants
	}

	public final void clearScreen(Color color)
	{
		//  if (_verbose) {
		//    ILogger::instance()->logInfo("GL::clearScreen()");
		//  }
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _clearScreenState->setClearColor(color);
		_clearScreenState.setClearColor(new Color(color));
		tangible.RefObject<GLGlobalState> tempRef__currentGLGlobalState = new tangible.RefObject<GLGlobalState>(_currentGLGlobalState);
		_clearScreenState.applyChanges(this, tempRef__currentGLGlobalState);
		_currentGLGlobalState = tempRef__currentGLGlobalState.argvalue;
    
		_nativeGL.clear(GLBufferType.colorBuffer() | GLBufferType.depthBuffer());
	}
	public final void clearDepthBuffer()
	{
		_nativeGL.clear(GLBufferType.depthBuffer());
	}

	//  void drawElements(int mode,
	//                    IShortBuffer* indices, const GLGlobalState& state,
	//                    GPUProgramManager& progManager,
	//                    const GPUProgramState* gpuState);

	public final void drawElements(int mode, IShortBuffer indices, GLState state, tangible.RefObject<GPUProgramManager> progManager)
	{
    
		state.applyOnGPU(this, progManager);
    
		_nativeGL.drawElements(mode, (int)indices.size(), indices);
	}

	//  void drawArrays(int mode,
	//                  int first,
	//                  int count, const GLGlobalState& state,
	//                  GPUProgramManager& progManager,
	//                  const GPUProgramState* gpuState);

	public final void drawArrays(int mode, int first, int count, GLState state, tangible.RefObject<GPUProgramManager> progManager)
	{
		//  if (_verbose) {
		//    ILogger::instance()->logInfo("GL::drawArrays(%d, %d, %d)",
		//                                 mode,
		//                                 first,
		//                                 count);
		//  }
    
		state.applyOnGPU(this, progManager);
    
		_nativeGL.drawArrays(mode, first, count);
	}

	public final int getError()
	{
		//  if (_verbose) {
		//    ILogger::instance()->logInfo("GL::getError()");
		//  }
    
		return _nativeGL.getError();
	}

	public final IGLTextureId uploadTexture(IImage image, int format, boolean generateMipmap)
	{
		return uploadTexture(image, format, generateMipmap, GLTextureParameterValue.clampToEdge());
	}
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: const IGLTextureId* uploadTexture(const IImage* image, int format, boolean generateMipmap, int wrapping = GLTextureParameterValue::clampToEdge())
	public final IGLTextureId uploadTexture(IImage image, int format, boolean generateMipmap, int wrapping)
	{
    
		final IGLTextureId texID = getGLTextureId();
		if (texID != null)
		{
			GLGlobalState newState = new GLGlobalState();
    
			newState.setPixelStoreIAlignmentUnpack(1);
			newState.bindTexture(0, texID);
    
			tangible.RefObject<GLGlobalState> tempRef__currentGLGlobalState = new tangible.RefObject<GLGlobalState>(_currentGLGlobalState);
			newState.applyChanges(this, tempRef__currentGLGlobalState);
			_currentGLGlobalState = tempRef__currentGLGlobalState.argvalue;
    
			final int texture2D = GLTextureType.texture2D();
			final int linear = GLTextureParameterValue.linear();
    
			if (generateMipmap)
			{
				_nativeGL.texParameteri(texture2D, GLTextureParameter.minFilter(), GLTextureParameterValue.linearMipmapNearest());
			}
			else
			{
				_nativeGL.texParameteri(texture2D, GLTextureParameter.minFilter(), linear);
			}
    
			_nativeGL.texParameteri(texture2D, GLTextureParameter.magFilter(), linear);
    
			_nativeGL.texParameteri(texture2D, GLTextureParameter.wrapS(), wrapping);
			_nativeGL.texParameteri(texture2D, GLTextureParameter.wrapT(), wrapping);
    
			_nativeGL.texImage2D(image, format);
    
			if (generateMipmap)
			{
				if (isPowerOfTwo(image.getWidth()) && isPowerOfTwo(image.getHeight()))
				{
					_nativeGL.generateMipmap(texture2D);
				}
				else
				{
					ILogger.instance().logError("Can't generate mipmap. Texture dimensions are not power of two.");
				}
			}
		}
		else
		{
			ILogger.instance().logError("can't get a valid texture id\n");
			return null;
		}
    
		return texID;
	}

	public final void deleteTexture(IGLTextureId textureId)
	{
		//  if (_verbose) {
		//    ILogger::instance()->logInfo("GL::deleteTexture()");
		//  }
    
		if (textureId != null)
		{
			_currentGLGlobalState.onTextureDelete(textureId);
    
			if (_nativeGL.deleteTexture(textureId))
			{
				_texturesIdBag.addLast(textureId);
			}
			else
			{
				if (textureId != null)
					textureId.dispose();
			}
    
			//ILogger::instance()->logInfo("  = delete textureId=%s", texture->description().c_str());
		}
	}

	//  void getViewport(int v[]) {
	//    _nativeGL->getIntegerv(GLVariable::viewport(), v);
	//  }

	public void dispose()
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		_nativeGL = null;
		if (_clearScreenState != null)
			_clearScreenState.dispose();
		if (_currentGLGlobalState != null)
			_currentGLGlobalState.dispose();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		_nativeGL.dispose();
		_clearScreenState.dispose();
		_currentGLGlobalState.dispose();
//#endif
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int createProgram() const
	public final int createProgram()
	{
		return _nativeGL.createProgram();
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void attachShader(int program, int shader) const
	public final void attachShader(int program, int shader)
	{
		_nativeGL.attachShader(program, shader);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int createShader(ShaderType type) const
	public final int createShader(ShaderType type)
	{
		return _nativeGL.createShader(type);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean compileShader(int shader, const String& source) const
	public final boolean compileShader(int shader, String source)
	{
		return _nativeGL.compileShader(shader, source);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean deleteShader(int shader) const
	public final boolean deleteShader(int shader)
	{
		return _nativeGL.deleteShader(shader);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void printShaderInfoLog(int shader) const
	public final void printShaderInfoLog(int shader)
	{
		_nativeGL.printShaderInfoLog(shader);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean linkProgram(int program) const
	public final boolean linkProgram(int program)
	{
		return _nativeGL.linkProgram(program);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void printProgramInfoLog(int program) const
	public final void printProgramInfoLog(int program)
	{
		_nativeGL.linkProgram(program);
	}

	public final boolean deleteProgram(GPUProgram program)
	{

		if (program == null)
		{
			return false;
		}

		if (_currentGPUProgram == program) //In case of deleting active program
		{
			_currentGPUProgram.removeReference();
			_currentGPUProgram = null;
		}

		return _nativeGL.deleteProgram(program.getProgramID());
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: INativeGL* getNative() const
	public final INativeGL getNative()
	{
		return _nativeGL;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void uniform2f(const IGLUniformID* loc, float x, float y) const
	public final void uniform2f(IGLUniformID loc, float x, float y)
	{
		_nativeGL.uniform2f(loc, x, y);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void uniform1f(const IGLUniformID* loc, float x) const
	public final void uniform1f(IGLUniformID loc, float x)
	{
		_nativeGL.uniform1f(loc, x);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void uniform1i(const IGLUniformID* loc, int v) const
	public final void uniform1i(IGLUniformID loc, int v)
	{
		_nativeGL.uniform1i(loc, v);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void uniformMatrix4fv(const IGLUniformID* location, boolean transpose, const Matrix44D* matrix) const
	public final void uniformMatrix4fv(IGLUniformID location, boolean transpose, Matrix44D matrix)
	{
		_nativeGL.uniformMatrix4fv(location, transpose, matrix);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void uniform4f(const IGLUniformID* location, float v0, float v1, float v2, float v3) const
	public final void uniform4f(IGLUniformID location, float v0, float v1, float v2, float v3)
	{
		_nativeGL.uniform4f(location, v0, v1, v2, v3);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void uniform3f(const IGLUniformID* location, float v0, float v1, float v2) const
	public final void uniform3f(IGLUniformID location, float v0, float v1, float v2)
	{
		_nativeGL.uniform3f(location, v0, v1, v2);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void vertexAttribPointer(int index, int size, boolean normalized, int stride, const IFloatBuffer* buffer) const
	public final void vertexAttribPointer(int index, int size, boolean normalized, int stride, IFloatBuffer buffer)
	{
		_nativeGL.vertexAttribPointer(index, size, normalized, stride, buffer);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void bindAttribLocation(const GPUProgram* program, int loc, const String& name) const
	public final void bindAttribLocation(GPUProgram program, int loc, String name)
	{
		_nativeGL.bindAttribLocation(program, loc, name);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getProgramiv(const GPUProgram* program, int pname) const
	public final int getProgramiv(GPUProgram program, int pname)
	{
		return _nativeGL.getProgramiv(program, pname);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GPUUniform* getActiveUniform(const GPUProgram* program, int i) const
	public final GPUUniform getActiveUniform(GPUProgram program, int i)
	{
		return _nativeGL.getActiveUniform(program, i);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GPUAttribute* getActiveAttribute(const GPUProgram* program, int i) const
	public final GPUAttribute getActiveAttribute(GPUProgram program, int i)
	{
		return _nativeGL.getActiveAttribute(program, i);
	}

	public final void useProgram(GPUProgram program)
	{
		if (program != null)
		{
			if (_currentGPUProgram != program)
			{
    
				if (_currentGPUProgram != null)
				{
					_currentGPUProgram.onUnused(this);
					_currentGPUProgram.removeReference();
				}
    
				_nativeGL.useProgram(program);
				program.onUsed();
				_currentGPUProgram = program;
				_currentGPUProgram.addReference();
			}
    
			//    if (!_nativeGL->isProgram(program->getProgramID())) {
			//      ILogger::instance()->logError("INVALID PROGRAM.");
			//    }
		}
    
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void enableVertexAttribArray(int location) const
	public final void enableVertexAttribArray(int location)
	{
		_nativeGL.enableVertexAttribArray(location);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void disableVertexAttribArray(int location) const
	public final void disableVertexAttribArray(int location)
	{
		_nativeGL.disableVertexAttribArray(location);
	}

	public final GLGlobalState getCurrentGLGlobalState()
	{
		return _currentGLGlobalState;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void viewport(int x, int y, int width, int height) const
	public final void viewport(int x, int y, int width, int height)
	{
		_nativeGL.viewport(x, y, width, height);
	}


}
//void GL::applyGLGlobalStateAndGPUProgramState(const GLGlobalState& state, GPUProgramManager& progManager, const GPUProgramState& progState) {
//  state.applyChanges(this, *_currentState);
//  setProgramState(progManager, progState);
//}
