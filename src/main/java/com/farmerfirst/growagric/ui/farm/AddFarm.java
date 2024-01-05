package com.farmerfirst.growagric.ui.farm;

public class AddFarm {
    private String strFarmedItem;
    private String strChickHouseCapacity;
    private String strCounty;
    private String strSubCounty;
    private String strWard;
    private String strNumberOfYearsFarmingSelectedItem;
    private String strNumberOfEmployees;
    private String strIsFarmInsured;
    private String strInsurerName;
    private String strListOfChallenges;
    private String strOtherChallenges;
    private String strNumberOfChickhouse;

    public AddFarm(String FarmedItem,String ChickHouseCapacity,String County,String SubCounty,String Ward,String NumberOfYearsFarmingSelectedItem,String NumberOfEmployees,String IsFarmInsured,String InsurerName,String ListOfChallenges,String OtherChallenges,String NumberOfChickhouse){
        strFarmedItem = FarmedItem;
        strChickHouseCapacity = ChickHouseCapacity;
        strCounty = County;
        strSubCounty = SubCounty;
        strWard = Ward;
        strNumberOfYearsFarmingSelectedItem = NumberOfYearsFarmingSelectedItem;
        strNumberOfEmployees = NumberOfEmployees;
        strIsFarmInsured = IsFarmInsured;
        strInsurerName = InsurerName;
        strListOfChallenges = ListOfChallenges;
        strOtherChallenges = OtherChallenges;
        strNumberOfChickhouse = NumberOfChickhouse;
    }

    public String getStrFarmedItem() {return strFarmedItem;}

    public String getStrChickHouseCapacity() {return strChickHouseCapacity;}

    public String getStrCounty() {return strCounty;}

    public String getStrSubCounty() {return strSubCounty;}

    public String getStrWard() {return strWard;}

    public String getStrNumberOfYearsFarmingSelectedItem(){return strNumberOfYearsFarmingSelectedItem;}

    public String getStrNumberOfEmployees() {return strNumberOfEmployees;}

    public String getStrIsFarmInsured() {return strIsFarmInsured;}

    public String getStrInsurerName() {return strInsurerName;}

    public String getStrListOfChallenges() {return strListOfChallenges;}

    public String getStrOtherChallenges() {return strOtherChallenges;}

    public String getStrNumberOfChickhouse() {return strNumberOfChickhouse;}
}

