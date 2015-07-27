<?php
function getClientHeaders() {
	$headers=array();
	foreach($_SERVER as $header => $value) {
		if(substr($header,0,5) == "HTTP_") {
			$header=str_replace('_',' ',substr($header,5));
			$header=str_replace(' ','-',ucwords(strtolower($header)));
			$headers[$header]=$value;
		}
	}
	return $headers;
}

$fullURL= 'http://'.$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI'];

$url=substr($fullURL,strpos($fullURL,"?url=")+strlen("?url="));

$maxTTL=31536000;

$original_headers=getClientHeaders();

$request_headers=array();

foreach($original_headers as $header => $value) {
if($header=="Host") {
$parsedUrl=parse_url($url);
$value=$parsedUrl["host"];
}

elseif($header=="User-Agent" || $header=="Accept-Encoding"/* || $header=="Referer"*/) continue;
$request_headers[]="$header: $value";
}

$response_headers=array();

$response_status="";

$content="";

$statusCode=200;

$ch=curl_init();

curl_setopt($ch, CURLOPT_HEADER, 1);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $request_headers);
curl_setopt($ch, CURLOPT_URL, $url);

$response=curl_exec($ch);

curl_close($ch);

$header_length=0;

$get_status=true;

$responseStatus="";

$validContent=false;

foreach(explode("\r\n",$response) as $value) {
	if($get_status) {
		$responseStatus=$value;
		$show_status=false;
	}
	$header_length+=(strlen($value)+2);
	if(stripos($value,"Content-Type")===0) {
		$validContent=true;
		$response_headers[]=$value;
	}
	if(stripos($value,"Content-Length")===0) $response_headers[]=$value;
if(strlen($value)==0) break;
}

$response_status="HTTP/1.1 200 OK";

$content=substr($response,$header_length);

$response_headers[]="Content-Length: ".strlen($content);

$response_headers[]="Cache-Control: max-age=$maxTTL";


header($response_status,true);

foreach($response_headers as $value) header($value,true);

if(strlen($content)>0) echo $content;

?>