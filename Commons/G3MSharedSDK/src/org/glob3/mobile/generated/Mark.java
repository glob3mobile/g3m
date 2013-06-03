

package org.glob3.mobile.generated;

public class Mark
         extends
            SceneGraphNode {
   /**
    * The text the mark displays. Useless if the mark does not have label.
    */
   private final String            _label;
   /**
    * Flag to know if the label will be located under the icon (if TRUE) or on its right (if FALSE). Useless if the mark does not
    * have label or icon. Default value: TRUE
    */
   private final boolean           _labelBottom;
   /**
    * The font size of the text. Useless if the mark does not have label. Default value: 20
    */
   private final float             _labelFontSize;
   /**
    * The color of the text. Useless if the mark does not have label. Default value: white
    */
   private final Color             _labelFontColor;
   /**
    * The color of the text shadow. Useless if the mark does not have label. Default value: black
    */
   private final Color             _labelShadowColor;
   /**
    * The number of pixels between the icon and the text. Useless if the mark does not have label or icon. Default value: 2
    */
   private final int               _labelGapSize;
   /**
    * The URL to get the image file. Useless if the mark does not have icon.
    */
   private URL                     _iconURL = new URL();
   /**
    * The point where the mark will be geo-located.
    */
   private final Geodetic3D        _position;
   /**
    * The minimun distance (in meters) to show the mark. If the camera is further than this, the mark will not be displayed.
    * Default value: 4.5e+06
    */
   private double                  _minDistanceToCamera;
   /**
    * The extra data to be stored by the mark. Usefull to store data such us name, URL...
    */
   private MarkUserData            _userData;
   /**
    * Flag to know if the mark is the owner of _userData and thus it must delete it on destruction. Default value: TRUE
    */
   private final boolean           _autoDeleteUserData;
   /**
    * Interface for listening to the touch event.
    */
   private final MarkTouchListener _listener;
   /**
    * Flag to know if the mark is the owner of _listener and thus it must delete it on destruction. Default value: FALSE
    */
   private final boolean           _autoDeleteListener;

   private IGLTextureId            _textureId;

   private Vector3D                _cartesianPosition;

   private IFloatBuffer            _vertices;


   private IFloatBuffer getVertices(final Planet planet) {
      if (_vertices == null) {
         final Vector3D pos = getCartesianPosition(planet);

         final FloatBufferBuilderFromCartesian3D vertex = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(),
                  Vector3D.zero());
         vertex.add(pos);
         vertex.add(pos);
         vertex.add(pos);
         vertex.add(pos);

         _vertices = vertex.create();
      }
      return _vertices;
   }

   private boolean               _textureSolved;
   private IImage                _textureImage;
   private int                   _textureWidth;
   private int                   _textureHeight;
   private final String          _imageID;

   private boolean               _renderedMark;

   private final GLGlobalState   _GLGlobalState     = new GLGlobalState();
   private final GPUProgramState _progState         = new GPUProgramState();

   private static IFloatBuffer   _billboardTexCoord = null;
   private Planet                _planet;                                   // REMOVED FINAL WORD BY CONVERSOR RULE
   private int                   _viewportWidth;
   private int                   _viewportHeight;


   /**
    * Creates a marker with icon and label
    */
   public Mark(final String label,
               final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final boolean labelBottom,
               final float labelFontSize,
               final Color labelFontColor,
               final Color labelShadowColor,
               final int labelGapSize,
               final MarkUserData userData,
               final boolean autoDeleteUserData,
               final MarkTouchListener listener) {
      this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor,
           labelGapSize, userData, autoDeleteUserData, listener, false);
   }


   public Mark(final String label,
               final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final boolean labelBottom,
               final float labelFontSize,
               final Color labelFontColor,
               final Color labelShadowColor,
               final int labelGapSize,
               final MarkUserData userData,
               final boolean autoDeleteUserData) {
      this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor,
           labelGapSize, userData, autoDeleteUserData, null, false);
   }


   public Mark(final String label,
               final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final boolean labelBottom,
               final float labelFontSize,
               final Color labelFontColor,
               final Color labelShadowColor,
               final int labelGapSize,
               final MarkUserData userData) {
      this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor,
           labelGapSize, userData, true, null, false);
   }


   public Mark(final String label,
               final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final boolean labelBottom,
               final float labelFontSize,
               final Color labelFontColor,
               final Color labelShadowColor,
               final int labelGapSize) {
      this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor,
           labelGapSize, null, true, null, false);
   }


   public Mark(final String label,
               final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final boolean labelBottom,
               final float labelFontSize,
               final Color labelFontColor,
               final Color labelShadowColor) {
      this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, 2, null,
           true, null, false);
   }


   public Mark(final String label,
               final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final boolean labelBottom,
               final float labelFontSize,
               final Color labelFontColor) {
      this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, Color.newFromRGBA(0, 0, 0,
               1), 2, null, true, null, false);
   }


   public Mark(final String label,
               final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final boolean labelBottom,
               final float labelFontSize) {
      this(label, iconURL, position, minDistanceToCamera, labelBottom, labelFontSize, Color.newFromRGBA(1, 1, 1, 1),
           Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
   }


   public Mark(final String label,
               final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final boolean labelBottom) {
      this(label, iconURL, position, minDistanceToCamera, labelBottom, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0,
               0, 1), 2, null, true, null, false);
   }


   public Mark(final String label,
               final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera) {
      this(label, iconURL, position, minDistanceToCamera, true, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1),
           2, null, true, null, false);
   }


   public Mark(final String label,
               final URL iconURL,
               final Geodetic3D position) {
      this(label, iconURL, position, 4.5e+06, true, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null,
           true, null, false);
   }


   public Mark(final String label,
               final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final boolean labelBottom,
               final float labelFontSize,
               final Color labelFontColor,
               final Color labelShadowColor,
               final int labelGapSize,
               final MarkUserData userData,
               final boolean autoDeleteUserData,
               final MarkTouchListener listener,
               final boolean autoDeleteListener) {
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
      _planet = null;

   }


   /**
    * Creates a marker just with label, without icon
    */
   public Mark(final String label,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final float labelFontSize,
               final Color labelFontColor,
               final Color labelShadowColor,
               final MarkUserData userData,
               final boolean autoDeleteUserData,
               final MarkTouchListener listener) {
      this(label, position, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, autoDeleteUserData,
           listener, false);
   }


   public Mark(final String label,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final float labelFontSize,
               final Color labelFontColor,
               final Color labelShadowColor,
               final MarkUserData userData,
               final boolean autoDeleteUserData) {
      this(label, position, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, autoDeleteUserData,
           null, false);
   }


   public Mark(final String label,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final float labelFontSize,
               final Color labelFontColor,
               final Color labelShadowColor,
               final MarkUserData userData) {
      this(label, position, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, true, null, false);
   }


   public Mark(final String label,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final float labelFontSize,
               final Color labelFontColor,
               final Color labelShadowColor) {
      this(label, position, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, null, true, null, false);
   }


   public Mark(final String label,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final float labelFontSize,
               final Color labelFontColor) {
      this(label, position, minDistanceToCamera, labelFontSize, labelFontColor, Color.newFromRGBA(0, 0, 0, 1), null, true, null,
           false);
   }


   public Mark(final String label,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final float labelFontSize) {
      this(label, position, minDistanceToCamera, labelFontSize, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1),
           null, true, null, false);
   }


   public Mark(final String label,
               final Geodetic3D position,
               final double minDistanceToCamera) {
      this(label, position, minDistanceToCamera, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true,
           null, false);
   }


   public Mark(final String label,
               final Geodetic3D position) {
      this(label, position, 4.5e+06, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
   }


   public Mark(final String label,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final float labelFontSize,
               final Color labelFontColor,
               final Color labelShadowColor,
               final MarkUserData userData,
               final boolean autoDeleteUserData,
               final MarkTouchListener listener,
               final boolean autoDeleteListener) {
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
      _planet = null;

   }


   /**
    * Creates a marker just with icon, without label
    */
   public Mark(final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final MarkUserData userData,
               final boolean autoDeleteUserData,
               final MarkTouchListener listener) {
      this(iconURL, position, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
   }


   public Mark(final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final MarkUserData userData,
               final boolean autoDeleteUserData) {
      this(iconURL, position, minDistanceToCamera, userData, autoDeleteUserData, null, false);
   }


   public Mark(final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final MarkUserData userData) {
      this(iconURL, position, minDistanceToCamera, userData, true, null, false);
   }


   public Mark(final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera) {
      this(iconURL, position, minDistanceToCamera, null, true, null, false);
   }


   public Mark(final URL iconURL,
               final Geodetic3D position) {
      this(iconURL, position, 4.5e+06, null, true, null, false);
   }


   public Mark(final URL iconURL,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final MarkUserData userData,
               final boolean autoDeleteUserData,
               final MarkTouchListener listener,
               final boolean autoDeleteListener) {
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
      _planet = null;

   }


   /**
    * Creates a marker whith a given pre-renderer IImage
    */
   public Mark(final IImage image,
               final String imageID,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final MarkUserData userData,
               final boolean autoDeleteUserData,
               final MarkTouchListener listener) {
      this(image, imageID, position, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
   }


   public Mark(final IImage image,
               final String imageID,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final MarkUserData userData,
               final boolean autoDeleteUserData) {
      this(image, imageID, position, minDistanceToCamera, userData, autoDeleteUserData, null, false);
   }


   public Mark(final IImage image,
               final String imageID,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final MarkUserData userData) {
      this(image, imageID, position, minDistanceToCamera, userData, true, null, false);
   }


   public Mark(final IImage image,
               final String imageID,
               final Geodetic3D position,
               final double minDistanceToCamera) {
      this(image, imageID, position, minDistanceToCamera, null, true, null, false);
   }


   public Mark(final IImage image,
               final String imageID,
               final Geodetic3D position) {
      this(image, imageID, position, 4.5e+06, null, true, null, false);
   }


   public Mark(final IImage image,
               final String imageID,
               final Geodetic3D position,
               final double minDistanceToCamera,
               final MarkUserData userData,
               final boolean autoDeleteUserData,
               final MarkTouchListener listener,
               final boolean autoDeleteListener) {
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
      _planet = null;

   }


   @Override
   public void dispose() {
      if (_cartesianPosition != null) {
         _cartesianPosition.dispose();
      }
      if (_vertices != null) {
         _vertices.dispose();
      }
      if (_autoDeleteListener) {
         if (_listener != null) {
            _listener.dispose();
         }
      }
      if (_autoDeleteUserData) {
         if (_userData != null) {
            _userData.dispose();
         }
      }
      if (_textureImage != null) {
         IFactory.instance().deleteImage(_textureImage);
      }
   }


   public final String getLabel() {
      return _label;
   }


   public final Geodetic3D getPosition() {
      return _position;
   }


   public final void initialize(final G3MContext context,
                                final long downloadPriority) {

      _planet = context.getPlanet();

      if (!_textureSolved) {
         final boolean hasLabel = (_label.length() != 0);
         final boolean hasIconURL = (_iconURL.getPath().length() != 0);

         if (hasIconURL) {
            final IDownloader downloader = context.getDownloader();

            downloader.requestImage(_iconURL, downloadPriority, TimeInterval.fromDays(30), true, new IconDownloadListener(this,
                     _label, _labelBottom, _labelFontSize, _labelFontColor, _labelShadowColor, _labelGapSize), true);
         }
         else {
            if (hasLabel) {
               ITextUtils.instance().createLabelImage(_label, _labelFontSize, _labelFontColor, _labelShadowColor,
                        new MarkLabelImageListener(null, this), true);
            }
            else {
               ILogger.instance().logWarning("Marker created without label nor icon");
            }
         }
      }
   }


   public final void render(final G3MRenderContext rc,
                            final Vector3D cameraPosition) {
      final Planet planet = rc.getPlanet();

      final Vector3D markPosition = getCartesianPosition(planet);

      final Vector3D markCameraVector = markPosition.sub(cameraPosition);


      // mark will be renderered only if is renderable by distance and placed on a visible globe area
      boolean renderableByDistance;
      if (_minDistanceToCamera == 0) {
         renderableByDistance = true;
      }
      else {
         final double squaredDistanceToCamera = markCameraVector.squaredLength();
         renderableByDistance = (squaredDistanceToCamera <= (_minDistanceToCamera * _minDistanceToCamera));
      }

      _renderedMark = false;
      if (renderableByDistance) {
         final Vector3D normalAtMarkPosition = planet.geodeticSurfaceNormal(markPosition);

         if (normalAtMarkPosition.angleBetween(markCameraVector)._radians > IMathUtils.instance().halfPi()) {

            if (_textureId == null) {
               if (_textureImage != null) {
                  _textureId = rc.getTexturesHandler().getGLTextureId(_textureImage, GLFormat.rgba(), _imageID, false);

                  rc.getFactory().deleteImage(_textureImage);
                  _textureImage = null;

                  _viewportWidth = rc.getCurrentCamera().getWidth();
                  _viewportHeight = rc.getCurrentCamera().getHeight();
                  actualizeGLGlobalState(rc.getCurrentCamera()); //Ready for rendering
               }
            }
            else {
               if ((rc.getCurrentCamera().getWidth() != _viewportWidth) || (rc.getCurrentCamera().getHeight() != _viewportHeight)) {
                  _viewportWidth = rc.getCurrentCamera().getWidth();
                  _viewportHeight = rc.getCurrentCamera().getHeight();
                  actualizeGLGlobalState(rc.getCurrentCamera()); //Ready for rendering
               }
            }

            if (_textureId != null) {
               final GL gl = rc.getGL();


               //        GLGlobalState state(parentState);
               //        state.bindTexture(_textureId);

               final GPUProgramManager progManager = rc.getGPUProgramManager();

               gl.drawArrays(GLPrimitive.triangleStrip(), 0, 4, _GLGlobalState, progManager, _progState);

               _renderedMark = true;
            }
         }
      }
   }


   public final boolean isReady() {
      return _textureSolved;
   }


   public final boolean isRendered() {
      return _renderedMark;
   }


   public final void onTextureDownloadError() {
      _textureSolved = true;

      if (_labelFontColor != null) {
         _labelFontColor.dispose();
      }
      if (_labelShadowColor != null) {
         _labelShadowColor.dispose();
      }

      ILogger.instance().logError("Can't create texture for Mark (iconURL=\"%s\", label=\"%s\")", _iconURL.getPath(), _label);
   }


   public final void onTextureDownload(final IImage image) {
      _textureSolved = true;

      if (_labelFontColor != null) {
         _labelFontColor.dispose();
      }
      if (_labelShadowColor != null) {
         _labelShadowColor.dispose();
      }
      //  _textureImage = image->shallowCopy();
      _textureImage = image;
      _textureWidth = _textureImage.getWidth();
      _textureHeight = _textureImage.getHeight();
      //  IFactory::instance()->deleteImage(image);
   }


   public final int getTextureWidth() {
      return _textureWidth;
   }


   public final int getTextureHeight() {
      return _textureHeight;
   }


   public final Vector2I getTextureExtent() {
      return new Vector2I(_textureWidth, _textureHeight);
   }


   public final MarkUserData getUserData() {
      return _userData;
   }


   public final void setUserData(final MarkUserData userData) {
      if (_autoDeleteUserData) {
         if (_userData != null) {
            _userData.dispose();
         }
      }
      _userData = userData;
   }


   public final boolean touched() {
      return (_listener == null) ? false : _listener.touchedMark(this);
      //  if (_listener == NULL) {
      //    return false;
      //  }
      //  return _listener->touchedMark(this);
   }


   public final Vector3D getCartesianPosition(final Planet planet) {
      if (_cartesianPosition == null) {
         _cartesianPosition = new Vector3D(planet.toCartesian(_position));
      }
      return _cartesianPosition;
   }


   public final void setMinDistanceToCamera(final double minDistanceToCamera) {
      _minDistanceToCamera = minDistanceToCamera;
   }


   public final double getMinDistanceToCamera() {
      return _minDistanceToCamera;
   }


   //Drawable client
   @Override
   public final GLGlobalState getGLGlobalState() {
      return _GLGlobalState;
   }


   @Override
   public final GPUProgramState getGPUProgramState() {
      _progState.clear();
      return _progState;
   }


   //  void getGLGlobalStateAndGPUProgramState(GLGlobalState** GLGlobalState, GPUProgramState** progState);

   //void Mark::getGLGlobalStateAndGPUProgramState(GLGlobalState** GLGlobalState, GPUProgramState** progState){
   //  _progState.clear();
   ////  (*GLGlobalState) = &_GLGlobalState;
   ////  (*progState) = &_progState;
   //}

   @Override
   public final void modifyGLGlobalState(final GLGlobalState GLGlobalState) {
      GLGlobalState.disableDepthTest();
      GLGlobalState.enableBlend();
      GLGlobalState.setBlendFactors(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
      GLGlobalState.bindTexture(_textureId);
   }


   @Override
   public final void modifyGPUProgramState(final GPUProgramState progState) {
      if (_planet == null) {
         ILogger.instance().logError("Planet NULL");
      }
      else {

         progState.setAttributeEnabled("Position", true);
         progState.setAttributeEnabled("TextureCoord", true);

         if (_billboardTexCoord == null) {
            final FloatBufferBuilderFromCartesian2D texCoor = new FloatBufferBuilderFromCartesian2D();
            texCoor.add(1, 1);
            texCoor.add(1, 0);
            texCoor.add(0, 1);
            texCoor.add(0, 0);
            _billboardTexCoord = texCoor.create();
         }

         progState.setAttributeValue("TextureCoord", _billboardTexCoord, 2, 2, 0, false, 0);

         final Vector3D pos = new Vector3D(_planet.toCartesian(_position));
         final FloatBufferBuilderFromCartesian3D vertex = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(),
                  Vector3D.zero());
         vertex.add(pos);
         vertex.add(pos);
         vertex.add(pos);
         vertex.add(pos);

         final IFloatBuffer vertices = vertex.create();

         progState.setAttributeValue("Position", vertices, 4, 3, 0, false, 0); //Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements

         progState.setUniformValue("TextureExtent", new Vector2D(_textureWidth, _textureHeight));
         progState.setUniformValue("ViewPortExtent", new Vector2D(_viewportWidth, _viewportHeight));
      }
   }


   //Scene Graph Node
   @Override
   public final void rawRender(final G3MRenderContext rc,
                               final GLStateTreeNode myStateTreeNode) {
      //  printf("RENDERING SGMARK\n");

      if (_textureId == null) {
         if (_textureImage != null) {
            _textureId = rc.getTexturesHandler().getGLTextureId(_textureImage, GLFormat.rgba(), _imageID, false);

            rc.getFactory().deleteImage(_textureImage);
            _textureImage = null;

            _viewportWidth = rc.getCurrentCamera().getWidth();
            _viewportHeight = rc.getCurrentCamera().getHeight();
            //      actualizeGLGlobalState(rc->getCurrentCamera()); //Ready for rendering
         }
      }

      if ((rc.getCurrentCamera().getWidth() != _viewportWidth) || (rc.getCurrentCamera().getHeight() != _viewportHeight)) {
         _viewportWidth = rc.getCurrentCamera().getWidth();
         _viewportHeight = rc.getCurrentCamera().getHeight();
         //actualizeGLGlobalState(rc->getCurrentCamera()); //Ready for rendering
      }

      _viewportWidth = 1280;
      _viewportHeight = 707;

      if (_textureId != null) {
         _planet = rc.getPlanet();

         final GL gl = rc.getGL();

         final GPUProgramManager progManager = rc.getGPUProgramManager();

         final GLState glState = myStateTreeNode.getGLState();

         gl.drawArrays(GLPrimitive.triangleStrip(), 0, 4, glState, progManager);
      }

   }


   @Override
   public final boolean isInsideCameraFrustum(final G3MRenderContext rc) {
      _renderedMark = false;

      final Planet planet = rc.getPlanet();

      final Vector3D markPosition = getCartesianPosition(planet);

      final Vector3D markCameraVector = markPosition.sub(rc.getCurrentCamera().getCartesianPosition());

      // mark will be renderered only if is renderable by distance and placed on a visible globe area
      boolean renderableByDistance;
      if (_minDistanceToCamera == 0) {
         renderableByDistance = true;
      }
      else {
         final double squaredDistanceToCamera = markCameraVector.squaredLength();
         renderableByDistance = (squaredDistanceToCamera <= (_minDistanceToCamera * _minDistanceToCamera));
      }

      _renderedMark = false;
      if (renderableByDistance) {
         final Vector3D normalAtMarkPosition = planet.geodeticSurfaceNormal(markPosition);
         if (normalAtMarkPosition.angleBetween(markCameraVector)._radians > IMathUtils.instance().halfPi()) {
            return true;
         }
      }

      //Checking with frustum

      _renderedMark = rc.getCurrentCamera().getFrustumInModelCoordinates().contains(_cartesianPosition);
      return _renderedMark;
   }


   @Override
   public final void modifiyGLState(final GLState state) {

      final GLGlobalState globalState = state.getGLGlobalState();

      globalState.disableDepthTest();
      globalState.enableBlend();
      globalState.setBlendFactors(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
      globalState.bindTexture(_textureId);

      final GPUProgramState progState = state.getGPUProgramState();

      if (_planet == null) {
         ILogger.instance().logError("Planet NULL");
      }
      else {

         progState.setAttributeEnabled("Position", true);
         progState.setAttributeEnabled("TextureCoord", true);

         if (_billboardTexCoord == null) {
            final FloatBufferBuilderFromCartesian2D texCoor = new FloatBufferBuilderFromCartesian2D();
            texCoor.add(1, 1);
            texCoor.add(1, 0);
            texCoor.add(0, 1);
            texCoor.add(0, 0);
            _billboardTexCoord = texCoor.create();
         }

         progState.setAttributeValue("TextureCoord", _billboardTexCoord, 2, 2, 0, false, 0);

         final Vector3D pos = new Vector3D(_planet.toCartesian(_position));
         final FloatBufferBuilderFromCartesian3D vertex = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(),
                  Vector3D.zero());
         vertex.add(pos);
         vertex.add(pos);
         vertex.add(pos);
         vertex.add(pos);

         final IFloatBuffer vertices = vertex.create();

         progState.setAttributeValue("Position", vertices, 4, 3, 0, false, 0); //Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements

         progState.setUniformValue("TextureExtent", new Vector2D(_textureWidth, _textureHeight));
         progState.setUniformValue("ViewPortExtent", new Vector2D(_viewportWidth, _viewportHeight));
      }

   }


   @Override
   public final void onInitialize(final G3MContext context) {

      _planet = context.getPlanet();

      if (!_textureSolved) {
         final boolean hasLabel = (_label.length() != 0);
         final boolean hasIconURL = (_iconURL.getPath().length() != 0);

         if (hasIconURL) {
            final IDownloader downloader = context.getDownloader();

            final int downloadPriority = 100;

            downloader.requestImage(_iconURL, downloadPriority, TimeInterval.fromDays(30), true, new IconDownloadListener(this,
                     _label, _labelBottom, _labelFontSize, _labelFontColor, _labelShadowColor, _labelGapSize), true);
         }
         else {
            if (hasLabel) {
               ITextUtils.instance().createLabelImage(_label, _labelFontSize, _labelFontColor, _labelShadowColor,
                        new MarkLabelImageListener(null, this), true);
            }
            else {
               ILogger.instance().logWarning("Marker created without label nor icon");
            }
         }
      }
   }

}
