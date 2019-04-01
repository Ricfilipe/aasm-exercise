import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class CommandRisk implements Command {

    private List<Double> listEU = new ArrayList<>();
    private List<Double> minU = new ArrayList<>();
    private List<Task> taskList = new ArrayList<>();
    private int[] bestProbs;
    private float maxValue = Float.NEGATIVE_INFINITY;

    @Override
    public boolean isForSingle() {
        return true;
    }



    public String execute(InterpreterState state) throws Exception {

        SingleState realState = (SingleState) state;

        for (int i = 0; i < realState.tasks.size(); i++) {
            Task t = realState.tasks.get(i);
            listEU.add(getTaskUtility(t));
            minU.add(t.getMinU());
            taskList.add(t);
        }

        List<Task> toRemove = new ArrayList<>();
        for (int i = 0; i < taskList.size() - 1; i++) {
            for (int j = i + 1; j < taskList.size(); j++) {
                if (listEU.get(i).floatValue() > listEU.get(j).floatValue()) {
                    if (minU.get(i).floatValue() >= minU.get(j).floatValue() && !toRemove.contains(taskList.get(j))) {
                        toRemove.add(taskList.get(j));
                    } else if (minU.get(i).floatValue() < 0 && minU.get(j).floatValue() < 0 && !toRemove.contains(taskList.get(i))) {
                        toRemove.add(taskList.get(i));
                    }
                } else if (listEU.get(i).floatValue() < listEU.get(j).floatValue()) {
                    if (minU.get(i).floatValue() <= minU.get(j).floatValue() && !toRemove.contains(taskList.get(i))) {
                        toRemove.add(taskList.get(i));
                    } else if (minU.get(i).floatValue() < 0 && minU.get(j).floatValue() < 0 && !toRemove.contains(taskList.get(j))) {
                        toRemove.add(taskList.get(j));
                    }
                }
            }
        }

        for (Task t : toRemove) {
            int idx = taskList.indexOf(t);
            taskList.remove(idx);
            minU.remove(idx);
            listEU.remove(idx);
        }

        return calculateProb();
    }

    @SuppressWarnings("unchecked")
    private String calculateProb() {
        if (listEU.size() == 1) {
            return "(1.00," + taskList.get(0).name + ")";
        }

        recursiveHelper(1000 - 10 * (taskList.size() - 1)
                , taskList.size(),
                new int[0]);
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



    private void recursiveHelper(int maxprob, int size, int[] probs) {

        if (size == 1) {
            float current = 0;
            float minCurrent = 0;
            int[] temp = new int[probs.length + 1];
            temp[probs.length] = maxprob;
            for (int i = 0; i < probs.length; i++) {
                temp[i] = probs[i];
                current += probs[i] * (listEU.get(i));
                minCurrent += probs[i] * minU.get(i);
            }

            current += maxprob * listEU.get(listEU.size() - 1).floatValue();
            minCurrent += maxprob * minU.get(listEU.size() - 1);
            if (current > maxValue && minCurrent >= 0) {
                maxValue = current;
                bestProbs = temp;
            } else if (current == maxValue && minCurrent >= 0) {
                boolean check = true;
                for (int i = 0; i < temp.length - 1; i++) {
                    for (int j = i + 1; j < temp.length; j++) {
                        if (listEU.get(i).equals(listEU.get(j)) && temp[i] == temp[j]) {
                            check = true;
                        } else if (listEU.get(i).equals(listEU.get(j))) {
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
        } else {
            int[] temp = new int[probs.length + 1];
            for (int i = 0; i < probs.length; i++) {
                temp[i] = probs[i];
            }
            for (int p = maxprob; p > 9; p = p - 1) {
                temp[probs.length] = p;
                recursiveHelper(maxprob - p + 10, size - 1, temp);
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
