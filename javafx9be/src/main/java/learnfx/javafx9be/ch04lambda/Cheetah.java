package learnfx.javafx9be.ch04lambda;

public class Cheetah implements Cat, Purrable {
    @Override
    public String getCatKind() {
        return getClass().getSimpleName();
    }

    @Override
    public String getFurDescription() {
        return "spotted";
    }
}
