package Commands;

import interpreter.InterpreterState;

public class CommandRational implements Command{


    @Override
    public boolean isForSingle() {
        return true;
    }

    @Override
    public String execute(InterpreterState state) {
        //TODO
        return "Por Fazer";
    }

    @Override
    public boolean checkNum(int numArgs) {
        return numArgs ==3;
    }

    @Override
    public boolean isSpecialCommand() {
        return false;
    }
}
