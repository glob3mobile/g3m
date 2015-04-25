package org.glob3.mobile.generated; 
public class ForceGraphRenderer extends DefaultRenderer
{

    private int _maxVisibleMarks;
    Planet _planet;

    private java.util.ArrayList<ForceGraphNode> _visibleMarks = new java.util.ArrayList<ForceGraphNode>();
    private java.util.ArrayList<ForceGraphNode> _marks = new java.util.ArrayList<ForceGraphNode>();
    private java.util.ArrayList<ForceGraphNode> _anchors = new java.util.ArrayList<ForceGraphNode>();
    private ShapesRenderer _shapesRenderer;
    private boolean _firstIteration;

    private void computeMarksToBeRendered(Camera cam, Planet planet)
    {
        _visibleMarks.clear();
    
        final Frustum frustrum = cam.getFrustumInModelCoordinates();
    
        for (int i = 0; i < _marks.size(); i++)
        {
            ForceGraphNode m = _marks.get(i);
    
            if (_visibleMarks.size() < _maxVisibleMarks && frustrum.contains(m.getCartesianPosition(planet)))
            {
                _visibleMarks.add(m);
            }
            else
            {
                //Resetting marks location of invisible anchors
                m.resetShapePositionVelocityAndForce();
            }
        }
    }

    private long _lastPositionsUpdatedTime;

    private GLState _connectorsGLState;
    private void renderConnectorLines(G3MRenderContext rc)
    {
        //all done in renderMarks
    }

    private void computeForces(Camera cam, Planet planet)
    {
    
    
        int iters = 1;
        //if(_firstIteration) iters = 10; //more iterations to start to get closer looking image
        for(int k = 0; k < iters; k++)
        {
            for (int i = 0; i < _visibleMarks.size(); i++)
            {
                ForceGraphNode mark = _visibleMarks.get(i);
                if(!mark.isAnchor())
                {
                    mark.applyHookesLaw(planet);
    
                    for (int j = 0; j < _visibleMarks.size(); j++)
                    {
                        if(i == j)
                           continue;
                        /*   if(_firstIteration) {
                         mark->applyCoulombsLaw(_visibleMarks[j], planet, false);
                         }
                         else {*/
                        //do planet adjustment only after getting nodes in somewhat right place (so after first iteration)
                        mark.applyCoulombsLaw(_visibleMarks.get(j), planet, true);
                        // }
                    }
                }
            }
        }
        _firstIteration = false;
    }
    private void renderMarks(G3MRenderContext rc, GLState glState)
    {
    
        renderConnectorLines(rc);
    
        //Draw Anchors and Marks
        for (int i = 0; i < _visibleMarks.size(); i++)
        {
            _visibleMarks.get(i).getCartesianPosition(rc.getPlanet()); //updates shapes positions
            //_visibleMarks[i]->render(rc, glState);
        }
        _shapesRenderer.render(rc, glState);
    }
    private void applyForces(long now, Camera cam)
    {
    
        if (_lastPositionsUpdatedTime != 0) //If not First frame
        {
    
            //Update Position based on last Forces
            for (int i = 0; i < _visibleMarks.size(); i++)
            {
                _visibleMarks.get(i).updatePositionWithCurrentForce(now - _lastPositionsUpdatedTime, cam.getViewPortWidth(), cam.getViewPortHeight(), _planet);
            }
        }
    
        _lastPositionsUpdatedTime = now;
    }



    public ForceGraphRenderer(ShapesRenderer shapesRenderer)
    {
       this(shapesRenderer, 30);
    }
    public ForceGraphRenderer(ShapesRenderer shapesRenderer, int maxVisibleMarks)
    {
       _maxVisibleMarks = maxVisibleMarks;
       _lastPositionsUpdatedTime = 0;
       _connectorsGLState = null;
       _shapesRenderer = shapesRenderer;
       _firstIteration = true;
    }

    public void dispose()
    {
        _connectorsGLState._release();
    
        for (int i = 0; i < _marks.size(); i++)
        {
            if (_marks.get(i) != null)
               _marks.get(i).dispose();
        }
    }

    public final void addMark(ForceGraphNode mark)
    {
        _marks.add(mark);
        if(mark.isAnchor())
        {
            _anchors.add(mark);
        }
        else if (mark.getAnchor() == null)
        {
            //place randomly on the globe if it has no anchor
            Geodetic3D p = mark.getPos();
            double r1 = IMathUtils.instance().nextRandomDouble();
            double r2 = IMathUtils.instance().nextRandomDouble();
             float randlat = -90 + r1 *90;
            float randlon = -180 + r2 *180;
            mark.setPos(new Geodetic3D(Angle.fromDegrees(randlat), Angle.fromDegrees(randlon), 1e5));
            Geodetic3D pafter = mark.getPos();
    
        }
        else
        {
            //place at a random position close to it's anchor
            double r1 = IMathUtils.instance().nextRandomDouble();
            double r2 = IMathUtils.instance().nextRandomDouble();
            Geodetic3D small = Geodetic3D.fromDegrees(10 *r1, 10 *r2, 1);
            mark.setPos(mark.getAnchor().getPos().add(small));
        }
    
        _shapesRenderer.addShape(mark.getShape());
    }

