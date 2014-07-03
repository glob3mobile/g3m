package org.glob3.mobile.generated; 
public class LODAugmentedSector
{
  public Sector _sector;
  public double _lodFactor;

  public LODAugmentedSector(Sector sector, double factor)
  {
     _sector = new Sector(sector);
     _lodFactor = factor;
  }

  //CANT DO THIS
  //    ~LODAugmentedSector(){
  //      delete _sector;
  //    }
}