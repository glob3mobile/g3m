package org.glob3.mobile.generated; 
public class RecreateTilesTask extends GTask
{
  private PlanetRenderer _planetRenderer;
  public RecreateTilesTask(PlanetRenderer planetRenderer)
  {
     _planetRenderer = planetRenderer;
  }

  public final void run(G3MContext context)
  {
    _planetRenderer.recreateTiles();
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

