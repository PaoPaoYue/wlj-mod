package com.github.paopaoyue.wljmod.character;

import com.megacrit.cardcrawl.core.CardCrawlGame;

public class CutscenePanel extends com.megacrit.cardcrawl.cutscenes.CutscenePanel {
    private String sfx;

    public CutscenePanel(String imgUrl, String sfx) {
        super(imgUrl);
        this.sfx = sfx;
    }

    public CutscenePanel(String imgUrl) {
        super(imgUrl);
    }

    public void activate() {
        if (sfx != null) {
            CardCrawlGame.sound.play(sfx);
        }

        this.activated = true;
    }


}
