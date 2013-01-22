

package org.glob3.mobile.demo;

import java.io.Serializable;
import java.util.ArrayList;


public class WindModelCsiro
    implements
      Serializable {

  public WindModelCsiro(final float latitude,
                        final float longitude,
                        final ArrayList<Float> levels,
                        final ArrayList<Float> meridWind,
                        final ArrayList<Float> zonalWind) {
    super();
    _latitude = latitude;
    _longitude = longitude;
    _levels = levels;
    _meridWind = meridWind;
    _zonalWind = zonalWind;
  }


  public WindModelCsiro() {

  }

  /**
    * 
    */
  private static final long serialVersionUID = 1L;
  float _latitude;
  float _longitude;
  ArrayList<Float> _levels;
  ArrayList<Float> _meridWind;
  ArrayList<Float> _zonalWind;


  public static long getSerialversionuid() {
    return serialVersionUID;
  }


  public float getLatitude() {
    return _latitude;
  }


  public float getLongitude() {
    return _longitude;
  }


  public ArrayList<Float> getLevels() {
    return _levels;
  }


  public ArrayList<Float> getMeridWind() {
    return _meridWind;
  }


  public ArrayList<Float> getZonalWind() {
    return _zonalWind;
  }


  public void setLatitude(final float latitude) {
    _latitude = latitude;
  }


  public void setLongitude(final float longitude) {
    _longitude = longitude;
  }


  public void setLevels(final ArrayList<Float> levels) {
    _levels = levels;
  }


  public void setMeridWind(final ArrayList<Float> meridWind) {
    _meridWind = meridWind;
  }


  public void setZonalWind(final ArrayList<Float> zonalWind) {
    _zonalWind = zonalWind;
  }


  @Override
  public String toString() {
    return "lat:" + _latitude + ",lon:" + _longitude + ",Num. levels:"
           + _levels.size() + ",Num zonal winds:" + _zonalWind.size()
           + ",Num merid winds:" + _meridWind.size();

  }
}
