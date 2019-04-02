import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CommandMixed implements  Command {
    @Override
    public boolean isForSingle() {
        return false;
    }

    @Override
    public String execute(InterpreterState state) throws Exception {
        // (a11,b11) (a12,b12)
        // (a21,b21) (a22,b22)
        MatrixState realState = (MatrixState) state;
        double pmine,ppeer;
        //(b11+b22-b12-b21)
        double div = realState.taskMatrix.get(0).peerValue + realState.taskMatrix.get(3).peerValue-
                realState.taskMatrix.get(1).peerValue - realState.taskMatrix.get(2).peerValue;
        if(div!=0){
            pmine = (realState.taskMatrix.get(3).peerValue-realState.taskMatrix.get(2).peerValue)/div;
        }else{
            return "blank-decision";
        }

        //(a11+a22-a12-a21)
         div = realState.taskMatrix.get(0).myValue + realState.taskMatrix.get(3).myValue-
                realState.taskMatrix.get(1).myValue - realState.taskMatrix.get(2).myValue;
        if(div!=0){
            ppeer = (realState.taskMatrix.get(3).myValue-realState.taskMatrix.get(1).myValue)/div;
        }else{
            return "blank-decision";
        }
        if(pmine>1 || pmine<0 || ppeer>1 || ppeer<0){
            return "blank-decision";
        }
        Locale locale  = new Locale("en", "UK");
        DecimalFormat df = (DecimalFormat)  NumberFormat.getNumberInstance(locale);
        df.applyPattern("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        return "mine=("+df.format(pmine)+","+df.format(1-pmine)+"),peer=("+df.format(ppeer)+","+
                df.format(1-ppeer)+")";
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
