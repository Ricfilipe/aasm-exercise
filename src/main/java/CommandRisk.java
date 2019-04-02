import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class CommandRisk implements Command {

    private List<Task> taskList = new ArrayList<>();
    private double[] bestProbs;
    private float maxValue = Float.NEGATIVE_INFINITY;

    @Override
    public boolean isForSingle() {
        return true;
    }



    public String execute(InterpreterState state) throws Exception {

        SingleState realState = (SingleState) state;
        for (int i = 0; i < realState.tasks.size(); i++) {
            Task t = realState.tasks.get(i);
            taskList.add(t);
            t.minU =t.getMinU();
            t.eu = getTaskUtility(t);
            t.options.clear();
        }
        List<Task> toRemove = new ArrayList<>();
        for (int i = 0; i < taskList.size() - 1; i++) {
            Task t1=taskList.get(i);
            for (int j = i + 1; j < taskList.size(); j++) {
               Task t2=taskList.get(j);
                if (t1.eu >= t2.eu) {
                    if (t1.minU >= t2.minU&& !toRemove.contains(t2)) {
                        toRemove.add(t2);
                    } else if (t1.minU < 0 && t2.minU<= 0 && !toRemove.contains(t1)) {
                        toRemove.add(t1);
                    }
                } else if (t1.eu <= t2.eu) {
                    if (t1.minU <= t2.minU&& !toRemove.contains(t1)) {
                        toRemove.add(t1);
                    } else if (t1.minU <= 0 && t2.minU< 0 && !toRemove.contains(t2)) {
                        toRemove.add(t2);
                    }
                }
            }
        }

        for (Task t : toRemove) {
            int idx = taskList.indexOf(t);
            taskList.remove(idx);
        }

        bestProbs = new double[taskList.size()];
        return calculateProb();
    }

    @SuppressWarnings("unchecked")
    private String calculateProb() {

        if (taskList.size() == 1) {
            return "(1.00," + taskList.get(0).name + ")";
        }else if(taskList.size() == 2) {
            Task t1 = taskList.get(0) , t2 = taskList.get(1);
            if(t2.eu == t1.eu  && (t2.minU == t1.minU || (t1.eu>0 && t2.eu>0))){
                bestProbs[0]=500;
                bestProbs[1]=500;
            }else{
                bestProbs[0] = (-t2.minU)*1000/(t1.minU-t2.minU);
                bestProbs[1] = 1000-bestProbs[0];
            }
        }else{
            return "";
        }


        String buffer = "(";
        for (int i = 0; i < bestProbs.length; i++) {
            buffer = buffer + "0." + String.format("%02d", Math.round(bestProbs[i] / 10f));
            buffer = buffer + "," + taskList.get(i).name;
            if (i < bestProbs.length - 1) {
                buffer = buffer + ";";
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





    private void calculateBest(float current, float minCurrent, double[] temp) {
        if (current > maxValue && minCurrent >= 0) {
            maxValue = current;
            bestProbs = temp;
        } else if (current == maxValue && minCurrent >= 0) {
            boolean check = true;
            for (int i = 0; i < temp.length - 1; i++) {
                for (int j = i + 1; j < temp.length; j++) {
                    if (taskList.get(i).equals(taskList.get(j)) && temp[i] == temp[j]) {
                        check = true;
                    } else if (taskList.get(i).equals(taskList.get(j))) {
                        check = false;
                        break;
                    }
                }
                if (!check) {
                    break;
                }
            }
            if (check) {
                bestProbs = temp;
                return;
            }
        }
    }


    @SuppressWarnings({"Duplicates","unchecked"})
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

    @SuppressWarnings({"Duplicates","unchecked"})
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

}
