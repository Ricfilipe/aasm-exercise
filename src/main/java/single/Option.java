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
        System.out.println("Create option "+name);
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
            for(int i=0; i<subOption.size();i++){
                if(i<subOption.size()-1){
                    buffer= buffer +",";
                }
               buffer=buffer + subOption.get(i).toString();

            }
        }else{
            buffer=buffer+","+value;
        }
        return buffer+")";
    }
}
