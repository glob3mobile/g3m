<?xml version="1.0" encoding="UTF-8"?>
<core:CityModel xmlns:app="http://www.opengis.net/citygml/appearance/2.0" xmlns:luse="http://www.opengis.net/citygml/landuse/2.0" xmlns:utility="http://www.citygml.org/ade/utility/0.9.2" xmlns:wtr="http://www.opengis.net/citygml/waterbody/2.0" xmlns:xAL="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0" xmlns:bldg="http://www.opengis.net/citygml/building/2.0" xmlns:core="http://www.opengis.net/citygml/2.0" xmlns:smil20="http://www.w3.org/2001/SMIL20/" xmlns:grp="http://www.opengis.net/citygml/cityobjectgroup/2.0" xmlns:dem="http://www.opengis.net/citygml/relief/2.0" xmlns:gen="http://www.opengis.net/citygml/generics/2.0" xmlns:tex="http://www.opengis.net/citygml/texturedsurface/2.0" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:tun="http://www.opengis.net/citygml/tunnel/2.0" xmlns:frn="http://www.opengis.net/citygml/cityfurniture/2.0" xmlns:tran="http://www.opengis.net/citygml/transportation/2.0" xmlns:gml="http://www.opengis.net/gml" xmlns:bridge="http://www.opengis.net/citygml/bridge/2.0" xmlns:pbase="http://www.opengis.net/citygml/profiles/base/2.0" xmlns:smil20lang="http://www.w3.org/2001/SMIL20/Language" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:veg="http://www.opengis.net/citygml/vegetation/2.0" xmlns:sch="http://www.ascc.net/xml/schematron" xsi:schemaLocation="http://www.citygml.org/ade/utility/0.9.2 CityGML_UtilityNetworkADE.xsd http://www.opengis.net/citygml/2.0 http://schemas.opengis.net/citygml/2.0/cityGMLBase.xsd">
	<gml:boundedBy>
		<gml:Envelope srsName="epsg:4326" srsDimension="3">
			<gml:lowerCorner>6.2189085 49.1026767 -2</gml:lowerCorner>
			<gml:upperCorner>6.22390886776226 49.105205947361 12</gml:upperCorner>
		</gml:Envelope>
	</gml:boundedBy>
	<core:cityObjectMember>
		<utility:Network gml:id="fbcb67a8-a2ec-11e7-bfbb-7824afca2075">
			<utility:transportedMedium>
				<utility:LiquidMedium gml:id="LiquidMediumID_fdec5a06-a2ec-11e7-bfbb-7824afca2075">
					<utility:type>freshWater</utility:type>
				</utility:LiquidMedium>
			</utility:transportedMedium>
			<utility:topoGraph>
				<utility:NetworkGraph gml:id="fbcb67a9-a2ec-11e7-bfbb-7824afca2075">
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a50-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a51-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22002878994209 49.1048310366529 0 6.21990191156883 49.1048192504919 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a31-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a3d-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a31-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22002878994209 49.1048310366529 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a3d-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.21990191156883 49.1048192504919 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a53-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a54-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22009222912871 49.1046817450734 0 6.21990019699622 49.1046239365592 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a4c-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a4e-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a4c-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22009222912871 49.1046817450734 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a4e-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.21990019699622 49.1046239365592 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a56-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a57-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22257321569778 49.103441934115 0 6.22251492022899 49.1034318314087 0 6.22246464353143 49.1035193797909 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a24-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a3a-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a24-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22257321569778 49.103441934115 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a3a-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22246464353143 49.1035193797909 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a59-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a5a-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22025614642678 49.1043047861346 0 6.22036656074659 49.1043326479728 0 6.22279699254154 49.1035918914964 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a49-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a4a-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a49-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22025614642678 49.1043047861346 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a4a-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22279699254154 49.1035918914964 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a5c-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a5d-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22016516530692 49.1041723625998 0 6.22073005014026 49.104001508582 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a38-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a48-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a38-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22016516530692 49.1041723625998 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a48-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22073005014026 49.104001508582 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a5f-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a60-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22000821507075 49.1037534332178 0 6.21908234586047 49.1035480124074 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a47-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a1e-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a47-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22000821507075 49.1037534332178 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a1e-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.21908234586047 49.1035480124074 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a62-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a63-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22276350659786 49.1030306803459 0 6.22324532816155 49.1031192077571 0 6.22337906482526 49.1028722505179 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a45-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a46-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a45-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22276350659786 49.1030306803459 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a46-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22337906482526 49.1028722505179 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a65-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a66-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.2230327211577 49.1036007708388 0 6.22290412821183 49.1035614826505 0 6.22281668500863 49.1035076016561 0 6.22258178856084 49.1034458629447 0 6.22283383073475 49.1028700054465 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a42-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a44-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a42-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.2230327211577 49.1036007708388 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a44-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22283383073475 49.1028700054465 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a68-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a69-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22009222912871 49.1046817450734 0 6.22002878994209 49.1048310366529 0 6.21994991960196 49.105205947361 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a4b-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a34-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a4b-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22009222912871 49.1046817450734 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a34-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.21994991960196 49.105205947361 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a6b-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a6c-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22029863845834 49.1043666334059 0 6.22018653062237 49.1043977530846 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a40-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a41-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a40-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22029863845834 49.1043666334059 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a41-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22018653062237 49.1043977530846 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a6e-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a6f-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22000186658641 49.1039346795016 0 6.21933267346177 49.1041014118852 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a2e-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a3f-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a2e-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22000186658641 49.1039346795016 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a3f-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.21933267346177 49.1041014118852 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a71-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a72-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22163746865239 49.1037248235772 0 6.22131814854605 49.1034121872516 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a1b-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a22-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a1b-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22163746865239 49.1037248235772 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a22-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22131814854605 49.1034121872516 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a74-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a75-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22054432488358 49.1038314183658 0 6.2206768983893 49.1036007708388 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a1d-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a1c-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a1d-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22054432488358 49.1038314183658 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a1c-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.2206768983893 49.1036007708388 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a77-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a78-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.2230327211577 49.1036007708388 0 6.22312530807873 49.1034885187899 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a43-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a37-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a43-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.2230327211577 49.1036007708388 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a37-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22312530807873 49.1034885187899 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a7a-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a7b-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.2199567778924 49.103869052207 0 6.22000821507075 49.1037534332178 0 6.22006136682171 49.1037276153484 0 6.22012309143573 49.1037264928321 0 6.22063231950139 49.1038533370204 0 6.22073005014026 49.104001508582 0 6.22233146095954 49.1035132142624 0 6.22234517754043 49.1034930088767 0 6.22279268099207 49.1035917906842 0 6.22288869705832 49.103594035723 0 6.2230327211577 49.1036007708388 0 6.22327961961378 49.1036703669816 0 6.22329847991251 49.1037186352168 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a20-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a2b-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a20-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.2199567778924 49.103869052207 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a2b-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22329847991251 49.1037186352168 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a7d-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a7e-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.2199567778924 49.103869052207 0 6.22044119669877 49.1045741267624 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a1f-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a3b-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a1f-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.2199567778924 49.103869052207 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a3b-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22044119669877 49.1045741267624 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a80-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a81-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22300554864784 49.1041418406234 0 6.22280639757296 49.1042046825041 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a21-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a23-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a21-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22300554864784 49.1041418406234 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a23-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22280639757296 49.1042046825041 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a83-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a84-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22301729000419 49.1041586597548 0 6.22324704273416 49.1040879417887 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a2a-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a25-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a2a-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22301729000419 49.1041586597548 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a25-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22324704273416 49.1040879417887 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a86-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a87-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22301729000419 49.1041586597548 0 6.22310473320739 49.1042899928532 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a28-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a2f-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a28-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22301729000419 49.1041586597548 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a2f-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22310473320739 49.1042899928532 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a89-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a8a-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22279959235116 49.1038468136431 0 6.22301729000419 49.1041586597548 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a26-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a29-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a26-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22279959235116 49.1038468136431 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a29-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22301729000419 49.1041586597548 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a8c-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a8d-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22125995693177 49.1045741246456 0 6.22100095261289 49.1046525602009 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a2c-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a27-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a2c-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22125995693177 49.1045741246456 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a27-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22100095261289 49.1046525602009 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a8f-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a90-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22111361922562 49.1043667573319 0 6.2213850168779 49.1047513397002 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a35-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a32-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a35-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22111361922562 49.1043667573319 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a32-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.2213850168779 49.1047513397002 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a92-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a93-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22196154644871 49.1045277750739 0 6.22171078567411 49.104606537867 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a2d-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a36-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a2d-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22196154644871 49.1045277750739 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a36-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22171078567411 49.104606537867 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a95-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a96-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22197641671904 49.1045482078522 0 6.22222687203022 49.1044752056064 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a39-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a33-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a39-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22197641671904 49.1045482078522 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a33-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22222687203022 49.1044752056064 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a98-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a99-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22171041974196 49.1041827083651 0 6.22211199566524 49.1047345022994 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a3e-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a30-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a3e-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22171041974196 49.1041827083651 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a30-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22211199566524 49.1047345022994 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_fdec5a9b-a2ec-11e7-bfbb-7824afca2075">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_fdec5a9c-a2ec-11e7-bfbb-7824afca2075">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>6.22009222912871 49.1046817450734 0 6.22311330607045 49.1037500656704 0 6.22329847991251 49.1037186352168 0 6.22343221657622 49.1037231252828 0 6.22390886776226 49.1038151715465 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fdec5a4d-a2ec-11e7-bfbb-7824afca2075"/>
									<utility:end xlink:href="NodeID_fdec5a3c-a2ec-11e7-bfbb-7824afca2075"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a4d-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22009222912871 49.1046817450734 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fdec5a3c-a2ec-11e7-bfbb-7824afca2075">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>6.22390886776226 49.1038151715465 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:linkMember>
						<utility:InterFeatureLink gml:id="InterFeatureLinkID_fdec5a9d-a2ec-11e7-bfbb-7824afca2075">
							<utility:start xlink:href="NodeID_fdec5a1f-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:end xlink:href="NodeID_fdec5a20-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:type>connects</utility:type>
						</utility:InterFeatureLink>
					</utility:linkMember>
					<utility:linkMember>
						<utility:InterFeatureLink gml:id="InterFeatureLinkID_fdec5a9e-a2ec-11e7-bfbb-7824afca2075">
							<utility:start xlink:href="NodeID_fdec5a28-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:end xlink:href="NodeID_fdec5a29-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:type>connects</utility:type>
						</utility:InterFeatureLink>
					</utility:linkMember>
					<utility:linkMember>
						<utility:InterFeatureLink gml:id="InterFeatureLinkID_fdec5a9f-a2ec-11e7-bfbb-7824afca2075">
							<utility:start xlink:href="NodeID_fdec5a28-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:end xlink:href="NodeID_fdec5a2a-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:type>connects</utility:type>
						</utility:InterFeatureLink>
					</utility:linkMember>
					<utility:linkMember>
						<utility:InterFeatureLink gml:id="InterFeatureLinkID_fdec5aa0-a2ec-11e7-bfbb-7824afca2075">
							<utility:start xlink:href="NodeID_fdec5a29-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:end xlink:href="NodeID_fdec5a2a-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:type>connects</utility:type>
						</utility:InterFeatureLink>
					</utility:linkMember>
					<utility:linkMember>
						<utility:InterFeatureLink gml:id="InterFeatureLinkID_fdec5aa1-a2ec-11e7-bfbb-7824afca2075">
							<utility:start xlink:href="NodeID_fdec5a42-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:end xlink:href="NodeID_fdec5a43-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:type>connects</utility:type>
						</utility:InterFeatureLink>
					</utility:linkMember>
					<utility:linkMember>
						<utility:InterFeatureLink gml:id="InterFeatureLinkID_fdec5aa2-a2ec-11e7-bfbb-7824afca2075">
							<utility:start xlink:href="NodeID_fdec5a4b-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:end xlink:href="NodeID_fdec5a4c-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:type>connects</utility:type>
						</utility:InterFeatureLink>
					</utility:linkMember>
					<utility:linkMember>
						<utility:InterFeatureLink gml:id="InterFeatureLinkID_fdec5aa3-a2ec-11e7-bfbb-7824afca2075">
							<utility:start xlink:href="NodeID_fdec5a4b-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:end xlink:href="NodeID_fdec5a4d-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:type>connects</utility:type>
						</utility:InterFeatureLink>
					</utility:linkMember>
					<utility:linkMember>
						<utility:InterFeatureLink gml:id="InterFeatureLinkID_fdec5aa4-a2ec-11e7-bfbb-7824afca2075">
							<utility:start xlink:href="NodeID_fdec5a4c-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:end xlink:href="NodeID_fdec5a4d-a2ec-11e7-bfbb-7824afca2075"/>
							<utility:type>connects</utility:type>
						</utility:InterFeatureLink>
					</utility:linkMember>
				</utility:NetworkGraph>
			</utility:topoGraph>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a4f-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a50-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22002878994209 49.1048310366529 -2 6.21990191156883 49.1048192504919 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a52-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a53-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22009222912871 49.1046817450734 -2 6.21990019699622 49.1046239365592 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a55-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a56-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22257321569778 49.103441934115 -2 6.22251492022899 49.1034318314087 -2 6.22246464353143 49.1035193797909 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a58-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a59-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22025614642678 49.1043047861346 -2 6.22036656074659 49.1043326479728 -2 6.22279699254154 49.1035918914964 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a5b-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a5c-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22016516530692 49.1041723625998 -2 6.22073005014026 49.104001508582 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a5e-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a5f-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22000821507075 49.1037534332178 -2 6.21908234586047 49.1035480124074 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a61-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a62-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22276350659786 49.1030306803459 -2 6.22324532816155 49.1031192077571 -2 6.22337906482526 49.1028722505179 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a64-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a65-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.2230327211577 49.1036007708388 -2 6.22290412821183 49.1035614826505 -2 6.22281668500863 49.1035076016561 -2 6.22258178856084 49.1034458629447 -2 6.22283383073475 49.1028700054465 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a67-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a68-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22009222912871 49.1046817450734 -2 6.22002878994209 49.1048310366529 -2 6.21994991960196 49.105205947361 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a6a-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a6b-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22029863845834 49.1043666334059 -2 6.22018653062237 49.1043977530846 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a6d-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a6e-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22000186658641 49.1039346795016 -2 6.21933267346177 49.1041014118852 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a70-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a71-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22163746865239 49.1037248235772 -2 6.22131814854605 49.1034121872516 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a73-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a74-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22054432488358 49.1038314183658 -2 6.2206768983893 49.1036007708388 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a76-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a77-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.2230327211577 49.1036007708388 -2 6.22312530807873 49.1034885187899 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a79-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a7a-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.2199567778924 49.103869052207 -2 6.22000821507075 49.1037534332178 -2 6.22006136682171 49.1037276153484 -2 6.22012309143573 49.1037264928321 -2 6.22063231950139 49.1038533370204 -2 6.22073005014026 49.104001508582 -2 6.22233146095954 49.1035132142624 -2 6.22234517754043 49.1034930088767 -2 6.22279268099207 49.1035917906842 -2 6.22288869705832 49.103594035723 -2 6.2230327211577 49.1036007708388 -2 6.22327961961378 49.1036703669816 -2 6.22329847991251 49.1037186352168 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a7c-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a7d-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.2199567778924 49.103869052207 -2 6.22044119669877 49.1045741267624 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a7f-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a80-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22300554864784 49.1041418406234 -2 6.22280639757296 49.1042046825041 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a82-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a83-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22301729000419 49.1041586597548 -2 6.22324704273416 49.1040879417887 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a85-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a86-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22301729000419 49.1041586597548 -2 6.22310473320739 49.1042899928532 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a88-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a89-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22279959235116 49.1038468136431 -2 6.22301729000419 49.1041586597548 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a8b-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a8c-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22125995693177 49.1045741246456 -2 6.22100095261289 49.1046525602009 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a8e-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a8f-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22111361922562 49.1043667573319 -2 6.2213850168779 49.1047513397002 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a91-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a92-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22196154644871 49.1045277750739 -2 6.22171078567411 49.104606537867 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a94-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a95-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22197641671904 49.1045482078522 -2 6.22222687203022 49.1044752056064 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a97-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a98-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22171041974196 49.1041827083651 -2 6.22211199566524 49.1047345022994 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_fdec5a9a-a2ec-11e7-bfbb-7824afca2075">
					<utility:hasMaterial xlink:href="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:topoGraph xlink:href="FeatureGraphID_fdec5a9b-a2ec-11e7-bfbb-7824afca2075"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>6.22009222912871 49.1046817450734 -2 6.22311330607045 49.1037500656704 -2 6.22329847991251 49.1037186352168 -2 6.22343221657622 49.1037231252828 -2 6.22390886776226 49.1038151715465 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter uom="cm">102</utility:exteriorDiameter>
					<utility:interiorDiameter uom="cm">100</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
		</utility:Network>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<utility:InteriorMaterial gml:id="InteriorMaterialID_fdec5a07-a2ec-11e7-bfbb-7824afca2075">
			<utility:type>steel</utility:type>
		</utility:InteriorMaterial>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<utility:ExteriorMaterial gml:id="ExteriorMaterialID_fdec5a08-a2ec-11e7-bfbb-7824afca2075">
			<utility:type>steel</utility:type>
		</utility:ExteriorMaterial>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a09-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195449 49.1041164 0 6.2195358 49.1040731 0 6.2195431 49.1040682 0 6.2195434 49.1040616 0 6.2195407 49.1040563 0 6.2195315 49.1040519 0 6.2195229 49.1040095 0 6.2191369 49.1040439 0 6.2191454 49.1040864 0 6.2191385 49.1040925 0 6.2191387 49.1041012 0 6.2191443 49.1041061 0 6.2191498 49.1041081 0 6.2191811 49.104257 0 6.2192467 49.1042511 0 6.219254 49.1042555 0 6.2192659 49.1042563 0 6.2192757 49.1042527 0 6.2192789 49.1042482 0 6.2193449 49.104242 0 6.2193226 49.1041359 0 6.2195449 49.1041164 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195449 49.1041164 0 6.2193226 49.1041359 0 6.2193226 49.1041359 9 6.2195449 49.1041164 9 6.2195449 49.1041164 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2193226 49.1041359 0 6.2193449 49.104242 0 6.2193449 49.104242 9 6.2193226 49.1041359 9 6.2193226 49.1041359 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2193449 49.104242 0 6.2192789 49.1042482 0 6.2192789 49.1042482 9 6.2193449 49.104242 9 6.2193449 49.104242 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192789 49.1042482 0 6.2192757 49.1042527 0 6.2192757 49.1042527 9 6.2192789 49.1042482 9 6.2192789 49.1042482 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192757 49.1042527 0 6.2192659 49.1042563 0 6.2192659 49.1042563 9 6.2192757 49.1042527 9 6.2192757 49.1042527 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192659 49.1042563 0 6.219254 49.1042555 0 6.219254 49.1042555 9 6.2192659 49.1042563 9 6.2192659 49.1042563 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.219254 49.1042555 0 6.2192467 49.1042511 0 6.2192467 49.1042511 9 6.219254 49.1042555 9 6.219254 49.1042555 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192467 49.1042511 0 6.2191811 49.104257 0 6.2191811 49.104257 9 6.2192467 49.1042511 9 6.2192467 49.1042511 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191811 49.104257 0 6.2191498 49.1041081 0 6.2191498 49.1041081 9 6.2191811 49.104257 9 6.2191811 49.104257 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191498 49.1041081 0 6.2191443 49.1041061 0 6.2191443 49.1041061 9 6.2191498 49.1041081 9 6.2191498 49.1041081 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191443 49.1041061 0 6.2191387 49.1041012 0 6.2191387 49.1041012 9 6.2191443 49.1041061 9 6.2191443 49.1041061 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191387 49.1041012 0 6.2191385 49.1040925 0 6.2191385 49.1040925 9 6.2191387 49.1041012 9 6.2191387 49.1041012 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191385 49.1040925 0 6.2191454 49.1040864 0 6.2191454 49.1040864 9 6.2191385 49.1040925 9 6.2191385 49.1040925 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191454 49.1040864 0 6.2191369 49.1040439 0 6.2191369 49.1040439 9 6.2191454 49.1040864 9 6.2191454 49.1040864 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191369 49.1040439 0 6.2195229 49.1040095 0 6.2195229 49.1040095 9 6.2191369 49.1040439 9 6.2191369 49.1040439 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195229 49.1040095 0 6.2195315 49.1040519 0 6.2195315 49.1040519 9 6.2195229 49.1040095 9 6.2195229 49.1040095 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195315 49.1040519 0 6.2195407 49.1040563 0 6.2195407 49.1040563 9 6.2195315 49.1040519 9 6.2195315 49.1040519 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195407 49.1040563 0 6.2195434 49.1040616 0 6.2195434 49.1040616 9 6.2195407 49.1040563 9 6.2195407 49.1040563 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195434 49.1040616 0 6.2195431 49.1040682 0 6.2195431 49.1040682 9 6.2195434 49.1040616 9 6.2195434 49.1040616 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195431 49.1040682 0 6.2195358 49.1040731 0 6.2195358 49.1040731 9 6.2195431 49.1040682 9 6.2195431 49.1040682 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195358 49.1040731 0 6.2195449 49.1041164 0 6.2195449 49.1041164 9 6.2195358 49.1040731 9 6.2195358 49.1040731 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195449 49.1041164 9 6.2193226 49.1041359 9 6.2193449 49.104242 9 6.2192789 49.1042482 9 6.2192757 49.1042527 9 6.2192659 49.1042563 9 6.219254 49.1042555 9 6.2192467 49.1042511 9 6.2191811 49.104257 9 6.2191498 49.1041081 9 6.2191443 49.1041061 9 6.2191387 49.1041012 9 6.2191385 49.1040925 9 6.2191454 49.1040864 9 6.2191369 49.1040439 9 6.2195229 49.1040095 9 6.2195315 49.1040519 9 6.2195407 49.1040563 9 6.2195434 49.1040616 9 6.2195431 49.1040682 9 6.2195358 49.1040731 9 6.2195449 49.1041164 9</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a0a-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199495 49.1044608 0 6.2199777 49.1045001 0 6.2199731 49.1045049 0 6.2199748 49.1045123 0 6.2199807 49.1045172 0 6.2199915 49.1045194 0 6.2200191 49.1045587 0 6.2202256 49.1044954 0 6.2202338 49.104498 0 6.2202445 49.1044973 0 6.2202536 49.104493 0 6.2202558 49.1044862 0 6.2203156 49.1044679 0 6.2202037 49.104311 0 6.2201508 49.1042368 0 6.2200914 49.104255 0 6.2200825 49.1042524 0 6.2200714 49.1042537 0 6.2200641 49.1042582 0 6.2200615 49.1042643 0 6.2200022 49.1042826 0 6.2200973 49.1044157 0 6.2199495 49.1044608 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199495 49.1044608 0 6.2200973 49.1044157 0 6.2200973 49.1044157 9 6.2199495 49.1044608 9 6.2199495 49.1044608 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2200973 49.1044157 0 6.2200022 49.1042826 0 6.2200022 49.1042826 9 6.2200973 49.1044157 9 6.2200973 49.1044157 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2200022 49.1042826 0 6.2200615 49.1042643 0 6.2200615 49.1042643 9 6.2200022 49.1042826 9 6.2200022 49.1042826 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2200615 49.1042643 0 6.2200641 49.1042582 0 6.2200641 49.1042582 9 6.2200615 49.1042643 9 6.2200615 49.1042643 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2200641 49.1042582 0 6.2200714 49.1042537 0 6.2200714 49.1042537 9 6.2200641 49.1042582 9 6.2200641 49.1042582 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2200714 49.1042537 0 6.2200825 49.1042524 0 6.2200825 49.1042524 9 6.2200714 49.1042537 9 6.2200714 49.1042537 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2200825 49.1042524 0 6.2200914 49.104255 0 6.2200914 49.104255 9 6.2200825 49.1042524 9 6.2200825 49.1042524 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2200914 49.104255 0 6.2201508 49.1042368 0 6.2201508 49.1042368 9 6.2200914 49.104255 9 6.2200914 49.104255 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2201508 49.1042368 0 6.2202037 49.104311 0 6.2202037 49.104311 9 6.2201508 49.1042368 9 6.2201508 49.1042368 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202037 49.104311 0 6.2203156 49.1044679 0 6.2203156 49.1044679 9 6.2202037 49.104311 9 6.2202037 49.104311 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2203156 49.1044679 0 6.2202558 49.1044862 0 6.2202558 49.1044862 9 6.2203156 49.1044679 9 6.2203156 49.1044679 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202558 49.1044862 0 6.2202536 49.104493 0 6.2202536 49.104493 9 6.2202558 49.1044862 9 6.2202558 49.1044862 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202536 49.104493 0 6.2202445 49.1044973 0 6.2202445 49.1044973 9 6.2202536 49.104493 9 6.2202536 49.104493 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202445 49.1044973 0 6.2202338 49.104498 0 6.2202338 49.104498 9 6.2202445 49.1044973 9 6.2202445 49.1044973 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202338 49.104498 0 6.2202256 49.1044954 0 6.2202256 49.1044954 9 6.2202338 49.104498 9 6.2202338 49.104498 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202256 49.1044954 0 6.2200191 49.1045587 0 6.2200191 49.1045587 9 6.2202256 49.1044954 9 6.2202256 49.1044954 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2200191 49.1045587 0 6.2199915 49.1045194 0 6.2199915 49.1045194 9 6.2200191 49.1045587 9 6.2200191 49.1045587 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199915 49.1045194 0 6.2199807 49.1045172 0 6.2199807 49.1045172 9 6.2199915 49.1045194 9 6.2199915 49.1045194 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199807 49.1045172 0 6.2199748 49.1045123 0 6.2199748 49.1045123 9 6.2199807 49.1045172 9 6.2199807 49.1045172 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199748 49.1045123 0 6.2199731 49.1045049 0 6.2199731 49.1045049 9 6.2199748 49.1045123 9 6.2199748 49.1045123 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199731 49.1045049 0 6.2199777 49.1045001 0 6.2199777 49.1045001 9 6.2199731 49.1045049 9 6.2199731 49.1045049 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199777 49.1045001 0 6.2199495 49.1044608 0 6.2199495 49.1044608 9 6.2199777 49.1045001 9 6.2199777 49.1045001 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199495 49.1044608 9 6.2200973 49.1044157 9 6.2200022 49.1042826 9 6.2200615 49.1042643 9 6.2200641 49.1042582 9 6.2200714 49.1042537 9 6.2200825 49.1042524 9 6.2200914 49.104255 9 6.2201508 49.1042368 9 6.2202037 49.104311 9 6.2203156 49.1044679 9 6.2202558 49.1044862 9 6.2202536 49.104493 9 6.2202445 49.1044973 9 6.2202338 49.104498 9 6.2202256 49.1044954 9 6.2200191 49.1045587 9 6.2199915 49.1045194 9 6.2199807 49.1045172 9 6.2199748 49.1045123 9 6.2199731 49.1045049 9 6.2199777 49.1045001 9 6.2199495 49.1044608 9</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a0b-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2214836 49.1045076 0 6.2216955 49.1048069 0 6.2219747 49.1047219 0 6.2217634 49.104423 0 6.2214836 49.1045076 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2214836 49.1045076 0 6.2217634 49.104423 0 6.2217634 49.104423 3 6.2214836 49.1045076 3 6.2214836 49.1045076 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2217634 49.104423 0 6.2219747 49.1047219 0 6.2219747 49.1047219 3 6.2217634 49.104423 3 6.2217634 49.104423 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2219747 49.1047219 0 6.2216955 49.1048069 0 6.2216955 49.1048069 3 6.2219747 49.1047219 3 6.2219747 49.1047219 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2216955 49.1048069 0 6.2214836 49.1045076 0 6.2214836 49.1045076 3 6.2216955 49.1048069 3 6.2216955 49.1048069 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2214836 49.1045076 3 6.2217634 49.104423 3 6.2219747 49.1047219 3 6.2216955 49.1048069 3 6.2214836 49.1045076 3</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a0c-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2207764 49.1046075 0 6.2209173 49.1048083 0 6.2210522 49.104767 0 6.2212141 49.1047174 0 6.2212222 49.1047149 0 6.2210805 49.1045141 0 6.2207764 49.1046075 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2207764 49.1046075 0 6.2210805 49.1045141 0 6.2210805 49.1045141 3 6.2207764 49.1046075 3 6.2207764 49.1046075 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2210805 49.1045141 0 6.2212222 49.1047149 0 6.2212222 49.1047149 3 6.2210805 49.1045141 3 6.2210805 49.1045141 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2212222 49.1047149 0 6.2212141 49.1047174 0 6.2212141 49.1047174 3 6.2212222 49.1047149 3 6.2212222 49.1047149 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2212141 49.1047174 0 6.2210522 49.104767 0 6.2210522 49.104767 3 6.2212141 49.1047174 3 6.2212141 49.1047174 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2210522 49.104767 0 6.2209173 49.1048083 0 6.2209173 49.1048083 3 6.2210522 49.104767 3 6.2210522 49.104767 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2209173 49.1048083 0 6.2207764 49.1046075 0 6.2207764 49.1046075 3 6.2209173 49.1048083 3 6.2209173 49.1048083 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2207764 49.1046075 3 6.2210805 49.1045141 3 6.2212222 49.1047149 3 6.2212141 49.1047174 3 6.2210522 49.104767 3 6.2209173 49.1048083 3 6.2207764 49.1046075 3</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a0d-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2225981 49.1041667 0 6.22274 49.1043668 0 6.2230445 49.1042732 0 6.2229039 49.1040722 0 6.2225981 49.1041667 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2225981 49.1041667 0 6.2229039 49.1040722 0 6.2229039 49.1040722 3 6.2225981 49.1041667 3 6.2225981 49.1041667 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2229039 49.1040722 0 6.2230445 49.1042732 0 6.2230445 49.1042732 3 6.2229039 49.1040722 3 6.2229039 49.1040722 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2230445 49.1042732 0 6.22274 49.1043668 0 6.22274 49.1043668 3 6.2230445 49.1042732 3 6.2230445 49.1042732 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.22274 49.1043668 0 6.2225981 49.1041667 0 6.2225981 49.1041667 3 6.22274 49.1043668 3 6.22274 49.1043668 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2225981 49.1041667 3 6.2229039 49.1040722 3 6.2230445 49.1042732 3 6.22274 49.1043668 3 6.2225981 49.1041667 3</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a0e-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2219852 49.1043542 0 6.2221962 49.1046534 0 6.2224754 49.104567 0 6.2222649 49.1042681 0 6.2219852 49.1043542 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2219852 49.1043542 0 6.2222649 49.1042681 0 6.2222649 49.1042681 3 6.2219852 49.1043542 3 6.2219852 49.1043542 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2222649 49.1042681 0 6.2224754 49.104567 0 6.2224754 49.104567 3 6.2222649 49.1042681 3 6.2222649 49.1042681 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2224754 49.104567 0 6.2221962 49.1046534 0 6.2221962 49.1046534 3 6.2224754 49.104567 3 6.2224754 49.104567 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2221962 49.1046534 0 6.2219852 49.1043542 0 6.2219852 49.1043542 3 6.2221962 49.1046534 3 6.2221962 49.1046534 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2219852 49.1043542 3 6.2222649 49.1042681 3 6.2224754 49.104567 3 6.2221962 49.1046534 3 6.2219852 49.1043542 3</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a0f-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195662 49.1048992 0 6.219566 49.1049202 0 6.219566 49.1049228 0 6.2195637 49.105019 0 6.2197464 49.1050854 0 6.219858 49.1050513 0 6.2198665 49.1050211 0 6.2198836 49.1049605 0 6.2199091 49.1048703 0 6.2199147 49.1048504 0 6.2199396 49.1048532 0 6.2199611 49.1047777 0 6.2195662 49.1048992 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195662 49.1048992 0 6.2199611 49.1047777 0 6.2199611 49.1047777 6 6.2195662 49.1048992 6 6.2195662 49.1048992 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199611 49.1047777 0 6.2199396 49.1048532 0 6.2199396 49.1048532 6 6.2199611 49.1047777 6 6.2199611 49.1047777 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199396 49.1048532 0 6.2199147 49.1048504 0 6.2199147 49.1048504 6 6.2199396 49.1048532 6 6.2199396 49.1048532 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199147 49.1048504 0 6.2199091 49.1048703 0 6.2199091 49.1048703 6 6.2199147 49.1048504 6 6.2199147 49.1048504 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199091 49.1048703 0 6.2198836 49.1049605 0 6.2198836 49.1049605 6 6.2199091 49.1048703 6 6.2199091 49.1048703 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2198836 49.1049605 0 6.2198665 49.1050211 0 6.2198665 49.1050211 6 6.2198836 49.1049605 6 6.2198836 49.1049605 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2198665 49.1050211 0 6.219858 49.1050513 0 6.219858 49.1050513 6 6.2198665 49.1050211 6 6.2198665 49.1050211 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.219858 49.1050513 0 6.2197464 49.1050854 0 6.2197464 49.1050854 6 6.219858 49.1050513 6 6.219858 49.1050513 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2197464 49.1050854 0 6.2195637 49.105019 0 6.2195637 49.105019 6 6.2197464 49.1050854 6 6.2197464 49.1050854 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195637 49.105019 0 6.219566 49.1049228 0 6.219566 49.1049228 6 6.2195637 49.105019 6 6.2195637 49.105019 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.219566 49.1049228 0 6.219566 49.1049202 0 6.219566 49.1049202 6 6.219566 49.1049228 6 6.219566 49.1049228 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.219566 49.1049202 0 6.2195662 49.1048992 0 6.2195662 49.1048992 6 6.219566 49.1049202 6 6.219566 49.1049202 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195662 49.1048992 6 6.2199611 49.1047777 6 6.2199396 49.1048532 6 6.2199147 49.1048504 6 6.2199091 49.1048703 6 6.2198836 49.1049605 6 6.2198665 49.1050211 6 6.219858 49.1050513 6 6.2197464 49.1050854 6 6.2195637 49.105019 6 6.219566 49.1049228 6 6.219566 49.1049202 6 6.2195662 49.1048992 6</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a10-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2232935 49.1041409 0 6.2235095 49.1042197 0 6.2235854 49.1041316 0 6.2233666 49.1040528 0 6.2233488 49.1040294 0 6.223397 49.1038662 0 6.2232441 49.1038463 0 6.223196 49.1040087 0 6.2231702 49.1040394 0 6.2230341 49.1040813 0 6.223097 49.1041743 0 6.2232339 49.1041331 0 6.2232935 49.1041409 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2232935 49.1041409 0 6.2232339 49.1041331 0 6.2232339 49.1041331 6 6.2232935 49.1041409 6 6.2232935 49.1041409 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2232339 49.1041331 0 6.223097 49.1041743 0 6.223097 49.1041743 6 6.2232339 49.1041331 6 6.2232339 49.1041331 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.223097 49.1041743 0 6.2230341 49.1040813 0 6.2230341 49.1040813 6 6.223097 49.1041743 6 6.223097 49.1041743 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2230341 49.1040813 0 6.2231702 49.1040394 0 6.2231702 49.1040394 6 6.2230341 49.1040813 6 6.2230341 49.1040813 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2231702 49.1040394 0 6.223196 49.1040087 0 6.223196 49.1040087 6 6.2231702 49.1040394 6 6.2231702 49.1040394 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.223196 49.1040087 0 6.2232441 49.1038463 0 6.2232441 49.1038463 6 6.223196 49.1040087 6 6.223196 49.1040087 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2232441 49.1038463 0 6.223397 49.1038662 0 6.223397 49.1038662 6 6.2232441 49.1038463 6 6.2232441 49.1038463 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.223397 49.1038662 0 6.2233488 49.1040294 0 6.2233488 49.1040294 6 6.223397 49.1038662 6 6.223397 49.1038662 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2233488 49.1040294 0 6.2233666 49.1040528 0 6.2233666 49.1040528 6 6.2233488 49.1040294 6 6.2233488 49.1040294 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2233666 49.1040528 0 6.2235854 49.1041316 0 6.2235854 49.1041316 6 6.2233666 49.1040528 6 6.2233666 49.1040528 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2235854 49.1041316 0 6.2235095 49.1042197 0 6.2235095 49.1042197 6 6.2235854 49.1041316 6 6.2235854 49.1041316 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2235095 49.1042197 0 6.2232935 49.1041409 0 6.2232935 49.1041409 6 6.2235095 49.1042197 6 6.2235095 49.1042197 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2232935 49.1041409 6 6.2232339 49.1041331 6 6.223097 49.1041743 6 6.2230341 49.1040813 6 6.2231702 49.1040394 6 6.223196 49.1040087 6 6.2232441 49.1038463 6 6.223397 49.1038662 6 6.2233488 49.1040294 6 6.2233666 49.1040528 6 6.2235854 49.1041316 6 6.2235095 49.1042197 6 6.2232935 49.1041409 6</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a11-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2233227 49.1028624 0 6.2233844 49.1029493 0 6.2234713 49.1029235 0 6.2234932 49.1029545 0 6.223561 49.1029328 0 6.2236015 49.1029198 0 6.2235822 49.1028924 0 6.2236556 49.1028698 0 6.2236186 49.1028165 0 6.2235931 49.1028247 0 6.2235726 49.1027949 0 6.2234182 49.1028435 0 6.2234122 49.1028353 0 6.2233227 49.1028624 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2233227 49.1028624 0 6.2234122 49.1028353 0 6.2234122 49.1028353 6 6.2233227 49.1028624 6 6.2233227 49.1028624 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2234122 49.1028353 0 6.2234182 49.1028435 0 6.2234182 49.1028435 6 6.2234122 49.1028353 6 6.2234122 49.1028353 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2234182 49.1028435 0 6.2235726 49.1027949 0 6.2235726 49.1027949 6 6.2234182 49.1028435 6 6.2234182 49.1028435 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2235726 49.1027949 0 6.2235931 49.1028247 0 6.2235931 49.1028247 6 6.2235726 49.1027949 6 6.2235726 49.1027949 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2235931 49.1028247 0 6.2236186 49.1028165 0 6.2236186 49.1028165 6 6.2235931 49.1028247 6 6.2235931 49.1028247 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2236186 49.1028165 0 6.2236556 49.1028698 0 6.2236556 49.1028698 6 6.2236186 49.1028165 6 6.2236186 49.1028165 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2236556 49.1028698 0 6.2235822 49.1028924 0 6.2235822 49.1028924 6 6.2236556 49.1028698 6 6.2236556 49.1028698 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2235822 49.1028924 0 6.2236015 49.1029198 0 6.2236015 49.1029198 6 6.2235822 49.1028924 6 6.2235822 49.1028924 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2236015 49.1029198 0 6.223561 49.1029328 0 6.223561 49.1029328 6 6.2236015 49.1029198 6 6.2236015 49.1029198 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.223561 49.1029328 0 6.2234932 49.1029545 0 6.2234932 49.1029545 6 6.223561 49.1029328 6 6.223561 49.1029328 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2234932 49.1029545 0 6.2234713 49.1029235 0 6.2234713 49.1029235 6 6.2234932 49.1029545 6 6.2234932 49.1029545 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2234713 49.1029235 0 6.2233844 49.1029493 0 6.2233844 49.1029493 6 6.2234713 49.1029235 6 6.2234713 49.1029235 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2233844 49.1029493 0 6.2233227 49.1028624 0 6.2233227 49.1028624 6 6.2233844 49.1029493 6 6.2233844 49.1029493 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2233227 49.1028624 6 6.2234122 49.1028353 6 6.2234182 49.1028435 6 6.2235726 49.1027949 6 6.2235931 49.1028247 6 6.2236186 49.1028165 6 6.2236556 49.1028698 6 6.2235822 49.1028924 6 6.2236015 49.1029198 6 6.223561 49.1029328 6 6.2234932 49.1029545 6 6.2234713 49.1029235 6 6.2233844 49.1029493 6 6.2233227 49.1028624 6</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a12-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.223324 49.1035286 0 6.2234352 49.1034943 0 6.2234272 49.1034838 0 6.2235374 49.1034502 0 6.2235438 49.1034611 0 6.2237524 49.1033971 0 6.2237351 49.1033723 0 6.2238291 49.1033431 0 6.2238425 49.1032877 0 6.2237789 49.1032806 0 6.2237699 49.1033202 0 6.2235067 49.1034016 0 6.223501 49.1033912 0 6.2233833 49.1034268 0 6.2233919 49.1034368 0 6.2233147 49.1034605 0 6.2231414 49.1034183 0 6.2230618 49.1035592 0 6.2231078 49.1035703 0 6.2231545 49.1034876 0 6.223324 49.1035286 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.223324 49.1035286 0 6.2231545 49.1034876 0 6.2231545 49.1034876 3 6.223324 49.1035286 3 6.223324 49.1035286 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2231545 49.1034876 0 6.2231078 49.1035703 0 6.2231078 49.1035703 3 6.2231545 49.1034876 3 6.2231545 49.1034876 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2231078 49.1035703 0 6.2230618 49.1035592 0 6.2230618 49.1035592 3 6.2231078 49.1035703 3 6.2231078 49.1035703 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2230618 49.1035592 0 6.2231414 49.1034183 0 6.2231414 49.1034183 3 6.2230618 49.1035592 3 6.2230618 49.1035592 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2231414 49.1034183 0 6.2233147 49.1034605 0 6.2233147 49.1034605 3 6.2231414 49.1034183 3 6.2231414 49.1034183 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2233147 49.1034605 0 6.2233919 49.1034368 0 6.2233919 49.1034368 3 6.2233147 49.1034605 3 6.2233147 49.1034605 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2233919 49.1034368 0 6.2233833 49.1034268 0 6.2233833 49.1034268 3 6.2233919 49.1034368 3 6.2233919 49.1034368 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2233833 49.1034268 0 6.223501 49.1033912 0 6.223501 49.1033912 3 6.2233833 49.1034268 3 6.2233833 49.1034268 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.223501 49.1033912 0 6.2235067 49.1034016 0 6.2235067 49.1034016 3 6.223501 49.1033912 3 6.223501 49.1033912 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2235067 49.1034016 0 6.2237699 49.1033202 0 6.2237699 49.1033202 3 6.2235067 49.1034016 3 6.2235067 49.1034016 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2237699 49.1033202 0 6.2237789 49.1032806 0 6.2237789 49.1032806 3 6.2237699 49.1033202 3 6.2237699 49.1033202 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2237789 49.1032806 0 6.2238425 49.1032877 0 6.2238425 49.1032877 3 6.2237789 49.1032806 3 6.2237789 49.1032806 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2238425 49.1032877 0 6.2238291 49.1033431 0 6.2238291 49.1033431 3 6.2238425 49.1032877 3 6.2238425 49.1032877 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2238291 49.1033431 0 6.2237351 49.1033723 0 6.2237351 49.1033723 3 6.2238291 49.1033431 3 6.2238291 49.1033431 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2237351 49.1033723 0 6.2237524 49.1033971 0 6.2237524 49.1033971 3 6.2237351 49.1033723 3 6.2237351 49.1033723 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2237524 49.1033971 0 6.2235438 49.1034611 0 6.2235438 49.1034611 3 6.2237524 49.1033971 3 6.2237524 49.1033971 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2235438 49.1034611 0 6.2235374 49.1034502 0 6.2235374 49.1034502 3 6.2235438 49.1034611 3 6.2235438 49.1034611 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2235374 49.1034502 0 6.2234272 49.1034838 0 6.2234272 49.1034838 3 6.2235374 49.1034502 3 6.2235374 49.1034502 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2234272 49.1034838 0 6.2234352 49.1034943 0 6.2234352 49.1034943 3 6.2234272 49.1034838 3 6.2234272 49.1034838 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2234352 49.1034943 0 6.223324 49.1035286 0 6.223324 49.1035286 3 6.2234352 49.1034943 3 6.2234352 49.1034943 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.223324 49.1035286 3 6.2231545 49.1034876 3 6.2231078 49.1035703 3 6.2230618 49.1035592 3 6.2231414 49.1034183 3 6.2233147 49.1034605 3 6.2233919 49.1034368 3 6.2233833 49.1034268 3 6.223501 49.1033912 3 6.2235067 49.1034016 3 6.2237699 49.1033202 3 6.2237789 49.1032806 3 6.2238425 49.1032877 3 6.2238291 49.1033431 3 6.2237351 49.1033723 3 6.2237524 49.1033971 3 6.2235438 49.1034611 3 6.2235374 49.1034502 3 6.2234272 49.1034838 3 6.2234352 49.1034943 3 6.223324 49.1035286 3</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a13-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2226966 49.1028254 0 6.2227571 49.1029126 0 6.2228446 49.1028859 0 6.222866 49.1029167 0 6.2229407 49.1028938 0 6.2229749 49.1028833 0 6.2229552 49.1028556 0 6.2230302 49.1028331 0 6.2229916 49.1027796 0 6.2229669 49.1027873 0 6.2229434 49.1027556 0 6.2228302 49.1027909 0 6.2228533 49.1028227 0 6.2228118 49.1028351 0 6.222786 49.1027986 0 6.2226966 49.1028254 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2226966 49.1028254 0 6.222786 49.1027986 0 6.222786 49.1027986 6 6.2226966 49.1028254 6 6.2226966 49.1028254 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.222786 49.1027986 0 6.2228118 49.1028351 0 6.2228118 49.1028351 6 6.222786 49.1027986 6 6.222786 49.1027986 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2228118 49.1028351 0 6.2228533 49.1028227 0 6.2228533 49.1028227 6 6.2228118 49.1028351 6 6.2228118 49.1028351 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2228533 49.1028227 0 6.2228302 49.1027909 0 6.2228302 49.1027909 6 6.2228533 49.1028227 6 6.2228533 49.1028227 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2228302 49.1027909 0 6.2229434 49.1027556 0 6.2229434 49.1027556 6 6.2228302 49.1027909 6 6.2228302 49.1027909 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2229434 49.1027556 0 6.2229669 49.1027873 0 6.2229669 49.1027873 6 6.2229434 49.1027556 6 6.2229434 49.1027556 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2229669 49.1027873 0 6.2229916 49.1027796 0 6.2229916 49.1027796 6 6.2229669 49.1027873 6 6.2229669 49.1027873 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2229916 49.1027796 0 6.2230302 49.1028331 0 6.2230302 49.1028331 6 6.2229916 49.1027796 6 6.2229916 49.1027796 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2230302 49.1028331 0 6.2229552 49.1028556 0 6.2229552 49.1028556 6 6.2230302 49.1028331 6 6.2230302 49.1028331 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2229552 49.1028556 0 6.2229749 49.1028833 0 6.2229749 49.1028833 6 6.2229552 49.1028556 6 6.2229552 49.1028556 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2229749 49.1028833 0 6.2229407 49.1028938 0 6.2229407 49.1028938 6 6.2229749 49.1028833 6 6.2229749 49.1028833 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2229407 49.1028938 0 6.222866 49.1029167 0 6.222866 49.1029167 6 6.2229407 49.1028938 6 6.2229407 49.1028938 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.222866 49.1029167 0 6.2228446 49.1028859 0 6.2228446 49.1028859 6 6.222866 49.1029167 6 6.222866 49.1029167 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2228446 49.1028859 0 6.2227571 49.1029126 0 6.2227571 49.1029126 6 6.2228446 49.1028859 6 6.2228446 49.1028859 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2227571 49.1029126 0 6.2226966 49.1028254 0 6.2226966 49.1028254 6 6.2227571 49.1029126 6 6.2227571 49.1029126 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2226966 49.1028254 6 6.222786 49.1027986 6 6.2228118 49.1028351 6 6.2228533 49.1028227 6 6.2228302 49.1027909 6 6.2229434 49.1027556 6 6.2229669 49.1027873 6 6.2229916 49.1027796 6 6.2230302 49.1028331 6 6.2229552 49.1028556 6 6.2229749 49.1028833 6 6.2229407 49.1028938 6 6.222866 49.1029167 6 6.2228446 49.1028859 6 6.2227571 49.1029126 6 6.2226966 49.1028254 6</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a14-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2230188 49.1036349 0 6.2230005 49.1036657 0 6.2230469 49.103677 0 6.2230644 49.1036466 0 6.2230188 49.1036349 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2230188 49.1036349 0 6.2230644 49.1036466 0 6.2230644 49.1036466 3 6.2230188 49.1036349 3 6.2230188 49.1036349 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2230644 49.1036466 0 6.2230469 49.103677 0 6.2230469 49.103677 3 6.2230644 49.1036466 3 6.2230644 49.1036466 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2230469 49.103677 0 6.2230005 49.1036657 0 6.2230005 49.1036657 3 6.2230469 49.103677 3 6.2230469 49.103677 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2230005 49.1036657 0 6.2230188 49.1036349 0 6.2230188 49.1036349 3 6.2230005 49.1036657 3 6.2230005 49.1036657 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2230188 49.1036349 3 6.2230644 49.1036466 3 6.2230469 49.103677 3 6.2230005 49.1036657 3 6.2230188 49.1036349 3</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a15-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191075 49.1032273 0 6.2189088 49.1032454 0 6.2189217 49.1033063 0 6.2189085 49.1033075 0 6.2189348 49.1034317 0 6.2189721 49.1034283 0 6.2189814 49.1034725 0 6.2189581 49.1034747 0 6.2189712 49.1035364 0 6.2189577 49.1035377 0 6.2189837 49.1036603 0 6.219021 49.1036568 0 6.2190305 49.1037017 0 6.2190069 49.1037038 0 6.2190197 49.1037644 0 6.2190073 49.1037655 0 6.2190334 49.1038888 0 6.2192439 49.1038695 0 6.2192049 49.1036858 0 6.2192611 49.1036806 0 6.2192783 49.1036346 0 6.2191955 49.1036414 0 6.2191562 49.1034558 0 6.2191453 49.1034568 0 6.2191359 49.1034128 0 6.2191466 49.1034119 0 6.2191075 49.1032273 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191075 49.1032273 0 6.2191466 49.1034119 0 6.2191466 49.1034119 9 6.2191075 49.1032273 9 6.2191075 49.1032273 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191466 49.1034119 0 6.2191359 49.1034128 0 6.2191359 49.1034128 9 6.2191466 49.1034119 9 6.2191466 49.1034119 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191359 49.1034128 0 6.2191453 49.1034568 0 6.2191453 49.1034568 9 6.2191359 49.1034128 9 6.2191359 49.1034128 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191453 49.1034568 0 6.2191562 49.1034558 0 6.2191562 49.1034558 9 6.2191453 49.1034568 9 6.2191453 49.1034568 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191562 49.1034558 0 6.2191955 49.1036414 0 6.2191955 49.1036414 9 6.2191562 49.1034558 9 6.2191562 49.1034558 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191955 49.1036414 0 6.2192783 49.1036346 0 6.2192783 49.1036346 9 6.2191955 49.1036414 9 6.2191955 49.1036414 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192783 49.1036346 0 6.2192611 49.1036806 0 6.2192611 49.1036806 9 6.2192783 49.1036346 9 6.2192783 49.1036346 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192611 49.1036806 0 6.2192049 49.1036858 0 6.2192049 49.1036858 9 6.2192611 49.1036806 9 6.2192611 49.1036806 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192049 49.1036858 0 6.2192439 49.1038695 0 6.2192439 49.1038695 9 6.2192049 49.1036858 9 6.2192049 49.1036858 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192439 49.1038695 0 6.2190334 49.1038888 0 6.2190334 49.1038888 9 6.2192439 49.1038695 9 6.2192439 49.1038695 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2190334 49.1038888 0 6.2190073 49.1037655 0 6.2190073 49.1037655 9 6.2190334 49.1038888 9 6.2190334 49.1038888 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2190073 49.1037655 0 6.2190197 49.1037644 0 6.2190197 49.1037644 9 6.2190073 49.1037655 9 6.2190073 49.1037655 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2190197 49.1037644 0 6.2190069 49.1037038 0 6.2190069 49.1037038 9 6.2190197 49.1037644 9 6.2190197 49.1037644 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2190069 49.1037038 0 6.2190305 49.1037017 0 6.2190305 49.1037017 9 6.2190069 49.1037038 9 6.2190069 49.1037038 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2190305 49.1037017 0 6.219021 49.1036568 0 6.219021 49.1036568 9 6.2190305 49.1037017 9 6.2190305 49.1037017 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.219021 49.1036568 0 6.2189837 49.1036603 0 6.2189837 49.1036603 9 6.219021 49.1036568 9 6.219021 49.1036568 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2189837 49.1036603 0 6.2189577 49.1035377 0 6.2189577 49.1035377 9 6.2189837 49.1036603 9 6.2189837 49.1036603 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2189577 49.1035377 0 6.2189712 49.1035364 0 6.2189712 49.1035364 9 6.2189577 49.1035377 9 6.2189577 49.1035377 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2189712 49.1035364 0 6.2189581 49.1034747 0 6.2189581 49.1034747 9 6.2189712 49.1035364 9 6.2189712 49.1035364 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2189581 49.1034747 0 6.2189814 49.1034725 0 6.2189814 49.1034725 9 6.2189581 49.1034747 9 6.2189581 49.1034747 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2189814 49.1034725 0 6.2189721 49.1034283 0 6.2189721 49.1034283 9 6.2189814 49.1034725 9 6.2189814 49.1034725 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2189721 49.1034283 0 6.2189348 49.1034317 0 6.2189348 49.1034317 9 6.2189721 49.1034283 9 6.2189721 49.1034283 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2189348 49.1034317 0 6.2189085 49.1033075 0 6.2189085 49.1033075 9 6.2189348 49.1034317 9 6.2189348 49.1034317 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2189085 49.1033075 0 6.2189217 49.1033063 0 6.2189217 49.1033063 9 6.2189085 49.1033075 9 6.2189085 49.1033075 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2189217 49.1033063 0 6.2189088 49.1032454 0 6.2189088 49.1032454 9 6.2189217 49.1033063 9 6.2189217 49.1033063 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2189088 49.1032454 0 6.2191075 49.1032273 0 6.2191075 49.1032273 9 6.2189088 49.1032454 9 6.2189088 49.1032454 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191075 49.1032273 9 6.2191466 49.1034119 9 6.2191359 49.1034128 9 6.2191453 49.1034568 9 6.2191562 49.1034558 9 6.2191955 49.1036414 9 6.2192783 49.1036346 9 6.2192611 49.1036806 9 6.2192049 49.1036858 9 6.2192439 49.1038695 9 6.2190334 49.1038888 9 6.2190073 49.1037655 9 6.2190197 49.1037644 9 6.2190069 49.1037038 9 6.2190305 49.1037017 9 6.219021 49.1036568 9 6.2189837 49.1036603 9 6.2189577 49.1035377 9 6.2189712 49.1035364 9 6.2189581 49.1034747 9 6.2189814 49.1034725 9 6.2189721 49.1034283 9 6.2189348 49.1034317 9 6.2189085 49.1033075 9 6.2189217 49.1033063 9 6.2189088 49.1032454 9 6.2191075 49.1032273 9</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a16-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2216058 49.1028772 0 6.2213667 49.1028187 0 6.2212245 49.1030729 0 6.2211558 49.1030569 0 6.2213253 49.1027527 0 6.2210093 49.1026767 0 6.2208953 49.1028786 0 6.2207638 49.1028474 0 6.2205611 49.1032162 0 6.2206148 49.1032286 0 6.2205505 49.1033451 0 6.2205863 49.1033929 0 6.2206646 49.1034106 0 6.2205832 49.1035591 0 6.2204702 49.1035323 0 6.2205169 49.1034482 0 6.2205281 49.1034281 0 6.2203636 49.1033891 0 6.2202208 49.1036491 0 6.2203845 49.1036881 0 6.2204419 49.1035838 0 6.2205546 49.1036105 0 6.2204689 49.1037652 0 6.2207106 49.1038225 0 6.2208787 49.103518 0 6.2209768 49.1035411 0 6.2211443 49.103237 0 6.2212146 49.1032535 0 6.2211011 49.1034574 0 6.221418 49.1035326 0 6.2215338 49.1033241 0 6.2212998 49.1032681 0 6.2213849 49.1031158 0 6.2216179 49.1031716 0 6.2216742 49.1030693 0 6.2215217 49.1030297 0 6.2216058 49.1028772 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2216058 49.1028772 0 6.2215217 49.1030297 0 6.2215217 49.1030297 12 6.2216058 49.1028772 12 6.2216058 49.1028772 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2215217 49.1030297 0 6.2216742 49.1030693 0 6.2216742 49.1030693 12 6.2215217 49.1030297 12 6.2215217 49.1030297 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2216742 49.1030693 0 6.2216179 49.1031716 0 6.2216179 49.1031716 12 6.2216742 49.1030693 12 6.2216742 49.1030693 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2216179 49.1031716 0 6.2213849 49.1031158 0 6.2213849 49.1031158 12 6.2216179 49.1031716 12 6.2216179 49.1031716 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2213849 49.1031158 0 6.2212998 49.1032681 0 6.2212998 49.1032681 12 6.2213849 49.1031158 12 6.2213849 49.1031158 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2212998 49.1032681 0 6.2215338 49.1033241 0 6.2215338 49.1033241 12 6.2212998 49.1032681 12 6.2212998 49.1032681 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2215338 49.1033241 0 6.221418 49.1035326 0 6.221418 49.1035326 12 6.2215338 49.1033241 12 6.2215338 49.1033241 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.221418 49.1035326 0 6.2211011 49.1034574 0 6.2211011 49.1034574 12 6.221418 49.1035326 12 6.221418 49.1035326 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2211011 49.1034574 0 6.2212146 49.1032535 0 6.2212146 49.1032535 12 6.2211011 49.1034574 12 6.2211011 49.1034574 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2212146 49.1032535 0 6.2211443 49.103237 0 6.2211443 49.103237 12 6.2212146 49.1032535 12 6.2212146 49.1032535 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2211443 49.103237 0 6.2209768 49.1035411 0 6.2209768 49.1035411 12 6.2211443 49.103237 12 6.2211443 49.103237 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2209768 49.1035411 0 6.2208787 49.103518 0 6.2208787 49.103518 12 6.2209768 49.1035411 12 6.2209768 49.1035411 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2208787 49.103518 0 6.2207106 49.1038225 0 6.2207106 49.1038225 12 6.2208787 49.103518 12 6.2208787 49.103518 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2207106 49.1038225 0 6.2204689 49.1037652 0 6.2204689 49.1037652 12 6.2207106 49.1038225 12 6.2207106 49.1038225 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2204689 49.1037652 0 6.2205546 49.1036105 0 6.2205546 49.1036105 12 6.2204689 49.1037652 12 6.2204689 49.1037652 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2205546 49.1036105 0 6.2204419 49.1035838 0 6.2204419 49.1035838 12 6.2205546 49.1036105 12 6.2205546 49.1036105 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2204419 49.1035838 0 6.2203845 49.1036881 0 6.2203845 49.1036881 12 6.2204419 49.1035838 12 6.2204419 49.1035838 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2203845 49.1036881 0 6.2202208 49.1036491 0 6.2202208 49.1036491 12 6.2203845 49.1036881 12 6.2203845 49.1036881 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202208 49.1036491 0 6.2203636 49.1033891 0 6.2203636 49.1033891 12 6.2202208 49.1036491 12 6.2202208 49.1036491 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2203636 49.1033891 0 6.2205281 49.1034281 0 6.2205281 49.1034281 12 6.2203636 49.1033891 12 6.2203636 49.1033891 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2205281 49.1034281 0 6.2205169 49.1034482 0 6.2205169 49.1034482 12 6.2205281 49.1034281 12 6.2205281 49.1034281 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2205169 49.1034482 0 6.2204702 49.1035323 0 6.2204702 49.1035323 12 6.2205169 49.1034482 12 6.2205169 49.1034482 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2204702 49.1035323 0 6.2205832 49.1035591 0 6.2205832 49.1035591 12 6.2204702 49.1035323 12 6.2204702 49.1035323 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2205832 49.1035591 0 6.2206646 49.1034106 0 6.2206646 49.1034106 12 6.2205832 49.1035591 12 6.2205832 49.1035591 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2206646 49.1034106 0 6.2205863 49.1033929 0 6.2205863 49.1033929 12 6.2206646 49.1034106 12 6.2206646 49.1034106 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2205863 49.1033929 0 6.2205505 49.1033451 0 6.2205505 49.1033451 12 6.2205863 49.1033929 12 6.2205863 49.1033929 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2205505 49.1033451 0 6.2206148 49.1032286 0 6.2206148 49.1032286 12 6.2205505 49.1033451 12 6.2205505 49.1033451 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2206148 49.1032286 0 6.2205611 49.1032162 0 6.2205611 49.1032162 12 6.2206148 49.1032286 12 6.2206148 49.1032286 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2205611 49.1032162 0 6.2207638 49.1028474 0 6.2207638 49.1028474 12 6.2205611 49.1032162 12 6.2205611 49.1032162 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2207638 49.1028474 0 6.2208953 49.1028786 0 6.2208953 49.1028786 12 6.2207638 49.1028474 12 6.2207638 49.1028474 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2208953 49.1028786 0 6.2210093 49.1026767 0 6.2210093 49.1026767 12 6.2208953 49.1028786 12 6.2208953 49.1028786 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2210093 49.1026767 0 6.2213253 49.1027527 0 6.2213253 49.1027527 12 6.2210093 49.1026767 12 6.2210093 49.1026767 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2213253 49.1027527 0 6.2211558 49.1030569 0 6.2211558 49.1030569 12 6.2213253 49.1027527 12 6.2213253 49.1027527 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2211558 49.1030569 0 6.2212245 49.1030729 0 6.2212245 49.1030729 12 6.2211558 49.1030569 12 6.2211558 49.1030569 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2212245 49.1030729 0 6.2213667 49.1028187 0 6.2213667 49.1028187 12 6.2212245 49.1030729 12 6.2212245 49.1030729 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2213667 49.1028187 0 6.2216058 49.1028772 0 6.2216058 49.1028772 12 6.2213667 49.1028187 12 6.2213667 49.1028187 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2216058 49.1028772 12 6.2215217 49.1030297 12 6.2216742 49.1030693 12 6.2216179 49.1031716 12 6.2213849 49.1031158 12 6.2212998 49.1032681 12 6.2215338 49.1033241 12 6.221418 49.1035326 12 6.2211011 49.1034574 12 6.2212146 49.1032535 12 6.2211443 49.103237 12 6.2209768 49.1035411 12 6.2208787 49.103518 12 6.2207106 49.1038225 12 6.2204689 49.1037652 12 6.2205546 49.1036105 12 6.2204419 49.1035838 12 6.2203845 49.1036881 12 6.2202208 49.1036491 12 6.2203636 49.1033891 12 6.2205281 49.1034281 12 6.2205169 49.1034482 12 6.2204702 49.1035323 12 6.2205832 49.1035591 12 6.2206646 49.1034106 12 6.2205863 49.1033929 12 6.2205505 49.1033451 12 6.2206148 49.1032286 12 6.2205611 49.1032162 12 6.2207638 49.1028474 12 6.2208953 49.1028786 12 6.2210093 49.1026767 12 6.2213253 49.1027527 12 6.2211558 49.1030569 12 6.2212245 49.1030729 12 6.2213667 49.1028187 12 6.2216058 49.1028772 12</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a17-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202631 49.1042199 0 6.2203056 49.1042791 0 6.2203124 49.104277 0 6.22031 49.1042737 0 6.2203767 49.104253 0 6.2203792 49.1042564 0 6.2203862 49.1042543 0 6.2203436 49.104195 0 6.2203369 49.1041971 0 6.2203391 49.1042002 0 6.2202722 49.1042209 0 6.2202699 49.1042178 0 6.2202631 49.1042199 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202631 49.1042199 0 6.2202699 49.1042178 0 6.2202699 49.1042178 3 6.2202631 49.1042199 3 6.2202631 49.1042199 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202699 49.1042178 0 6.2202722 49.1042209 0 6.2202722 49.1042209 3 6.2202699 49.1042178 3 6.2202699 49.1042178 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202722 49.1042209 0 6.2203391 49.1042002 0 6.2203391 49.1042002 3 6.2202722 49.1042209 3 6.2202722 49.1042209 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2203391 49.1042002 0 6.2203369 49.1041971 0 6.2203369 49.1041971 3 6.2203391 49.1042002 3 6.2203391 49.1042002 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2203369 49.1041971 0 6.2203436 49.104195 0 6.2203436 49.104195 3 6.2203369 49.1041971 3 6.2203369 49.1041971 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2203436 49.104195 0 6.2203862 49.1042543 0 6.2203862 49.1042543 3 6.2203436 49.104195 3 6.2203436 49.104195 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2203862 49.1042543 0 6.2203792 49.1042564 0 6.2203792 49.1042564 3 6.2203862 49.1042543 3 6.2203862 49.1042543 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2203792 49.1042564 0 6.2203767 49.104253 0 6.2203767 49.104253 3 6.2203792 49.1042564 3 6.2203792 49.1042564 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2203767 49.104253 0 6.22031 49.1042737 0 6.22031 49.1042737 3 6.2203767 49.104253 3 6.2203767 49.104253 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.22031 49.1042737 0 6.2203124 49.104277 0 6.2203124 49.104277 3 6.22031 49.1042737 3 6.22031 49.1042737 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2203124 49.104277 0 6.2203056 49.1042791 0 6.2203056 49.1042791 3 6.2203124 49.104277 3 6.2203124 49.104277 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2203056 49.1042791 0 6.2202631 49.1042199 0 6.2202631 49.1042199 3 6.2203056 49.1042791 3 6.2203056 49.1042791 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2202631 49.1042199 3 6.2202699 49.1042178 3 6.2202722 49.1042209 3 6.2203391 49.1042002 3 6.2203369 49.1041971 3 6.2203436 49.104195 3 6.2203862 49.1042543 3 6.2203792 49.1042564 3 6.2203767 49.104253 3 6.22031 49.1042737 3 6.2203124 49.104277 3 6.2203056 49.1042791 3 6.2202631 49.1042199 3</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a18-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191474 49.1046777 0 6.2194965 49.1048023 0 6.2195824 49.1047759 0 6.2200119 49.1046438 0 6.2199423 49.1045454 0 6.2196419 49.1046383 0 6.219617 49.1046461 0 6.2195027 49.1046813 0 6.2194329 49.1046556 0 6.2193684 49.1046328 0 6.2192969 49.1046073 0 6.2192813 49.1045257 0 6.2192778 49.1045078 0 6.2192356 49.1042932 0 6.2191159 49.1043035 0 6.2190726 49.1043072 0 6.2191363 49.1046228 0 6.2191474 49.1046777 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191474 49.1046777 0 6.2191363 49.1046228 0 6.2191363 49.1046228 12 6.2191474 49.1046777 12 6.2191474 49.1046777 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191363 49.1046228 0 6.2190726 49.1043072 0 6.2190726 49.1043072 12 6.2191363 49.1046228 12 6.2191363 49.1046228 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2190726 49.1043072 0 6.2191159 49.1043035 0 6.2191159 49.1043035 12 6.2190726 49.1043072 12 6.2190726 49.1043072 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191159 49.1043035 0 6.2192356 49.1042932 0 6.2192356 49.1042932 12 6.2191159 49.1043035 12 6.2191159 49.1043035 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192356 49.1042932 0 6.2192778 49.1045078 0 6.2192778 49.1045078 12 6.2192356 49.1042932 12 6.2192356 49.1042932 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192778 49.1045078 0 6.2192813 49.1045257 0 6.2192813 49.1045257 12 6.2192778 49.1045078 12 6.2192778 49.1045078 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192813 49.1045257 0 6.2192969 49.1046073 0 6.2192969 49.1046073 12 6.2192813 49.1045257 12 6.2192813 49.1045257 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192969 49.1046073 0 6.2193684 49.1046328 0 6.2193684 49.1046328 12 6.2192969 49.1046073 12 6.2192969 49.1046073 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2193684 49.1046328 0 6.2194329 49.1046556 0 6.2194329 49.1046556 12 6.2193684 49.1046328 12 6.2193684 49.1046328 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2194329 49.1046556 0 6.2195027 49.1046813 0 6.2195027 49.1046813 12 6.2194329 49.1046556 12 6.2194329 49.1046556 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195027 49.1046813 0 6.219617 49.1046461 0 6.219617 49.1046461 12 6.2195027 49.1046813 12 6.2195027 49.1046813 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.219617 49.1046461 0 6.2196419 49.1046383 0 6.2196419 49.1046383 12 6.219617 49.1046461 12 6.219617 49.1046461 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2196419 49.1046383 0 6.2199423 49.1045454 0 6.2199423 49.1045454 12 6.2196419 49.1046383 12 6.2196419 49.1046383 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2199423 49.1045454 0 6.2200119 49.1046438 0 6.2200119 49.1046438 12 6.2199423 49.1045454 12 6.2199423 49.1045454 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2200119 49.1046438 0 6.2195824 49.1047759 0 6.2195824 49.1047759 12 6.2200119 49.1046438 12 6.2200119 49.1046438 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195824 49.1047759 0 6.2194965 49.1048023 0 6.2194965 49.1048023 12 6.2195824 49.1047759 12 6.2195824 49.1047759 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2194965 49.1048023 0 6.2191474 49.1046777 0 6.2191474 49.1046777 12 6.2194965 49.1048023 12 6.2194965 49.1048023 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2191474 49.1046777 12 6.2191363 49.1046228 12 6.2190726 49.1043072 12 6.2191159 49.1043035 12 6.2192356 49.1042932 12 6.2192778 49.1045078 12 6.2192813 49.1045257 12 6.2192969 49.1046073 12 6.2193684 49.1046328 12 6.2194329 49.1046556 12 6.2195027 49.1046813 12 6.219617 49.1046461 12 6.2196419 49.1046383 12 6.2199423 49.1045454 12 6.2200119 49.1046438 12 6.2195824 49.1047759 12 6.2194965 49.1048023 12 6.2191474 49.1046777 12</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a19-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2210522 49.104767 0 6.2211222 49.1048641 0 6.2211343 49.1048825 0 6.2213609 49.1049659 0 6.2214991 49.1047982 0 6.2214334 49.1047692 0 6.2212731 49.1048169 0 6.2212426 49.1047741 0 6.2212536 49.1047708 0 6.2212141 49.1047174 0 6.2210522 49.104767 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2210522 49.104767 0 6.2212141 49.1047174 0 6.2212141 49.1047174 3 6.2210522 49.104767 3 6.2210522 49.104767 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2212141 49.1047174 0 6.2212536 49.1047708 0 6.2212536 49.1047708 3 6.2212141 49.1047174 3 6.2212141 49.1047174 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2212536 49.1047708 0 6.2212426 49.1047741 0 6.2212426 49.1047741 3 6.2212536 49.1047708 3 6.2212536 49.1047708 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2212426 49.1047741 0 6.2212731 49.1048169 0 6.2212731 49.1048169 3 6.2212426 49.1047741 3 6.2212426 49.1047741 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2212731 49.1048169 0 6.2214334 49.1047692 0 6.2214334 49.1047692 3 6.2212731 49.1048169 3 6.2212731 49.1048169 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2214334 49.1047692 0 6.2214991 49.1047982 0 6.2214991 49.1047982 3 6.2214334 49.1047692 3 6.2214334 49.1047692 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2214991 49.1047982 0 6.2213609 49.1049659 0 6.2213609 49.1049659 3 6.2214991 49.1047982 3 6.2214991 49.1047982 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2213609 49.1049659 0 6.2211343 49.1048825 0 6.2211343 49.1048825 3 6.2213609 49.1049659 3 6.2213609 49.1049659 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2211343 49.1048825 0 6.2211222 49.1048641 0 6.2211222 49.1048641 3 6.2211343 49.1048825 3 6.2211343 49.1048825 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2211222 49.1048641 0 6.2210522 49.104767 0 6.2210522 49.104767 3 6.2211222 49.1048641 3 6.2211222 49.1048641 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2210522 49.104767 3 6.2212141 49.1047174 3 6.2212536 49.1047708 3 6.2212426 49.1047741 3 6.2212731 49.1048169 3 6.2214334 49.1047692 3 6.2214991 49.1047982 3 6.2213609 49.1049659 3 6.2211343 49.1048825 3 6.2211222 49.1048641 3 6.2210522 49.104767 3</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<bldg:Building gml:id="BuildingID_fdec5a1a-a2ec-11e7-bfbb-7824afca2075">
			<bldg:lod1Solid>
				<gml:Solid srsName="epsg:4326" srsDimension="3">
					<gml:exterior>
						<gml:CompositeSurface>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2193684 49.1046328 0 6.2194329 49.1046556 0 6.2194658 49.1046152 0 6.2194528 49.1046101 0 6.2194646 49.104596 0 6.219507 49.1046112 0 6.219576 49.1045896 0 6.219617 49.1046461 0 6.2196419 49.1046383 0 6.2196007 49.1045819 0 6.2197647 49.1045319 0 6.2197224 49.1044729 0 6.2197966 49.10445 0 6.2197275 49.1043556 0 6.2197437 49.1043358 0 6.2196126 49.1042892 0 6.2195964 49.1043085 0 6.219437 49.1043215 0 6.2194377 49.1043264 0 6.2193381 49.1043341 0 6.2193717 49.1044999 0 6.2192778 49.1045078 0 6.2192813 49.1045257 0 6.2193751 49.104518 0 6.2193844 49.1045671 0 6.2194278 49.1045828 0 6.2194159 49.1045968 0 6.2194026 49.1045919 0 6.2193684 49.1046328 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2193684 49.1046328 0 6.2194026 49.1045919 0 6.2194026 49.1045919 3 6.2193684 49.1046328 3 6.2193684 49.1046328 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2194026 49.1045919 0 6.2194159 49.1045968 0 6.2194159 49.1045968 3 6.2194026 49.1045919 3 6.2194026 49.1045919 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2194159 49.1045968 0 6.2194278 49.1045828 0 6.2194278 49.1045828 3 6.2194159 49.1045968 3 6.2194159 49.1045968 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2194278 49.1045828 0 6.2193844 49.1045671 0 6.2193844 49.1045671 3 6.2194278 49.1045828 3 6.2194278 49.1045828 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2193844 49.1045671 0 6.2193751 49.104518 0 6.2193751 49.104518 3 6.2193844 49.1045671 3 6.2193844 49.1045671 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2193751 49.104518 0 6.2192813 49.1045257 0 6.2192813 49.1045257 3 6.2193751 49.104518 3 6.2193751 49.104518 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192813 49.1045257 0 6.2192778 49.1045078 0 6.2192778 49.1045078 3 6.2192813 49.1045257 3 6.2192813 49.1045257 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2192778 49.1045078 0 6.2193717 49.1044999 0 6.2193717 49.1044999 3 6.2192778 49.1045078 3 6.2192778 49.1045078 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2193717 49.1044999 0 6.2193381 49.1043341 0 6.2193381 49.1043341 3 6.2193717 49.1044999 3 6.2193717 49.1044999 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2193381 49.1043341 0 6.2194377 49.1043264 0 6.2194377 49.1043264 3 6.2193381 49.1043341 3 6.2193381 49.1043341 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2194377 49.1043264 0 6.219437 49.1043215 0 6.219437 49.1043215 3 6.2194377 49.1043264 3 6.2194377 49.1043264 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.219437 49.1043215 0 6.2195964 49.1043085 0 6.2195964 49.1043085 3 6.219437 49.1043215 3 6.219437 49.1043215 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2195964 49.1043085 0 6.2196126 49.1042892 0 6.2196126 49.1042892 3 6.2195964 49.1043085 3 6.2195964 49.1043085 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2196126 49.1042892 0 6.2197437 49.1043358 0 6.2197437 49.1043358 3 6.2196126 49.1042892 3 6.2196126 49.1042892 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2197437 49.1043358 0 6.2197275 49.1043556 0 6.2197275 49.1043556 3 6.2197437 49.1043358 3 6.2197437 49.1043358 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2197275 49.1043556 0 6.2197966 49.10445 0 6.2197966 49.10445 3 6.2197275 49.1043556 3 6.2197275 49.1043556 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2197966 49.10445 0 6.2197224 49.1044729 0 6.2197224 49.1044729 3 6.2197966 49.10445 3 6.2197966 49.10445 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2197224 49.1044729 0 6.2197647 49.1045319 0 6.2197647 49.1045319 3 6.2197224 49.1044729 3 6.2197224 49.1044729 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2197647 49.1045319 0 6.2196007 49.1045819 0 6.2196007 49.1045819 3 6.2197647 49.1045319 3 6.2197647 49.1045319 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2196007 49.1045819 0 6.2196419 49.1046383 0 6.2196419 49.1046383 3 6.2196007 49.1045819 3 6.2196007 49.1045819 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2196419 49.1046383 0 6.219617 49.1046461 0 6.219617 49.1046461 3 6.2196419 49.1046383 3 6.2196419 49.1046383 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.219617 49.1046461 0 6.219576 49.1045896 0 6.219576 49.1045896 3 6.219617 49.1046461 3 6.219617 49.1046461 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.219576 49.1045896 0 6.219507 49.1046112 0 6.219507 49.1046112 3 6.219576 49.1045896 3 6.219576 49.1045896 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.219507 49.1046112 0 6.2194646 49.104596 0 6.2194646 49.104596 3 6.219507 49.1046112 3 6.219507 49.1046112 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2194646 49.104596 0 6.2194528 49.1046101 0 6.2194528 49.1046101 3 6.2194646 49.104596 3 6.2194646 49.104596 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2194528 49.1046101 0 6.2194658 49.1046152 0 6.2194658 49.1046152 3 6.2194528 49.1046101 3 6.2194528 49.1046101 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2194658 49.1046152 0 6.2194329 49.1046556 0 6.2194329 49.1046556 3 6.2194658 49.1046152 3 6.2194658 49.1046152 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2194329 49.1046556 0 6.2193684 49.1046328 0 6.2193684 49.1046328 3 6.2194329 49.1046556 3 6.2194329 49.1046556 0</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
							<gml:surfaceMember>
								<gml:Polygon>
									<gml:exterior>
										<gml:LinearRing>
											<gml:posList>6.2193684 49.1046328 3 6.2194026 49.1045919 3 6.2194159 49.1045968 3 6.2194278 49.1045828 3 6.2193844 49.1045671 3 6.2193751 49.104518 3 6.2192813 49.1045257 3 6.2192778 49.1045078 3 6.2193717 49.1044999 3 6.2193381 49.1043341 3 6.2194377 49.1043264 3 6.219437 49.1043215 3 6.2195964 49.1043085 3 6.2196126 49.1042892 3 6.2197437 49.1043358 3 6.2197275 49.1043556 3 6.2197966 49.10445 3 6.2197224 49.1044729 3 6.2197647 49.1045319 3 6.2196007 49.1045819 3 6.2196419 49.1046383 3 6.219617 49.1046461 3 6.219576 49.1045896 3 6.219507 49.1046112 3 6.2194646 49.104596 3 6.2194528 49.1046101 3 6.2194658 49.1046152 3 6.2194329 49.1046556 3 6.2193684 49.1046328 3</gml:posList>
										</gml:LinearRing>
									</gml:exterior>
								</gml:Polygon>
							</gml:surfaceMember>
						</gml:CompositeSurface>
					</gml:exterior>
				</gml:Solid>
			</bldg:lod1Solid>
		</bldg:Building>
	</core:cityObjectMember>
</core:CityModel>