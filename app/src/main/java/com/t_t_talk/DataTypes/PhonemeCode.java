package com.t_t_talk.DataTypes;

public enum PhonemeCode {
    S1("S"), A("A"), T("T"), P("P"),
    I("I"), N("N"), M("M"), D("D"),
    G("G"), O("O"), C("C"), K("K"),
    CK("CK"), E("E"), U("U"), R("R"),
    H("H"), B("B"), F("F"), FF("FF"),
    L("L"), LL("LL"), S6("S"), SS("SS"),
    J("J"), V("V"), W("W"), X("X"),
    Y("Y"), Z("Z"), ZZ("ZZ"), QU("QU"),
    CH("CH"), SH("SH"), TH("TH"), NG("NG"),
    AI("AI"), EE("EE"), IGH("IGH"), OA("OA"),
    AR("AR"), OR("OR"), UR("UR"), OW("OW"),
    OI("OI"), EAR("EAR"), AIR("AIR"), ER("ER");

    private String str;

    private PhonemeCode(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
