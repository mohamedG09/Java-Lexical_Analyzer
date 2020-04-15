/*
Right hand side of the assigments are of only two terms or one term not more;
*/

import java.util.Arrays;
import java.util.HashMap;


public class LexicalAnalyzer {

    public static HashMap<String, String> variables = new HashMap<>();

    public static void main(String[] args) {

        String x = "String x = 5 + 10;";
        String variable = x.substring(x.indexOf(" "), x.indexOf("=")).trim();

        String temp = x.substring(x.indexOf("=") + 1, x.indexOf(";")).trim();

        System.out.println(temp.contains("+"));
        String[] result = splitRHS(temp);
        System.out.println(Arrays.toString(result));


    }

    public static boolean evaluateStatementTrue(String x) {

        //Extracting left hand side
        String datatype = x.substring(0, x.indexOf(" "));    //Identifying the datatype
        String variable = x.substring(x.indexOf(" "), x.indexOf("=")).trim(); //Identifying the variable
        String eq = x.substring(x.indexOf(variable) + 1, x.indexOf("=") + 1).trim(); //Identifying equal sign

        //Right hand side
        String rhs = x.substring(x.indexOf("=") + 1, x.indexOf(";")).trim();

        if (datatype.equals("double")) {

            //Assigning a var=num or var=var
            if (!(rhs.contains("+") || rhs.contains("*") || rhs.contains("-") || rhs.contains("/"))) {

                try {
                    //If it is a single number
                    double temp = Double.parseDouble(rhs);
                    variables.put(variable, temp + "");
                } catch (NumberFormatException e) { //Incase it is another variable

                    if (variables.containsKey(rhs)) {
                        variables.put(variable, rhs);
                    } else {
                        System.out.println("Variable is not declared in {" + x + "}");
                        System.exit(1);
                    }

                }
            }
            //Assining num + num or var + var or num + num
            else {

                //Unboxing of the function
                String[] result = splitRHS(rhs);
                String rightLiteral_str = result[0];
                String operator = result[1];
                String leftLiteral_str = result[2];

                Double rightLiteral_double = null;
                Double leftLiteral_double = null;

                //Evaluating rightLiteral(num or var)
                try {
                    //Checking if it is a number
                    rightLiteral_double = Double.parseDouble(rightLiteral_str);

                } catch (NumberFormatException e) {

                    if (variables.containsKey(rightLiteral_str)) {
                        rightLiteral_double = Double.parseDouble(variables.get(rightLiteral_str));
                    } else {
                        System.out.println("Error in evaluating right hand in {" + x + "}");
                        System.exit(1);
                    }

                }

                //Evaluating leftLiteral(num or var)
                try {
                    //Checking if it is a number
                    leftLiteral_double = Double.parseDouble(leftLiteral_str);

                } catch (NumberFormatException e) {

                    if (variables.containsKey(leftLiteral_str)) {
                        leftLiteral_double = Double.parseDouble(variables.get(leftLiteral_str));
                    } else {
                        System.out.println("Error in evaluating left hand in {" + x + "}");
                        System.exit(1);
                    }

                }
                //Doing the operation
                switch (operator) {
                    case "*": {

                        double temp = leftLiteral_double * rightLiteral_double;
                        variables.put(variable, temp + "");

                        break;
                    }
                    case "/": {

                        double temp = leftLiteral_double / rightLiteral_double;
                        variables.put(variable, temp + "");

                        break;
                    }
                    case "+": {

                        double temp = leftLiteral_double + rightLiteral_double;
                        variables.put(variable, temp + "");

                        break;
                    }
                    case "-": {

                        double temp = leftLiteral_double - rightLiteral_double;
                        variables.put(variable, temp + "");

                        break;
                    }
                }


            }

        }
        else if (datatype.equals("boolean")) {

        } else if (datatype.equals("string")) {

        } else {
            System.out.println("Data type in {" + x + "} is not valid");
            System.out.println("Terminating");
            System.exit(1);
        }


        return true;
    }

    public static String[] splitRHS(String rhs) {

        String leftLiteral = null;
        String operation = null;
        String rightLiteral = null;

        try {

            //Splitting for addition
            leftLiteral = rhs.substring(0, rhs.indexOf('+')).trim();
            operation = "+";
            rightLiteral = rhs.substring(rhs.indexOf("+") + 1).trim().trim();

        } catch (IndexOutOfBoundsException e) {

            try {
                //Splitting for subtraction
                leftLiteral = rhs.substring(0, rhs.indexOf('-')).trim();
                operation = "-";
                rightLiteral = rhs.substring(rhs.indexOf("-") + 1).trim();
            } catch (IndexOutOfBoundsException e2) {

                //Splitting for division
                try {

                    leftLiteral = rhs.substring(0, rhs.indexOf('/')).trim();
                    operation = "/";
                    rightLiteral = rhs.substring(rhs.indexOf("/") + 1).trim();

                } catch (IndexOutOfBoundsException e3) {

                    try {

                        //Splitting for multiplication
                        leftLiteral = rhs.substring(0, rhs.indexOf('*')).trim();
                        operation = "*";
                        rightLiteral = rhs.substring(rhs.indexOf("*") + 1).trim();

                    } catch (IndexOutOfBoundsException e4) {

                        System.out.println("The opperation is not supported in {" + rhs + "}");
                        System.exit(1);
                    }


                }

            }


        }

        String[] result = new String[3];
        result[0] = leftLiteral;
        result[1] = operation;
        result[2] = rightLiteral;

        return result;


    }

}
