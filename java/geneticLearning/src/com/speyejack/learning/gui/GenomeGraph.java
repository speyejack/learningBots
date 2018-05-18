package com.speyejack.learning.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import com.speyejack.learning.neat.Genome;
import com.speyejack.learning.neat.Link;
import com.speyejack.learning.neat.Network;
import com.speyejack.learning.neat.Neuron;

public class GenomeGraph extends JPanel implements ActionListener {
	private static final long serialVersionUID = -4801800601757690481L;
	private static final double NEURON_SIZE = 0.03;

	private List<JCheckBox> inputs = new ArrayList<JCheckBox>();
	private List<Neuron> neurons;
	private Network network;
	private int numInputs;
	private int numOutputs;
	private Map<Neuron, double[]> map;
	private List<Link> links;
	private List<Neuron> inputsN;
	private Genome g;

	public GenomeGraph(Dimension dim) {
		this.setLayout(null);
		this.setPreferredSize(dim);
	}

	public void setGenome(Genome g) {
		this.g = g;
		this.removeAll();
		inputs.clear();
		numOutputs = g.getNumOutputs();
		network = g.buildNetwork();
		inputsN = network.getInputs();
		numInputs = inputsN.size();
		for (int i = 0; i < numInputs; i++) {
			JCheckBox box = new JCheckBox();
			int width = adjustWidth((double) (i + 1) / (numInputs + 1) - NEURON_SIZE / 2);
			int height = adjustHeight(0.01 - NEURON_SIZE / 2);
			int size = adjustHeight(0.05);
			box.setBounds(width, height, size, size);
			box.addActionListener(this);
			inputs.add(box);
			this.add(box);
		}
		JSlider s = new JSlider(SwingConstants.VERTICAL,0, 100, 100);
		s.setBounds(adjustHeight(0.01), adjustHeight(0.01), adjustWidth(0.1), adjustHeight(0.1));
		this.add(s);
		// inputs.get(inputs.size()-1).setSelected(true);
		neurons = network.getNeurons();
		map = new HashMap<Neuron, double[]>();
		links = new ArrayList<Link>();

		for (int i = 0; i < numInputs; i++) {
			map.put(neurons.get(i), new double[] { (double) (i + 1) / (numInputs + 1), 0.01 });
		}

		int size = neurons.size() - numInputs - numOutputs;
		for (int i = numInputs; i < neurons.size() - numOutputs; i++) {
			double pos = (double) (i - numInputs + 1) / (size + 1);
			map.put(neurons.get(i), new double[] { Math.random(), pos });
			links.addAll(neurons.get(i).getLinks());
		}

		int startIndex = neurons.size() - numOutputs;
		for (int i = startIndex; i < neurons.size(); i++) {
			map.put(neurons.get(i), new double[] { 1.0 / (numOutputs + 1) * (i - startIndex + 1), 0.95 });
			links.addAll(neurons.get(i).getLinks());
		}

		network.evalutate(new double[] { 1, 1, 1 });
		network.evalutate(new double[] { 0, 0, 0 });
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (network == null)
			return;
		double[] inputs = new double[this.inputs.size()];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = (this.inputs.get(i).isSelected() ? 1 : 0);
		}
		network.evalutate(inputs);

		for (int i = 0; i < links.size(); i++) {
			Neuron n1 = links.get(i).getInto();
			Neuron n2 = links.get(i).getOut();
			g.setColor(getColor(links.get(i).getWeight()));
			g.drawLine(adjustWidth(map.get(n1)[0]), adjustHeight(map.get(n1)[1]), adjustWidth(map.get(n2)[0]),
					adjustHeight(map.get(n2)[1]));
		}

		for (int i = numInputs; i < neurons.size(); i++) {
			Neuron n = neurons.get(i);
			double[] pos = map.get(n);
			g.setColor(getColor(n.getValue()));
			g.drawString(Integer.toString(n.getId()), adjustWidth(pos[0] - NEURON_SIZE / 2),
					adjustHeight(pos[1] - NEURON_SIZE / 2));
			g.fillRect(adjustWidth(pos[0] - NEURON_SIZE / 2), adjustHeight(pos[1] - NEURON_SIZE / 2),
					adjustWidth(NEURON_SIZE), adjustHeight(NEURON_SIZE));
			g.setColor(Color.black);
			g.drawRect(adjustWidth(pos[0] - NEURON_SIZE / 2), adjustHeight(pos[1] - NEURON_SIZE / 2),
					adjustWidth(NEURON_SIZE), adjustHeight(NEURON_SIZE));
		}

	}

	public boolean debug() {
		return network.debug();
	}

	private Color getColor(double value) {
		Color color;
		if (value > 0)
			color = new Color(0, 255, 0);
		else if (value == 0)
			color = new Color(255, 0, 0, 100);
		else if (value < 0)
			color = new Color(255, 0, 0);
		else
			color = new Color(0, 0, 0);
		return color;
	}

	private int adjustWidth(double width) {
		return (int) (this.getPreferredSize().getWidth() * width);
	}

	private int adjustHeight(double height) {
		return (int) (this.getPreferredSize().getHeight() * height);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
}
