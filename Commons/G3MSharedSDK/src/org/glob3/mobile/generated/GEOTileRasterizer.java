package org.glob3.mobile.generated; 
//
//  GEOTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

//
//  GEOTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//



//class GEORasterSymbol;


public class GEOTileRasterizer extends CanvasTileRasterizer
{
  private QuadTree _quadTree = new QuadTree();
  private G3MContext _context;

  public GEOTileRasterizer()
  {
     _context = null;
  }

  public final String getId()
  {
    return "GEOTileRasterizer";
  }


  ///#include "IThreadUtils.hpp"
  ///#include "IFactory.hpp"
  
  public final void initialize(G3MContext context)
  {
    _context = context;
  }


  //class GEOTileRasterizer_ASync : public GAsyncTask {
  //private:
  //  const IImage*   _image;
  //  const Tile*     _tile;
  //  const bool      _mercator;
  //  IImageListener* _listener;
  //  bool            _autodelete;
  //  const QuadTree* _quadTree;
  //  
  //public:
  //  GEOTileRasterizer_ASync(const IImage*   image,
  //                          const Tile*     tile,
  //                          const bool      mercator,
  //                          IImageListener* listener,
  //                          bool            autodelete,
  //                          const QuadTree* quadTree) :
  //  _image(image),
  //  _tile(tile),
  //  _mercator(mercator),
  //  _listener(listener),
  //  _quadTree(quadTree)
  //  {
  //
  //  }
  //
  //  void runInBackground(const G3MContext* context) {
  //    const int width  = _image->getWidth();
  //    const int height = _image->getHeight();
  //
  //    GEORasterProjection* projection = new GEORasterProjection(_tile->getSector(), _mercator,
  //                                                              width, height);
  //
  //    ICanvas* canvas = IFactory::instance()->createCanvas();
  //    canvas->initialize(width, height);
  //
  //    canvas->drawImage(_image, 0, 0);
  //
  //    _quadTree->acceptVisitor(_tile->getSector(),
  //                             GEOTileRasterizer_QuadTreeVisitor(canvas, projection, _tile->_level));
  //
  //    canvas->createImage(_listener, _autodelete);
  //
  //    delete _image;
  //    _image = NULL;
  //    
  //    delete projection;
  //
  //    delete canvas;
  //  }
  //
  //  void onPostExecute(const G3MContext* context) {
  //
  //  }
  //
  //};
  
  public final void rawRasterize(IImage image, TileRasterizerContext trc, IImageListener listener, boolean autodelete)
  {
  
  //  _context->getThreadUtils()->invokeAsyncTask(new GEOTileRasterizer_ASync(image,
  //                                                                          trc._tile,
  //                                                                          trc._mercator,
  //                                                                          listener,
  //                                                                          autodelete,
  //                                                                          &_quadTree),
  //                                              true);
  
    final Tile tile = trc._tile;
    final boolean mercator = trc._mercator;
  
    final int width = image.getWidth();
    final int height = image.getHeight();
  
    GEORasterProjection projection = new GEORasterProjection(tile._sector, mercator, width, height);
  
    ICanvas canvas = getCanvas(width, height);
  
    canvas.drawImage(image, 0, 0);
  
    _quadTree.acceptVisitor(tile._sector, new GEOTileRasterizer_QuadTreeVisitor(canvas, projection, tile._level));
  
    canvas.createImage(listener, autodelete);
  
    if (image != null)
       image.dispose();
  
    if (projection != null)
       projection.dispose();
  }

  public final void addSymbol(GEORasterSymbol symbol)
  {
    final Sector sector = symbol.getSector();
  
    if (sector == null)
    {
      //    ILogger::instance()->logError("Symbol %s has not sector, can't symbolize",
      //                                  symbol->description().c_str());
      if (symbol != null)
         symbol.dispose();
    }
    else
    {
      final boolean added = _quadTree.add(sector, symbol);
      if (added)
      {
        notifyChanges();
      }
      else
      {
        if (symbol != null)
           symbol.dispose();
      }
    }
  }

}