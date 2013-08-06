package org.glob3.mobile.generated; 
public class Mark
{
  /**
   * The text the mark displays.
   * Useless if the mark does not have label.
   */
  private final String _label;
  /**
   * Flag to know if the label will be located under the icon (if TRUE) or on its right (if FALSE).
   * Useless if the mark does not have label or icon.
   * Default value: TRUE
   */
  private final boolean _labelBottom;
  /**
   * The font size of the text.
   * Useless if the mark does not have label.
   * Default value: 20
   */
  private final float _labelFontSize;
  /**
   * The color of the text.
   * Useless if the mark does not have label.
   * Default value: white
   */
  private final Color _labelFontColor;
  /**
   * The color of the text shadow.
   * Useless if the mark does not have label.
   * Default value: black
   */
  private final Color _labelShadowColor;
  /**
   * The number of pixels between the icon and the text.
   * Useless if the mark does not have label or icon.
   * Default value: 2
   */
  private final int _labelGapSize;
  /**
   * The URL to get the image file.
   * Useless if the mark does not have icon.
   */
  private URL _iconURL = new URL();
  /**
   * The point where the mark will be geo-located.
   */
  private final Geodetic3D _position ;
  /**
   * The minimun distance (in meters) to show the mark. If the camera is further than this, the mark will not be displayed.
   * Default value: 4.5e+06
   */
  private double _minDistanceToCamera;
  /**
   * The extra data to be stored by the mark.
   * Usefull to store data such us name, URL...
   */
  private MarkUserData _userData;
  /**
   * Flag to know if the mark is the owner of _userData and thus it must delete it on destruction.
   * Default value: TRUE
   */
  private final boolean _autoDeleteUserData;
  /**
   * Interface for listening to the touch event.
   */
  private MarkTouchListener _listener;
  /**
   * Flag to know if the mark is the owner of _listener and thus it must delete it on destruction.
   * Default value: FALSE
   */
  private final boolean _autoDeleteListener;

  private IGLTextureId _textureId;

  private Vector3D _cartesianPosition;

  private IFloatBuffer _vertices;
//  IFloatBuffer* getVertices(const Planet* planet);

  private boolean _textureSolved;
  private IImage _textureImage;
  private int _textureWidth;
  private int _textureHeight;
  private final String _imageID;

  private boolean _renderedMark;

  private static IFloatBuffer _billboardTexCoord = null;
  private int _viewportWidth;
  private int _viewportHeight;

  private GLState _glState = new GLState();

  private void createGLState(Planet planet, int viewportWidth, int viewportHeight)
  {
  
    _viewportHeight = viewportHeight;
    _viewportWidth = viewportWidth;
  
    if (_vertices == null)
    {
      final Vector3D pos = new Vector3D(planet.toCartesian(_position));
      FloatBufferBuilderFromCartesian3D vertex = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
      vertex.add(pos);
      vertex.add(pos);
      vertex.add(pos);
      vertex.add(pos);
  
      _vertices = vertex.create();
    }
  
    _glState.clearGLFeatureGroup(GLFeatureGroupName.NO_GROUP);
  //  _glState.addGLFeature(new BillboardGLFeature(_textureWidth, _textureHeight,
  //                                               viewportWidth, viewportHeight), false);
  
    _glState.addGLFeature(new TextureExtentGLFeature(_textureWidth, _textureHeight), false);
  
    _glState.addGLFeature(new GeometryGLFeature(_vertices, 3, 0, false, 0, false, false, 0, false, 0, 0, (float)1.0, false, (float)1.0), false); //POINT SIZE - LINE WIDTH - NO POLYGON OFFSET - NO CULLING - NO DEPTH TEST - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
  }

  private IFloatBuffer getBillboardTexCoords()
  {
    if (_billboardTexCoord == null)
    {
      FloatBufferBuilderFromCartesian2D texCoor = new FloatBufferBuilderFromCartesian2D();
      texCoor.add(1,1);
      texCoor.add(1,0);
      texCoor.add(0,1);
      texCoor.add(0,0);
      _billboardTexCoord = texCoor.create();
    }
    return _billboardTexCoord;
  }

