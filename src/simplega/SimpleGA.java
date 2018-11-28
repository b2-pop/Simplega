package simplega;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BPop
 */
public class SimpleGA {

    static final int POPSIZE = 100;          // Population size
    static final int GENESIZE = 80;         // Chromosome size
    static final int GENERATIONNUM = 50;    // Generation number
    static final int condLength = 7;        // Condition length
    static final int DATAROW = 64;          // Amount of data rows within the file.
    static final int RULENUM = 5;          // Rule size
    static final int ruleLength = 8;        // The length of the condition and the action together.
    static final double MUTATIONRATE = 0.02;// Sets the mutation rate.    
    static ArrayList<Rule> dataSet = new ArrayList<Rule>();
    static Individual[] population = new Individual[POPSIZE];
    static Individual[] offspring = new Individual[POPSIZE];
    static Individual[] cross = new Individual[POPSIZE];
    static Individual[] mutate = new Individual[POPSIZE]; 
    //static String dataFile = "src//simplega//data1.txt";
    static String dataFile = "src//simplega//data2.txt";
    

    public static void main(String[] args) {

        
        // Load data to dataSet ArrayList
        LoadData();
        generateGenes(population);
        //Loop through Generations
        for (int gen = 1; gen <= GENERATIONNUM; gen++) {
           //System.out.println("GENERATION NUMBER: " + gen );

            TournamentSelection(population, offspring);

            offspring = crossover(offspring, cross);

            offspring = MutatePopulation(offspring, mutate);

            ReCalculateFitnessOf(offspring);

            population = EndOfGenerationSelection();

            PrintGenerationInformation(population);
        }
    }

    private static Individual[] EndOfGenerationSelection() {
        //retrieves the best and swaps it
        int popFittestIndex = 0;
        int offspringLowestFitIndex = 0;
        Individual Temp;

        popFittestIndex = RetrieveTheIndexOfTheBestIndividual(population);
        Temp = population[popFittestIndex];

        for (int i = 0; i < POPSIZE; i++) {
            population[i] = offspring[i];
        }
        
        offspringLowestFitIndex = RetrieveTheIndexOfTheWorstIndividual(population);
        population[offspringLowestFitIndex] = Temp;

        return population;
    }

    private static int RetrieveTheIndexOfTheBestIndividual(Individual[] pop) {
        int fittest = 0;
        int fittestIndex = 0;
        //best population
        for (int i = 0; i < pop.length; i++) {
            if (fittest < pop[i].getFitness()) {
                fittest = pop[i].getFitness();
                fittestIndex = i;
            }
        }

        return fittestIndex;
    }
    
    private static int RetrieveTheIndexOfTheWorstIndividual(Individual[] pop) {
        int worst = 0;
        int worstIndex = 0;
        //best population
        for (int i = 0; i < pop.length; i++) {
            if (worst > pop[i].getFitness()) {
                worst = pop[i].getFitness();
                worstIndex = i;
            }
        }

        return worstIndex;
    }

    private static Individual[] MutatePopulation(Individual[] offspring, Individual[] mutate) {
        //Mutation
        // Iterate through populations
        for (int i = 0; i < POPSIZE; i++) {

            // mutates each generation
            int[] child = MutateIndividual(offspring[i].getGene());

            mutate[i] = new Individual(0, child);

        }
        // assign offspring to the newly mutated genes
        offspring = mutate;
        return offspring;
    }

    private static Individual[] crossover(Individual[] offspring, Individual[] cross) {
        // Crossover
        // Create 2 parents genes list
        int[] par1;
        int[] par2;
        
        for (int i = 0; i < POPSIZE; i++) {
            // Iterate through each chromosomes
            for (int j = 0; j < GENESIZE; j++) {

                par1 = offspring[new Random().nextInt(POPSIZE)].getGene();
                par2 = offspring[new Random().nextInt(POPSIZE)].getGene();

                // Perform crossover 
                int[] child = ChangeValuesOnPoint(par1, par2);

                cross[i] = new Individual(0, child);
            }

        }
        offspring = cross;
        return offspring;
    }

