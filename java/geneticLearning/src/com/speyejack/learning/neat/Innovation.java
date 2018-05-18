package com.speyejack.learning.neat;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Innovation implements Serializable{
	private static final long serialVersionUID = 4527372854080147954L;
	private int inno = 0;
	private Map<Dimension, Integer> innovations = new HashMap<Dimension, Integer>();

	public int getInnovation(int in, int out) {
		Dimension dim = new Dimension(in, out);
		if (!innovations.containsKey(dim))
			innovations.put(dim, inno++);
		return innovations.get(dim);
	}
	
	public void clearInnovation(){
		innovations.clear();
	}

}
