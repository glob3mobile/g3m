//
//  TileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef TileLODTester_hpp
#define TileLODTester_hpp

#include "TileTessellator.hpp"
#include "TileTexturizer.hpp"
#include "Context.hpp"

class TileLODTesterData{
  //Empty class. Each TileLODTester will implement a different set of associated data and will
  //store it inside the tile using its unique level id
public:
#ifdef C_CODE
  virtual ~TileLODTesterData() { }
#endif
#ifdef JAVA_CODE
  void dispose(){}
#endif
};

class TileLODTester{
  
public:
  
  TileLODTester(){}
  
  virtual ~TileLODTester(){}
  
  virtual bool meetsRenderCriteria(int testerLevel,
                                   Tile* tile, const G3MRenderContext& rc) const = 0;
  
  virtual bool isVisible(int testerLevel, Tile* tile, const G3MRenderContext& rc) const = 0;
  
  virtual void onTileHasChangedMesh(int testerLevel, Tile* tile) const = 0;
};

class TileLODTesterResponder: public TileLODTester{
  
  TileLODTester* _nextTesterRightLoD;
  TileLODTester* _nextTesterWrongLoD;
  TileLODTester* _nextTesterVisible;
  TileLODTester* _nextTesterNotVisible;
  
protected:
  
  virtual bool _meetsRenderCriteria(int testerLevel,
                                    Tile* tile,
                                    const G3MRenderContext& rc) const = 0;
  
  virtual bool _isVisible(int testerLevel,
                          Tile* tile,
                          const G3MRenderContext& rc) const = 0;
  
  virtual void _onTileHasChangedMesh(int testerLevel, Tile* tile) const{}
  
public:
  
  TileLODTesterResponder(TileLODTester* nextTesterRightLoD,
                         TileLODTester* nextTesterWrongLoD,
                         TileLODTester* nextTesterVisible,
                         TileLODTester* nextTesterNotVisible):
  _nextTesterRightLoD(nextTesterRightLoD),
  _nextTesterWrongLoD(nextTesterWrongLoD),
  _nextTesterVisible(nextTesterVisible),
  _nextTesterNotVisible(nextTesterNotVisible){
    
  }
  
  virtual ~TileLODTesterResponder();
  
  bool meetsRenderCriteria(int testerLevel,
                           Tile* tile, const G3MRenderContext& rc) const;
  
  bool isVisible(int testerLevel, Tile* tile, const G3MRenderContext& rc) const;
  
  virtual void onTileHasChangedMesh(int testerLevel, Tile* tile) const;
};

#endif /* TileLODTester_hpp */
