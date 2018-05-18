package com.speyejack.learning.neat;

import java.util.List;

import javax.management.RuntimeErrorException;

public class Organisum {
	private Genome genome; // Genotype
	private Network network; // Phenotype
	private List<Neuron> inputs;

	public Organisum(Genome g) {
		genome = g;
		network = genome.buildNetwork();
		inputs = network.getInputs();
	}

	public List<Neuron> evalutate(List<Double> inputs) {
		if (inputs.size() != this.inputs.size())
			throw new RuntimeErrorException(new Error("Invalid input size"));
		return network.evalutate(inputs);
	}

	public double[] evalutate(double[] inputs) {
		if (inputs.length != this.inputs.size())
			throw new RuntimeErrorException(new Error("Invalid input size"));
		List<Neuron> outputsL = network.evalutate(inputs);
		double[] outputs = new double[outputsL.size()];
		for (int i = 0; i < outputsL.size(); i++) {
			outputs[i] = outputsL.get(i).getValue();
		}
		return outputs;
	}

	public void setFitness(double fitness) {
		genome.setFitness(fitness);
		network.setFitness(fitness);
	}
	
	public void addFitness(double fitness){
		genome.addFitness(fitness);
		network.addFitness(fitness);
	}

}
