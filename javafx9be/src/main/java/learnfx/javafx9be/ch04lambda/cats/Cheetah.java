package learnfx.javafx9be.ch04lambda.cats;

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
