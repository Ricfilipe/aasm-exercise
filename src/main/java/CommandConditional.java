public class CommandConditional implements Command {

    @Override
    public boolean isForSingle() {
        return false;
    }

    @Override
    public String execute(InterpreterState state) throws Exception {
        Command cmd = new CommandNash();
        String result = cmd.execute(state);
        if(result.equals("blank-decision")){
            cmd = new CommandMixed();
            result = cmd.execute(state);
        }

        return result;
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
