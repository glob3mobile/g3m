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

  public final void visitTile(java.util.ArrayList<Layer> layers, Tile tile)
  {


    for (int i = 0; i < layers.size(); i++)
    {
      java.util.ArrayList<Petition> pets = layers.get(i).createTileMapPetitions(_renderContext, _ltrp, tile);
      for (int j = 0; j < pets.size(); j++)
      {
        _urls.addLast(pets.get(j).getURL().getPath());
      }
    }

  }

}