package com.github.jimorc.trilliumshowfx;

/**
 * TitleAndSortData contains the data retrieved from TitleAndSortDialog.
*/
public class TitleAndSortData {
    private final String startTitle;
    private final String endTitle;
    private SortOrder order;

    /**
     * Constructor.
     * @param title contents of the title input
     * @param order sort order
     * @param lastNameAsInitial display last name as initial?
     */
    TitleAndSortData(String startTitle, String endTitle, SortOrder order) {
        this.startTitle = startTitle;
        this.endTitle = endTitle;
        this.order = order;
    }

    public String getStartTitle() {
        return startTitle;
    }

    public String getEndTitle() {
        return endTitle;
    }

    public SortOrder getOrder() {
        return order;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("TitleAndSortData:");
        sb.append("\n   start title: " + startTitle);
        sb.append("\n   end title:" + endTitle);
        sb.append("\n   sortOrder: " + order);
        return sb.toString();
    }
}