    public final void setAllVisibleAsUnvisited()
    {
        for(int i = 0; i < _visibleMarks.size(); i++)
        {
            _visibleMarks.get(i).setVisited(false);
        }
    }

    public RenderState getRenderState(G3MRenderContext rc)
    {
        return RenderState.ready();
    }

    public void render(G3MRenderContext rc, GLState glState)
    {
    
        final Camera cam = rc.getCurrentCamera();
    
        float altitude = cam.getGeodeticPosition()._height;
    
        _planet = rc.getPlanet();
    
        //  ShapesRenderer sr = ShapesRenderer();
        MeshRenderer _meshrender = new MeshRenderer();
    
    
        float max_dist;
        for(int i = 0; i < _visibleMarks.size(); i++)
        {
            float s = fmax(.1 + altitude/20000000, 0);
            _visibleMarks.get(i).setScale(s);
            for(int j = 0; j < _visibleMarks.get(i).getNeighbors().size(); j++)
            {
                ForceGraphNode neighbor = _visibleMarks.get(i).getNeighbors().get(j);
                Vector3D p1 = _visibleMarks.get(i).getCartesianPosition(_planet);
                Vector3D p2 = neighbor.getCartesianPosition(_planet);
                Vector3D mid = (p2.add(p1)).times(0.5);
                Vector3D mid2 = new Vector3D(mid._x, mid._y, mid._z);
    
                Geodetic3D p1g = new Geodetic3D(_planet.toGeodetic3D(p1));
                Geodetic3D p2g = new Geodetic3D(_planet.toGeodetic3D(p2));
                Geodetic3D midg = p1g.add(p2g).div(2.0f);
    
                Geodetic3D position = new Geodetic3D(_planet.toGeodetic3D(mid2)); //midpoint
                //Geodetic3D *position = new Geodetic3D(midg);
    
                Vector3D extent = new Vector3D(10000, 1000, p1.distanceTo(p2));
                //todo: rotation, mark as visited, don't allocate memory
                float borderWidth = 2F;
                Color col = Color.fromRGBA((float).5, (float) 1, (float) 1, (float) 1);
    
                // create vertices
                FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
                vertices.add(p1);
                vertices.add(p2);
                ShortBufferBuilder indices = new ShortBufferBuilder();
                indices.add((short) 0);
                indices.add((short) 1);
    
    
                Mesh mesh = new IndexedMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, new Color(col));
                _meshrender.addMesh(mesh);
    
    
            }
        }
    
        // sr.render(rc, glState);
        _meshrender.render(rc, glState);
        // sr.removeAllShapes();
        //TODO: get rid of this stuff
        /*for(int i = 0; i < _visibleMarks.size(); i++) {
         _visibleMarks[i]->getShape()->setPosition(Geodetic3D::fromDegrees(0, 0, 1));
         _visibleMarks[i]->getShape()->setTranslation(_visibleMarks[i]->getCartesianPosition(_planet));
         }*/
    
        //todo: add this back
    
        //computeMarksToBeRendered(cam, _planet);
        _visibleMarks = _marks; //temporary
    
        computeForces(cam, _planet);
    
        applyForces(rc.getFrameStartTimer().nowInMilliseconds(), cam);
    
        renderMarks(rc, glState);
    }

    public boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
    {
        if (touchEvent.getTapCount() == 1)
        {
            final float x = touchEvent.getTouch(0).getPos()._x;
            final float y = touchEvent.getTouch(0).getPos()._y;
            for (int i = 0; i < _visibleMarks.size(); i++)
            {
                if (_visibleMarks.get(i).onTouchEvent(x, y))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void onResizeViewportEvent(G3MEventContext ec, int width, int height)
    {
        for (int i = 0; i < _marks.size(); i++)
        {
            _marks.get(i).onResizeViewportEvent(width, height);
        }
    
    }

    public void start(G3MRenderContext rc)
    {

    }

    public void stop(G3MRenderContext rc)
    {

    }

    public SurfaceElevationProvider getSurfaceElevationProvider()
    {
        return null;
    }

    public PlanetRenderer getPlanetRenderer()
    {
        return null;
    }

    public boolean isPlanetRenderer()
    {
        return false;
    }

}