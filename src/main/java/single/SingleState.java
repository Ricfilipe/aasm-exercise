package single;

import interpreter.InterpreterState;


import java.util.ArrayList;
import java.util.List;

public class SingleState extends InterpreterState {

    List<Task> tasks = new ArrayList<>();

    @Override
    public boolean isSingle() {
        return true;
    }

    public void generateState(List<String> args) throws Exception {
        for(String task:args){
            for(int i=0; i<task.length();i++){
                if(task.charAt(i)=='='){
                    Task t=new Task(task.substring(0,i));
                    if(CheckForCollision(t)){
                        throw new Exception();
                    }
                    tasks.add(t);
                    generateOptions(task.substring(i+2,task.length()-1),t);
                    break;
                }
            }
        }
    }

    private boolean CheckForCollision(Task t) {
        for(Task task:this.tasks){
            if(task.taskName == t.taskName){
                return true;
            }
        }
        return false;
    }


    private boolean CheckForCollision(Task t,Option option) {
        for(Option op: t.options){
            if(op.optionName == option.optionName){
                return true;
            }
        }
        return false;
    }

    private boolean CheckForCollision(Option parent,Option option) {
        for(Option op: parent.subOption){
            if(op.optionName == option.optionName){
                return true;
            }
        }
        return false;
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
                t.subOption.add(op);
                i= getFirstOption(op,substring.substring(i+1))+i+1;
                i = getSecondOption(op,substring.substring(i))+i+1;
                help=i;

            }

        }
    }

    private int getSecondOption(Option op, String rest) throws Exception {

        if(rest.charAt(0)!='['){
            for(int i=0; i<rest.length();i++){
                if(rest.charAt(i)==')'){
                    op.value = Integer.valueOf(rest.substring(0, i));
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

    public String toString(){
        String buffer ="(";
        for(int i=0; i<tasks.size();i++){
            buffer= buffer+tasks.get(i).toString();
            if(i<tasks.size()-1) {
            buffer = buffer +",";
            }
        }

        return buffer+")";
    }
}