    private static void generateGenes(Individual[] population) {
        for (int i = 0; i < POPSIZE; i++) {
            Individual individual = new Individual();
            individual.createGene(GENESIZE);
            individual.calcFitness(dataSet);
            population[i] = individual;
        }
    }

    private static void TournamentSelection(Individual[] population, Individual[] offspring) {
        int fit;
        int fittest;
        // Selection 
        fit = 0;
        fittest = 0;
        for (int i = 0; i < POPSIZE; i++) {     
            for (int j = 0; j < GENESIZE; j++) {   

                // create 2 parents
                int par1 = new Random().nextInt(POPSIZE);
                int par2 = new Random().nextInt(POPSIZE);

                // if parent1 fitness is better then parent2.
                if (population[par1].getFitness() >= population[par2].getFitness()) {
                    offspring[i] = population[par1];
                } else {
                    offspring[i] = population[par2];
                }

            }

        }
    }

    private static void PrintGenerationInformation(Individual[] population) {
        for (Individual population1 : population) {
            population1.calcFitness(dataSet);
        }

        int fitness = 0;
        int totalFit = 0;

        for (int i = 0; i < POPSIZE; i++) {

            if (population[i].fitness > fitness) {
                fitness = population[i].fitness;
            }
            totalFit += population[i].fitness;
        }
//        System.out.println("FITTEST: " + fitness );
//        System.out.println("AVG: " + (totalFit / POPSIZE)); 
//        System.out.println("");
          System.out.println(fitness + " " +(totalFit / POPSIZE));
    }

    private static ArrayList<Rule> LoadData() {
        //load data from data sets
        String line;
        Scanner scan = null;
        ArrayList data = new ArrayList();

        try {
            scan = new Scanner(new File(dataFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleGA.class.getName()).log(Level.SEVERE, null, ex);
        }

        scan.nextLine();                                
        while (scan.hasNextLine()) {
            line = scan.nextLine().replace(" ", "");    
            data.add(line);                             // add to ArrayList
        }

        char aChar = ' ';
        for (int i = 0; i < DATAROW; i++) {
            int[] rule = new int[condLength];
            for (int j = 0; j < condLength; j++) {
                aChar = data.get(i).toString().charAt(j);       
                rule[j] = Character.getNumericValue(aChar);     
            }
            int out = Character.getNumericValue(data.get(i).toString().charAt((condLength))); //get output
            Rule objData = new Rule(rule, out);
            dataSet.add(objData);
        }
        return dataSet;
    }

    private static int[] MutateIndividual(int[] mutate) {
        int[] mutChild = new int[mutate.length];
        float ranRate = 0;
        double rate = 0;
        Random rand = new Random();
        int randomNumber = rand.nextInt(2);
        for (int i = 0; i < mutate.length; i++) {
            ranRate = new Random().nextFloat();
            if (ranRate < MUTATIONRATE) {
                if ((i + 1) == ruleLength) {
                    if (mutate[i] == 0) {
                        mutChild[i] = 1;
                    } else if (mutate[i] == 1) {

                        mutChild[i] = 0;
                    } else {
                        if (randomNumber == 0) {
                            mutChild[i] = 0;
                        } else {

                            mutChild[i] = 1;
                        }
                    }
                }
                if (mutate[i] == 0) {
                    
                    mutChild[i] = new Random().nextInt(2) + 1;
                }
                if (mutate[i] == 1) {
                    boolean check = new Random().nextBoolean();
                    if (check) {
                        mutChild[i] = 0;
                    } else {
                        mutChild[i] = 2;
                    }
                }
                if (mutate[i] == 2) {
                    mutChild[i] = new Random().nextInt(2);
                }
            } else {
                mutChild[i] = mutate[i];
            }
        }
        return mutChild;
    }

    private static int[] ChangeValuesOnPoint(int[] par1, int[] par2) {
        int[] child = new int[par1.length];
        int selecPoint = new Random().nextInt(GENESIZE);

        for (int i = 0; i < par1.length; i++) {
            if (i < selecPoint) {
                child[i] = par1[i];
            } else {
                child[i] = par2[i];
            }
        }
        return child;
    }

    private static void ReCalculateFitnessOf(Individual[] pop) {
        for (Individual individual : pop) {
            individual.calcFitness(dataSet);
        }
    }
}
