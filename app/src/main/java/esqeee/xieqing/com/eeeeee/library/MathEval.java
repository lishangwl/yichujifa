package esqeee.xieqing.com.eeeeee.library;
import com.xieqing.codeutils.util.ScreenUtils;

import parsii.eval.*;
import parsii.tokenizer.ParseException;

public class MathEval {
    public static int eval(String eval){
        try {
            Scope scope = new Scope();
            Variable screen_width = scope.getVariable("屏幕宽");
            Variable screen_height = scope.getVariable("屏幕高");

            Expression expr = null;
            expr = Parser.parse(eval, scope);

            screen_width.setValue(ScreenUtils.getScreenWidth());
            screen_height.setValue(ScreenUtils.getScreenHeight());

            //System.out.println(expr.evaluate());
            return (int) expr.evaluate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
