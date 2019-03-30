package single;

import interpreter.NamedClasse;

public class Task implements NamedClasse {

    public String name;

    public Task(String task){
        this.name =task;

    }
    public String toString(){
        String buffer= name +"=[";
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
