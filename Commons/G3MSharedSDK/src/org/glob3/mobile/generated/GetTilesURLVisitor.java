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


    for (int i = 0; i < layers.size(); i++)
    {


      GlobalMembersPlanetRenderer.TILES_VISITED[tile._level]++;

      java.util.ArrayList<Petition> pets = layers.get(i).createTileMapPetitions(_renderContext, _ltrp, tile);
      for (int j = 0; j < pets.size(); j++)
      {
        _urls.addLast(pets.get(j).getURL().getPath());
      }
    }

  }

}
//std::vector<std::string> PlanetRenderer::getInfo() {
//  _info.clear();
//  std::vector<std::string> info = _layerSet->getInfo();
//  
///#ifdef C_CODE
//      _info.insert(_info.end(),info.begin(), info.end());
///#endif
///#ifdef JAVA_CODE
//      _infos.add(info);
///#endif
//  
//  return _info;
//}

