package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.CityGMLBuilding;
import org.glob3.mobile.generated.CityGMLRendererListener;

public class MyCityGMLRendererListener extends CityGMLRendererListener{
	
	G3MWebGLTestingApplication vc;
	
	public MyCityGMLRendererListener(G3MWebGLTestingApplication vc){
		this.vc = vc;
	}

	@Override
	public void onBuildingsLoaded(ArrayList<CityGMLBuilding> buildings) {
		// TODO Auto-generated method stub
		vc.onCityModelLoaded();
		//Decreasing consumed memory
		for (int i = 0; i < buildings.size(); i++) {
			buildings.get(i).removeSurfaceData();
		}
	}
}


/*
class MyCityGMLRendererListener: public CityGMLRendererListener{
ViewController* _vc;
public:
MyCityGMLRendererListener(ViewController* vc):_vc(vc){}

void onBuildingsLoaded(const std::vector<CityGMLBuilding*>& buildings){
  [_vc onCityModelLoaded];
  
#pragma mark UNCOMMENT TO SAVE MEMORY
  //Decreasing consumed memory
  for (size_t i = 0; i < buildings.size(); i++) {
    buildings[i]->removeSurfaceData();
  }
}

};
*/