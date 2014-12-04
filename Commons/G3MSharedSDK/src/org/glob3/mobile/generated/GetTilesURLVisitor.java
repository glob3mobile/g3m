package org.glob3.mobile.generated; 
public class GetTilesURLVisitor implements ITileVisitor
{
  private final G3MRenderContext _renderContext;

  private final LayerTilesRenderParameters _ltrp;



  public java.util.LinkedList<String> _urls = new java.util.LinkedList<String>();

  public GetTilesURLVisitor(G3MRenderContext renderContext, LayerTilesRenderParameters ltrp)
  {
     _renderContext = renderContext;
     _ltrp = ltrp;
  }

  public void dispose()
  {
  }

  public final void visitTile(java.util.ArrayList<Layer> layers, Tile tile)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning NEEDS_REDO
//
//    for (int i = 0; i < layers.size(); i++) {
//
//
//      TILES_VISITED[tile->_level]++;
//
//      std::vector<Petition*> pets = layers[i]->createTileMapPetitions(_renderContext, _ltrp, tile);
//      for (int j = 0; j < pets.size(); j++) {
//        _urls.push_back( pets[j]->getURL().getPath() );
//      }
//    }

  }

}