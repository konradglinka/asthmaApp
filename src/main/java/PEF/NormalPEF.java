package PEF;

public class NormalPEF {
    public double calculateNormalPEFValue(int age,double growth, boolean isWhomen){
        double normalPEFValue=0;
        if(age<18){
            normalPEFValue=((growth*100-100.0)*5)+100;
        }
        if(age>=18 && isWhomen==true){
            normalPEFValue=((growth*3.72)+2.24-((double)age*0.03))*60;
        }
        if(age>=18 && isWhomen==false){
            normalPEFValue=((growth*5.48)+1.58-((double)age*0.041))*60;
        }
    return Math.round(normalPEFValue);
    }
}
