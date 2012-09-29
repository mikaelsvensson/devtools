import java.util.Map;

public interface GenericInterface<N extends Number> {
    N getNumber();

    void setNumber(N number);

    Map<N, String> getMap();

    void setMap(Map<N, String> map);
}
