public class LexToToken {

    private String lexeme;
    private String token;

    public LexToToken(String lexeme, String token) {
        this.lexeme = lexeme;
        this.token = token;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return  "{" +
                "lexeme='" + lexeme + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
