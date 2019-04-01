public class CommandRational implements Command {


    @Override
    public boolean isForSingle() {
        return true;
    }


    @SuppressWarnings("unchecked")
    public String execute(InterpreterState state) throws Exception {

        double current = 0;
        double max = Double.NEGATIVE_INFINITY;
        Task bestTask = null;
        SingleState realState = (SingleState) state;

        for (Task t : realState.tasks) {
            current = getTaskUtility(t);
            if (max < current) {
                max = current;
                bestTask = t;
            }
        }
        realState.pickedTask = bestTask;
        return bestTask.name;
    }

    @SuppressWarnings({"Duplicates","unchecked"})
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

    @Override
    public boolean checkNum(int numArgs) {
        return numArgs == 3 || numArgs == 2;
    }

    @Override
    public boolean isSpecialCommand() {
        return false;
    }

    @SuppressWarnings({"Duplicates","unchecked"})
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

