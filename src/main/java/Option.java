public class Option extends NamedClasse {


    public double belief;
    public int Observed;
    public boolean isObservation;
    public double value;


    public Option(String name) {
        this.name = name;
        Observed = 0;

    }

    public boolean isFinal() {
        return options.isEmpty();
    }

    public String toString() {
        String buffer = name + "=(";
        if (isObservation) {
            buffer = buffer + Observed;
        } else {
            buffer = buffer + belief + "%";
        }
        if (!isFinal()) {
            buffer = buffer + ",[";
            for (int i = 0; i < options.size(); i++) {
                if (i != 0) {
                    buffer = buffer + ",";
                }
                buffer = buffer + options.get(i).toString();

            }
            buffer = buffer + "]";
        } else {
            buffer = buffer + "," + value;
        }
        return buffer + ")";
    }

    public double getTotalObersavation() throws Exception {
        double total = 0;
        for (Option op : options) {
            if (!op.isObservation) {
                throw new Exception();
            }
            total += op.Observed;
        }
        return total;
    }

    public boolean hasObeservation() {

        for (Option o : options) {
            if (o.isObservation) {
                return true;
            } else if (!o.isFinal() && o.hasObeservation()) {
                o.isObservation = true;
                o.Observed = 0;
            }
        }
        return false;
    }
}
