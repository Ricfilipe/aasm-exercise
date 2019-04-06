import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unchecked")
public class CommandRisk implements Command {

    private List<Task> taskList = new ArrayList<>();
    private List<Task> positiveTasks = new ArrayList<>();
    private List<Task> negativeTasks = new ArrayList<>();
    private double[] bestProbs = new double[2];
    private double maxValue = Double.NEGATIVE_INFINITY;
    private boolean pair = false;

    @Override
    public boolean isForSingle() {
        return true;
    }


    @SuppressWarnings("Duplicates")
    public String execute(InterpreterState state) throws Exception {

        SingleState realState = (SingleState) state;
        for (int i = 0; i < realState.tasks.size(); i++) {
            Task t = realState.tasks.get(i);
            taskList.add(t);
            t.minU = t.getMinU();
            t.eu = getTaskUtility(t);
            t.options.clear();
        }
        List<Task> toRemove = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            Task t1 = taskList.get(i);
            if (t1.minU >= 0) {
                if (maxValue < t1.eu) {
                    positiveTasks.clear();
                    positiveTasks.add(t1);
                    negativeTasks.clear();
                    bestProbs[0]=1;
                    bestProbs[1]=0;
                    maxValue = t1.eu;
                    pair = false;
                } else if (t1.eu == maxValue) {
                    if (pair) {
                        positiveTasks.clear();
                        positiveTasks.add(t1);
                        negativeTasks.clear();
                        bestProbs[0]=1;
                        bestProbs[1]=0;
                        pair = false;
                    } else {
                        positiveTasks.add(t1);
                    }
                }
            } else {
                if (positiveTasks.isEmpty()) {
                    if (negativeTasks.isEmpty()) {
                        negativeTasks.add(t1);
                        bestProbs[0]=0;
                        bestProbs[1]=1;
                        maxValue = t1.eu;
                    } else {
                        if ((maxValue < t1.eu && negativeTasks.get(0).minU == t1.minU)|| t1.minU > negativeTasks.get(0).minU) {
                            negativeTasks.clear();
                            negativeTasks.add(t1);
                            maxValue = t1.eu;
                        } else if (t1.eu == maxValue && negativeTasks.get(0).minU == t1.minU) {
                            negativeTasks.add(t1);
                        }
                    }
                }else{
                    if (t1.eu < maxValue)
                        continue;
                }
            }

            for (int j = i + 1; j < taskList.size(); j++) {
                Task t2 = taskList.get(j);
                double tempP = 0;
                double tempMax = 0;
                if (t1.minU > 0 && t2.minU < 0) {
                    tempP = (-t2.minU) / (t1.minU - t2.minU);
                    tempMax = tempP * t1.eu + (1-tempP)* t2.eu;

                    if (tempMax == maxValue && pair) {
                        Task temp = negativeTasks.get(0);
                        if ( temp.eu < t2.eu) {
                            positiveTasks.clear();
                            negativeTasks.clear();
                            positiveTasks.add(t1);
                            negativeTasks.add(t2);
                            bestProbs[0] = tempP;
                            bestProbs[1] = 1 - tempP;
                            pair = true;
                        } else if (temp.minU == t2.minU && temp.eu == t2.eu) {
                            addTasks(t1,t2);
                            pair = true;
                        }
                    } else if (tempMax > maxValue || (!negativeTasks.isEmpty() && !pair)) {
                        positiveTasks.clear();
                        negativeTasks.clear();
                        positiveTasks.add(t1);
                        negativeTasks.add(t2);
                        bestProbs[0] = tempP;
                        bestProbs[1] = 1 - tempP;
                        maxValue = tempMax;
                        pair = true;
                    }
                } else if (t1.minU < 0 && t2.minU > 0) {
                    tempP = (-t2.minU) / (t1.minU - t2.minU);
                    tempMax = tempP * t1.eu + (1-tempP)* t2.eu;

                    if (tempMax == maxValue && pair) {
                        Task temp = negativeTasks.get(0);
                        if ( temp.eu < t1.eu) {
                            positiveTasks.clear();
                            negativeTasks.clear();
                            negativeTasks.add(t1);
                            positiveTasks.add(t2);
                            bestProbs[1] = tempP;
                            bestProbs[0] = 1 - tempP;
                            pair = true;
                        } else if (temp.minU == t1.minU && temp.eu == t1.eu) {
                           addTasks(t2,t1);
                            pair = true;
                        }
                    } else if (tempMax > maxValue || (!negativeTasks.isEmpty() && !pair)) {
                        positiveTasks.clear();
                        negativeTasks.clear();
                        negativeTasks.add(t1);
                        positiveTasks.add(t2);
                        pair = true;
                        maxValue = tempMax;
                        bestProbs[1] = tempP;
                        bestProbs[0] = 1 - tempP;
                    }
                }
            }
        }

        for (Task t : toRemove) {
            int idx = taskList.indexOf(t);
            taskList.remove(idx);
        }

        return calculateProb();
    }

    @SuppressWarnings("Duplicates")
    private String calculateProb() {


            Locale locale  = new Locale("en", "UK");
            DecimalFormat df = (DecimalFormat)  NumberFormat.getNumberInstance(locale);
            df.applyPattern("0.00");
            df.setRoundingMode(RoundingMode.HALF_UP);

            String buffer = "(";
            int i =0;
            for (Task t : taskList) {


                if(negativeTasks.contains(t)){
                    if (i > 0) {
                        buffer = buffer + ";";
                    }
                    buffer = buffer + df.format(bestProbs[1]/negativeTasks.size());
                    buffer = buffer + "," + t.name;
                    i++;
                }else if(positiveTasks.contains(t)){
                    if (i > 0) {
                        buffer = buffer + ";";
                    }
                    buffer = buffer  + df.format(bestProbs[0]/positiveTasks.size());
                    buffer = buffer + "," + t.name;
                    i++;
                }

            }
            return buffer + ")";
        }




    @Override
    public boolean checkNum(int numArgs) {
        return numArgs == 2;
    }

    @Override
    public boolean isSpecialCommand() {
        return false;
    }


    @SuppressWarnings({"Duplicates", "unchecked"})
    private double getSubOptionUtility(Option top) throws Exception {
        double utility = 0;
        for (Option op : top.options) {
            if (op.isFinal()) {
                if (!op.isObservation) {

                    utility += op.value * 0.01 * op.belief;
                } else {

                    utility += op.value * (op.Observed / top.getTotalObersavation());
                }
            } else {

                if (!op.isObservation) {

                    utility += 0.01 * op.belief * getSubOptionUtility(op);

                } else {
                    utility += (op.Observed / top.getTotalObersavation()) * getSubOptionUtility(op);
                }

            }
        }
        return utility;
    }

    @SuppressWarnings({"Duplicates", "unchecked"})
    private double getTaskUtility(Task t) throws Exception {
        double utility = 0;
        for (Option op : t.options) {
            if (op.isFinal()) {
                if (!op.isObservation) {

                    utility += op.value * 0.01 * op.belief;
                } else {
                    utility += op.value * (op.Observed / t.getTotalObersavation());
                }
            } else {

                if (!op.isObservation) {
                    utility += 0.01 * op.belief * getSubOptionUtility(op);

                } else {
                    utility += (op.Observed / t.getTotalObersavation()) * getSubOptionUtility(op);
                }
            }


        }
        return utility;
    }

    private void addTasks(Task t1, Task t2){
        if(!negativeTasks.contains(t2)){
            negativeTasks.add(t2);
        }
        if(!positiveTasks.contains(t1)){
            positiveTasks.add(t1);
        }
    }

}
