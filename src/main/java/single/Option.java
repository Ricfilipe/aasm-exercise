package single;

import java.util.ArrayList;
import java.util.List;

public class Option {

    public List<Option> subOption= new ArrayList<>();
    public double belief;
    public int Observed;
    public boolean isObservation;
    public int value;
    public String optionName;
    public Option( String name){
        this.optionName=name;

    }

    public boolean isFinal(){
        return subOption.isEmpty();
    }

    public String toString(){
        String buffer=optionName+"=(";
        if(isObservation){
            buffer=buffer+Observed;
        }else{
            buffer=buffer+belief+"%";
        }
        if(!isFinal()){
            buffer=buffer + ",[";
            for(int i=0; i<subOption.size();i++){
                if(i!=0){
                    buffer= buffer +",";
                }
               buffer=buffer + subOption.get(i).toString();

            }
            buffer=buffer+"]";
        }else{
            buffer=buffer+","+value;
        }
        return buffer+")";
    }
}
