package com.jjoe64.graphview;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

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

	@Override
	public void drawSeries(Canvas canvas, GraphViewData[] values,
			float graphwidth, float graphheight, float border, double minX,
			double minY, double diffX, double diffY, float horstart) {
		// draw background
		double lastEndY = 0;
		double lastEndX = 0;
		if (drawBackground) {
			float startY = graphheight + border;
			for (int i = 0; i < values.length; i++) {
				double valY = values[i].valueY - minY;
				double ratY = valY / diffY;
				double y = graphheight * ratY;

				double valX = values[i].valueX - minX;
				double ratX = valX / diffX;
				double x = graphwidth * ratX;

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

		for (int i = 0; i < values.length; i++) {
			double valY = values[i].valueY - minY;
			double ratY = valY / diffY;
			double y = graphheight * ratY;

			double valX = values[i].valueX - minX;
			double ratX = valX / diffX;
			double x = graphwidth * ratX;

			dotsX.add(Float.valueOf((float) x));
			dotsY.add(Float.valueOf((float) (border - y) + graphheight));

			if (i > 0) {
				float startX = (float) lastEndX + (horstart + 1);
				float startY = (float) (border - lastEndY) + graphheight;
				float endX = (float) x + (horstart + 1);
				float endY = (float) (border - y) + graphheight;

				canvas.drawLine(startX, startY, endX, endY, paint);
			}
			lastEndY = y;
			lastEndX = x;
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
}
