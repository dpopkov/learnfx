package learnfx.javafx9be.ch07graphics.photoviewer;

import javafx.scene.effect.ColorAdjust;

/**
 * POJO that contains various image display properties.
 */
public class ImageInfo {
    private String url;
    private double degrees;
    private ColorAdjust colorAdjust;

    public ImageInfo(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getDegrees() {
        return degrees;
    }

    public void setDegrees(double degrees) {
        this.degrees = degrees;
    }

    public void addDegrees(double degrees) {
        setDegrees(this.degrees + degrees);
    }

    public ColorAdjust getColorAdjust() {
        if (colorAdjust == null) {
            colorAdjust = new ColorAdjust();
        }
        return colorAdjust;
    }

    public void setColorAdjust(ColorAdjust colorAdjust) {
        this.colorAdjust = colorAdjust;
    }
}
