package Repositories.FromDB;

import java.util.ArrayList;

public final class OWMClaudinesTranslatorRepository {
    private ArrayList<String> englishStringArraylist;
    private ArrayList<String> polishStringArraylist;

   public final ArrayList<String> getEnglishStringArraylist() {
        return englishStringArraylist;
    }
   public final ArrayList<String> getPolishStringArraylist() {
        return polishStringArraylist;
    }
    public OWMClaudinesTranslatorRepository(ArrayList<String> englishStringArraylist, ArrayList<String> polishStringArraylist) {
        this.englishStringArraylist = englishStringArraylist;
        this.polishStringArraylist = polishStringArraylist;
    }

    public String translateEnglishToPolish(String englishString ){
        String polishString=null;
        for(int i=0;i<englishStringArraylist.size();i++)
        {
            if(englishStringArraylist.get(i).equals(englishString))
            {
                polishString= polishStringArraylist.get(i);
                break;
            }
        }
        return polishString;
    }

}