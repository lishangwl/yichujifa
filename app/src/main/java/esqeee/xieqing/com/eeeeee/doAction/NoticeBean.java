package esqeee.xieqing.com.eeeeee.doAction;

public class NoticeBean {
    private String actionId ;
    private boolean isRemove;
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionId() {
        return actionId;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    public static NoticeBean options(String actionId, boolean isRemove){
        NoticeBean noticeBean = new NoticeBean();
        noticeBean.setActionId(actionId);
        noticeBean.setRemove(isRemove);
        return noticeBean;
    }
}
