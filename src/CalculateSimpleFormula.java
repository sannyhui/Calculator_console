import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * This program is to calculate simple formula
 */
public class CalculateSimpleFormula {
    public static void main(String args[]) {
        /*
         * Declare variables and objects
         */
        Scanner myObj = new Scanner(System.in);
        MainProcess callProcess = new MainProcess();
        Boolean runProgram = true;
        /*
         * Call helpScreen method
         */
        callProcess.helpScreen();
        /*
         * User have to type 0 at the beginning to exit the while loop
         */
        while (runProgram) {
            System.out.println("Input the formula here -> ");
            String formulaString = myObj.nextLine();
            runProgram = callProcess.calculateFormula(formulaString);
        }
        // Message before exit application
        System.out.println("Good Bye");
    }
}

class MainProcess {
    /*
     * Declare class variable and 2d ArrayList
     */
    ArrayList<ArrayList<String>> formulaByString = new ArrayList<ArrayList<String>>();
    boolean runProgram = true;

    /**
     * This is the method to display help when ? is entered.
     */
    public void helpScreen() {
        System.out.println("? to display help, 0 to quit.");
        System.out.println("Supported operator: + - * / % ^");
        System.out.println("Supported trigonometric: sin cos tan");
    }

    /**
     * This is the method to validate the syntax of the formula.
     * If variable error > 0, the formula is invalid.
     */
    public int checkValidation (String formulaType, String formulaString) {
        int error = 0;
        //System.out.println(formulaString);
        switch (formulaType) {
            case "Value":
                try {
                    Double a = Double.parseDouble(formulaString);
                } catch (NumberFormatException e) {
                    error = 1;
                }
                break;
            case "Operator":
                error = 0;
                break;
            case "Trigonometric":
                if (Objects.equals(formulaString, "sin") || Objects.equals(formulaString, "cos") ||
                        Objects.equals(formulaString, "tan")) {
                    error = 0;
                } else {
                    error = 1;
                }
                break;
            default: error = 1;
        }
        return error;
    }

    /**
     * This is the method to remove records from the ArrayList "formulaString"
     * and put the calculated result back to the formula.
     */
    public boolean reduceFormula (int indexNumber, double changedValue) {
        formulaByString.remove(indexNumber + 1);
        formulaByString.remove(indexNumber);
        formulaByString.remove(indexNumber - 1);
        formulaByString.add(indexNumber - 1, new ArrayList<String>());
        formulaByString.get(indexNumber - 1).add(0,"Value");
        formulaByString.get(indexNumber - 1).add(1, Double.toString(changedValue));
        return true;
    }

