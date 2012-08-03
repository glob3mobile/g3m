package org.glob3.mobile.specific;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.glob3.mobile.generated.CullFace;
import org.glob3.mobile.generated.IGL;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.MutableMatrix44D;
import org.glob3.mobile.generated.Vector2D;
import org.glob3.mobile.generated.Vector3D;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class GL2 extends IGL {

	// stack of ModelView matrices
	MutableMatrix44D _modelView = new MutableMatrix44D();
	List<MutableMatrix44D> _matrixStack = new LinkedList<MutableMatrix44D>();

	public UniformsStruct getUniforms() {
		return Uniforms;
	}

	public AttributesStruct getAtt() {
		return Attributes;
	}

	public class UniformsStruct {
		public int Projection;
		public int Modelview;
		public int Sampler;
		public int EnableTexture;
		public int FlatColor;
		public int BillBoard;
		public int ViewPortRatio;
	}

	public UniformsStruct Uniforms = new UniformsStruct();

	public class AttributesStruct {
		public int Position;
		public int TextureCoord;
	}

	public AttributesStruct Attributes = new AttributesStruct();

	// TOO SLOW (PUT ANDROID BUG)
	static public long timeConvertingFloat = 0;
	Map<float[], FloatBuffer> arrayToBufferMap = new HashMap<float[], FloatBuffer>();

	private FloatBuffer floatArrayToFloatBuffer(final float[] fv) {

		// Clear the map when is big
		final int size = arrayToBufferMap.size();
		if (size > 5000) {
			arrayToBufferMap.clear();
		}

		if (!arrayToBufferMap.containsKey(fv)) {

			// Log.d("GL", "CREANDO BUFFER");
			final ByteBuffer byteBuf = ByteBuffer.allocateDirect(fv.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			final FloatBuffer fb = byteBuf.asFloatBuffer();

			final long t1 = System.nanoTime();

			fb.put(fv); // /TOO SLOW UNTIL VERSION GINGERBEAD (BECAUSE OF THIS,
						// USE HASHMAP)

			final long t2 = System.nanoTime();
			timeConvertingFloat += (t2 - t1);

			fb.position(0);

			arrayToBufferMap.put(fv, fb);

			return fb;
		}

		return arrayToBufferMap.get(fv);
	}


	@Override
	public void enableTextures() {
		GLES20.glEnableVertexAttribArray(Attributes.TextureCoord);
	}

	@Override
	public void enableTexture2D() {
		GLES20.glUniform1i(Uniforms.EnableTexture, 1);
	}

	@Override
	public void disableTexture2D() {
		GLES20.glUniform1i(Uniforms.EnableTexture, 0);
	}


	@Override
	public void disableTextures() {
		GLES20.glDisableVertexAttribArray(Attributes.TextureCoord);
	}

	@Override
	public void clearScreen(float r, float g, float b, float a) {
		GLES20.glClearColor(r, g, b, 1);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		GLES20.glUniform4f(Uniforms.FlatColor, r, g, b, 1);
	}

	@Override
	public void pushMatrix() {
		MutableMatrix44D m = new MutableMatrix44D(_modelView);
		_matrixStack.add(m);
	}

	@Override
	public void popMatrix() {
		_modelView = _matrixStack.remove(_matrixStack.size() - 1);
		float[] m = new float[16];
		_modelView.copyToFloatMatrix(m);

		GLES20.glUniformMatrix4fv(Uniforms.Modelview, 1, false, m, 0);
	}

	@Override
	public void loadMatrixf(MutableMatrix44D m) {
		float[] mf = new float[16];
		m.copyToFloatMatrix(mf);
		_modelView = new MutableMatrix44D(m); // STORING

		GLES20.glUniformMatrix4fv(Uniforms.Modelview, 1, false, mf, 0);
	}

	@Override
	public void multMatrixf(MutableMatrix44D m) {
		// TODO remove this TODO if this method is right 
		MutableMatrix44D product = _modelView.multiply(m);

		_modelView = new MutableMatrix44D(product);

		float[] mf = new float[16];
		product.copyToFloatMatrix(mf);
		GLES20.glUniformMatrix4fv(Uniforms.Modelview, 1, false, mf, 0);
	}

	@Override
	public void vertexPointer(int size, int stride, float[] vertex) {
		final FloatBuffer fb = floatArrayToFloatBuffer(vertex);
		GLES20.glVertexAttribPointer(Attributes.Position, size,
				GLES20.GL_FLOAT, false, stride, fb);
	}

	@Override
	public void drawTriangleStrip(int n, byte[] i) {
		final ByteBuffer indexBuffer = ByteBuffer.wrap(i);
		GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, n,
				GLES20.GL_UNSIGNED_BYTE, indexBuffer);
	}

	@Override
	public void drawLines(int n, byte[] i) {
		final ByteBuffer indexBuffer = ByteBuffer.wrap(i);
		GLES20.glDrawElements(GLES20.GL_LINES, n, GLES20.GL_UNSIGNED_BYTE,
				indexBuffer);
	}

	@Override
	public void drawLineLoop(int n, byte[] i) {
		final ByteBuffer indexBuffer = ByteBuffer.wrap(i);
		GLES20.glDrawElements(GLES20.GL_LINE_LOOP, n, GLES20.GL_UNSIGNED_BYTE,
				indexBuffer);
	}

	@Override
	public void setProjection(MutableMatrix44D projection) {
		float[] mf = new float[16];
		projection.copyToFloatMatrix(mf);
		GLES20.glUniformMatrix4fv(Uniforms.Projection, 1, false, mf, 0);
	}

	@Override
	public void useProgram(int program) {
		// set shaders
		GLES20.glUseProgram(program);

		// Extract the handles to attributes
		Attributes.Position = GLES20.glGetAttribLocation(program, "Position");
		Attributes.TextureCoord = GLES20.glGetAttribLocation(program,
				"TextureCoord");

		// Extract the handles to uniforms
		Uniforms.Projection = GLES20
				.glGetUniformLocation(program, "Projection");
		Uniforms.Modelview = GLES20.glGetUniformLocation(program, "Modelview");
		Uniforms.Sampler = GLES20.glGetUniformLocation(program, "Sampler");
		Uniforms.EnableTexture = GLES20.glGetUniformLocation(program,
				"EnableTexture");
		Uniforms.FlatColor = GLES20.glGetUniformLocation(program, "FlatColor");

		// BILLBOARDS
		Uniforms.BillBoard = GLES20.glGetUniformLocation(program, "BillBoard");
		GLES20.glUniform1i(Uniforms.BillBoard, 0); // NOT DRAWING BILLBOARD
		Uniforms.ViewPortRatio = GLES20.glGetUniformLocation(program,
				"ViewPortRatio");
	}

	@Override
	public void enablePolygonOffset(float factor, float units) {
		GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
		GLES20.glPolygonOffset(factor, units);
	}

	@Override
	public void disablePolygonOffset() {
		GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
	}

	@Override
	public void lineWidth(float width) {
		GLES20.glLineWidth(width);
	}

	@Override
	public int getError() {
		return GLES20.glGetError();
	}

	@Override
	public int uploadTexture(IImage image, int widthTexture, int heightTexture) {

		final Bitmap bitmap = ((Image_Android) image).getBitmap();
		if (bitmap == null) {
			return 0;
		}

		final int[] ids = new int[1];
		GLES20.glGenTextures(1, ids, 0);
		int texID = ids[0];
		if (texID == 0)
			return 0;

		// upload to GPU
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texID);

		// Transparent
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();

		return texID;
	}

	@Override
	public void setTextureCoordinates(int size, int stride, float[] texcoord) {
		final FloatBuffer fb = floatArrayToFloatBuffer(texcoord);
		GLES20.glVertexAttribPointer(Attributes.TextureCoord, size,
				GLES20.GL_FLOAT, false, stride, fb);
	}

	@Override
	public void bindTexture(int n) {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, n);
	}

	@Override
	public void deleteTexture(int glTextureId) {
		int[] ts = { glTextureId };
		GLES20.glDeleteTextures(1, ts, 0);
	}

	@Override
	public void enableVerticesPosition() {
		// TODO remove this TODO if this method is right
		GLES20.glEnableVertexAttribArray(Attributes.Position);
	}

	@Override
	public void disableVerticesPosition() {
		// TODO remove this TODO if this method is right
		GLES20.glDisableVertexAttribArray(Attributes.Position);
	}

	@Override
	public void enableVertexColor(float[] colors, float intensity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableVertexColor() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableVertexFlatColor(float r, float g, float b, float a,
			float intensity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableVertexFlatColor() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableVertexNormal(float[] normals) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableVertexNormal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableDepthTest() {
		// TODO remove this TODO if this method is right
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	}

	@Override
	public void disableDepthTest() {
		// TODO remove this TODO if this method is right
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
	}

	@Override
	public void enableBlend() {
		// TODO remove this TODO if this method is right
		GLES20.glEnable(GLES20.GL_BLEND);
	}

	@Override
	public void disableBlend() {
		// TODO remove this TODO if this method is right
		GLES20.glDisable(GLES20.GL_BLEND);
	}

	@Override
	public void enableCullFace(CullFace face) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableCullFace() {
		// TODO remove this TODO if this method is right
		GLES20.glDisable(GLES20.GL_CULL_FACE);
	}

	@Override
	public void drawTriangleStrip(int n, int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawLines(int n, int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawLineLoop(int n, int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawPoints(int n, int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pointSize(float size) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawBillBoard(int textureId, Vector3D pos, float viewPortRatio) {
		// TODO remove this TODO if this method is right
		
		GLES20.glUniform1i(Uniforms.BillBoard, 1); // DRAWING BILLBOARD

		final float vertex[] = { (float) pos.x(), (float) pos.y(), (float) pos.z(), (float) pos.x(), (float) pos.y(), (float) pos.z(), (float) pos.x(), (float) pos.y(), (float) pos.z(), (float) pos.x(), (float) pos.y(), (float) pos.z() };

		GLES20.glUniform1f(Uniforms.ViewPortRatio, viewPortRatio);

		final float texcoord[] = { 0, 0, 0, 1, 1, 0, 1, 1 };

		final FloatBuffer fbv = floatArrayToFloatBuffer(vertex);
		final FloatBuffer fbt = floatArrayToFloatBuffer(texcoord);

		GLES20.glDisable(GLES20.GL_DEPTH_TEST);

		GLES20.glUniform1i(Uniforms.EnableTexture, 1);
		GLES20.glUniform4f(Uniforms.FlatColor, 1.0f, 0.0f, 0.0f, 1.0f);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glVertexAttribPointer(Attributes.Position, 3, GLES20.GL_FLOAT,
				false, 0, fbv);
		GLES20.glVertexAttribPointer(Attributes.TextureCoord, 2,
				GLES20.GL_FLOAT, false, 0, fbt);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		GLES20.glUniform1i(Uniforms.BillBoard, 0); // NOT DRAWING BILLBOARD
	}

	@Override
	public void transformTexCoords(Vector2D scale, Vector2D translation) {
		// TODO Auto-generated method stub

	}

}
