import java.util.ArrayList;
import java.util.List;

public class Option {
    public boolean isFinal;
    public List<Option> subOption= new ArrayList<>();
    public double belief;
    public int Observed;
    public boolean isObservation;
    public int value;
}
