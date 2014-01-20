package org.glob3.mobile.generated; 
public class Mark implements SurfaceElevationListener
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
  private Geodetic3D _position;
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

  private TextureIDReference _textureId;

  private Vector3D _cartesianPosition;

  private boolean _textureSolved;
  private IImage _textureImage;
  private int _textureWidth;
  private int _textureHeight;
  private final String _imageID;

  private boolean _renderedMark;

  private static IFloatBuffer _billboardTexCoord = null;

  private GLState _glState;
  private void createGLState(Planet planet)
  {
  
    _glState = new GLState();
  
    _glState.addGLFeature(new BillboardGLFeature(getCartesianPosition(planet), _textureWidth, _textureHeight), false);
  
    if (_textureId != null)
    {
      _glState.addGLFeature(new TextureGLFeature(_textureId.getID(), getBillboardTexCoords(), 2, 0, false, 0, true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
    }
  }

  private static IFloatBuffer getBillboardTexCoords()
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

  private SurfaceElevationProvider _surfaceElevationProvider;
  private double _currentSurfaceElevation;
  private AltitudeMode _altitudeMode;

  private Vector3D _normalAtMarkPosition;

  /**
   * Creates a marker with icon and label
   */
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
     this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, userData, autoDeleteUserData, listener, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData, boolean autoDeleteUserData)
  {
     this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, userData, autoDeleteUserData, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData)
  {
     this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, userData, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize)
  {
     this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor)
  {
     this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor)
  {
     this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize)
  {
     this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom)
  {
     this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera)
  {
     this(label, iconURL, position, altitudeMode, minDistanceToCamera, true, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode)
  {
     this(label, iconURL, position, altitudeMode, 4.5e+06, true, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
     _label = label;
     _iconURL = new URL(iconURL);
     _position = new Geodetic3D(position);
     _altitudeMode = altitudeMode;
     _labelBottom = labelBottom;
     _labelFontSize = labelFontSize;
     _labelFontColor = labelFontColor;
     _labelShadowColor = labelShadowColor;
     _labelGapSize = labelGapSize;
     _textureId = null;
     _cartesianPosition = null;
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
     _surfaceElevationProvider = null;
     _currentSurfaceElevation = 0.0;
     _glState = null;
     _normalAtMarkPosition = null;
  
  }

  /**
   * Creates a marker just with label, without icon
   */
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
     this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, autoDeleteUserData, listener, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData, boolean autoDeleteUserData)
  {
     this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, autoDeleteUserData, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData)
  {
     this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, true, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor)
  {
     this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor)
  {
     this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, labelFontColor, Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize)
  {
     this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera)
  {
     this(label, position, altitudeMode, minDistanceToCamera, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode)
  {
     this(label, position, altitudeMode, 4.5e+06, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
     _label = label;
     _labelBottom = true;
     _iconURL = new URL("", false);
     _position = new Geodetic3D(position);
     _altitudeMode = altitudeMode;
     _labelFontSize = labelFontSize;
     _labelFontColor = labelFontColor;
     _labelShadowColor = labelShadowColor;
     _labelGapSize = 2;
     _textureId = null;
     _cartesianPosition = null;
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
     _surfaceElevationProvider = null;
     _currentSurfaceElevation = 0.0;
     _glState = null;
     _normalAtMarkPosition = null;
  
  }

  /**
   * Creates a marker just with icon, without label
   */
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
     this(iconURL, position, altitudeMode, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
  }
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData)
  {
     this(iconURL, position, altitudeMode, minDistanceToCamera, userData, autoDeleteUserData, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData)
  {
     this(iconURL, position, altitudeMode, minDistanceToCamera, userData, true, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera)
  {
     this(iconURL, position, altitudeMode, minDistanceToCamera, null, true, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode)
  {
     this(iconURL, position, altitudeMode, 4.5e+06, null, true, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
     _label = "";
     _labelBottom = true;
     _iconURL = new URL(iconURL);
     _position = new Geodetic3D(position);
     _altitudeMode = altitudeMode;
     _labelFontSize = 20F;
     _labelFontColor = Color.newFromRGBA(1, 1, 1, 1);
     _labelShadowColor = Color.newFromRGBA(0, 0, 0, 1);
     _labelGapSize = 2;
     _textureId = null;
     _cartesianPosition = null;
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
     _surfaceElevationProvider = null;
     _currentSurfaceElevation = 0.0;
     _glState = null;
     _normalAtMarkPosition = null;
  
  }

  /**
   * Creates a marker whith a given pre-renderer IImage
   */
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
     this(image, imageID, position, altitudeMode, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData)
  {
     this(image, imageID, position, altitudeMode, minDistanceToCamera, userData, autoDeleteUserData, null, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData)
  {
     this(image, imageID, position, altitudeMode, minDistanceToCamera, userData, true, null, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera)
  {
     this(image, imageID, position, altitudeMode, minDistanceToCamera, null, true, null, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode)
  {
     this(image, imageID, position, altitudeMode, 4.5e+06, null, true, null, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
     _label = "";
     _labelBottom = true;
     _iconURL = new URL(new URL("", false));
     _position = new Geodetic3D(position);
     _altitudeMode = altitudeMode;
     _labelFontSize = 20F;
     _labelFontColor = null;
     _labelShadowColor = null;
     _labelGapSize = 2;
     _textureId = null;
     _cartesianPosition = null;
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
     _surfaceElevationProvider = null;
     _currentSurfaceElevation = 0.0;
     _glState = null;
     _normalAtMarkPosition = null;
  
  }

  public void dispose()
  {
  
    if (_position != null)
       _position.dispose();
  
    if (_normalAtMarkPosition != null)
       _normalAtMarkPosition.dispose();
  
    if (_surfaceElevationProvider != null)
    {
      if (!_surfaceElevationProvider.removeListener(this))
      {
        ILogger.instance().logError("Couldn't remove mark as listener of Surface Elevation Provider.");
      }
    }
  
    if (_cartesianPosition != null)
       _cartesianPosition.dispose();
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
  
    if (_glState != null)
    {
      _glState._release();
    }
  
    if (_textureId != null)
    {
      _textureId.dispose();
      _textureId = null; //Releasing texture
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
  
    _surfaceElevationProvider = context.getSurfaceElevationProvider();
    if (_surfaceElevationProvider != null)
    {
      _surfaceElevationProvider.addListener(_position._latitude, _position._longitude, this);
    }
  
    if (!_textureSolved)
    {
      final boolean hasIconURL = (_iconURL.getPath().length() != 0);
      if (hasIconURL)
      {
        IDownloader downloader = context.getDownloader();
  
        downloader.requestImage(_iconURL, downloadPriority, TimeInterval.fromDays(30), true, new IconDownloadListener(this, _label, _labelBottom, _labelFontSize, _labelFontColor, _labelShadowColor, _labelGapSize), true);
      }
      else
      {
        final boolean hasLabel = (_label.length() != 0);
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
  
    _textureImage = image;
    _textureWidth = _textureImage.getWidth();
    _textureHeight = _textureImage.getHeight();
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
  }

  public final void setMinDistanceToCamera(double minDistanceToCamera)
  {
    _minDistanceToCamera = minDistanceToCamera;
  }
  public final double getMinDistanceToCamera()
  {
    return _minDistanceToCamera;
  }

  public final Vector3D getCartesianPosition(Planet planet)
  {
    if (_cartesianPosition == null)
    {
  
      double altitude = _position._height;
      if (_altitudeMode == AltitudeMode.RELATIVE_TO_GROUND)
      {
        altitude += _currentSurfaceElevation;
      }
  
      Geodetic3D positionWithSurfaceElevation = new Geodetic3D(_position._latitude, _position._longitude, altitude);
  
      _cartesianPosition = new Vector3D(planet.toCartesian(positionWithSurfaceElevation));
    }
    return _cartesianPosition;
  }

  public final void render(G3MRenderContext rc, Vector3D cameraPosition, double cameraHeight, GLState parentGLState, Planet planet, GL gl)
  {
  
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
      boolean occludedByHorizon = false;
  
      if (_position._height > cameraHeight)
      {
        //Computing horizon culling
        final java.util.ArrayList<Double> dists = planet.intersectionsDistances(cameraPosition, markCameraVector);
        if (dists.size() > 0)
        {
          final double dist = dists.get(0);
          if (dist > 0.0 && dist < 1.0)
          {
            occludedByHorizon = true;
          }
        }
  
      }
      else
      {
        //if camera position is upper than mark we can compute horizon culling in a much simpler way
        if (_normalAtMarkPosition == null)
        {
          _normalAtMarkPosition = new Vector3D(planet.geodeticSurfaceNormal(markPosition));
        }
        occludedByHorizon = (_normalAtMarkPosition.angleBetween(markCameraVector)._radians <= DefineConstants.HALF_PI);
      }
  
  
      if (!occludedByHorizon)
      {
  
        if (_textureId == null)
        {
          if (_textureImage != null)
          {
            _textureId = rc.getTexturesHandler().getTextureIDReference(_textureImage, GLFormat.rgba(), _imageID, false);
  
            rc.getFactory().deleteImage(_textureImage);
            _textureImage = null;
            createGLState(planet);
          }
        }
        else
        {
  
          if (_glState == null)
          {
            createGLState(planet); //If GLState was disposed due to elevation change
          }
  
          _glState.setParent(parentGLState); //Linking with parent
  
          rc.getGL().drawArrays(GLPrimitive.triangleStrip(), 0, 4, _glState, rc.getGPUProgramManager());
  
          _renderedMark = true;
        }
      }
    }
  
  }

  public final void elevationChanged(Geodetic2D position, double rawElevation, double verticalExaggeration)
  {
  
    if ((rawElevation != rawElevation))
    {
      _currentSurfaceElevation = 0; //USING 0 WHEN NO ELEVATION DATA
    }
    else
    {
      _currentSurfaceElevation = rawElevation * verticalExaggeration;
    }
  
    if (_cartesianPosition != null)
       _cartesianPosition.dispose();
    _cartesianPosition = null;
  
    if (_glState != null)
    {
      _glState._release();
      _glState = null;
    }
  }

  public final void elevationChanged(Sector position, ElevationData rawElevationData, double verticalExaggeration) //Without considering vertical exaggeration
  {
  }
}