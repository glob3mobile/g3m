<?xml version="1.0" encoding="UTF-8"?>
<core:CityModel xmlns:app="http://www.opengis.net/citygml/appearance/2.0" xmlns:luse="http://www.opengis.net/citygml/landuse/2.0" xmlns:utility="http://www.citygml.org/ade/utility/0.9.2" xmlns:wtr="http://www.opengis.net/citygml/waterbody/2.0" xmlns:xAL="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0" xmlns:dem="http://www.opengis.net/citygml/relief/2.0" xmlns:gen="http://www.opengis.net/citygml/generics/2.0" xmlns:core="http://www.opengis.net/citygml/2.0" xmlns:smil20="http://www.w3.org/2001/SMIL20/" xmlns:grp="http://www.opengis.net/citygml/cityobjectgroup/2.0" xmlns:bldg="http://www.opengis.net/citygml/building/2.0" xmlns:tex="http://www.opengis.net/citygml/texturedsurface/2.0" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:tun="http://www.opengis.net/citygml/tunnel/2.0" xmlns:frn="http://www.opengis.net/citygml/cityfurniture/2.0" xmlns:tran="http://www.opengis.net/citygml/transportation/2.0" xmlns:gml="http://www.opengis.net/gml" xmlns:bridge="http://www.opengis.net/citygml/bridge/2.0" xmlns:pbase="http://www.opengis.net/citygml/profiles/base/2.0" xmlns:smil20lang="http://www.w3.org/2001/SMIL20/Language" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:veg="http://www.opengis.net/citygml/vegetation/2.0" xmlns:sch="http://www.ascc.net/xml/schematron" xsi:schemaLocation="http://www.citygml.org/ade/utility/0.9.2 CityGML_UtilityNetworkADE.xsd http://www.opengis.net/citygml/2.0 http://schemas.opengis.net/citygml/2.0/cityGMLBase.xsd">
	<gml:boundedBy>
		<gml:Envelope srsName="epsg:4326" srsDimension="3">
			<gml:lowerCorner>8.43723601434381 49.0191944412257 -3</gml:lowerCorner>
			<gml:upperCorner>8.44104037128994 49.0209664684547 0</gml:upperCorner>
		</gml:Envelope>
	</gml:boundedBy>
	<core:cityObjectMember>
		<utility:ExteriorMaterial gml:id="ExteriorMaterialID_9f6d356d-8207-4baa-840e-b5bff504d663">
			<utility:type>rubber</utility:type>
		</utility:ExteriorMaterial>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<utility:InteriorMaterial gml:id="InteriorMaterialID_51982b12-f5c7-4101-8ac8-4bc6e264c866">
			<utility:type>copper</utility:type>
		</utility:InteriorMaterial>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<utility:Network gml:id="a9b0ea76-4479-4d4c-92ed-d1ff86eabc1b">
			<utility:topoGraph>
				<utility:NetworkGraph gml:id="8a244d7f-985b-4c0f-943a-1544ce0ad8d5">
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_0381a840-84d8-4679-91b9-a82b1024148b">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_6df199dc-cd92-486c-b7f5-48e70a0e27a6">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43934680490424 49.020354397847 0 8.43927853887153 49.0202741829022 0 8.43927378527703 49.0202546993424 0 8.43928923445931 49.0201276663454 0 8.43942490645175 49.0200629247443 0 8.43943788537858 49.0200577220819 0 8.43947343624818 49.0200419383728 0 8.43952691418689 49.0199764732761 0 8.43950314621416 49.0199515341691 0 8.43947343624818 49.0199242570063 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_810c9020-2e13-4a8c-8bb5-62bec379b49f"/>
									<utility:end xlink:href="NodeID_9b7e7753-d589-4e95-a516-dedb4be8f077"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_810c9020-2e13-4a8c-8bb5-62bec379b49f">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43934680490424 49.020354397847 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_9b7e7753-d589-4e95-a516-dedb4be8f077">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43947343624818 49.0199242570063 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_5d02a950-25b4-4be9-8d4f-334314b9c68b">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_b1128f05-35be-421f-91de-712fa37e0242">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.4396155682183 49.0202447058197 0 8.43945323347134 49.0200723328527 0 8.43941639311358 49.0200372622973 0 8.43937372981148 49.019991442381 0 8.43932250962118 49.0199367265683 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_a18b76d3-a198-452a-9da0-f5bdf1fb1478"/>
									<utility:end xlink:href="NodeID_70b877b1-4ccd-4312-803f-767a5cb27a22"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_a18b76d3-a198-452a-9da0-f5bdf1fb1478">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.4396155682183 49.0202447058197 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_70b877b1-4ccd-4312-803f-767a5cb27a22">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43932250962118 49.0199367265683 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_9468fb23-fe48-4213-8500-ef61cf27124a">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_c148b30b-decc-43eb-926b-7e0798379b9a">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43987641241913 49.0201382456378 0 8.43980381106958 49.0200442764104 0 8.43978123149549 49.020027130799 0 8.43975152152951 49.020015440606 0 8.43970160878673 49.0200045297567 0 8.43967308721939 49.0199959569448 0 8.43959108771338 49.019977252623 0 8.43953760977467 49.0199616656828 0 8.4394948274237 49.0199180222241 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_551ea560-b221-4e07-bcc9-8c68fec32f76"/>
									<utility:end xlink:href="NodeID_612f0558-9e03-4d1f-b4f9-7df1a540010e"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_551ea560-b221-4e07-bcc9-8c68fec32f76">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43987641241913 49.0201382456378 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_612f0558-9e03-4d1f-b4f9-7df1a540010e">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.4394948274237 49.0199180222241 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_cbd0b97f-2932-48ac-864d-a7d32b65fab8">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_62b1f31d-62ca-4996-a7c6-252fed3052bd">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.44032547255719 49.0199549670254 0 8.44003369193117 49.0196464674784 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_b220ece3-ac37-49f2-91a9-211f9e862852"/>
									<utility:end xlink:href="NodeID_13683c8d-b127-4062-a0f7-af7c323b4545"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_b220ece3-ac37-49f2-91a9-211f9e862852">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.44032547255719 49.0199549670254 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_13683c8d-b127-4062-a0f7-af7c323b4545">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.44003369193117 49.0196464674784 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_40c3327c-c7ae-4adf-99a9-6936fe662602">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_e7966e80-e65c-4c60-818e-aeb1f439c0e2">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43960144951994 49.0202504681771 0 8.43952268051675 49.0201608371862 0 8.4393028267687 49.019940672015 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_2c572582-055b-473c-b04f-7518f8088174"/>
									<utility:end xlink:href="NodeID_b68095e1-e2d3-4c5c-bf8a-7ceb9b8516e5"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_2c572582-055b-473c-b04f-7518f8088174">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43960144951994 49.0202504681771 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_b68095e1-e2d3-4c5c-bf8a-7ceb9b8516e5">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.4393028267687 49.019940672015 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_6661bae1-6532-480c-a95d-451980a0fdb9">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_52fef973-6f1a-48d6-bcf1-b7b282893e32">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43729626434073 49.0204979797178 0 8.44004194277011 49.0193182716784 0 8.44023990704561 49.0192336025619 0 8.44069608559343 49.0198037051665 0 8.43784712145511 49.0209664684547 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_ecce4044-dcdd-4bf0-8f16-44ed89409476"/>
									<utility:end xlink:href="NodeID_fca3a505-890f-4886-9522-81746bb31a04"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_ecce4044-dcdd-4bf0-8f16-44ed89409476">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43729626434073 49.0204979797178 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fca3a505-890f-4886-9522-81746bb31a04">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43784712145511 49.0209664684547 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_f00b21c3-e663-492f-8915-04cb7c51d206">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_94bf368d-4851-41d2-911f-9d769bfd82ba">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43723601434381 49.0204528239439 0 8.4400935856246 49.019227957949 0 8.44061001416934 49.0198488615293 0 8.44104037128994 49.0204020236493 0 8.44030876418493 49.0206616690549 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_7e493820-71bf-4f32-9431-981de83a8aa0"/>
									<utility:end xlink:href="NodeID_3c3f95cf-16a7-450f-a361-f0f547d3d6b8"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_7e493820-71bf-4f32-9431-981de83a8aa0">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43723601434381 49.0204528239439 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_3c3f95cf-16a7-450f-a361-f0f547d3d6b8">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.44030876418493 49.0206616690549 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
				</utility:NetworkGraph>
			</utility:topoGraph>
			<utility:component>
				<utility:Cable gml:id="CableID_dc91fb95-f74a-4c0a-9df4-2793d26e3590">
					<utility:hasMaterial xlink:href="InteriorMaterialID_51982b12-f5c7-4101-8ac8-4bc6e264c866"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_9f6d356d-8207-4baa-840e-b5bff504d663"/>
					<utility:topoGraph xlink:href="FeatureGraphID_0381a840-84d8-4679-91b9-a82b1024148b"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43934680490424 49.020354397847 -2.5 8.43927853887153 49.0202741829022 -2.5 8.43927378527703 49.0202546993424 -2.5 8.43928923445931 49.0201276663454 -2.5 8.43942490645175 49.0200629247443 -2.5 8.43943788537858 49.0200577220819 -2.5 8.43947343624818 49.0200419383728 -2.5 8.43952691418689 49.0199764732761 -2.5 8.43950314621416 49.0199515341691 -2.5 8.43947343624818 49.0199242570063 -2.5</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:isTransmission>true</utility:isTransmission>
					<utility:isCommunication>false</utility:isCommunication>
					<utility:crossSection uom="cm">3</utility:crossSection>
				</utility:Cable>
			</utility:component>
			<utility:component>
				<utility:Cable gml:id="CableID_b014aa96-a26e-4e28-b345-0b415a628fff">
					<utility:hasMaterial xlink:href="InteriorMaterialID_51982b12-f5c7-4101-8ac8-4bc6e264c866"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_9f6d356d-8207-4baa-840e-b5bff504d663"/>
					<utility:topoGraph xlink:href="FeatureGraphID_5d02a950-25b4-4be9-8d4f-334314b9c68b"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.4396155682183 49.0202447058197 -2.5 8.43945323347134 49.0200723328527 -2.5 8.43941639311358 49.0200372622973 -2.5 8.43937372981148 49.019991442381 -2.5 8.43932250962118 49.0199367265683 -2.5</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:isTransmission>true</utility:isTransmission>
					<utility:isCommunication>false</utility:isCommunication>
					<utility:crossSection uom="cm">3</utility:crossSection>
				</utility:Cable>
			</utility:component>
			<utility:component>
				<utility:Cable gml:id="CableID_bcb86e1e-aeb1-4ba7-b633-bd0c770e02ef">
					<utility:hasMaterial xlink:href="InteriorMaterialID_51982b12-f5c7-4101-8ac8-4bc6e264c866"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_9f6d356d-8207-4baa-840e-b5bff504d663"/>
					<utility:topoGraph xlink:href="FeatureGraphID_9468fb23-fe48-4213-8500-ef61cf27124a"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43987641241913 49.0201382456378 -2.5 8.43980381106958 49.0200442764104 -2.5 8.43978123149549 49.020027130799 -2.5 8.43975152152951 49.020015440606 -2.5 8.43970160878673 49.0200045297567 -2.5 8.43967308721939 49.0199959569448 -2.5 8.43959108771338 49.019977252623 -2.5 8.43953760977467 49.0199616656828 -2.5 8.4394948274237 49.0199180222241 -2.5</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:isTransmission>true</utility:isTransmission>
					<utility:isCommunication>false</utility:isCommunication>
					<utility:crossSection uom="cm">3</utility:crossSection>
				</utility:Cable>
			</utility:component>
			<utility:component>
				<utility:Cable gml:id="CableID_98793e34-5a68-4db2-ad78-f81f585f1aa8">
					<utility:hasMaterial xlink:href="InteriorMaterialID_51982b12-f5c7-4101-8ac8-4bc6e264c866"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_9f6d356d-8207-4baa-840e-b5bff504d663"/>
					<utility:topoGraph xlink:href="FeatureGraphID_cbd0b97f-2932-48ac-864d-a7d32b65fab8"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.44032547255719 49.0199549670254 -2.5 8.44003369193117 49.0196464674784 -2.5</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:isTransmission>true</utility:isTransmission>
					<utility:isCommunication>false</utility:isCommunication>
					<utility:crossSection uom="cm">3</utility:crossSection>
				</utility:Cable>
			</utility:component>
			<utility:component>
				<utility:Cable gml:id="CableID_770abfba-1ba0-4062-9fa1-b27c5fce230d">
					<utility:hasMaterial xlink:href="InteriorMaterialID_51982b12-f5c7-4101-8ac8-4bc6e264c866"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_9f6d356d-8207-4baa-840e-b5bff504d663"/>
					<utility:topoGraph xlink:href="FeatureGraphID_40c3327c-c7ae-4adf-99a9-6936fe662602"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43960144951994 49.0202504681771 -2.5 8.43952268051675 49.0201608371862 -2.5 8.4393028267687 49.019940672015 -2.5</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:isTransmission>true</utility:isTransmission>
					<utility:isCommunication>false</utility:isCommunication>
					<utility:crossSection uom="cm">3</utility:crossSection>
				</utility:Cable>
			</utility:component>
			<utility:component>
				<utility:Cable gml:id="CableID_1f0518cf-f04e-4a4a-ac58-bb70808b0fa6">
					<utility:hasMaterial xlink:href="InteriorMaterialID_51982b12-f5c7-4101-8ac8-4bc6e264c866"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_9f6d356d-8207-4baa-840e-b5bff504d663"/>
					<utility:topoGraph xlink:href="FeatureGraphID_6661bae1-6532-480c-a95d-451980a0fdb9"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43729626434073 49.0204979797178 -2.5 8.44004194277011 49.0193182716784 -2.5 8.44023990704561 49.0192336025619 -2.5 8.44069608559343 49.0198037051665 -2.5 8.43784712145511 49.0209664684547 -2.5</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:isTransmission>true</utility:isTransmission>
					<utility:isCommunication>false</utility:isCommunication>
					<utility:crossSection uom="cm">3</utility:crossSection>
				</utility:Cable>
			</utility:component>
			<utility:component>
				<utility:Cable gml:id="CableID_2dd0642f-7856-4b7d-82d4-04fe4ddfffa7">
					<utility:hasMaterial xlink:href="InteriorMaterialID_51982b12-f5c7-4101-8ac8-4bc6e264c866"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_9f6d356d-8207-4baa-840e-b5bff504d663"/>
					<utility:topoGraph xlink:href="FeatureGraphID_f00b21c3-e663-492f-8915-04cb7c51d206"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43723601434381 49.0204528239439 -2.5 8.4400935856246 49.019227957949 -2.5 8.44061001416934 49.0198488615293 -2.5 8.44104037128994 49.0204020236493 -2.5 8.44030876418493 49.0206616690549 -2.5</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:isTransmission>true</utility:isTransmission>
					<utility:isCommunication>false</utility:isCommunication>
					<utility:crossSection uom="cm">3</utility:crossSection>
				</utility:Cable>
			</utility:component>
		</utility:Network>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<utility:ExteriorMaterial gml:id="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7">
			<utility:type>steel</utility:type>
		</utility:ExteriorMaterial>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<utility:InteriorMaterial gml:id="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c">
			<utility:type>steel</utility:type>
		</utility:InteriorMaterial>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<utility:Network gml:id="52f41083-a503-4610-8d7c-732426986cbd">
			<utility:transportedMedium>
				<utility:GaseousMedium gml:id="GaseousMediumID_a8db2b4b-6480-4d9d-a104-2ed35c1394fe">
					<utility:type>naturalGas</utility:type>
				</utility:GaseousMedium>
			</utility:transportedMedium>
			<utility:topoGraph>
				<utility:NetworkGraph gml:id="a06c504d-c141-4b83-90aa-760f6b5510db">
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_e59e358e-8ab8-4b96-baa2-7356ec4c37a1">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_4425fb0f-699c-4f12-95ad-bba0ee719b2a">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43962733301845 49.0202399041739 0 8.43956256614607 49.0201666335569 0 8.43949601582234 49.0200941545192 0 8.43939975553262 49.0199912808651 0 8.4393866831476 49.01996244503 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_75a6e9c3-7030-4ccf-bd49-8dea9b113d77"/>
									<utility:end xlink:href="NodeID_2725405f-349c-4f5c-96e8-f2141794e23e"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_75a6e9c3-7030-4ccf-bd49-8dea9b113d77">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43962733301845 49.0202399041739 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_2725405f-349c-4f5c-96e8-f2141794e23e">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.4393866831476 49.01996244503 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_5ef4d84b-1784-445d-b72b-49ec8a28ef44">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_2225f425-873e-4ea7-95d6-11def742e432">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43933033780717 49.0203611186569 0 8.43925595929744 49.0202788589555 0 8.43925001730423 49.0202523613147 0 8.4392547708988 49.0202344364321 0 8.43929108674848 49.0200269317495 0 8.43927735047293 49.0200123232207 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_0df46b38-794c-4594-93b6-f74801bfaddd"/>
									<utility:end xlink:href="NodeID_ca070a98-4934-4c46-84ac-579d01620750"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_0df46b38-794c-4594-93b6-f74801bfaddd">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43933033780717 49.0203611186569 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_ca070a98-4934-4c46-84ac-579d01620750">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43927735047293 49.0200123232207 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_7a28aa52-2fdb-4f14-9df0-95d26c08c48e">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_0adc19e1-949f-4669-b3d5-bac026580f5f">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43838045294376 49.0200321506444 0 8.43841338466305 49.0199632243771 0 8.43857738367508 49.0198907450431 0 8.43866413677566 49.0198533363133 0 8.43877703464632 49.0198042373128 0 8.43891251209105 49.0197473447596 0 8.43899213479981 49.0197130533262 0 8.43909790227856 49.0196686302979 0 8.43922149573693 49.0196156343526 0 8.43931062563479 49.0197138326773 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_7c743b40-4940-4c7f-a372-757e62c78309"/>
									<utility:end xlink:href="NodeID_dbbb6aa3-b300-4e57-9d0b-eb971ce325ca"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_7c743b40-4940-4c7f-a372-757e62c78309">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43838045294376 49.0200321506444 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_dbbb6aa3-b300-4e57-9d0b-eb971ce325ca">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43931062563479 49.0197138326773 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_542bc752-f837-489b-a34a-9618af4181b4">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_9d8321fe-4ba0-4666-947b-fd30d85b8a8a">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43841449878679 49.0199796393728 0 8.43849419577042 49.0200598633248 0 8.43853935491864 49.0200715535073 0 8.43858689086421 49.0200684361256 0 8.43861422403287 49.0200567459423 0 8.43867839755936 49.020027910145 0 8.43897430882022 49.019893862436 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_cc73cda0-a6c9-4bf4-a687-e7c167102679"/>
									<utility:end xlink:href="NodeID_0c29384d-7fc8-43bf-9734-dc1ec569cad5"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_cc73cda0-a6c9-4bf4-a687-e7c167102679">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43841449878679 49.0199796393728 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_0c29384d-7fc8-43bf-9734-dc1ec569cad5">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43897430882022 49.019893862436 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_e0a4d8c4-1f32-4978-bbf2-7f4ce796abde">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_9ce67391-c984-4b62-925a-9f46da011ad0">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.44006043090052 49.0192743254741 0 8.43967123034651 49.0194399387516 0 8.43950782553378 49.0195100806794 0 8.43932956573806 49.0195860676564 0 8.43933453833384 49.0196138222984 0 8.43933253673468 49.0197088156049 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_fa0b4ed6-0804-4fc4-ab49-d5e88b3cc9e7"/>
									<utility:end xlink:href="NodeID_0bdc615b-5b97-4d48-a092-eac743c284c8"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_fa0b4ed6-0804-4fc4-ab49-d5e88b3cc9e7">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.44006043090052 49.0192743254741 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_0bdc615b-5b97-4d48-a092-eac743c284c8">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43933253673468 49.0197088156049 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_a7b908af-c5f5-4bd4-b8e9-8a5fa813b041">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_0355cfa6-4de1-4b41-b2c5-2fd064a234d7">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43909132839083 49.0197267157637 0 8.43916021893216 49.0197945441513 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_4498995d-88f6-4410-96dd-19d8402a0eb7"/>
									<utility:end xlink:href="NodeID_29ae9e54-2b7d-47b5-8f6a-a77a76207d62"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_4498995d-88f6-4410-96dd-19d8402a0eb7">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43909132839083 49.0197267157637 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_29ae9e54-2b7d-47b5-8f6a-a77a76207d62">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43916021893216 49.0197945441513 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_04ccab9c-20fd-44d4-9ee5-4346b62e3aec">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_8e72758c-eb27-42ae-98a1-7efc552f88bb">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43867966136864 49.0198340245476 0 8.43872948967736 49.0198821836888 0 8.43921347767324 49.0204088134215 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_3c123cb6-fb0a-4e7f-9029-7423d335cc70"/>
									<utility:end xlink:href="NodeID_9e3edaec-0c99-46eb-b82c-e654f940a551"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_3c123cb6-fb0a-4e7f-9029-7423d335cc70">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43867966136864 49.0198340245476 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_9e3edaec-0c99-46eb-b82c-e654f940a551">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43921347767324 49.0204088134215 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_295bed5a-a1c9-4299-b1e6-b5c711adcb84">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_cbcfad3f-af44-445e-a137-aa6142268ddc">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43939343891438 49.0195280708 0 8.43965043337039 49.0197867506531 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_b280b7e5-ec6e-403a-b10d-353faef131de"/>
									<utility:end xlink:href="NodeID_418fe775-f903-4665-83c7-1ba99896ff70"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_b280b7e5-ec6e-403a-b10d-353faef131de">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43939343891438 49.0195280708 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_418fe775-f903-4665-83c7-1ba99896ff70">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43965043337039 49.0197867506531 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_07422295-049e-4881-8cd9-5fb21f7f5a16">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_14cd6c81-33db-4821-98c1-13a055629c1c">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43895064954263 49.0197178683449 0 8.43900172886785 49.0197652132152 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_d400a9a1-13a8-4682-b69a-3f0517db31b2"/>
									<utility:end xlink:href="NodeID_f2a2f647-99c0-4a9a-924d-3e2bf85f05ae"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_d400a9a1-13a8-4682-b69a-3f0517db31b2">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43895064954263 49.0197178683449 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_f2a2f647-99c0-4a9a-924d-3e2bf85f05ae">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43900172886785 49.0197652132152 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
				</utility:NetworkGraph>
			</utility:topoGraph>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_78046244-7665-4e07-ac02-b1038afbe431">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_e59e358e-8ab8-4b96-baa2-7356ec4c37a1"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43962733301845 49.0202399041739 -3 8.43956256614607 49.0201666335569 -3 8.43949601582234 49.0200941545192 -3 8.43939975553262 49.0199912808651 -3 8.4393866831476 49.01996244503 -3</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>5</utility:exteriorDiameter>
					<utility:interiorDiameter>4.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_1b98779a-76e5-423c-bb67-fcdffa94a252">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_5ef4d84b-1784-445d-b72b-49ec8a28ef44"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43933033780717 49.0203611186569 -3 8.43925595929744 49.0202788589555 -3 8.43925001730423 49.0202523613147 -3 8.4392547708988 49.0202344364321 -3 8.43929108674848 49.0200269317495 -3 8.43927735047293 49.0200123232207 -3</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>5</utility:exteriorDiameter>
					<utility:interiorDiameter>4.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_4de45fc8-e16c-416b-922a-f7ffdd2017e6">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_7a28aa52-2fdb-4f14-9df0-95d26c08c48e"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43838045294376 49.0200321506444 -3 8.43841338466305 49.0199632243771 -3 8.43857738367508 49.0198907450431 -3 8.43866413677566 49.0198533363133 -3 8.43877703464632 49.0198042373128 -3 8.43891251209105 49.0197473447596 -3 8.43899213479981 49.0197130533262 -3 8.43909790227856 49.0196686302979 -3 8.43922149573693 49.0196156343526 -3 8.43931062563479 49.0197138326773 -3</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>5</utility:exteriorDiameter>
					<utility:interiorDiameter>4.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_8ddd1bac-90e1-417d-9ed8-eab39d622338">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_542bc752-f837-489b-a34a-9618af4181b4"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43841449878679 49.0199796393728 -3 8.43849419577042 49.0200598633248 -3 8.43853935491864 49.0200715535073 -3 8.43858689086421 49.0200684361256 -3 8.43861422403287 49.0200567459423 -3 8.43867839755936 49.020027910145 -3 8.43897430882022 49.019893862436 -3</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>5</utility:exteriorDiameter>
					<utility:interiorDiameter>4.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_beb4d2f6-8c4c-4bbf-ac02-79d706dcd844">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_e0a4d8c4-1f32-4978-bbf2-7f4ce796abde"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.44006043090052 49.0192743254741 -3 8.43967123034651 49.0194399387516 -3 8.43950782553378 49.0195100806794 -3 8.43932956573806 49.0195860676564 -3 8.43933453833384 49.0196138222984 -3 8.43933253673468 49.0197088156049 -3</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>5</utility:exteriorDiameter>
					<utility:interiorDiameter>4.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_adc2a425-dff3-49c2-891f-6b70ef3ee3f0">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_a7b908af-c5f5-4bd4-b8e9-8a5fa813b041"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43909132839083 49.0197267157637 -3 8.43916021893216 49.0197945441513 -3</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>5</utility:exteriorDiameter>
					<utility:interiorDiameter>4.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_7f108fe2-6168-4526-bc2d-ff98dbd961bf">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_04ccab9c-20fd-44d4-9ee5-4346b62e3aec"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43867966136864 49.0198340245476 -3 8.43872948967736 49.0198821836888 -3 8.43921347767324 49.0204088134215 -3</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>5</utility:exteriorDiameter>
					<utility:interiorDiameter>4.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_8e402e41-843a-4d52-a8f0-c20334dd6423">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_295bed5a-a1c9-4299-b1e6-b5c711adcb84"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43939343891438 49.0195280708 -3 8.43965043337039 49.0197867506531 -3</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>5</utility:exteriorDiameter>
					<utility:interiorDiameter>4.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_31102f91-8f9f-41e7-8695-7eb7a256aadc">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_07422295-049e-4881-8cd9-5fb21f7f5a16"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43895064954263 49.0197178683449 -3 8.43900172886785 49.0197652132152 -3</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>5</utility:exteriorDiameter>
					<utility:interiorDiameter>4.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
		</utility:Network>
	</core:cityObjectMember>
	<core:cityObjectMember>
		<utility:Network gml:id="49d5e97c-b811-4e12-a922-2a3c748fd693">
			<utility:transportedMedium>
				<utility:LiquidMedium gml:id="LiquidMediumID_a5a70298-ec61-46a7-938d-767beb48cc61">
					<utility:type>wasteWater</utility:type>
				</utility:LiquidMedium>
			</utility:transportedMedium>
			<utility:topoGraph>
				<utility:NetworkGraph gml:id="b786db01-08b7-43b1-859c-348d974766f6">
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_504b51c6-4935-46d4-8787-ddc8177fc75a">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_141a662d-609e-44d1-a82b-9a980961d8fb">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43841449878679 49.0199796393728 0 8.43853036765397 49.0199270334325 0 8.43887203226242 49.0197750604037 0 8.43896947958464 49.0197353217111 0 8.43905623405136 49.0196990737153 0 8.43919228721998 49.0196418180357 0 8.43937059218388 49.0196067267162 0 8.4396326073908 49.01988027255 0 8.43904732106153 49.020131611777 0 8.43927149294109 49.0203851353392 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_c2115736-65be-4d89-a70b-df4d77b68cc0"/>
									<utility:end xlink:href="NodeID_ef5a41d7-188f-4d07-a9ff-b0e6a5187731"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_c2115736-65be-4d89-a70b-df4d77b68cc0">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43841449878679 49.0199796393728 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_ef5a41d7-188f-4d07-a9ff-b0e6a5187731">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43927149294109 49.0203851353392 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_ce38c5f8-6c13-44ec-a48c-1415892e1276">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_0972dcad-83ce-4514-a263-2e8510f60fab">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.44012876382217 49.0191944412257 0 8.43944840560186 49.019471112954 0 8.43917804491175 49.0195782741256 0 8.43888391624881 49.0197127123602 0 8.43867594648717 49.0198023376482 0 8.43875844824315 49.0198697413451 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_6133dc73-3a92-47eb-bc53-28d9ad6f71f6"/>
									<utility:end xlink:href="NodeID_1feab42c-31e2-426f-a9e0-a2aab592d7d0"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_6133dc73-3a92-47eb-bc53-28d9ad6f71f6">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.44012876382217 49.0191944412257 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_1feab42c-31e2-426f-a9e0-a2aab592d7d0">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43875844824315 49.0198697413451 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
					<utility:featureGraphMember>
						<utility:FeatureGraph gml:id="FeatureGraphID_7f81e4f4-eb96-4de4-bd40-eb472fcc1299">
							<utility:linkMember>
								<utility:InteriorFeatureLink gml:id="InteriorFeatureLinkID_c5db9206-3b19-4ea4-a907-93e26d6616a0">
									<utility:realization>
										<gml:LineString srsName="epsg:4326" srsDimension="3">
											<gml:posList>8.43917200347275 49.0196229871325 0 8.43922286049049 49.0196702014512 0</gml:posList>
										</gml:LineString>
									</utility:realization>
									<utility:start xlink:href="NodeID_17ddaee5-e9df-4d7b-a132-428d881cfea1"/>
									<utility:end xlink:href="NodeID_6720d0f3-1e35-4839-9379-90b82d4f9cc6"/>
								</utility:InteriorFeatureLink>
							</utility:linkMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_17ddaee5-e9df-4d7b-a132-428d881cfea1">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43917200347275 49.0196229871325 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
							<utility:nodeMember>
								<utility:Node gml:id="NodeID_6720d0f3-1e35-4839-9379-90b82d4f9cc6">
									<utility:type>exterior</utility:type>
									<utility:realization>
										<gml:Point srsName="epsg:4326" srsDimension="3">
											<gml:pos>8.43922286049049 49.0196702014512 0</gml:pos>
										</gml:Point>
									</utility:realization>
								</utility:Node>
							</utility:nodeMember>
						</utility:FeatureGraph>
					</utility:featureGraphMember>
				</utility:NetworkGraph>
			</utility:topoGraph>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_3c94af0c-cc79-409f-ad92-a25ee2badf9f">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_504b51c6-4935-46d4-8787-ddc8177fc75a"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43841449878679 49.0199796393728 -2 8.43853036765397 49.0199270334325 -2 8.43887203226242 49.0197750604037 -2 8.43896947958464 49.0197353217111 -2 8.43905623405136 49.0196990737153 -2 8.43919228721998 49.0196418180357 -2 8.43937059218388 49.0196067267162 -2 8.4396326073908 49.01988027255 -2 8.43904732106153 49.020131611777 -2 8.43927149294109 49.0203851353392 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>10</utility:exteriorDiameter>
					<utility:interiorDiameter>9.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_8cee76c9-e3a1-449a-af5d-5fae576d4ce9">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_ce38c5f8-6c13-44ec-a48c-1415892e1276"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.44012876382217 49.0191944412257 -2 8.43944840560186 49.019471112954 -2 8.43917804491175 49.0195782741256 -2 8.43888391624881 49.0197127123602 -2 8.43867594648717 49.0198023376482 -2 8.43875844824315 49.0198697413451 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>10</utility:exteriorDiameter>
					<utility:interiorDiameter>9.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
			<utility:component>
				<utility:RoundPipe gml:id="RoundPipeID_c7e4280f-ba1b-4b92-8b45-928334bf2067">
					<utility:hasMaterial xlink:href="InteriorMaterialID_b26cf580-9839-4031-98da-d1ed7b17420c"/>
					<utility:hasMaterial xlink:href="ExteriorMaterialID_f4f52c23-9ca1-4c62-89dc-47d1241a41f7"/>
					<utility:topoGraph xlink:href="FeatureGraphID_7f81e4f4-eb96-4de4-bd40-eb472fcc1299"/>
					<utility:lod1Geometry>
						<gml:LineString srsName="epsg:4326" srsDimension="3">
							<gml:posList>8.43917200347275 49.0196229871325 -2 8.43922286049049 49.0196702014512 -2</gml:posList>
						</gml:LineString>
					</utility:lod1Geometry>
					<utility:exteriorDiameter>10</utility:exteriorDiameter>
					<utility:interiorDiameter>9.5</utility:interiorDiameter>
				</utility:RoundPipe>
			</utility:component>
		</utility:Network>
	</core:cityObjectMember>
</core:CityModel>
