package Commands;

import interpreter.InterpreterState;

public class CommandRisk implements Command{
    @Override
    public boolean isForSingle() {
        return true;
    }

    @Override
    public String execute(InterpreterState state) {
        return null;
    }

    @Override
    public boolean checkNum(int numArgs) {
        return numArgs==2;
    }

    @Override
    public boolean isSpecialCommand() {
        return false;
    }
}
