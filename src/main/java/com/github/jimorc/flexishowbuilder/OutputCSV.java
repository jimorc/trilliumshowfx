package com.github.jimorc.flexishowbuilder;

/**
 * The OutputCSV class is used to build the CSV lines to be passed to LibreOffice or Excel
 * to generate an XLS file representing the slide show.
 */
public class OutputCSV {
    private FlexiBeans flexiBeans;

    /**
     * Constructor creates an empty OutputCSV object.
     */
    public OutputCSV() {
        flexiBeans = new FlexiBeans();
        flexiBeans.getBeans().add(FlexiBean.HEADER_BEAN);
    }

    /**
     * deleteAllBeans deletes all beans in the object.
     */
    public void deleteAllBeans() {
        flexiBeans = new FlexiBeans();
    }

    /**
     * Appends a Bean from the original InputCSV object.
     * @param bean the FlexiBean to append to the OutputCSV object.
     */
    public void appendBean(FlexiBean bean) {
        flexiBeans.append(bean);
    }

    /**
     * Return the number of beans in the CSV.
     * @return number of beans
     */
    public int length() {
        return flexiBeans.getBeans().size();
    }

    /**
     * Retrieves the FlexiBeans object in this object.
     * @return the FlexiBeans object
     */
    public FlexiBeans getBeans() {
        return flexiBeans;
    }

    /**
     * Returns a string representation of the CSV object.
     * @return a string representation of the CSV object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (FlexiBean bean : flexiBeans.getBeans()) {
            sb.append(bean.toString());
            sb.append("\n");
        }
        String s = sb.toString();
        s = s.substring(0, s.length() - 1); // Remove last newline
        return s;
    }

}
