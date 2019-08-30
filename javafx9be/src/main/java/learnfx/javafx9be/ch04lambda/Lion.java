package learnfx.javafx9be.ch04lambda;

public class Lion implements Cat, Roarable {
    @Override
    public String getCatKind() {
        return getClass().getSimpleName();
    }

    @Override
    public String getFurDescription() {
        return "gold-brown";
    }
}
