package com.github.gkane1234;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class CountingMain {
    public static void main(String[] args) {
        System.out.println("Max Memory: " + Runtime.getRuntime().maxMemory());
        System.out.println("Total Memory: " + Runtime.getRuntime().totalMemory());
        System.out.println("Free Memory: " + Runtime.getRuntime().freeMemory());

        long startTime = System.currentTimeMillis();

        testExpressionCompression(false,true);

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: "+(endTime-startTime)/1000.0+" seconds");

        


        //System.err.println(expressionSetDB);
        
        // Start time

        
        //long startTime = System.nanoTime();  

        //84.0, 18.0, 78.0, 91.0, 61.0, 87.0, to make 2027
        //(((((18/78)+84)-61)*91)-87)

        // 3.0, 4.0, 9.0, 14.0, 7.0, 16.0, to make 2027
        //((((4/7)+(16*9))*14)+3) 1=
        
        //{791,27,18632,1,19.315,-793.2,-8537.214}

        
        /*
         * For example the inequivalent expressions using the 2 variables a and b would be  f(a)⊕f(b) or    {a+b,a-b,b-a,ab,a/b,b/a} – these expressions can be evaluated for imputed values, such as at (5,12), which would evaluate to 17, -7, 7, 60, 5/12, and 12/5 respectively. So for 4 numbers, a,b,c, and d the space to check should be:

         * f(a)⊕f(b,c,d), f(b)⊕f(a,c,d), f(c)⊕f(a,b,d), f(d)⊕f(a,b,c)
         * f(a,b)⊕f(c,d), f(a,c)⊕f(b,d), f(a,d)⊕f(b,c)
         * 
         * 
         */


        /* 24
        int[] range = {1,13};
        List<SolutionSet> questions = s.findSolvableValues(10,137,range,new int[]{1,10000});
        for (SolutionSet question : questions) {
            System.out.println(question);
        }
        */

        /*
        Solver s = new Solver(numValues); 
        List<SolutionSet> allSolutions = s.findAllPossibleSolvableValuesInRange(range, 24, true);

        SolutionWriter solutionWriter = new SolutionWriter("counting_operations/outputs", allSolutions, false);
        solutionWriter.createFile();
        */
        

 
        
        
    }
    /*
     * This method is for when compression is changed to update the compressed files using the uncompressed files.
     */
    public static void updateCompressions() {

        for (int i = 1; i <= 7; i++) {
            try {
                ExpressionList e = ExpressionList.load(i);
                ExpressionCompression.compressExpressionList(e);
                ExpressionList.saveCompressed(e, true);
            } catch (FileNotFoundException e) {
                System.err.println("File not found for "+i);
            }
        }
    }
    public static void inputGoal(int numValues) {
        Scanner scanner = new Scanner(System.in);
        Solver s = new Solver(numValues, true, true, null, true);
        while (true) {
            System.out.print("Enter a goal value (or type 'exit' to quit): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                double goal = Double.parseDouble(input);
                try {
                    SolutionList solutions = s.findSolvableValues(goal, new int[]{1,15}, new int[]{1,10000});
                    System.out.println(solutions.getNumSolutions()+" solutions found for this goal.");
                    System.out.println(Arrays.toString(solutions.getValues()));
                    System.out.println("Input anything to see the first solution");
                    scanner.nextLine();
                    System.out.println(solutions.getEvaluatedExpression());
                } catch (Exception e) {
                    System.out.println("No solutions found for this goal.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter valid numbers.");
            }
        }
        scanner.close();
    }
    public static void inputValuesForGoalAndSolutions(int numValues, int[] solutionRange) {
        if (solutionRange==null) {
            solutionRange = new int[]{1,10000};
        }
        if (numValues==0) {
            numValues = 7;
        }
        
        Scanner scanner = new Scanner(System.in);
        Solver s = new Solver(numValues, false, true, null, true);

        while (true) {
            System.out.print("Enter a goal value (or type 'exit' to quit): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                double goal = Double.parseDouble(input);
                
                System.out.print("Enter " + numValues + " values separated by spaces: ");
                String[] valueStrings = scanner.nextLine().trim().split("\\s+");
                
                if (valueStrings.length != numValues) {
                    System.out.println("Please enter exactly " + numValues + " values.");
                    continue;
                }

                double[] values = new double[numValues];
                for (int i = 0; i < numValues; i++) {
                    values[i] = Double.parseDouble(valueStrings[i]);
                }

                SolutionList solutions = s.findAllSolutions(values, goal, solutionRange[1]);
                
                if (solutions.getNumSolutions() == 0) {
                    System.out.println("No solutions found for these values and goal.");
                } else {
                    System.out.println("Found " + solutions.getNumSolutions() + " solutions:");
                    for (EvaluatedExpression solution : solutions.getEvaluatedExpressionList()) {
                        System.out.println(solution.display() + " = " + goal);
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter valid numbers.");
            }
        }
        scanner.close();
    }
    public static void inputValuesForFirstInRange(int numValues, int[] range) {
        if (range==null) {
            range = new int[]{1,100000};
        }
        if (numValues==0) {
            numValues = 7;
        }
        long startTime = System.nanoTime();  // Use System.nanoTime() for higher precision
        Scanner scanner = new Scanner(System.in);
        Solver s = new Solver(numValues, false, true, null, true);

        while (true) {
            System.out.print("Enter a new goal value (or type 'exit' to quit): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                //double goal = Double.parseDouble(input);
                
                System.out.print("Enter " + numValues + " values separated by spaces: ");
                String[] valueStrings = scanner.nextLine().trim().split("\\s+");
                
                if (valueStrings.length != numValues) {
                    System.out.println("Please enter exactly " + numValues + " values.");
                    continue;
                }

                double[] values = new double[numValues];
                for (int i = 0; i < numValues; i++) {
                    values[i] = Double.parseDouble(valueStrings[i]);
                }

                s.findFirstInRange(values, range, true, true);
                /*
                if (solutions.getNumSolutions() == 0) {
                    System.out.println("No solutions found for these values and goal.");
                } else {
                    System.out.println("Found " + solutions.getNumSolutions() + " solutions:");
                    for (Solution solution : solutions.getSolutions()) {
                        System.out.println(solution.display());
                        }
                    }
                */

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter valid numbers.");
            }
        }

        scanner.close();
    






        long endTime = System.nanoTime();

        long durationInNano = endTime - startTime;
        double durationInMillis = durationInNano / 1_000_000.0;  // Convert nanoseconds to milliseconds


        System.out.println("Execution time: " + durationInNano + " nanoseconds");
        System.out.println("Execution time: " + durationInMillis + " milliseconds");
        //System.err.println(expressionList);
        //System.err.println(expressionList.getExpressions().size());
        //System.err.println(Counter.run(numValues));
    }
    public static void testCompressionOfEntireExpressionList(int numValues) {
        ExpressionList e1 = null;
        try {
            e1 = ExpressionList.load(numValues, Counter.run(numValues).intValue(), false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        ExpressionList.saveCompressed(e1, true);
        ExpressionList e2 = null;
        try {
            e2 = ExpressionList.loadCompressed(numValues);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (e1.equals(e2)) {
            System.err.println("Expression sets are equal");
        } else {
            System.err.println("Expression sets are not equal");
        }
    }
    public static void testExpressionCompression(boolean load, boolean compressed) {
        for (int i = 1; i <= 7; i++) {
            System.err.println("Testing "+i);
            testExpressionCompressionOneValue(i, 100, load, compressed);
        }
    }
    public static void testExpressionCompressionOneValue(int numValues,int numExpressions, boolean load, boolean compressed) {

        Solver s = new Solver(numValues, false, load, null, compressed);
        Random r = new Random();

       



        Expression[] expressions = new Expression[numExpressions];
        System.err.println("Testing "+numExpressions+" expressions");
        for (int i = 0; i < numExpressions; i++) {
            int rIndex = (int)(r.nextDouble()*s.solverSet.getNumExpressions());
            Expression randomExpression = s.solverSet.get(rIndex);
            long compressedExpression = ExpressionCompression.compressExpression(randomExpression);
            Expression decompressedExpression  = ExpressionCompression.decompressExpression(compressedExpression, numValues);
            expressions[i]=randomExpression;
            if (!randomExpression.equals(decompressedExpression)) {
                System.err.println("Expressions are not equal");
                System.err.println(randomExpression);
                System.err.println(decompressedExpression);
            }
        }
        System.err.println("Done testing expressions");
        System.err.println("Compressing expression set");
        ExpressionSet e = new ExpressionSet(expressions,expressions.length,numValues);
        
        CompressedExpressionList compressedExpressionList = ExpressionCompression.compressExpressionList(e);

        ExpressionList decompressedExpressionList = compressedExpressionList.decompress();
        if (!e.equals(decompressedExpressionList)) {
            System.err.println("Expression lists are not equal");
        }
        System.err.println("Done testing expression set");
        
        
    }


}