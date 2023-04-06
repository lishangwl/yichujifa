package esqeee.xieqing.com.eeeeee.ui.floatmenu;

import com.xieqing.codeutils.util.PermissionUtils;
import com.yicu.yichujifa.GlobalContext;

import java.lang.ref.WeakReference;

public class FloatyWindowManger {

    private static WeakReference<CircularMenu> sCircularMenu;

    public static boolean isCircularMenuShowing() {
        return sCircularMenu != null && sCircularMenu.get() != null;
    }

    public static void showCircularMenuIfNeeded() {
        if (isCircularMenuShowing()) {
            return;
        }
        showCircularMenu();
    }

    public static boolean showCircularMenu() {
        if (!PermissionUtils.getAppOps(GlobalContext.getContext())) {
            PermissionUtils.openAps(GlobalContext.getContext());
            return false;
        } else {
            CircularMenu menu = new CircularMenu(GlobalContext.getContext());
            sCircularMenu = new WeakReference<>(menu);
            return true;
        }
    }

    public static void hideCircularMenu() {
        if (sCircularMenu == null)
            return;
        CircularMenu menu = sCircularMenu.get();
        if (menu != null)
            menu.close();
        sCircularMenu = null;
    }
}
