
import java.util.ArrayList;
import java.util.List;


public class MatrixState extends InterpreterState {
    public int linha =0,colunas=0;
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

                    generateOptions(task.substring(i+2,task.length()-1),t.myTask);
                    t.myValue = getTaskUtility(t.myTask);
                    break;
                }
            }
        }
    }
    public void generatePeerState(List<String> args) throws Exception {

        String currentName = "";
        for(String task:args){
            for(int i=0; i<task.length();i++){
                if(task.charAt(i)=='='){
                    String[] names = task.substring(0,i).split("\\|");
                    MatrixTask t =getState(names[1],names[0]);
                    generateOptions(task.substring(i+2,task.length()-1),t.peerTask);
                    t.peerValue = getTaskUtility(t.peerTask);
                    break;
                }
            }
        }
    }

    private MatrixTask getState(String my, String peer) throws Exception {
        for(MatrixTask t: taskMatrix){
            if(t.myName.equals(my) && t.peerName.equals(peer)){
                return t;
            }
        }
        throw new Exception();

    }


    private void generateOptions(String substring, Task t) throws Exception {
        int help=0;
        for(int i =0; i<substring.length();i++){
            if(substring.charAt(i)=='='){
                Option op =new Option(substring.substring(help,i));
                if(CheckForCollision(t,op)){
                    throw new Exception();
                }
                t.options.add(op);
                i= getFirstOption(op,substring.substring(i+1))+i+1;
                i = getSecondOption(op,substring.substring(i))+i+1;
                help=i;

            }

        }
    }

    private void generateOptions(String substring, Option t) throws Exception {
        int help=0;
        for(int i =0; i<substring.length();i++){
            if(substring.charAt(i)=='='){
                Option op =new Option(substring.substring(help,i));
                if(CheckForCollision(t,op)){
                    throw new Exception();
                }
                t.options.add(op);

                i= getFirstOption(op,substring.substring(i+1))+i+1;
                i = getSecondOption(op,substring.substring(i))+i+1;
                help=i;

            }

        }
    }

    private boolean CheckForCollision(Task t,Option option) {
        for(Option op: t.options){
            if(op.name == option.name){
                return true;
            }
        }
        return false;
    }

    private boolean CheckForCollision(Option parent,Option option) {
        for(Option op: parent.options){
            if(op.name == option.name){
                return true;
            }
        }
        return false;
    }


    private int getSecondOption(Option op, String rest) throws Exception {

        if(rest.charAt(0)!='['){
            for(int i=0; i<rest.length();i++){
                if(rest.charAt(i)==')'){
                    op.value = Double.valueOf(rest.substring(0, i));
                    return i+1;
                }
            }
        }else{
            int depth =0;

            for(int j=0;j<rest.length();j++){

                if(rest.charAt(j)=='[' || rest.charAt(j)=='(' ){
                    depth++;
                }else  if(rest.charAt(j)==')' ||rest.charAt(j)==']'){
                    depth--;
                }
                if(depth==0){
                    generateOptions(rest.substring(1,j),op);
                    return j+2;
                }
            }

        }
        throw new Exception();
    }



    private int getFirstOption(Option option, String rest) throws Exception {

        for(int i =0; i<rest.length();i++) {

            if (rest.charAt(i) == ',') {
                option.Observed = Integer.valueOf(rest.substring(1, i));
                option.isObservation=true;
                i++;
                return i;
            } else if (rest.charAt(i) == '%') {
                option.belief = Double.valueOf(rest.substring(1, i));
                option.isObservation=false;
                i = i + 2;
                return i;
            }
        }
        throw new Exception();
    }

    private double getTaskUtility(Task t) throws Exception {
        double utility=0;
        for (Option op : t.options) {
            if (op.isFinal()) {
                if (!op.isObservation) {

                    utility +=  op.value * 0.01 * op.belief;
                } else {
                    utility += op.value *(op.Observed/t.getTotalObersavation());
                }
            } else {

                if (!op.isObservation) {
                    utility += 0.01 * op.belief * getSubOptionUtility(op);

                } else {
                    utility += (op.Observed/t.getTotalObersavation()) * getSubOptionUtility(op);
                }
            }


        }
        return utility;
    }

    private double getSubOptionUtility(Option top) throws Exception {
        double utility=0;
        for (Option op : top.options) {
            if (op.isFinal()) {
                if (!op.isObservation) {

                    utility += op.value * 0.01 * op.belief;
                } else {

                    utility += op.value *(op.Observed/top.getTotalObersavation());
                }
            } else {

                if (!op.isObservation) {

                    utility += 0.01 * op.belief * getSubOptionUtility(op);

                } else {
                    utility +=(op.Observed/top.getTotalObersavation()) * getSubOptionUtility(op);
                }

            }
        }
        return utility;
    }
}
