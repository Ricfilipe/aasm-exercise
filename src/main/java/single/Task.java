package single;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public List<Option> options= new ArrayList<>();
    public String taskName;

    public Task(String task){
        this.taskName=task;

    }
    public String toString(){
        String buffer=taskName+"=[";
        for(int i=0; i<options.size();i++){
            buffer= buffer+ options.get(i).toString();
            if(i<options.size()-1){
                buffer= buffer +",";
            }
        }
        return buffer+"]";
    }

    public double getTotalObersavation() throws Exception {
        double total=0;
        for(Option op : options){
            if(!op.isObservation){
                throw new Exception();
            }
            total+= op.Observed;
        }
        return total;
    }
}