    /**
     * This is the function to calculate the formula
     */
    public Boolean calculateFormula(String formulaString) {
        formulaByString.clear();
        String trigonometricString = "";
        String numberString = "";
        int j = 0;

        /*
         * Divide strings into 2d ArrayList with types and strings.
         */
        for (char i: formulaString.toCharArray()) {
            j += 1;
            /*
             * Check for numeric and dot
             */
            if (i >= '0' && i <='9' || i == '.') {
                numberString += Character.toString(i);
                if (trigonometricString != "") {
                    formulaByString.add(new ArrayList<String>());
                    formulaByString.get(formulaByString.size()-1).add(0,"Trigonometric");
                    formulaByString.get(formulaByString.size()-1).add(1, trigonometricString);
                    trigonometricString = "";
                }
                if (j == formulaString.length()) {
                    formulaByString.add(new ArrayList<String>());
                    formulaByString.get(formulaByString.size()-1).add(0,"Value");
                    formulaByString.get(formulaByString.size()-1).add(1, numberString);
                }
            /*
             * Check for operators
             */
            } else if (i == '+' || i == '-' || i == '*' || i == '/' || i == '^' || i == '%') {
                if (j == 1 && i == '-') {
                    numberString += Character.toString(i);
                } else {
                    if (numberString != "") {
                        formulaByString.add(new ArrayList<String>());
                        formulaByString.get(formulaByString.size()-1).add(0,"Value");
                        formulaByString.get(formulaByString.size()-1).add(1, numberString);
                        numberString = "";
                    }
                    if (trigonometricString != "") {
                        formulaByString.add(new ArrayList<String>());
                        formulaByString.get(formulaByString.size()-1).add(0,"Trigonometric");
                        formulaByString.get(formulaByString.size()-1).add(1, trigonometricString);
                        trigonometricString = "";
                    }
                    formulaByString.add(new ArrayList<String>());
                    formulaByString.get(formulaByString.size()-1).add(0,"Operator");
                    formulaByString.get(formulaByString.size()-1).add(1, Character.toString(i));
                }
            /*
             * Other characters except space
             */
            } else if (i != ' ') {
                trigonometricString += Character.toString(i);
                if (j == formulaString.length()) {
                    formulaByString.add(new ArrayList<String>());
                    formulaByString.get(formulaByString.size()-1).add(0,"Trigonometric");
                    formulaByString.get(formulaByString.size()-1).add(1, trigonometricString);
                }
            }
        }
        /*
         * Validate formula
         */
        int errorDetected = 0;
        if (formulaByString.size() > 1) {
            for (int i = 0; i < (formulaByString.size() - 1); i++) {
                int errorFound = checkValidation(formulaByString.get(i).get(0), formulaByString.get(i).get(1));
                errorDetected += errorFound;
            }
            for (int i = 0; i < (formulaByString.size() - 1); i++) {
                switch (formulaByString.get(i).get(0)) {
                    case "Value":
                        if (formulaByString.get(i + 1).get(0) != "Operator") errorDetected++;
                        break;
                    case "Operator":
                        if (formulaByString.get(i + 1).get(0) != "Value" &&
                            formulaByString.get(i + 1).get(0) != "Trigonometric") errorDetected++;
                        break;
                    case "Trigonometric":
                        if (formulaByString.get(i + 1).get(0) != "Value") errorDetected++;
                        break;
                }
            }
        } else {
            errorDetected ++; //formula size is less than 2
        }
        /*
         * ? to show help, 0 to quit
         */
        if (formulaString.startsWith("?")) errorDetected = 8888;
        if (formulaString.startsWith("0")) errorDetected = 9999;
        /*
         * If no error found, do the following code.
         */
        if (errorDetected == 0) {
            ArrayList<ArrayList<String>> formulaForChecking = new ArrayList<ArrayList<String>>();
            boolean calculateChecking = true;
            double mergeTrigonometric = 0.0000001;
            /*
             * Calculate trigonometric values (Sin, Cos, Tan) in first priority
             */
            while (calculateChecking) {
                formulaForChecking.clear();
                formulaForChecking.addAll(formulaByString);
                calculateChecking = false;
                for (int i = 0; i < (formulaForChecking.size() - 1); i++) {
                    if (formulaForChecking.get(i).get(0) == "Trigonometric") {
                        switch (formulaForChecking.get(i).get(1)) {
                            case "sin":
                                mergeTrigonometric =
                                    java.lang.Math.sin(Math.toRadians(Double.valueOf(formulaForChecking.get(i + 1).get(1))));
                                break;
                            case "cos":
                                mergeTrigonometric =
                                    java.lang.Math.cos(Math.toRadians(Double.valueOf(formulaForChecking.get(i + 1).get(1))));
                                break;
                            case "tan":
                                mergeTrigonometric =
                                    java.lang.Math.tan(Math.toRadians(Double.valueOf(formulaForChecking.get(i + 1).get(1))));
                                break;
                        }
                        formulaByString.remove(i + 1);
                        formulaByString.remove(i);
                        formulaByString.add(i, new ArrayList<String>());
                        formulaByString.get(i).add(0,"Value");
                        formulaByString.get(i).add(1, Double.toString(mergeTrigonometric));
                        calculateChecking = true;
                        //System.out.println(mergeTrigonometric);
                        break;
                    }
                }
            }

            /*
             * Calculate "^" "%" operators in second priority
             */
            calculateChecking = true;
            while (calculateChecking) {
                calculateChecking = false;
                formulaForChecking.clear();
                formulaForChecking.addAll(formulaByString);
                double calculateFirst = 0.0;
                for (int i = 0; i < (formulaForChecking.size() - 1); i++) {
                    if (Objects.equals(formulaForChecking.get(i).get(1), "^")) {
                        calculateFirst = Math.pow(Double.valueOf(formulaForChecking.get(i - 1).get(1)),
                                Double.valueOf(formulaForChecking.get(i + 1).get(1)).intValue());
                        calculateChecking = reduceFormula(i, calculateFirst);
                        //System.out.println("Hello");
                        break;
                    } else if (Objects.equals(formulaForChecking.get(i).get(1), "%")) {
                        calculateFirst = Double.valueOf(formulaForChecking.get(i - 1).get(1)) %
                                Double.valueOf(formulaForChecking.get(i + 1).get(1));
                        calculateChecking = reduceFormula(i, calculateFirst);
                        break;
                    }
                }
            }
            /*
             * Calculate "*" "/" operators in third priority
             */
            calculateChecking = true;
            while (calculateChecking) {
                calculateChecking = false;
                formulaForChecking.clear();
                formulaForChecking.addAll(formulaByString);
                double calculateFirst = 0.0;
                for (int i = 0; i < (formulaForChecking.size() - 1); i++) {
                    if (Objects.equals(formulaForChecking.get(i).get(1), "*")) {
                        calculateFirst = Double.valueOf(formulaForChecking.get(i - 1).get(1)) *
                                Double.valueOf(formulaForChecking.get(i + 1).get(1));
                        calculateChecking = reduceFormula(i, calculateFirst);
                        break;
                    } else if (Objects.equals(formulaForChecking.get(i).get(1), "/")) {
                        calculateFirst = Double.valueOf(formulaForChecking.get(i - 1).get(1)) /
                                Double.valueOf(formulaForChecking.get(i + 1).get(1));
                        calculateChecking = reduceFormula(i, calculateFirst);
                        break;
                    }
                }
            }
            /*
             * Calculate "+" "-" operators at last
             */
            calculateChecking = true;
            while (calculateChecking) {
                calculateChecking = false;
                formulaForChecking.clear();
                formulaForChecking.addAll(formulaByString);
                Double calculateFirst = 0.0;

                for (int i = 0; i <= (formulaForChecking.size() - 2); i++) {
                    if (Objects.equals(formulaForChecking.get(i).get(1), "+")) {
                        calculateFirst = Double.valueOf(formulaForChecking.get(i - 1).get(1)) +
                                  Double.valueOf(formulaForChecking.get(i + 1).get(1));
                        calculateChecking = reduceFormula(i, calculateFirst);
                        //System.out.println("Hello");
                        break;
                    } else if (Objects.equals(formulaForChecking.get(i).get(1), "-")) {
                        calculateFirst = Double.valueOf(formulaForChecking.get(i - 1).get(1)) -
                                Double.valueOf(formulaForChecking.get(i + 1).get(1));
                        calculateChecking = reduceFormula(i, calculateFirst);
                        break;
                    }
                }

            }
            /*
             * Display result
             */
            System.out.println("The result is " + formulaByString.get(0).get(1));
        } else {
            /*
             * Other actions
             */
            switch (errorDetected) {
                case 9999:
                    runProgram = false;
                    break;
                case 8888:
                    helpScreen();
                    break;
                default:
                    System.out.println("Formula syntax is incorrect!");
                    break;
            }
        }
        /*
         * Return false to quit while loop
         */
        return runProgram;
    }
}
