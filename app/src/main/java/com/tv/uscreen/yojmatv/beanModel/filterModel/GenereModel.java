package com.tv.uscreen.yojmatv.beanModel.filterModel;

public class GenereModel {

    private String genere;


    public GenereModel(String genere){
        setGenere(genere);

    }



    public boolean isGenereChecked() {
        return isGenereChecked;
    }

    public void setGenereChecked(boolean genereChecked) {
        isGenereChecked = genereChecked;
    }

    private boolean isGenereChecked;



    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }


}
