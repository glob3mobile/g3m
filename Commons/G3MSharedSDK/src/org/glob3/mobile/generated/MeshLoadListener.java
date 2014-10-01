<<<<<<< HEAD
package org.glob3.mobile.generated; 
//
//  MeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//

//
//  MeshRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//



//class Mesh;


public interface MeshLoadListener
{
  void dispose();

  void onBeforeAddMesh(Mesh mesh);
  void onAfterAddMesh(Mesh mesh);
=======
package org.glob3.mobile.generated; 
//
//  MeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//

//
//  MeshRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//



//class Mesh;


public interface MeshLoadListener
{
  void dispose();

  void onError(URL url);
  void onBeforeAddMesh(Mesh mesh);
  void onAfterAddMesh(Mesh mesh);
>>>>>>> point-cloud
}