package org.simbotics.frc2017.imaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.simbotics.frc2017.util.Debugger;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoException;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SimCamera implements Runnable {

	private UsbCamera camera;

	private CvSink cvSink;
	private CvSource outputStream;
	private boolean processing = false;
	private boolean initialized = false;

	private Point midPoint;
	private Mat source;
	private SimProcessing processingPipeline;

	public SimCamera() {
		this.processingPipeline = new SimProcessing();
		this.source = new Mat();
	}

	public void setProcessingOn(boolean on) {
		this.processing = on;
	}

	public void init() {
		SmartDashboard.putBoolean("2_CAMERA PROCESSING: ", processing);

		try {
			this.camera = new UsbCamera("usb cam", "/dev/video0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CameraServer.getInstance().addCamera(camera);
		this.camera.setResolution(320, 240);
		this.camera.setFPS(30);
		this.camera.setBrightness(0);
		this.camera.setExposureManual(10);

		this.cvSink = CameraServer.getInstance().getVideo();
		this.outputStream = CameraServer.getInstance().putVideo("cam0", 320, 240);
		this.initialized = true;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) { //thread is always running

			if (this.camera == null && this.initialized == false) {
				try {
					this.init();
				} catch (VideoException ve) {
					ve.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (this.camera.isConnected() && this.initialized == true) {
				this.midPoint = null; // reset the midpoint, will stay null if
										// not seeing target
				if (CameraServer.getInstance() != null) {
					this.cvSink.grabFrameNoTimeout(this.source);
					
					if (this.processing) {

						/*
						 * Camera Image Retrieval
						 * ---------------------------------------------------
						 */
						// Retrieve the current output that the Camera sees

						this.processingPipeline.process(this.source);
						Mat output = new Mat();
						output = this.processingPipeline.maskOutput();

						/*
						 * Generate List of Contours
						 * ---------------------------------------------------
						 */
						ArrayList<SimTarget> simTargetArrayList;
						simTargetArrayList = this.processingPipeline.filterContoursOutput(); // list
																								// of
																								// contours

						// Now that we have the list of contours we need to sort
						// them. This function
						// will provide of with a list of sorted contours
						// ranging from least -> greatest
						List<SimTarget> simTargetList = simTargetArrayList;
						Collections.sort(simTargetList, new Comparator<SimTarget>() {
							@Override
							public int compare(SimTarget mop1, SimTarget mop2) {
								return (int) Imgproc.contourArea(mop2.getMop())
										- (int) Imgproc.contourArea(mop1.getMop());
							}
						});

						for (int i = 0; i < simTargetList.size(); i++) { // for
																			// each
																			// contour
																			// in
																			// the
																			// array

							SimTarget target = simTargetList.get(i);

							/*
							 * Drawing the Rotated Rectangle
							 * -------------------------------------------------
							 * --
							 */

							// Iterate over each side of the "rectangle" and
							// draw a physical line
							for (int j = 0; j < 4; j++) {
								Imgproc.line(output, target.getMinAreaRectVertices()[j],
										target.getMinAreaRectVertices()[(j + 1) % 4], new Scalar(0, 0, 255), 3); // draw
																													// a
																													// line
																													// to
																													// the
																													// output
																													// Mat
							}

							/*
							 * Drawing the Bounding Rectangle
							 * -------------------------------------------------
							 * --
							 */

							Imgproc.rectangle(output, new Point(target.getBoundingRectX(), target.getBoundingRectY()),
									new Point(target.getBoundingRectX() + target.getBoundingRectWidth(),
											target.getBoundingRectY() + target.getBoundingRectHeight()),
									new Scalar(255, 0, 0), 2);

							Debugger.println(
									"#" + i + " Area: " + target.getBoundingRectArea() + " W: "
											+ target.getBoundingRectWidth() + " H: " + target.getBoundingRectHeight(),
									"CAMERA");

						}

						/*
						 * Drawing the Center point Between Rectangles
						 * ---------------------------------------------------
						 */

						// This is the case that we only want to do when we have
						// detected 2 unique
						// objects in our field of view. Here we take the two
						// rectangles and find the
						// vertical center. That is where we are going to draw
						// our line
						/*if (simTargetList.size() >= 2) {

							// A quick reference to the two contours that we
							// found earlier.
							// These represent the two biggest rectangles that
							// are generated
							SimTarget target1 = simTargetList.get(0);
							SimTarget target2 = simTargetList.get(1);

							int xSpacing = Math
									.abs(target1.getBoundingRectCenterX() - target2.getBoundingRectCenterX());
							int xMiddle = Math.min(target1.getBoundingRectCenterX(), target2.getBoundingRectCenterX())
									+ xSpacing / 2;

							// Determine the absolute lowest y and the absolute
							// highest y
							// This will give us the height that we want to use
							// when we draw our
							// center point
							int yBottom = Math.min(target1.getBoundingRectY(), target2.getBoundingRectY());
							int yTop = Math.max(target1.getBoundingRectY() + target1.getBoundingRectHeight(),
									target2.getBoundingRectY() + target2.getBoundingRectHeight());
							int yMiddle = (yBottom + yTop) / 2;
							// Now that we have generated our x co-ordinate and
							// our height, we can draw the
							// new line between the rectangles
							Imgproc.line(output, new Point(xMiddle, yBottom), new Point(xMiddle, yTop),
									new Scalar(255, 255, 255), 2);

							Imgproc.line(output,
									new Point(Math.min(target1.getBoundingRectX(), target2.getBoundingRectX()),
											yMiddle),
									new Point(
											Math.max(target1.getBoundingRectX() + target1.getBoundingRectWidth(),
													target2.getBoundingRectX() + target2.getBoundingRectWidth()),
											yMiddle),
									new Scalar(255, 255, 255), 2);

							this.midPoint = new Point(xMiddle, yMiddle);

							Imgproc.circle(output, midPoint, 4, new Scalar(255, 0, 0), 2);

						} else 
						*/
						if (simTargetList.size() > 0) { // only use 1 target 
							SimTarget target = simTargetList.get(0);

							int xMiddle = target.getBoundingRectCenterX();
							int yMiddle = target.getBoundingRectCenterY();
							this.midPoint = new Point(xMiddle, yMiddle);
							Imgproc.line(output, new Point(xMiddle, target.getBoundingRectY()),
									new Point(xMiddle, target.getBoundingRectY() + target.getBoundingRectHeight()),
									new Scalar(255, 255, 255), 1);
							Imgproc.circle(output, this.midPoint, 4, new Scalar(255, 0, 0), 2);
						}

						this.outputStream.putFrame(output); // put the output
															// Mat in the stream
					} else {
						this.outputStream.putFrame(source);
					}
				}
			}

		}
	}

	public Point getMidPoint() {
		return this.midPoint;
	}
	
	public void setMinArea(double area){
		this.processingPipeline.setMinArea(area);
	}

}
