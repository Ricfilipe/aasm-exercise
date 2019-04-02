import java.util.ArrayList;
import java.util.List;

public class CommandNash implements Command {
    @Override
    public boolean isForSingle() {
        return false;
    }

    @Override
    public String execute(InterpreterState state) throws Exception {
        List<MatrixTask> nash = new ArrayList<>();
        MatrixState realState = (MatrixState) state;
        for(int i = 0;i<realState.taskMatrix.size();i++){
            int[] colEntrys = new int[realState.linha-1];
            boolean stop = false;
            int colPos =0;
            int[] lineEntrys = new int[realState.colunas-1];
            int linePos =0;

            //check column
            for(int c = 0; c<realState.linha;c++){
                // obter colunas
                int entry = i%realState.colunas + c*realState.colunas;
                if(i!=entry){
                    colEntrys[colPos]=entry;
                    colPos++;
                }
            }

            for(int e : colEntrys){
                if(realState.taskMatrix.get(e).myValue>realState.taskMatrix.get(i).myValue){
                    stop = true;
                    break;
                }
            }
            if(stop)
                continue;


            //check line
            for(int l = 0; l<realState.colunas;l++){
                // obter colunas

                int entry = l+(i/realState.colunas)*realState.colunas;

                if(i!=entry){
                    lineEntrys[linePos]=entry;
                    linePos++;
                }
            }

            for(int e : lineEntrys){
                if(realState.taskMatrix.get(e).peerValue>realState.taskMatrix.get(i).peerValue){
                    stop = true;
                    break;
                }
            }
            if(stop){
                continue;
            }

            nash.add(realState.taskMatrix.get(i));


        }

        if(nash.isEmpty()){
            return "blank-decision";
        }else{
            MatrixTask best = null;
            double bestValue = Double.NEGATIVE_INFINITY;
            for(MatrixTask t : nash){
                if(t.peerValue+t.myValue>bestValue){
                    bestValue= t.peerValue+t.myValue;
                    best = t;
                }
            }
            return "mine="+best.myName+",peer="+best.peerName;
        }
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
