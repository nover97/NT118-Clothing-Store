package app.nover.clothingstore.models;

public class StatusCartComparator implements java.util.Comparator<StatusCart> {
    @Override
    public int compare(StatusCart statusCart, StatusCart t1) {
        return t1.getTimestampCreateAt() - statusCart.getTimestampCreateAt();
    }
}
