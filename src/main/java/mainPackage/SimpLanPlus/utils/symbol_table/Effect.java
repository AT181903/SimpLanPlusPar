package mainPackage.SimpLanPlus.utils.symbol_table;

import java.util.ArrayList;
import java.util.Objects;

public class Effect {
    public static final Integer bot = 0;
    public static final Integer rw = 1;
    public static final Integer del = 2;
    public static final Integer top = 3;

    private final ArrayList<Boolean> isUsed;

    private final ArrayList<Integer> states;

    public Effect() {
        this.isUsed = new ArrayList<>();
        this.states = new ArrayList<>();
        this.states.add(0, Effect.bot);
        this.isUsed.add(0, false);
    }

    public Boolean getUsed(Integer derefLevel) {
        return isUsed.get(derefLevel - 1);
    }

    public Integer getActualStatus(Integer derefLevel) {
        return states.get(derefLevel - 1);
    }

    public void newDerefLevel(Integer derefLevel) {
        this.states.add(derefLevel - 1, Effect.bot);
        this.isUsed.add(derefLevel - 1, false);
    }

    public Integer getMaxRefLevel() {
        return states.size();
    }

    public void setUsed(Integer derefLevel) {
        this.isUsed.set(derefLevel - 1, true);
    }

    public void newStatus(Integer derefLevel, Integer status) {
        this.states.set(derefLevel - 1, status);
    }

    public static Integer max(final Integer e1, final Integer e2) {
        return Math.max(e1, e2);
    }

    public static Integer par(final Integer e1, final Integer e2) {
        return max(seq(e1, e2), seq(e2, e1));
    }

    public static Integer seq(final Integer e1, final Integer e2) {
        if (max(e1, e2) <= rw) {
            return Effect.max(e1, e2);
        }

        if ((e1 <= rw && Objects.equals(e2, del)) ||
                (e1.equals(del) && Objects.equals(e2, bot))) {
            return del;
        }

        return top;
    }
}
