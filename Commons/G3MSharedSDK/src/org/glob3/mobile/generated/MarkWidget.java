package org.glob3.mobile.generated; 
public class MarkWidget
{
    private GLState _glState;
    private Geometry2DGLFeature _geo2Dfeature;
    private ViewportExtentGLFeature _viewportExtent;
    private IImage _image;
    private String _imageName;
    private IImageBuilder _imageBuilder;
    private TexturesHandler _texHandler;
  //  const Planet* _planet;

    private float _halfWidth;
    private float _halfHeight;

    //float _x, _y; //Screen position

    private MarkWidgetTouchListener _touchListener;

    private static class WidgetImageListener implements IImageBuilderListener
    {
        private MarkWidget _widget;
        public WidgetImageListener(MarkWidget widget)
        {
           this(widget, null);
        }
        public WidgetImageListener(MarkWidget widget, MarkWidgetTouchListener touchListener)
        {
           _widget = widget;
        }

        public void dispose()
        {
        }

        public void imageCreated(IImage image, String imageName)
        {
            _widget.prepareWidget(image, imageName);
        }

        public void onError(String error)
        {
            ILogger.instance().logError(error);
        }

    }

    private void prepareWidget(IImage image, String imageName)
    {
        _image = image;
        _imageName = imageName;
    
        _halfWidth = image.getWidth() / 2;
        _halfHeight = image.getHeight() / 2;
    
         GlobalMembersNonOverlapping3DMarksRenderer._shapesRenderer = new ShapesRenderer();
    
        FloatBufferBuilderFromCartesian2D pos2D = new FloatBufferBuilderFromCartesian2D();
        pos2D.add(-_halfWidth, -_halfHeight); //vertex 1
        pos2D.add(-_halfWidth, _halfHeight); //vertex 2
        pos2D.add(_halfWidth, -_halfHeight); //vertex 3
        pos2D.add(_halfWidth, _halfHeight); //vertex 4
    
    
        //const int wheelSize = 7;
       // int _colorIndex = (_colorIndex + 1) % wheelSize;
    
       GlobalMembersNonOverlapping3DMarksRenderer._geo3Dfeature = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(0), Angle.fromDegrees(0), 5), AltitudeMode.ABSOLUTE, new Vector3D(100, 100, 100), 16, 0, false, false, Color.fromRGBA(1, 0, 0, .5));
        GlobalMembersNonOverlapping3DMarksRenderer._geo3Dfeature.setScale(1000);
    
         GlobalMembersNonOverlapping3DMarksRenderer._shapesRenderer.addShape(GlobalMembersNonOverlapping3DMarksRenderer._geo3Dfeature);
    
    
       /* _geo2Dfeature = new Geometry2DGLFeature(pos2D.create(),
                                                2,
                                                0,
                                                true,
                                                0,
                                                1.0f,
                                                true,
                                                10.0f,
                                                Vector2F(_x, _y));*/
    
    
    
       /* _glState->addGLFeature(_geo2Dfeature,
                               false);*/
    
       /* FloatBufferBuilderFromCartesian2D texCoords;
        texCoords.add( 0.0f, 1.0f); //vertex 1
        texCoords.add( 0.0f, 0.0f); //vertex 2
        texCoords.add( 1.0f, 1.0f); //vertex 3
        texCoords.add( 1.0f, 0.0f); //vertex 4
       
        const TextureIDReference* textureID = _texHandler->getTextureIDReference(_image,
                                                                                 GLFormat::rgba(),
                                                                                 _imageName,
                                                                                 false);
       
        SimpleTextureMapping* textureMapping = new SimpleTextureMapping(textureID,
                                                                        texCoords.create(),
                                                                        true,
                                                                        true);
    
    
       
        textureMapping->modifyGLState(*_glState);*/
    }

    public MarkWidget(IImageBuilder imageBuilder)
    /*_x(NANF),
    _y(NANF),
    _z(NANF),*/
    {
       _image = null;
       _imageBuilder = imageBuilder;
       _viewportExtent = null;
       _geo2Dfeature = null;
       _glState = null;
       _halfHeight = 0F;
       _halfWidth = 0F;
       _touchListener = null;
    
    }

    public void dispose()
    {
        _image = null;
        if (_imageBuilder != null)
        {
           if (_imageBuilder != null)
              _imageBuilder.dispose();
        }
        if (_touchListener != null)
           _touchListener.dispose();
    
        _glState._release();
    }

    //const Planet* getPlanet();

    public final void init(G3MRenderContext rc, int viewportWidth, int viewportHeight)
    {
        if (_glState == null)
        {
            _glState = new GLState();
            _viewportExtent = new ViewportExtentGLFeature(viewportWidth, viewportHeight);
    
            _texHandler = rc.getTexturesHandler();
            _imageBuilder.build(rc, new WidgetImageListener(this), true);
    
            _glState.addGLFeature(_viewportExtent, false);
            GlobalMembersNonOverlapping3DMarksRenderer._radius = 100000F;
           // _planet = rc->getPlanet();
    
        }
    }

    public final void render(G3MRenderContext rc, GLState glState)
    {
      // rc->getGL()->drawArrays(GLPrimitive::triangleStrip(), 0, 4, _glState, *rc->getGPUProgramManager());
        GlobalMembersNonOverlapping3DMarksRenderer._shapesRenderer.render(rc, glState);
    }

    //void setScreenPos(float x, float y, float z);
    public final void set3DPos(float x, float y, float z)
    {
        if(GlobalMembersNonOverlapping3DMarksRenderer._geo3Dfeature != null)
        {
         //_geo3Dfeature->setPosition(Geodetic3D::fromDegrees(x, y, 3));
         GlobalMembersNonOverlapping3DMarksRenderer._geo3Dfeature.setTranslation(x, y, z);
        }
        /*_x = x;
        _y = y;
        _z = z;*/
    }
    //void setScreenPos3d(Geodetic3D &geo3d);

   // Vector2F getScreenPos() const{ return Vector2F(_x, _y);}
    /*
     
     void MarkWidget::setScreenPos(float x, float y){
     if (_geo2Dfeature != NULL){
     _geo2Dfeature->setTranslation(x, y);
     }
     if(_geo3Dfeature != NULL) {
     //_geo3Dfeature->setPosition(Geodetic3D::fromDegrees(x, y, 3));
     _geo3Dfeature->setTranslation(x, y, 3);
     }
     _x = x;
     _y = y;
     }
     /*
     */
    /*
    void MarkWidget::setScreenPos3d(Geodetic3D &geo3d){
        if (_geo2Dfeature != NULL){
            //_geo2Dfeature->setTranslation(x, y);
        }
        if(_geo3Dfeature != NULL) {
            _geo3Dfeature->setPosition(geo3d);
        }
        //_x = x;
        //_y = y;
    }*/
    
    public final void resetPosition()
    {
        if (_geo2Dfeature != null)
        {
            _geo2Dfeature.setTranslation(0, 0);
        }
        if(GlobalMembersNonOverlapping3DMarksRenderer._geo3Dfeature != null)
        {
            GlobalMembersNonOverlapping3DMarksRenderer._geo3Dfeature.setPosition(Geodetic3D.fromDegrees(0, 0, 3));
        }
        /*_x = NANF;
        _y = NANF;*/
    }

    public final float getHalfWidth()
    {
       return _halfWidth;
    }
    public final float getHalfHeight()
    {
       return _halfHeight;
    }

    public final void onResizeViewportEvent(int width, int height)
    {
        if (_viewportExtent != null)
        {
            _viewportExtent.changeExtent(width, height);
        }
        GlobalMembersNonOverlapping3DMarksRenderer._radius = width;
    }

    public final boolean isReady()
    {
        return _image != null;
    }


    /*const Planet* MarkWidget::getPlanet() {
        return _planet;
    }*/
    
    
    public final void clampPositionInsideScreen(int viewportWidth, int viewportHeight, int margin)
    {
       /* const IMathUtils* mu = IMathUtils::instance();
        float x = mu->clamp(_x, _halfWidth + margin, viewportWidth - _halfWidth - margin);
        float y = mu->clamp(_y, _halfHeight + margin, viewportHeight - _halfHeight - margin);
       
        setScreenPos(x, y);*/
        //TODO
    }

    public final boolean onTouchEvent(float x, float y)
    {
    
        //TODO
        /*const IMathUtils* mu = IMathUtils::instance();
        //float _x = pointtoPixel(_geo3Dfeature->getPosition())
        if (mu->isBetween(x, _x - _halfWidth, _x + _halfWidth) &&
            mu->isBetween(y, _y - _halfHeight, _y + _halfHeight)){
            if (_touchListener != NULL){
                _touchListener->touchedMark(this, x, y);
            }
            return true;
            //todo: does zoom resize stuff go here?
        }
        return false;*/
    }

    public final void setTouchListener(MarkWidgetTouchListener tl)
    {
        if (_touchListener != null && _touchListener != tl)
        {
            if (_touchListener != null)
               _touchListener.dispose();
        }
        _touchListener = tl;
    }

}