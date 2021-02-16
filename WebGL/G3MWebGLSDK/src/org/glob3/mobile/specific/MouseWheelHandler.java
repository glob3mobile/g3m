package org.glob3.mobile.specific;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.glob3.mobile.generated.*;

import com.google.gwt.user.client.Window;


public class MouseWheelHandler extends CameraEventHandler {

	@Override
	public RenderState getRenderState(G3MRenderContext rc) {
		return RenderState.ready();
	}

	@Override
	public void render(G3MRenderContext rc, CameraContext cameraContext) {
		// TODO Auto-generated method stub
		//Window.alert("ok2");
		
		headingPitchRoll = rc.getCurrentCamera().getHeadingPitchRoll();

	}
	
	private void log(String msg) {
		Logger.getLogger("").log(Level.INFO, msg);
	}
	
	TaitBryanAngles headingPitchRoll;
//
//	@Override
//	public boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext) {
//		
//		
//		
//		
//		//Logger.getLogger("").log(Level.INFO, "touch");
//		Touch touch = touchEvent.getTouch(0);
//		double wheelDelta = touch.getMouseWheelDelta();
//		if (wheelDelta != 0) {
//			log("wheel -- " + wheelDelta + "------------------------------------------------------");
//			Camera cam = cameraContext.getNextCamera();
//			//nextCam.print();
//			log(cam.getCartesianPosition().description());
//			
//			
////			Geodetic3D pos = cam.getGeodeticPosition();
////			Geodetic3D pos2 = Geodetic3D.fromDegrees(pos._latitude._degrees, pos._longitude._degrees, 5000);
////			
////			cameraContext.getNextCamera().setGeodeticPosition(pos2); 
//			
////			Vector3D pos = cam.getCartesianPosition();
////			pos = pos.add(new Vector3D(0, 0, -100));
////			cam.setCartesianPosition(pos);
////			
////			log(cameraContext.getNextCamera().getGeodeticPosition().toString());
//			
//			MutableVector3D position = new MutableVector3D(), center = new MutableVector3D(), up = new MutableVector3D();
//		    cam.getLookAtParamsInto(position, center, up);
//		    
//		    //Vector2F pixel = new Vector2F(nextCam.getViewPortWidth()/2,nextCam.getViewPortHeight()/2);
//		    Vector2F pixel = touch.getPos().times(2.0f); //PIXEL COORDS ARE WRONG
//		    
//		    
//			Vector3D rayDir = cam.pixel2Ray(pixel);
//			Vector3D p = eventContext.getPlanet().closestIntersection(position.asVector3D(), rayDir);
//			Vector3D moveDir = p.sub(position).normalized();
//
//			//log(pixel.description());
//		    
//		    //nextCam.setLookAtParams(newPos, center, up);
//		    
//
//			headingPitchRoll = cam.getHeadingPitchRoll();
//			log(headingPitchRoll.description());
//			
//			
//		    //cam.setGeodeticPosition(Geodetic3D.fromDegrees(0, 0, 1000));
//			//CoordinateSystem camCS0 = nextCam.getLocalCoordinateSystem();
//			//MutableMatrix44D rotM0 = camCS0.getRotationMatrix();
//			
//			Vector3D pos0 = cam.getCartesianPosition();
//			
//			
//		    cam.move(moveDir, cam.getGeodeticHeight() * 0.05 * wheelDelta);
//		    
//		    
//		    Vector3D pos1 = cam.getCartesianPosition();
//		    Angle rotation = pos0.angleBetween(pos1);
//		    log(rotation.description());
//		    MutableMatrix44D mat = MutableMatrix44D.createGeneralRotationMatrix(rotation, pos0.cross(pos1), pos1);
//		    cam.applyTransform(mat);
//		    
//		    //nextCam.setHeadingPitchRoll(headingPitchRoll._heading, headingPitchRoll._pitch, headingPitchRoll._roll);
//		    
//		    //log(nextCam.getGeodeticPosition().description());
//		    //CoordinateSystem camCS1 = nextCam.getLocalCoordinateSystem();
//		    //MutableMatrix44D rotM1 = camCS1.getRotationMatrix();
//		    
//		   // nextCam.applyTransform(rotM1.inversed().multiply(rotM0));
//		    
//		    
//		    
//		    //log(camCS1.description());
//			//CoordinateSystem rs1 = eventContext.getPlanet().getCoordinateSystemAt(nextCam.getGeodeticPosition());
//		    
//
//			headingPitchRoll = cam.getHeadingPitchRoll();
//			log(headingPitchRoll.description());
//			
//		    
//		    //nextCam.print();
//		    //log(nextCam.getCartesianPosition().description());
//			//log("" + nextCam.getFrustumData()._zFar + " " + nextCam.getFrustumData()._zNear);
//			return true;
//		}
//		
//		return false;
//	}
	

	@Override
	public boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext) {
		
		double speed = 1.;//10.;
		
		Touch touch = touchEvent.getTouch(0);
		double wheelDelta = touch.getMouseWheelDelta();
		if (wheelDelta != 0) {
			Camera cam = cameraContext.getNextCamera();
		    Vector2F pixel = touch.getPos();//.times(2.0f); //PIXEL COORDS ARE WRONG

			Vector3D rayDir = cam.pixel2Ray(pixel);
			Planet planet = eventContext.getPlanet();

			Vector3D pos0 = cam.getCartesianPosition();
			Vector3D p1 = planet.closestIntersection(pos0, rayDir);
			
			double heightDelta = cam.getGeodeticHeight() * 0.05 * wheelDelta * speed;
			
		    if (!planet.isFlat()) {
		    
			    cam.move(pos0.normalized().times(-1), heightDelta);
			    
			    Vector3D p2 = planet.closestIntersection(cam.getCartesianPosition(), cam.pixel2Ray(pixel));
			    Angle angleP1P2 = Vector3D.angleBetween(p1, p2);
			    Vector3D rotAxis = p2.cross(p1);
			    
			    if (!rotAxis.isNan() && !angleP1P2.isNan()) {
				    MutableMatrix44D mat = MutableMatrix44D.createGeneralRotationMatrix(angleP1P2, rotAxis, Vector3D.ZERO);
				    //log(mat.description() + " " + rotAxis.description() + " " + angleP1P2.description());
				    cam.applyTransform(mat);
				}
		    }else {
		    	Vector3D moveDir = p1.sub(pos0).normalized();
		    	cam.move(moveDir, heightDelta);
		    }
		    
		    log(cam.getGeodeticPosition().description() + " " + cam.getHeadingPitchRoll().description());
		    
			return true;
		}
		
		return false;
	}

	@Override
	public void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext) {
		// TODO Auto-generated method stub


	}

	@Override
	public void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext) {
		// TODO Auto-generated method stub
		Window.alert("ok3");


	}

	@Override
	public void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext) {
		// TODO Auto-generated method stub
		//Window.alert("ok2");

	}

}
