package Commands;

import interpreter.InterpreterState;

public class CmdError implements Command{
    private String message;

    @Override
    public boolean isForSingle() {
        return false;
    }

    @Override
    public String execute(InterpreterState state) {
        return message;
    }

    public CmdError(String errorMsg){
        this.message=errorMsg;
    }


    @Override
    public boolean checkNum(int numArgs) {
        return true;
    }

    @Override
    public boolean isSpecialCommand() {
        return true;
    }
}
