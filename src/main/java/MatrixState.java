import com.sun.corba.se.spi.orbutil.fsm.State;

import java.util.ArrayList;
import java.util.List;

public class MatrixState extends InterpreterState {
    private int linha =0,colunas=0;
    public List<MatrixTask> taskMatrix = new ArrayList<>();


    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public void findOrCreateOption(String s, String s1) throws Exception {

    }


    public void generateMyState(List<String> args) throws Exception {
        String currentName = "";
        for(String task:args){
            for(int i=0; i<task.length();i++){
                if(task.charAt(i)=='='){
                    MatrixTask t=new MatrixTask(task.substring(0,i));
                    taskMatrix.add(t);
                    if(!t.myName.equals(currentName)){
                        this.linha++;
                        currentName=t.myName;
                    }
                    if(this.linha==1){
                        this.colunas++;
                    }


                    //generateOptions(task.substring(i+2,task.length()-1),t);
                    break;
                }
            }
        }
    }
    public void generatePeerState(List<String> args) throws Exception {
//TODO
    }
}
