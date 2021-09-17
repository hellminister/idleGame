
package idlegame.util.textfilter;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public abstract class TextFilter<T> implements UnaryOperator<TextFormatter.Change> {
    protected final Treat<T> valueCondition;

    protected final String regex;

    public TextFilter(String reg){
        valueCondition = new NoCondition();
        regex = reg;
    }

    public TextFilter(String reg, ValueCondition<T> vc, Transform<T> trans){
        valueCondition = new Treat<>(vc, trans);
        regex = reg;
    }


    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        String newText = change.getControlNewText();
        if (newText.matches(regex) && valueCondition.treat(newText)) {
            return change;
        }
        return null;
    }


    public interface ValueCondition<T> {
        boolean isAcceptable(T d);
    }

    protected interface Transform<T> {
        T transform(String newString);
    }

    protected class Treat<E extends T>{
        protected final ValueCondition<E> vc;
        protected final Transform<E> transform;

        protected Treat(ValueCondition<E> vc, Transform<E> transform) {
            this.vc = vc;
            this.transform = transform;
        }

        protected boolean treat(String newText){
            E d = transform.transform(newText);
            return vc.isAcceptable(d);
        }

    }

    protected class NoCondition extends Treat<T> {

        protected NoCondition() {
            super(null, null);
        }

        @Override
        public boolean treat(String newText) {
            return true;
        }
    }
}