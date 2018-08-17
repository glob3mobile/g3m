package org.glob3.mobile.generated;//
//  AbstractMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

//
//  AbstractMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//




public abstract class AbstractMesh extends Mesh
{
	protected final int _primitive;
	protected final boolean _owner;
	protected final Vector3D _center = new Vector3D();
	protected final MutableMatrix44D _translationMatrix;
	protected final IFloatBuffer _vertices;
	protected final Color _flatColor;
	protected final IFloatBuffer _colors;
	protected final float _colorsIntensity;
	protected final float _lineWidth;
	protected final float _pointSize;
	protected final boolean _depthTest;
	protected final IFloatBuffer _normals;

	protected VertexColorScheme _vertexColorScheme;

	protected float _transparencyDistanceThreshold;
	protected ModelGLFeature _model;

	protected ModelTransformGLFeature _modelTransform;

	protected BoundingVolume _boundingVolume;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: BoundingVolume* computeBoundingVolume() const
	protected final BoundingVolume computeBoundingVolume()
	{
		final int vertexCount = getVertexCount();
    
		if (vertexCount == 0)
		{
			return null;
		}
    
		double minX = 1e12;
		double minY = 1e12;
		double minZ = 1e12;
    
		double maxX = -1e12;
		double maxY = -1e12;
		double maxZ = -1e12;
    
		for (int i = 0; i < vertexCount; i++)
		{
			final int i3 = i * 3;
    
			final double x = _vertices.get(i3) + _center._x;
			final double y = _vertices.get(i3 + 1) + _center._y;
			final double z = _vertices.get(i3 + 2) + _center._z;
    
			if (x < minX)
				minX = x;
			if (x > maxX)
				maxX = x;
    
			if (y < minY)
				minY = y;
			if (y > maxY)
				maxY = y;
    
			if (z < minZ)
				minZ = z;
			if (z > maxZ)
				maxZ = z;
		}
    
		return new Box(new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ));
	}

	protected final boolean _polygonOffsetFill;
	protected final float _polygonOffsetFactor;
	protected final float _polygonOffsetUnits;

	protected AbstractMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, VertexColorScheme vertexColorScheme, float transparencyDistanceThreshold)
	{
		_primitive = primitive;
		_owner = owner;
		_vertices = vertices;
		_flatColor = flatColor;
		_colors = colors;
		_colorsIntensity = colorsIntensity;
		_boundingVolume = null;
		_center = new Vector3D(center);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _translationMatrix = (center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D::createTranslationMatrix(center));
		_translationMatrix = (center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(new Vector3D(center)));
		_lineWidth = lineWidth;
		_pointSize = pointSize;
		_depthTest = depthTest;
		_glState = new GLState();
		_normals = normals;
		_normalsMesh = null;
		_showNormals = false;
		_polygonOffsetFactor = polygonOffsetFactor;
		_polygonOffsetUnits = polygonOffsetUnits;
		_polygonOffsetFill = polygonOffsetFill;
		_vertexColorScheme = vertexColorScheme;
		_modelTransform = new ModelTransformGLFeature(Matrix44D.createIdentity());
		_transparencyDistanceThreshold = transparencyDistanceThreshold;
		_model = null;
		createGLState();
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void rawRender(const G3MRenderContext* rc) const = 0;
	protected abstract void rawRender(G3MRenderContext rc);

	protected GLState _glState;

	protected final void createGLState()
	{
    
		_glState.addGLFeature(new GeometryGLFeature(_vertices, 3, 0, false, 0, _depthTest, false, 0, _polygonOffsetFill, _polygonOffsetFactor, _polygonOffsetUnits, _lineWidth, true, _pointSize), false); //Polygon Offset - Cull and culled face - Depth test - Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
    
		if (_normals != null)
		{
			_glState.addGLFeature(new VertexNormalGLFeature(_normals, 3, 0, false, 0), false);
		}
    
    
		_glState.addGLFeature(_modelTransform, true);
    
		if (_translationMatrix != null)
		{
			_glState.addGLFeature(new ModelTransformGLFeature(_translationMatrix.asMatrix44D()), false);
		}
    
		if (_transparencyDistanceThreshold > 0.0)
		{
			_glState.addGLFeature(new TransparencyDistanceThresholdGLFeature(_transparencyDistanceThreshold), false);
			_model = new ModelGLFeature(Matrix44D.createIdentity());
			_glState.addGLFeature(_model, true);
		}
    
		if (_flatColor != null && _colors == null) //FlatColorMesh Shader
		{
			_glState.addGLFeature(new FlatColorGLFeature(_flatColor, _flatColor.isTransparent(), GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
		}
		else if (_colors != null)
		{
			_glState.addGLFeature(new ColorGLFeature(_colors, 4, 0, false, 0, true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false); // Stride 0 -  Not normalized -  Index 0 -  Our buffer contains elements of 4 -  The attribute is a float vector of 4 elements RGBA
    
		}
		else if (_vertexColorScheme != null)
		{
			_glState.addGLFeature(_vertexColorScheme.getGLFeature(), true);
		}
    
	}

	protected boolean _showNormals;
	protected Mesh _normalsMesh;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createNormalsMesh() const
	protected final Mesh createNormalsMesh()
	{
    
		DirectMesh verticesMesh = new DirectMesh(GLPrimitive.points(), false, _center, _vertices, (float)1.0, (float)2.0, new Color(Color.red()), null, (float)1.0, false, null);
    
		FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
    
		BoundingVolume volume = getBoundingVolume();
		Sphere sphere = volume.createSphere();
		double normalsSize = sphere.getRadius() / 100.0;
		if (sphere != null)
			sphere.dispose();
    
		final int size = _vertices.size();
		for (int i = 0; i < size; i+=3)
		{
			final Vector3D v = new Vector3D(_vertices.get(i), _vertices.get(i+1), _vertices.get(i+2));
			final Vector3D n = new Vector3D(_normals.get(i), _normals.get(i+1), _normals.get(i+2));
    
			final Vector3D v_n = v.add(n.normalized().times(normalsSize));
    
			fbb.add(v);
			fbb.add(v_n);
		}
    
		DirectMesh normalsMesh = new DirectMesh(GLPrimitive.lines(), true, _center, fbb.create(), (float)2.0, (float)1.0, new Color(Color.blue()));
    
		if (fbb != null)
			fbb.dispose();
    
		CompositeMesh compositeMesh = new CompositeMesh();
		compositeMesh.addMesh(verticesMesh);
		compositeMesh.addMesh(normalsMesh);
    
		return compositeMesh;
    
	}

	public void dispose()
	{
		if (_owner)
		{
			if (_vertices != null)
				_vertices.dispose();
			if (_colors != null)
				_colors.dispose();
			if (_normals != null)
				_normals.dispose();
		}
    
		//Always deleting flatColor
		if (_flatColor != null)
			_flatColor.dispose();
    
		if (_boundingVolume != null)
			_boundingVolume.dispose();
		_boundingVolume = null;
		if (_translationMatrix != null)
			_translationMatrix.dispose();
		_translationMatrix = null;
    
		_glState._release();
    
		if (_normalsMesh != null)
			_normalsMesh.dispose();
    
		if (_modelTransform != null)
		{
			_modelTransform._release();
		}
		if (_model != null)
		{
			_model._release();
		}
    
		if (_vertexColorScheme != null)
			_vertexColorScheme.dispose();
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		super.dispose();
//#endif
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: BoundingVolume* getBoundingVolume() const
	public final BoundingVolume getBoundingVolume()
	{
		if (_boundingVolume == null)
		{
			_boundingVolume = computeBoundingVolume();
		}
		return _boundingVolume;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getVertexCount() const
	public final int getVertexCount()
	{
		return _vertices.size() / 3;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector3D getVertex(int i) const
	public final Vector3D getVertex(int i)
	{
		final int p = i * 3;
		return new Vector3D(_vertices.get(p) + _center._x, _vertices.get(p+1) + _center._y, _vertices.get(p+2) + _center._z);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent(const G3MRenderContext* rc) const
	public final boolean isTransparent(G3MRenderContext rc)
	{
		if (_flatColor == null)
		{
			return false;
		}
		return _flatColor.isTransparent();
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const
	public final void rawRender(G3MRenderContext rc, GLState parentGLState)
	{
		if (_model != null)
		{
			Matrix44D model = rc.getCurrentCamera().getModelMatrix44D();
	//        Matrix44D* transform = _translationMatrix->asMatrix44D();
	//        Matrix44D* m = transform->createMultiplication(*model);
			_model.setMatrix(model);
		}
    
		_glState.setParent(parentGLState);
		rawRender(rc);
    
		//RENDERING NORMALS
		if (_normals != null)
		{
			if (_showNormals)
			{
				if (_normalsMesh == null)
				{
					//_normalsMesh = createNormalsMesh();
				}
				if (_normalsMesh != null)
				{
					//_normalsMesh->render(rc, parentGLState);
				}
			}
			else
			{
				if (_normalsMesh != null)
				{
					if (_normalsMesh != null)
						_normalsMesh.dispose();
					_normalsMesh = null;
				}
			}
		}
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getCenter() const
	public final Vector3D getCenter()
	{
		return _center;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showNormals(boolean v) const
	public final void showNormals(boolean v)
	{
		_showNormals = v;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* getColorsFloatBuffer() const
	public final IFloatBuffer getColorsFloatBuffer()
	{
		return (IFloatBuffer)_colors;
	}

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Chano adding stuff.

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* getVerticesFloatBuffer() const
	public final IFloatBuffer getVerticesFloatBuffer()
	{
		return (IFloatBuffer) _vertices;
	}

	public void setColorTransparency(java.util.ArrayList<Double> transparency)
	{
		IFloatBuffer colors = (IFloatBuffer)_colors;
		if (colors != null)
		{
			for (int j = 3, c=0; j<colors.size(); j = j+4, c++)
			{
				double ndt = transparency.get(c);
				colors.put(j,(float)ndt); //Suponiendo que sea un valor de alpha
			}
		}
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: VertexColorScheme* getVertexColorScheme() const
	public final VertexColorScheme getVertexColorScheme()
	{
		return _vertexColorScheme;
	}

	public final void setTransformation(Matrix44D matrix)
	{
		_modelTransform.setMatrix(matrix);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getClosestVertex(const Vector3D& v, double & distance) const
	public final int getClosestVertex(Vector3D v, tangible.RefObject<Double> distance)
	{
		int n = getVertexCount();
		int index = n+1;
		distance.argvalue = IMathUtils.instance().maxDouble();
		for (int i = 0; i < n; ++i)
		{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double d = getVertex(i).squaredDistanceTo(v);
			double d = getVertex(i).squaredDistanceTo(new Vector3D(v));
			if (distance.argvalue > d)
			{
				distance.argvalue = d;
				index = i;
			}
		}
		return index;
	}


}
