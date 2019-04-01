import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class exercise {
    public static void main(String[] args) throws IOException {
        BufferedReader input =
                new BufferedReader(new InputStreamReader(System.in));
        Interpreter interpreter = new Interpreter();

        while (true){
            String in = input.readLine();
            if(in != null) {
                System.out.println(interpreter.interpret(in));
            }else{
                System.exit(0);
            }

        }
    }
}
