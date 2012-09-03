package com.jjoe64.graphview;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView.GraphAxisStyle;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.GraphViewSeries;

/**
 * GraphViewDemo creates some dummy data to demonstrate the GraphView component.
 *
 * IMPORTANT: For examples take a look at GraphView-Demos (https://github.com/jjoe64/GraphView-Demos)
 *
 * Copyright (C) 2011 Jonas Gehring
 * Licensed under the GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 */
public class GraphViewDemo extends Activity {
	/**
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LineGraphView graphView = new LineGraphView(
				this
				, "GraphViewDemo"
		);
		float[] intervals = new float[]{2.0f,2.0f};
		DashPathEffect effect = new DashPathEffect(intervals, 2);
		graphView.setAxisVert(new GraphAxisStyle(Color.BLUE, 0, effect));
		
		
		GraphViewData[] data = new GraphViewData[] {
				new GraphViewData(24d, 100d)
				, new GraphViewData(25d, 150d)
				, new GraphViewData(26d, 200d)
				, new GraphViewData(27d, 150d)
				, new GraphViewData(28d, 250d)
				, new GraphViewData(29d, 200d)
				, new GraphViewData(30d, 150d)
		};
		int[] indexes = new int[data.length-1];
		for(int i=0;i<2/*indexes.length-1*/;i++){
			indexes[i] = i;
		}
		VertexManager manager = new VertexManager();
		manager.addVertex(R.drawable.point, indexes);
		manager.addVertex(R.drawable.big_point, new int[]{data.length-1});
		String[] labels = {"24 okt", "25 okt"};
		graphView.setHorizontalLabels(labels);
		String[] labels2 = {"300","200", "100"};
		graphView.setVerticalLabels(labels2);
		graphView.addSeries(new GraphViewSeries(data));
		graphView.setManager(manager);
		setContentView(graphView);
	}
}
