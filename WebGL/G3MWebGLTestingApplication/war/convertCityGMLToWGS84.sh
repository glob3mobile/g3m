export PATH=/Library/Frameworks/GDAL.framework/Programs:$PATH

#BEGIN="<gml:posList>"
#STR=$( grep "<gml:posList>" lindenallee_kranichweg_v1.gml )
#STR2="${STR/"<gml:posList>"/}"
#echo STR
#echo "----"
#echo $STR2

grep "<gml:posList>\([^<]*\)</gml:posList>" lindenallee_kranichweg_v1.gml |
sed 's/<gml:posList>//g' |
sed 's/<\/gml:posList>//g' | 
sed 's/ /\'$'\n/g' > ./grepout3.txt 
#sed 's/ /q/g' > ./grepout3.txt

#sed 's/q/e/2g' ./grepout3.txt > out.txt

echo "OK"
#gdaltransform -s_srs EPSG:31463 -t_srs EPSG:4326