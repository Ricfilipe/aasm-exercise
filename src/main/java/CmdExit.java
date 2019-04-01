public class CmdExit implements Command {

    @Override
    public boolean isForSingle() {
        return true;
    }

    @Override
    public String execute(InterpreterState state) {
        System.out.println("Bye!");
        System.exit(0);
        return "Never gets here...";
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
