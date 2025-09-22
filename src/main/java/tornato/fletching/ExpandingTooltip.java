package tornato.fletching;

public class ExpandingTooltip {
    public static ExpandingTooltip.Interface INSTANCE = null;

    public interface Interface {
        boolean isHoldingShift();
    }
}