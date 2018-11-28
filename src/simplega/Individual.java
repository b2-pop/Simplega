package simplega;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import static simplega.SimpleGA.condLength;
import static simplega.SimpleGA.dataSet;

/**
 *
 * @author BPop
 */
public class Individual {

    public int[] gene;
    public int fitness;
    ArrayList<Rule> ruleSet = new ArrayList<Rule>();
    static int outputLocation = 6;// Location in dataset

    public Individual(int fitness, int[] gene) {
        this.fitness = fitness;
        this.gene = gene;
    }

    /**
     * Name: Individual Description: Creates a constructor for the
     * <see cref = "SimpleGA"/>.
     */
    Individual() {
        // Simple GA uses this constuctor.
    }

    public ArrayList<Rule> getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(ArrayList<Rule> ruleSet) {
        this.ruleSet = ruleSet;
    }

    public void createGene(int n) {
        gene = new int[n];
        this.fitness = 0; //resetting fitness
        for (int j = 0; j < n; j++) {
            int r;

            if (j != 0 && ((j + 1) % outputLocation) == 0) {
                r = new Random().nextInt(2); // output
            } else {
                r = new Random().nextInt(3); // Command
            }

            this.gene[j] = r;
        }
    }

    public int calcFitness(ArrayList<Rule> dataSet) {
        CreateIndividualRules();
        this.fitness = 0;

        for (int i = 0; i < dataSet.size(); i++) {
            for (int j = 0; j < this.ruleSet.size(); j++) {
                if (CompareTestDataWithRules(this.ruleSet.get(j).getCondition(), dataSet.get(i).getCondition())) {

                    if (this.ruleSet.get(j).getOutput() == dataSet.get(i).getOutput()) {
                        this.fitness++;
                    }
                    break;
                }
            }

        }
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int getFitness() {
        return this.fitness;
    }

    public int[] getGene() {
        return gene;
    }

    private boolean CompareTestDataWithRules(int[] rules, int[] trainingSet) {
        boolean isAMatch = true;

        if (rules.length == trainingSet.length) {

            for (int i = 0; i < rules.length; i++) {
                if (rules[i] != 2) {
                    if (rules[i] != trainingSet[i]) {
                        isAMatch = false;
                        i = dataSet.size();
                    }
                }
            }
        } else {
            isAMatch = false;
        }

        return isAMatch;
    }

    private void CreateIndividualRules() {
        this.ruleSet = new ArrayList<>();

        int[] rule;
        int j = 0;
        for (int i = 0; i < 10; i++) {              
            rule = new int[condLength];
            for (int k = 0; k < condLength; k++) {  
                rule[k] = this.gene[j];
                j++;
            }
            int out = this.gene[j];
            j++;
            Rule individualRule = new Rule(rule, out);

            this.ruleSet.add(individualRule);
        }
    }

    @Override
    public String toString() {
        return "Individual"
                + " | " + Arrays.toString(gene).replace(", ", " ").replace("[", "").replace("]", "")
                + " | FITNESS = " + fitness;
    }
}
