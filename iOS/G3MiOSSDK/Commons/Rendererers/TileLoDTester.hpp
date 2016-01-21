//
//  TileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef TileLODTester_hpp
#define TileLODTester_hpp

class Tile;
class G3MRenderContext;
class LayerTilesRenderParameters;


class TileLODTesterData{
  //Empty class. Each TileLODTester will implement a different set of associated data and will
  //store it inside the tile using its unique level id
public:
#ifdef C_CODE
  virtual ~TileLODTesterData() { }
#endif
#ifdef JAVA_CODE
  void dispose() {}
#endif
};

class TileLODTester {

public:

  TileLODTester() { }

  virtual ~TileLODTester() { }

  virtual bool meetsRenderCriteria(int testerLevel,
                                   Tile* tile,
                                   const G3MRenderContext& rc) const = 0;

  virtual bool isVisible(int testerLevel,
                         Tile* tile,
                         const G3MRenderContext& rc) const = 0;

  virtual void onTileHasChangedMesh(int testerLevel,
                                    Tile* tile) const = 0;

  virtual void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) = 0;

};

class TileLODTesterResponder: public TileLODTester {

  TileLODTester* _nextTesterRightLOD;
  TileLODTester* _nextTesterWrongLOD;
  TileLODTester* _nextTesterVisible;
  TileLODTester* _nextTesterNotVisible;

protected:

  virtual bool _meetsRenderCriteria(int testerLevel,
                                    Tile* tile,
                                    const G3MRenderContext& rc) const = 0;

  virtual bool _isVisible(int testerLevel,
                          Tile* tile,
                          const G3MRenderContext& rc) const = 0;

  virtual void _onTileHasChangedMesh(int testerLevel, Tile* tile) const {}

  virtual void _onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) = 0;

public:

  TileLODTesterResponder(TileLODTester* nextTesterRightLOD,
                         TileLODTester* nextTesterWrongLOD,
                         TileLODTester* nextTesterVisible,
                         TileLODTester* nextTesterNotVisible):
  _nextTesterRightLOD(nextTesterRightLOD),
  _nextTesterWrongLOD(nextTesterWrongLOD),
  _nextTesterVisible(nextTesterVisible),
  _nextTesterNotVisible(nextTesterNotVisible) {

  }

  virtual ~TileLODTesterResponder();

  bool meetsRenderCriteria(int testerLevel,
                           Tile* tile,
                           const G3MRenderContext& rc) const;

  bool isVisible(int testerLevel,
                 Tile* tile,
                 const G3MRenderContext& rc) const;

  void onTileHasChangedMesh(int testerLevel,
                            Tile* tile) const;

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);
};

#endif
