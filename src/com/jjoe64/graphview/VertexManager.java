package com.jjoe64.graphview;

import android.util.SparseIntArray;
/**
 * This class links the resource id and vertex id.
 * @author Vadim
 *
 */
public class VertexManager {
	private SparseIntArray indexes = new SparseIntArray();
	
	public void addVertex(int resId, int[] indexes) {
		for (Integer index : indexes) {
			this.indexes.put(index, resId);
		}
	}
	
	/**
	 * 
	 * @param idVertex
	 * @return return 0 if resource for vertex not founded
	 */
	public int getResourceId(int idVertex){
		return indexes.get(idVertex);
	}
}