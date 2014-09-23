//  NSGAIIRunner.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2014 Antonio J. Nebro
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal3.metaheuristic.multiobjective.nsgaii;

import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal3.core.Algorithm;
import org.uma.jmetal3.core.Problem;
import org.uma.jmetal3.core.Solution;
import org.uma.jmetal3.operator.crossover.CrossoverOperator;
import org.uma.jmetal3.operator.crossover.impl.IntegerSBXCrossover;
import org.uma.jmetal3.operator.mutation.MutationOperator;
import org.uma.jmetal3.operator.mutation.impl.IntegerPolynomialMutation;
import org.uma.jmetal3.operator.selection.SelectionOperator;
import org.uma.jmetal3.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal3.problem.multiobjective.NMMin;
import org.uma.jmetal3.util.AlgorithmRunner;
import org.uma.jmetal3.util.fileoutput.DefaultFileOutputContext;
import org.uma.jmetal3.util.fileoutput.SolutionSetOutput;

import java.util.List;

/**
 * Class to configure and execute the NSGA-II algorithm (including Steady State and parallel versions)
 */
public class NSGAIIRunner2 {
  /**
   * @param args Command line arguments.
   * @throws org.uma.jmetal.util.JMetalException
   * @throws java.io.IOException
   * @throws SecurityException
   * @throws ClassNotFoundException
   * Usage: three options
   *        - org.uma.jmetal.runner.multiobjective.NSGAIIRunner
   *        - org.uma.jmetal.runner.multiobjective.NSGAIIRunner problemName
   *        - org.uma.jmetal.runner.multiobjective.NSGAIIRunner problemName paretoFrontFile
   */
  public static void main(String[] args) throws
          Exception {

    Problem problem;
    Algorithm algorithm;
    CrossoverOperator crossover;
    MutationOperator mutation;
    SelectionOperator selection;

    problem = new NMMin();

    crossover = new IntegerSBXCrossover.Builder()
            .setDistributionIndex(20)
            .setProbability(0.9)
            .build() ;

    mutation = new IntegerPolynomialMutation.Builder()
            .setProbability(1.0 / problem.getNumberOfVariables())
            .build();

    selection = new BinaryTournamentSelection.Builder()
            .build();

    algorithm = new NSGAIITemplate.Builder(problem)
            .setCrossover(crossover)
            .setMutation(mutation)
            .setSelection(selection)
            .setMaxEvaluations(25000)
            .setPopulationSize(100)
            .setVariant("NSGAII")
            .build() ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

    List<Solution> population = algorithmRunner.getSolutionSet() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    new SolutionSetOutput.Printer(population)
            .setSeparator("\t")
            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
            .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

  }
}
