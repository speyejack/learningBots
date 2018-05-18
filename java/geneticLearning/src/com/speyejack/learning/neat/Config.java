package com.speyejack.learning.neat;

class Config {
	
	final static int Population = 10;
	final static double DeltaExcess = 1.0;
	final static double DeltaDisjoint = 1.0;
	final static double DeltaWeights = 1.0;
	final static double DeltaThreshold = 1.5;

	final static int StaleSpecies = 20;
	final static double GeneMutationChance = 0.05;
	final static double NodeMutationChance = 0.005;
	final static double PerturbChance = 0.05;
	final static double StepChance = 0.01;
	final static double StepSize = 0.05;
	final static double PreturbMutationChance = 0.05;
	final static double EnableMutationChange = 0.01;
	final static double DisableMutationChange = 0.01;
	
	final static String GenomeFileName = "genome.gnm";

}
