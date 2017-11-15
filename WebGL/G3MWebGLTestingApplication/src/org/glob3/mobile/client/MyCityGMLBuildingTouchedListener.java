package org.glob3.mobile.client;

import org.glob3.mobile.generated.CityGMLBuilding;
import org.glob3.mobile.generated.CityGMLBuildingTouchedListener;

public class MyCityGMLBuildingTouchedListener extends CityGMLBuildingTouchedListener {

  
  public MyCityGMLBuildingTouchedListener(){
	  
  }
  
  @Override
  public void dispose()
  {
	  
  }


  @Override
  public void onBuildingTouched(CityGMLBuilding building) {
	  NativeUtils.callNativeLog("Is this ever called?");
	  String name = "ID: " + building._name + "\n" + building.getPropertiesDescription();
	  
	  NativeUtils.callNativeAlert("Building selected: \n"+name);
	  //Provisional: pareciera que esto activara otros menús por ahí.
    
	 /* UIAlertController * alert=   [UIAlertController
	                                alertControllerWithTitle:@"Building selected"
	                                message:[NSString stringWithUTF8String:name.c_str()]
	                                		preferredStyle:UIAlertControllerStyleAlert];
    
	  UIAlertAction* yesButton = [UIAlertAction
	                              actionWithTitle:@"Ok"
	                              style:UIAlertActionStyleDefault
	                              handler:^(UIAlertAction * action)
	                              {
                                  	//Handel your yes please button action here
                                  
	                              }];
    
	  [alert addAction:yesButton];
    
    
    
	  UIAlertAction* SRButton = [UIAlertAction
	                             actionWithTitle:@"Show Solar Radiation Data"
	                             style:UIAlertActionStyleDefault
	                             handler:^(UIAlertAction * action)
	                             {
                                 	[_vc loadSolarRadiationPointCloudForBuilding:building];
	                             }];
    
    [alert addAction:SRButton];
    
    [_vc presentViewController:alert animated:YES completion:nil];	*/
  }

}
