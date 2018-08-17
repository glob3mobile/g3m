package org.glob3.mobile.generated;import java.util.*;

//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//


//
//  IndexedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IShortBuffer;

public class IndexedMesh extends AbstractMesh
{
	private IShortBuffer _indices;
	private boolean _ownsIndices;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc) const
	protected final void rawRender(G3MRenderContext rc)
	{
		GL gl = rc.getGL();
		gl.drawElements(_primitive, _indices, _glState, rc.getGPUProgramManager());
	}

	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, VertexColorScheme vertexColorScheme)
	{
		this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, vertexColorScheme, -1.0f);
	}
	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
	{
		this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, null, -1.0f);
	}
	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor)
	{
		this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, 0, null, -1.0f);
	}
	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill)
	{
		this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, 0, 0, null, -1.0f);
	}
	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals)
	{
		this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, false, 0, 0, null, -1.0f);
	}
	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest)
	{
		this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, null, false, 0, 0, null, -1.0f);
	}
	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity)
	{
		this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, true, null, false, 0, 0, null, -1.0f);
	}
	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors)
	{
		this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, 0.0f, true, null, false, 0, 0, null, -1.0f);
	}
	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor)
	{
		this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, null, 0.0f, true, null, false, 0, 0, null, -1.0f);
	}
	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize)
	{
		this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, null, null, 0.0f, true, null, false, 0, 0, null, -1.0f);
	}
	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth)
	{
		this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, 1, null, null, 0.0f, true, null, false, 0, 0, null, -1.0f);
	}
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedMesh(const int primitive, const Vector3D& center, IFloatBuffer* vertices, boolean ownsVertices, IShortBuffer* indices, boolean ownsIndices, float lineWidth, float pointSize = 1, const Color* flatColor = null, IFloatBuffer* colors = null, const float colorsIntensity = 0.0f, boolean depthTest = true, IFloatBuffer* normals = null, boolean polygonOffsetFill = false, float polygonOffsetFactor = 0, float polygonOffsetUnits = 0, VertexColorScheme* vertexColorScheme = null, float transparencyDistanceThreshold = -1.0f) : AbstractMesh(primitive, ownsVertices, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, vertexColorScheme, transparencyDistanceThreshold), _indices(indices), _ownsIndices(ownsIndices)
	public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, VertexColorScheme vertexColorScheme, float transparencyDistanceThreshold)
	{
		super(primitive, ownsVertices, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, vertexColorScheme, transparencyDistanceThreshold);
		_indices = indices;
		_ownsIndices = ownsIndices;
    
	}

	public void dispose()
	{
		if (_ownsIndices)
		{
			if (_indices != null)
				_indices.dispose();
		}
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		super.dispose();
//#endif
    
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IShortBuffer* getIndices() const
	public final IShortBuffer getIndices()
	{
		return _indices;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const void getTrianglePrimitive(short t, MutableVector3D& v1, MutableVector3D& v2, MutableVector3D& v3) const
	public final void getTrianglePrimitive(short t, tangible.RefObject<MutableVector3D> v1, tangible.RefObject<MutableVector3D> v2, tangible.RefObject<MutableVector3D> v3)
	{
		t *= 3;
		short i1 = _indices.get(t);
		short i2 = _indices.get(t+1);
		short i3 = _indices.get(t+2);
    
		v1.argvalue.copyFrom(getVertex(i1));
		v2.argvalue.copyFrom(getVertex(i2));
		v3.argvalue.copyFrom(getVertex(i3));
	}

	public final Vector3D getHitWithRayForTrianglePrimitive(Vector3D origin, Vector3D ray, short firstTriangle)
	{
		return getHitWithRayForTrianglePrimitive(origin, ray, firstTriangle, -1);
	}
	public final Vector3D getHitWithRayForTrianglePrimitive(Vector3D origin, Vector3D ray)
	{
		return getHitWithRayForTrianglePrimitive(origin, ray, -1, -1);
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector3D getHitWithRayForTrianglePrimitive(const Vector3D& origin, const Vector3D& ray, short firstTriangle =-1, short lastTriangle =-1) const
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
	public final Vector3D getHitWithRayForTrianglePrimitive(Vector3D origin, Vector3D ray, short firstTriangle, short lastTriangle)
	{
		if (_primitive != GLPrimitive.triangles() || _primitive != GLPrimitive.triangleStrip())
		{
			ILogger.instance().logError("getHitWithRayForTrianglePrimitive(): Primitive not supported");
		}
    
    
		if (firstTriangle < 0)
		{
			firstTriangle = 0;
		}
		if (lastTriangle < 0)
		{
			lastTriangle = (short)_indices.size() / 3;
		}
    
		MutableVector3D v1 = new MutableVector3D();
		MutableVector3D v2 = new MutableVector3D();
		MutableVector3D v3 = new MutableVector3D();
		MutableVector3D closestP = MutableVector3D.nan();
		double minDist = IMathUtils.instance().maxDouble();
		//Triangle intersection
		for(short t = firstTriangle; t < lastTriangle; ++t)
		{
			tangible.RefObject<MutableVector3D> tempRef_v1 = new tangible.RefObject<MutableVector3D>(v1);
			tangible.RefObject<MutableVector3D> tempRef_v2 = new tangible.RefObject<MutableVector3D>(v2);
			tangible.RefObject<MutableVector3D> tempRef_v3 = new tangible.RefObject<MutableVector3D>(v3);
			getTrianglePrimitive(t, tempRef_v1, tempRef_v2, tempRef_v3);
			v1 = tempRef_v1.argvalue;
			v2 = tempRef_v2.argvalue;
			v3 = tempRef_v3.argvalue;
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D p = Vector3D::rayIntersectsTriangle(origin, ray, v1.asVector3D(), v2.asVector3D(), v3.asVector3D());
			Vector3D p = Vector3D.rayIntersectsTriangle(new Vector3D(origin), new Vector3D(ray), v1.asVector3D(), v2.asVector3D(), v3.asVector3D());
    
			if (!p.isNan())
			{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double dist = p.squaredDistanceTo(origin);
				double dist = p.squaredDistanceTo(new Vector3D(origin));
				if (dist < minDist)
				{
					closestP.copyFrom(p);
					minDist = dist;
				}
			}
		}
		return closestP.asVector3D();
	}

}
