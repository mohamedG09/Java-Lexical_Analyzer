import java.util.HashMap;



public class LexicalAnalyzer {

    public static HashMap<String, String> lex_token = new HashMap<>();

    public static void main(String[] args) {

        String x = "String x = psychologix + youngpharaoh + 9;";
        String variable = x.substring(x.indexOf(" "),x.indexOf("=")).trim();

        System.out.println(x.substring(x.indexOf("=")+1,x.indexOf(";")).trim());



    }

    public static boolean isStatementTrue(String x) {

        String datatype = x.substring(0,x.indexOf(" "));    //Identifying the datatype
        String variable = x.substring(x.indexOf(" "),x.indexOf("=")).trim(); //Identifying the variable
        String eq = x.substring(x.indexOf(variable)+1,x.indexOf("=")+1).trim(); //Identifying equal sign




        return true;
    }

}
