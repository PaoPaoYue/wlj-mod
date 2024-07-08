package com.github.paopaoyue.wljmod.card;

public class Util {

    static String getImagePath(String cardID) {
        if (!cardID.startsWith("Wlj:")) {
            return null;
        }
        cardID = cardID.replace("Wlj:", "");
        StringBuilder sb = new StringBuilder();
        sb.append("image/card/");
        for (int i = 0; i < cardID.length(); i++) {
            if (!Character.isLetter(cardID.charAt(i))) continue;
            if (Character.isUpperCase(cardID.charAt(i))) {
                if (i > 0) {
                    sb.append("_");
                }
                sb.append(Character.toLowerCase(cardID.charAt(i)));
            } else {
                sb.append(cardID.charAt(i));
            }
        }
        sb.append(".png");
        return sb.toString();
    }
}
