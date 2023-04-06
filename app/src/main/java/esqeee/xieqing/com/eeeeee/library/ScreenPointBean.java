package esqeee.xieqing.com.eeeeee.library;

public class ScreenPointBean {
    private int imgX;
    private int imgXlegth;
    private int imgY;
    private int imgYlength;

    public ScreenPointBean(int imgX, int imgY, int imgXlegth, int imgYlength) {
        this.imgX = imgX;
        this.imgY = imgY;
        this.imgXlegth = imgXlegth;
        this.imgYlength = imgYlength;
    }

    public int getImgX() {
        return this.imgX;
    }

    public void setImgX(int imgX) {
        this.imgX = imgX;
    }

    public int getImgY() {
        return this.imgY;
    }

    public void setImgY(int imgY) {
        this.imgY = imgY;
    }

    public int getImgXlegth() {
        return this.imgXlegth;
    }

    public void setImgXlegth(int imgXlegth) {
        this.imgXlegth = imgXlegth;
    }

    public int getImgYlength() {
        return this.imgYlength;
    }

    public void setImgYlength(int imgYlength) {
        this.imgYlength = imgYlength;
    }
}