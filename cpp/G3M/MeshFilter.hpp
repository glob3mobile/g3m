//
//  MeshFilter.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 11/6/18.
//

#ifndef MeshFilter_hpp
#define MeshFilter_hpp

class Mesh;


class MeshFilter {
public:
  virtual ~MeshFilter() {
  }

  virtual bool test(const Mesh* mesh) const = 0;

};

#endif

