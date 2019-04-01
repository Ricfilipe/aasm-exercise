

import java.util.ArrayList;
import java.util.List;

public abstract class NamedClasse {
    public String name = "";
    public List<Option> options = new ArrayList<>();

    public double getMinU() {
        double min = Double.POSITIVE_INFINITY;
        double current = 0;
        for (Option op : options) {
            if (op.isFinal()) {
                current = op.value;
            } else {
                current = op.getMinU();
            }
            if (current < min) {
                min = current;
            }
        }
        return min;
    }
}
