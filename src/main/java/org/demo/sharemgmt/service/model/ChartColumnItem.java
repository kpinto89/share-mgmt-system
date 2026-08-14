package org.demo.sharemgmt.service.model;

public class ChartColumnItem {

    private final String label;
    private final int count;
    private final int heightPercentage;

    public ChartColumnItem(String label, int count, int heightPercentage) {
        this.label = label;
        this.count = count;
        this.heightPercentage = heightPercentage;
    }

    public String getLabel() {
        return label;
    }

    public int getCount() {
        return count;
    }

    public int getHeightPercentage() {
        return heightPercentage;
    }
}
