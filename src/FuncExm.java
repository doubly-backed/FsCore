
@FunctionalInterface
public interface FuncExm {
    public int addSum(int a, int b);
//    int subSum(int a, int b);
    default int mult(int a, int b) {
        return a*b;
    }
}
