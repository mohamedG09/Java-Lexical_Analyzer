/*
Right hand side of the assigments are of only two terms or one term not more;
String as text it can take "Text", var + var, var;
*/

//TODO While() loop implementation
//TODO switch(),case,break statement

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class LexicalAnalyzer {

    public static HashMap<String, String> variables = new HashMap<>();


    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("Enter the code to be analyzed and enter exit when done: ");
        ArrayList<String> list = new ArrayList<>();

        while (true) {

            String line = in.nextLine();
            if (line.equals("exit")) {
                break;
            }
            list.add(line);

        }

        for (int i = 0; i < list.size(); i++) {
            String keyword;
            String condition;

            try {
                keyword = list.get(i).substring(0, list.get(i).indexOf("(")).trim();
                condition = list.get(i).substring(list.get(i).indexOf("(") + 1, list.get(i).length() - 1);
            } catch (IndexOutOfBoundsException e) {
                keyword = "None";
                condition = "None";
            }

            if (keyword.equals("currently")) {

                if(variables.containsKey(condition)) {
                    i++; //Increment I to the point beyond the condition
                    boolean cond = Boolean.parseBoolean(variables.get(condition));
                    if (cond){
                        int markPos = i;
                        while(cond) {


                            if(list.get(i).equals("check")){
                                cond = Boolean.parseBoolean(variables.get(condition));
                                if(!cond) {
                                    i++;
                                    break;
                                }
                                i = markPos;


                            }
                            evaluateStatementTrue(list.get(i));
                            i++;

                        }
                    } else {
                        while(!(list.get(i).equals("check"))){
                            i++;
                        }
                    }

                } else {
                    System.out.println("Condition {"+condition+"} does not exit");
                }

            } else if(keyword.equals("None") && condition.equals("None")) {
                evaluateStatementTrue(list.get(i));
            }

        }

        System.out.println("Values for all variables after some computation:");
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }


    }

    public static void evaluateStatementTrue(String x) {


        //Extracting left hand side
        String datatype = x.substring(0, x.indexOf(" "));     //Identifying the datatype
        String variable = x.substring(x.indexOf(" "), x.indexOf("=")).trim(); //Identifying the variable

        //Right hand side
        String rhs = x.substring(x.indexOf("=") + 1, x.indexOf(";")).trim();

        if (datatype.equals("dotval")) {

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

                        //Intetionally Switched right and left literals
                        double temp = rightLiteral_double - leftLiteral_double;
                        variables.put(variable, temp + "");

                        break;
                    }
                }

            }

        } else if (datatype.equals("text")) {

            // var str = "Statement" analysis
            if (rhs.charAt(0) == '"' && rhs.charAt(rhs.length() - 1) == '"') {
                variables.put(variable, rhs);
            } else {

                try { //Identifying concatation of strings

                    String leftLiteral = rhs.substring(0, rhs.indexOf('+'));
                    String rightLiteral = rhs.substring(rhs.indexOf("+") + 1).trim();

                    variables.put(variable, leftLiteral + rightLiteral);

                } catch (IndexOutOfBoundsException e) {

                    variables.put(variable, rhs);

                } catch (Exception e) {
                    System.out.println("Undefined Text Value in {" + x + "}");
                    System.exit(1);
                }

            }

        } else if (datatype.equals("flag")) {

            boolean temp = splitBoolExp(rhs);
            variables.put(variable, temp + "");


        } else {
            System.out.println("Data type in {" + x + "} is not valid");
            System.out.println("Terminating");
            System.exit(1);
        }


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

    //it supports == or !=
    public static boolean splitBoolExp(String boolExp) {

        //var == var
        try {
            String leftLiteral = boolExp.substring(0, boolExp.indexOf("=")).trim();
            String rightLiteral = boolExp.substring(boolExp.lastIndexOf("=") + 1).trim();


            return variables.get(leftLiteral).equals(variables.get(rightLiteral));

        } catch (NullPointerException e) {
            //var != var
            String leftLiteral = boolExp.substring(0, boolExp.indexOf("!") - 1).trim();
            String rightLiteral = boolExp.substring(boolExp.lastIndexOf("=") + 1).trim();

            return !(variables.get(leftLiteral).equals(variables.get(rightLiteral)));
        } catch (Exception e) {
            System.out.println("Expression {" + boolExp + "} can not be evaluated");
            System.out.println(e.getMessage());
            System.exit(1);
            return false;
        }

    }


}