  /**
   * Creates a marker with icon and label
   */
  public Mark(String label, URL iconURL, Geodetic3D position, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
     this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, userData, autoDeleteUserData, listener, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData, boolean autoDeleteUserData)
  {
     this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, userData, autoDeleteUserData, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData)
  {
     this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, userData, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize)
  {
     this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor)
  {
     this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor)
  {
     this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, double minDistanceToCamera, boolean labelBottom, float labelFontSize)
  {
     this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, double minDistanceToCamera, boolean labelBottom)
  {
     this(label, iconURL, position, minDistanceToCamera, labelBottom, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, double minDistanceToCamera)
  {
     this(label, iconURL, position, minDistanceToCamera, true, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position)
  {
     this(label, iconURL, position, 4.5e+06, true, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
     _label = label;
     _iconURL = new URL(iconURL);
     _position = new Geodetic3D(position);
     _labelBottom = labelBottom;
     _labelFontSize = labelFontSize;
     _labelFontColor = labelFontColor;
     _labelShadowColor = labelShadowColor;
     _labelGapSize = labelGapSize;
     _textureId = null;
     _cartesianPosition = null;
     _vertices = null;
     _textureSolved = false;
     _textureImage = null;
     _renderedMark = false;
     _textureWidth = 0;
     _textureHeight = 0;
     _userData = userData;
     _autoDeleteUserData = autoDeleteUserData;
     _minDistanceToCamera = minDistanceToCamera;
     _listener = listener;
     _autoDeleteListener = autoDeleteListener;
     _imageID = iconURL.getPath() + "_" + label;
  
  }

  /**
   * Creates a marker just with label, without icon
   */
  public Mark(String label, Geodetic3D position, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
     this(label, position, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, autoDeleteUserData, listener, false);
  }
  public Mark(String label, Geodetic3D position, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData, boolean autoDeleteUserData)
  {
     this(label, position, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, autoDeleteUserData, null, false);
  }
  public Mark(String label, Geodetic3D position, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData)
  {
     this(label, position, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, true, null, false);
  }
  public Mark(String label, Geodetic3D position, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor)
  {
     this(label, position, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, double minDistanceToCamera, float labelFontSize, Color labelFontColor)
  {
     this(label, position, minDistanceToCamera, labelFontSize, labelFontColor, Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, double minDistanceToCamera, float labelFontSize)
  {
     this(label, position, minDistanceToCamera, labelFontSize, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, double minDistanceToCamera)
  {
     this(label, position, minDistanceToCamera, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
  public Mark(String label, Geodetic3D position)
  {
     this(label, position, 4.5e+06, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
     _label = label;
     _labelBottom = true;
     _iconURL = new URL("", false);
     _position = new Geodetic3D(position);
     _labelFontSize = labelFontSize;
     _labelFontColor = labelFontColor;
     _labelShadowColor = labelShadowColor;
     _labelGapSize = 2;
     _textureId = null;
     _cartesianPosition = null;
     _vertices = null;
     _textureSolved = false;
     _textureImage = null;
     _renderedMark = false;
     _textureWidth = 0;
     _textureHeight = 0;
     _userData = userData;
     _autoDeleteUserData = autoDeleteUserData;
     _minDistanceToCamera = minDistanceToCamera;
     _listener = listener;
     _autoDeleteListener = autoDeleteListener;
     _imageID = "_" + label;
  
  }

  /**
   * Creates a marker just with icon, without label
   */
  public Mark(URL iconURL, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
     this(iconURL, position, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
  }
  public Mark(URL iconURL, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData)
  {
     this(iconURL, position, minDistanceToCamera, userData, autoDeleteUserData, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, double minDistanceToCamera, MarkUserData userData)
  {
     this(iconURL, position, minDistanceToCamera, userData, true, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, double minDistanceToCamera)
  {
     this(iconURL, position, minDistanceToCamera, null, true, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position)
  {
     this(iconURL, position, 4.5e+06, null, true, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
     _label = "";
     _labelBottom = true;
     _iconURL = new URL(iconURL);
     _position = new Geodetic3D(position);
     _labelFontSize = 20F;
     _labelFontColor = Color.newFromRGBA(1, 1, 1, 1);
     _labelShadowColor = Color.newFromRGBA(0, 0, 0, 1);
     _labelGapSize = 2;
     _textureId = null;
     _cartesianPosition = null;
     _vertices = null;
     _textureSolved = false;
     _textureImage = null;
     _renderedMark = false;
     _textureWidth = 0;
     _textureHeight = 0;
     _userData = userData;
     _autoDeleteUserData = autoDeleteUserData;
     _minDistanceToCamera = minDistanceToCamera;
     _listener = listener;
     _autoDeleteListener = autoDeleteListener;
     _imageID = iconURL.getPath() + "_";
  
  }

  /**
   * Creates a marker whith a given pre-renderer IImage
   */
  public Mark(IImage image, String imageID, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
     this(image, imageID, position, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData)
  {
     this(image, imageID, position, minDistanceToCamera, userData, autoDeleteUserData, null, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, double minDistanceToCamera, MarkUserData userData)
  {
     this(image, imageID, position, minDistanceToCamera, userData, true, null, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, double minDistanceToCamera)
  {
     this(image, imageID, position, minDistanceToCamera, null, true, null, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position)
  {
     this(image, imageID, position, 4.5e+06, null, true, null, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
     _label = "";
     _labelBottom = true;
     _iconURL = new URL(new URL("", false));
     _position = new Geodetic3D(position);
     _labelFontSize = 20F;
     _labelFontColor = null;
     _labelShadowColor = null;
     _labelGapSize = 2;
     _textureId = null;
     _cartesianPosition = null;
     _vertices = null;
     _textureSolved = true;
     _textureImage = image;
     _renderedMark = false;
     _textureWidth = image.getWidth();
     _textureHeight = image.getHeight();
     _userData = userData;
     _autoDeleteUserData = autoDeleteUserData;
     _minDistanceToCamera = minDistanceToCamera;
     _listener = listener;
     _autoDeleteListener = autoDeleteListener;
     _imageID = imageID;
  
  }

  public void dispose()
  {
    if (_cartesianPosition != null)
       _cartesianPosition.dispose();
    if (_vertices != null)
       _vertices.dispose();
    if (_autoDeleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
    if (_autoDeleteUserData)
    {
      if (_userData != null)
         _userData.dispose();
    }
    if (_textureImage != null)
    {
      IFactory.instance().deleteImage(_textureImage);
    }
  }

  public final String getLabel()
  {
    return _label;
  }

  public final Geodetic3D getPosition()
  {
    return _position;
  }

  public final void initialize(G3MContext context, long downloadPriority)
  {
    if (!_textureSolved)
    {
      final boolean hasLabel = (_label.length() != 0);
      final boolean hasIconURL = (_iconURL.getPath().length() != 0);
  
      if (hasIconURL)
      {
        IDownloader downloader = context.getDownloader();
  
        downloader.requestImage(_iconURL, downloadPriority, TimeInterval.fromDays(30), true, new IconDownloadListener(this, _label, _labelBottom, _labelFontSize, _labelFontColor, _labelShadowColor, _labelGapSize), true);
      }
      else
      {
        if (hasLabel)
        {
          ITextUtils.instance().createLabelImage(_label, _labelFontSize, _labelFontColor, _labelShadowColor, new MarkLabelImageListener(null, this), true);
        }
        else
        {
          ILogger.instance().logWarning("Marker created without label nor icon");
        }
      }
    }
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void render(G3MRenderContext rc, Vector3D cameraPosition);

  public final boolean isReady()
  {
    return _textureSolved;
  }

  public final boolean isRendered()
  {
    return _renderedMark;
  }

  public final void onTextureDownloadError()
  {
    _textureSolved = true;
  
    if (_labelFontColor != null)
       _labelFontColor.dispose();
    if (_labelShadowColor != null)
       _labelShadowColor.dispose();
  
    ILogger.instance().logError("Can't create texture for Mark (iconURL=\"%s\", label=\"%s\")", _iconURL.getPath(), _label);
  }

  public final void onTextureDownload(IImage image)
  {
    _textureSolved = true;
  
    if (_labelFontColor != null)
       _labelFontColor.dispose();
    if (_labelShadowColor != null)
       _labelShadowColor.dispose();
    //  _textureImage = image->shallowCopy();
    _textureImage = image;
    _textureWidth = _textureImage.getWidth();
    _textureHeight = _textureImage.getHeight();
    //  IFactory::instance()->deleteImage(image);
  }

  public final int getTextureWidth()
  {
    return _textureWidth;
  }

  public final int getTextureHeight()
  {
    return _textureHeight;
  }

  public final Vector2I getTextureExtent()
  {
    return new Vector2I(_textureWidth, _textureHeight);
  }

  public final MarkUserData getUserData()
  {
    return _userData;
  }

  public final void setUserData(MarkUserData userData)
  {
    if (_autoDeleteUserData)
    {
      if (_userData != null)
         _userData.dispose();
    }
    _userData = userData;
  }

  public final boolean touched()
  {
    return (_listener == null) ? false : _listener.touchedMark(this);
    //  if (_listener == NULL) {
    //    return false;
    //  }
    //  return _listener->touchedMark(this);
  }

  public final Vector3D getCartesianPosition(Planet planet)
  {
    if (_cartesianPosition == null)
    {
      _cartesianPosition = new Vector3D(planet.toCartesian(_position));
    }
    return _cartesianPosition;
  }

  public final void setMinDistanceToCamera(double minDistanceToCamera)
  {
    _minDistanceToCamera = minDistanceToCamera;
  }
  public final double getMinDistanceToCamera()
  {
    return _minDistanceToCamera;
  }

  public final void render(G3MRenderContext rc, Vector3D cameraPosition, GLState parentGLState, Planet planet, GL gl)
  {
  //  const Planet* planet = rc->getPlanet();
  
    final Vector3D markPosition = getCartesianPosition(planet);
  
    final Vector3D markCameraVector = markPosition.sub(cameraPosition);
  
    // mark will be renderered only if is renderable by distance and placed on a visible globe area
    boolean renderableByDistance;
    if (_minDistanceToCamera == 0)
    {
      renderableByDistance = true;
    }
    else
    {
      final double squaredDistanceToCamera = markCameraVector.squaredLength();
      renderableByDistance = (squaredDistanceToCamera <= (_minDistanceToCamera * _minDistanceToCamera));
    }
  
    _renderedMark = false;
    if (renderableByDistance)
    {
      final Vector3D normalAtMarkPosition = planet.geodeticSurfaceNormal(markPosition);
  
      if (normalAtMarkPosition.angleBetween(markCameraVector)._radians > DefineConstants.HALF_PI)
      {
  
        if (_textureId == null)
        {
          if (_textureImage != null)
          {
            _textureId = rc.getTexturesHandler().getGLTextureId(_textureImage, GLFormat.rgba(), _imageID, false);
  
            rc.getFactory().deleteImage(_textureImage);
            _textureImage = null;
  
            _glState.addGLFeature(new TextureGLFeature(_textureId, getBillboardTexCoords(), 2, 0, false, 0, true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), false, Vector2D.zero(), Vector2D.zero()), false);
          }
        }
        else
        {
          if (rc.getCurrentCamera().getWidth() != _viewportWidth || rc.getCurrentCamera().getHeight() != _viewportHeight)
          {
            createGLState(rc.getPlanet(), rc.getCurrentCamera().getWidth(), rc.getCurrentCamera().getHeight());
          }
  
  //        GL* gl = rc->getGL();
  
          GPUProgramManager progManager = rc.getGPUProgramManager();
  
          _glState.setParent(parentGLState); //Linking with parent
  
          gl.drawArrays(GLPrimitive.triangleStrip(), 0, 4, _glState, progManager);
  
          _renderedMark = true;
        }
      }
    }
  
  }

}