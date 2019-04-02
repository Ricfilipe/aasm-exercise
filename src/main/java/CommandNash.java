public class CommandNash implements Command {
    @Override
    public boolean isForSingle() {
        return false;
    }

    @Override
    public String execute(InterpreterState state) throws Exception {
        return "";
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
