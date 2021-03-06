package com.jjoe64.graphview;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Line Graph View. This draws a line chart.
 * 
 * @author jjoe64 - jonas gehring - http://www.jjoe64.com
 * 
 *         Copyright (C) 2011 Jonas Gehring Licensed under the GNU Lesser
 *         General Public License (LGPL) http://www.gnu.org/licenses/lgpl.html
 */
public class LineGraphView extends GraphView {
	private final Paint paintBackground;
	private boolean drawBackground;
	private VertexManager verMan;
	private int colorSpecialBackground;
	private int alpha;
	private boolean drawSpecialBackground;
	private boolean signLastPoint;
	private Typeface signLastPointTypeface;
	
	public Typeface getSignLastPointTypeface() {
		return signLastPointTypeface;
	}

	public void setSignLastPointTypeface(Typeface signLastPointTypeface) {
		this.signLastPointTypeface = signLastPointTypeface;
	}

	public boolean isSignLastPoint() {
		return signLastPoint;
	}

	public void setSignLastPoint(boolean signLastPoint) {
		this.signLastPoint = signLastPoint;
	}

	public int getAlpha() {
		return alpha;
	}

	public int getColorSpecialBackground() {
		return colorSpecialBackground;
	}

	public void setColorSpecialBackground(int colorSpecialBackground) {
		this.colorSpecialBackground = colorSpecialBackground;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public boolean isDrawSpecialBackground() {
		return drawSpecialBackground;
	}

	public void setDrawSpecialBackground(boolean draSpecialBackground) {
		this.drawSpecialBackground = draSpecialBackground;
	}

	public Paint getPaintBackground() {
		return paintBackground;
	}

	@Override
	public void setVertexManager(VertexManager manager) {
		verMan = manager;
	}

	public LineGraphView(Context context, String title) {
		super(context, title);
		paintBackground = new Paint();
		paintBackground.setARGB(255, 20, 40, 60);
		paintBackground.setStrokeWidth(4);
	}

	public LineGraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paintBackground = new Paint(paint);
	}

	public LineGraphView(Context context) {
		super(context);
		paintBackground = new Paint(paint);
	}

	private static double calcXcoordinate(double valueX, double minX, double diffX,
			double graphwidth) {
		double valX = valueX - minX;
		double ratX = valX / diffX;
		double x = graphwidth * ratX + GraphView.padding;
		return x;
	}

	private static double calcYcoordinate(double valueY, double minY, double diffY,
			double graphheight) {
		double valY = valueY - minY;
		double ratY = valY / diffY;
		double y = graphheight * ratY;
		return y;
	}

	@Override
	public void drawSeries(Canvas canvas, GraphViewData[] values,
			float graphwidth, float graphheight, float border, double minX,
			double minY, double diffX, double diffY, float horstart) {
		// draw background
		graphwidth = graphwidth - GraphView.padding;
		double lastEndY = 0;
		double lastEndX = 0;
		if (drawBackground) {
			float startY = graphheight + border;
			for (int i = 0; i < values.length; i++) {

				double y = calcYcoordinate(values[i].valueY, minY, diffY,
						graphheight);

				double x = calcXcoordinate(values[i].valueX, minX, diffX,
						graphwidth);

				float endX = (float) x + (horstart + 1);
				float endY = (float) (border - y) + graphheight + 2;

				if (i > 0) {
					// fill space between last and current point
					int numSpace = (int) ((endX - lastEndX) / 3f) + 1;
					for (int xi = 0; xi < numSpace; xi++) {
						float spaceX = (float) (lastEndX + ((endX - lastEndX)
								* xi / (numSpace - 1)));
						float spaceY = (float) (lastEndY + ((endY - lastEndY)
								* xi / (numSpace - 1)));

						// start => bottom edge
						float startX = spaceX;

						// do not draw over the left edge
						if (startX - horstart > 1) {
							canvas.drawLine(startX, startY, spaceX, spaceY,
									paintBackground);
						}
					}
				}

				lastEndY = endY;
				lastEndX = endX;
			}
		}

		// draw data
		lastEndY = 0;
		lastEndX = 0;

		List<Float> dotsX = new LinkedList<Float>();
		List<Float> dotsY = new LinkedList<Float>();
		paint.setAntiAlias(true);
		for (int i = 0; i < values.length; i++) {
			double y = calcYcoordinate(values[i].valueY, minY, diffY,
					graphheight);

			double x = calcXcoordinate(values[i].valueX, minX, diffX,
					graphwidth);

			dotsX.add(Float.valueOf((float) x));
			dotsY.add(Float.valueOf((float) (border - y) + graphheight));

			if (i > 0) {
				float startX = (float) lastEndX + (horstart + 1);
				float startY = (float) (border - lastEndY) + graphheight;
				float endX = (float) x + (horstart + 1);
				float endY = (float) (border - y) + graphheight;

				canvas.drawLine(startX, startY, endX, endY, paint);
			}

			if (signLastPoint && i == values.length - 1) {
				Paint paintD = new Paint(paint);
				paintD.setColor(Color.WHITE);
				paintD.setTypeface(signLastPointTypeface);
				paintD.setTextAlign(Align.CENTER);
				paintD.setTextSize(Float.valueOf((float) 20.93));
				canvas.drawText(String.valueOf(Math.round(values[i].valueY)), (float) x,
						(float) y + 60, paintD);
				paintD.setColor(Color.rgb(51, 153, 153));
				paintD.setTextSize(Float.valueOf((float) 13.82));
				canvas.drawText("POINTS", (float) x, (float) y + 75, paintD);
			}

			lastEndY = y;
			lastEndX = x;
		}
		if (drawSpecialBackground) {
			float startY = (float) calcYcoordinate(0, minY, diffY, graphheight);
			drawFilledPath(dotsX, dotsY, border - startY + graphheight, canvas);
		}
		drawDots(dotsX, dotsY, canvas);
	}

	public boolean getDrawBackground() {
		return drawBackground;
	}

	/**
	 * @param drawBackground
	 *            true for a light blue background under the graph line
	 */
	public void setDrawBackground(boolean drawBackground) {
		this.drawBackground = drawBackground;
	}

	public void drawDots(List<Float> dotsX, List<Float> dotsY, Canvas canvas) {

		Paint paintDots = new Paint(paint);

		Bitmap b = BitmapFactory.decodeResource(getResources(),
				verMan.getResourceId(0)); // for a first vertex load resources
		int idPrevios = verMan.getResourceId(0);
		int idCurrent;
		for (int i = 0; i < dotsX.size(); i++) {
			idCurrent = verMan.getResourceId(i);

			if (idCurrent != idPrevios) {
				b = BitmapFactory.decodeResource(getResources(), idCurrent);
			}

			if (b != null) {
				canvas.drawBitmap(b, (float) (dotsX.get(i) - b.getWidth() / 2),
						(float) (dotsY.get(i) - b.getHeight() / 2), paintDots);
			}
		}
	}

	public void drawFilledPath(List<Float> dotsX, List<Float> dotsY, Float y,
			Canvas canvas) {
		Paint paintBack = new Paint(paint);

		paintBack.setStyle(Paint.Style.FILL);
		paintBack.setColor(colorSpecialBackground);
		paintBack.setAlpha(alpha);

		Path path = new Path();

		path.moveTo(dotsX.get(0), y);

		int i = 0;
		for (i = 0; i < dotsX.size() && i < dotsY.size(); i++) {
			path.lineTo(dotsX.get(i), dotsY.get(i));
		}

		path.lineTo(dotsX.get(dotsX.size() - 1), y);

		canvas.drawPath(path, paintBack);
	}
}
