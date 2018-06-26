//
//  UtilityJSONParser.cpp
//  EIFER App
//
//  Created by Chano on 21/6/18.
//
//

#include "UtilityJSONParserTwo.hpp"

#include "PipesModel.hpp"

#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/JSONParser_iOS.hpp>
#include <G3MiOSSDK/JSONArray.hpp>
#include <G3MiOSSDK/JSONObject.hpp>
#include <G3MiOSSDK/JSONNumber.hpp>

class UtilityJSONBufferListener : public IBufferDownloadListener {
public:
    /**
     Callback method invoked on a successful download.  The buffer has to be deleted in C++ / .disposed() in Java
     */
    void onDownload(const URL& url, IByteBuffer* buffer, bool expired){
        /*const std::string s = buffer->getAsString();
        
        const JSONArray * array =  JSONParser_iOS::instance()->parse(buffer)->asArray();
        UtilityJSONParserTwo::correctHeightIfNecessary(array);
        PipesModel::parseComplexContent(array,_planet,_mr, _elevationData, _hOffset);
        
        if (buffer != NULL)
        delete buffer;*/
    };
    
    /**
     Callback method invoke after an error trying to download url
     */
    void onError(const URL& url){};
    
    /**
     Callback method invoke after canceled request
     */
    void onCancel(const URL& url){};
    
    /**
     This method will be call, before onCancel, when the data arrived before the cancelation.
     
     The buffer WILL be deleted/disposed after the method finishs.  If you need to keep the buffer, use shallowCopy() to store a copy of the buffer.
     */
    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired){};
    
};

const Planet * UtilityJSONParserTwo::_planet;
IDownloader * UtilityJSONParserTwo::_downloader;
const ElevationData * UtilityJSONParserTwo::_elevationData;
MeshRenderer * UtilityJSONParserTwo::_mr;
double UtilityJSONParserTwo::_hOffset;
const IThreadUtils * UtilityJSONParserTwo::_tUtils;


void UtilityJSONParserTwo::initialize (const G3MContext *context,
                                       const ElevationData *elevationData,
                                       MeshRenderer *mr,
                                       double heightOffset)
{
    _planet = context->getPlanet();
    _downloader = context->getDownloader();
    _elevationData = elevationData;
    _mr = mr;
    _hOffset = heightOffset;
    _tUtils = context->getThreadUtils();
}

void UtilityJSONParserTwo::parseFromURL(URL &url)
{
    
    if (_downloader == NULL)
    {
        throw ("UtilityParser not initialized");
    }
    
    _downloader->requestBuffer(url, DownloadPriority::HIGHEST, TimeInterval::fromHours(1), true, new UtilityJSONBufferListener(), true);
}

void UtilityJSONParserTwo::correctHeightIfNecessary(const JSONArray *a){
   /* if (_elevationData == NULL)
        return;
    
    Sector s = _elevationData->getSector();
    
    for (int i=0;i<a->size();i++){ //Objetos
        for (int j=0;j<a->getAsObject(i)->getAsArray("covers")->size();j++) { //Covers
            for (int k=0;k<a->getAsObject(i)->getAsArray("covers")->getAsArray(j)->size();k++){ //Puntos
                double lat = a->getAsObject(i)->getAsArray("covers")->getAsArray(j)->getAsArray(k)->getAsNumber(1)->value();
                double lon = a->getAsObject(i)->getAsArray("covers")->getAsArray(j)->getAsArray(k)->getAsNumber(0)->value();
                double hgt = a->getAsObject(i)->getAsArray("covers")->getAsArray(j)->getAsArray(k)->getAsNumber(2)->value();
                if (s.contains(Angle::fromDegrees(lat),Angle::fromDegrees(lon))){
                    hgt = hgt + _elevationData->getElevationAt(Angle::fromDegrees(lat),Angle::fromDegrees(lon));
                    a->getAsObject(i)->getAsArray("covers")->getAsArray(j)->getAsArray(k)->getAsNumber(2)->updateValue(hgt);
                }
                
            }
        }
        for (int j=0;j<a->getAsObject(i)->getAsArray("ditches")->size();j++) { //Covers
            for (int k=0;k<a->getAsObject(i)->getAsArray("ditches")->getAsArray(j)->size();k++){ //Puntos
                double lat = a->getAsObject(i)->getAsArray("ditches")->getAsArray(j)->getAsArray(k)->getAsNumber(1)->value();
                double lon = a->getAsObject(i)->getAsArray("ditches")->getAsArray(j)->getAsArray(k)->getAsNumber(0)->value();
                double hgt = a->getAsObject(i)->getAsArray("ditches")->getAsArray(j)->getAsArray(k)->getAsNumber(2)->value();
                if (s.contains(Angle::fromDegrees(lat),Angle::fromDegrees(lon))){
                    hgt = hgt + _elevationData->getElevationAt(Angle::fromDegrees(lat),Angle::fromDegrees(lon));
                    a->getAsObject(i)->getAsArray("ditches")->getAsArray(j)->getAsArray(k)->getAsNumber(2)->updateValue(hgt);
                }
            }
        }
        
        for (int j=0;j<a->getAsObject(i)->getAsArray("line")->size();j++) { //Covers
            double lat = a->getAsObject(i)->getAsArray("line")->getAsArray(j)->getAsNumber(1)->value();
            double lon = a->getAsObject(i)->getAsArray("line")->getAsArray(j)->getAsNumber(0)->value();
            double hgt = a->getAsObject(i)->getAsArray("line")->getAsArray(j)->getAsNumber(2)->value();
            if (s.contains(Angle::fromDegrees(lat),Angle::fromDegrees(lon))){
                hgt = hgt + _elevationData->getElevationAt(Angle::fromDegrees(lat),Angle::fromDegrees(lon));
                a->getAsObject(i)->getAsArray("line")->getAsArray(j)->getAsNumber(2)->updateValue(hgt);
            }
        }
        
        double lat = a->getAsObject(i)->getAsArray("startPoint")->getAsNumber(1)->value();
        double lon = a->getAsObject(i)->getAsArray("startPoint")->getAsNumber(0)->value();
        double hgt = a->getAsObject(i)->getAsArray("startPoint")->getAsNumber(2)->value();
        if (s.contains(Angle::fromDegrees(lat),Angle::fromDegrees(lon))){
            hgt = hgt + _elevationData->getElevationAt(Angle::fromDegrees(lat),Angle::fromDegrees(lon));
            a->getAsObject(i)->getAsArray("startPoint")->getAsNumber(2)->updateValue(hgt);
        }
        lat = a->getAsObject(i)->getAsArray("endPoint")->getAsNumber(1)->value();
        lon = a->getAsObject(i)->getAsArray("endPoint")->getAsNumber(0)->value();
        hgt = a->getAsObject(i)->getAsArray("endPoint")->getAsNumber(2)->value();
        if (s.contains(Angle::fromDegrees(lat),Angle::fromDegrees(lon))){
            hgt = hgt + _elevationData->getElevationAt(Angle::fromDegrees(lat),Angle::fromDegrees(lon));
            a->getAsObject(i)->getAsArray("endPoint")->getAsNumber(2)->updateValue(hgt);
        }
    }
    return a;*/
}
