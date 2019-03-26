package Commands;
import interpreter.*;

public interface Command {
    public boolean isForSingle();
    public String execute(InterpreterState state);
    public boolean checkNum(int numArgs);
    public boolean isSpecialCommand();
}
