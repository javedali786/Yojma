package com.tv.uscreen.yojmatv.beanModel.filterModel;

public class SortModel {
    private String sortLists;

    public boolean isSortChecked() {
        return isSortChecked;
    }

    public void setSortChecked(boolean sortChecked) {
        isSortChecked = sortChecked;
    }

    private boolean isSortChecked;


    public SortModel(String sortLists) {
        setSortLists(sortLists);

    }


    public String getSortLists() {
        return sortLists;
    }

    public void setSortLists(String sortLists) {
        this.sortLists = sortLists;
    }


}
