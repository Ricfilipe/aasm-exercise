import interpreter.Interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class exercise {
    public static void main(String[] args) throws IOException {
        BufferedReader input =
                new BufferedReader(new InputStreamReader(System.in));
        Interpreter interpreter = new Interpreter();

        while (true){
            System.out.print(interpreter.operation+">");
            System.out.println(interpreter.interpret(input.readLine()));

        }
    }
}
