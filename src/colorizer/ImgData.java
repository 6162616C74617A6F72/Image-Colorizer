package colorizer;

/**
 * Author: (Alex) Olexandr Matveyev
 * @author Olexandr Matveyev
 */
public class ImgData
{
    private int alpha = 0;
    private int pixel = 0;
    private int avg = 0;

    //RGB
    private int r = 0;
    private int g = 0;
    private int b = 0;

    //HSB-HSV
    private float h = 0;
    private float s = 0;
    private float b_v = 0;

    ImgData()
    {

    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getPixel() {
        return pixel;
    }

    public void setPixel(int rgb) {
        this.pixel = rgb;
    }

    public int getAvg() {
        return avg;
    }

    public void setAvg(int avg) {
        this.avg = avg;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public float getS() {
        return s;
    }

    public void setS(float s) {
        this.s = s;
    }

    public float getB_v() {
        return b_v;
    }

    public void setB_v(float b_v) {
        this.b_v = b_v;
    }
}
