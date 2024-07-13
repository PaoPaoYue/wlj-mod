package com.github.paopaoyue.wljmod.sfx;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.ArrayList;
import java.util.List;

public class SfxUtil {

    private static final int SFX_NONE = 999;

    private static List<SfxUtil> sfxList = new ArrayList<>();

    public static SfxUtil createInstance(String[] sfxIds, boolean consecutive, float sfxProb, float deltaProb, float minProb) {
        SfxUtil sfx = new SfxUtil(sfxIds, consecutive, sfxProb, deltaProb, minProb);
        sfxList.add(sfx);
        return sfx;
    }

    public static void resetAll() {
        sfxList.forEach(sfx -> sfx.reset());
    }

    private String[] sfxIds;
    private boolean consecutive;
    private float initProb;
    private float deltaProb;
    private float minProb;

    private float prob;
    private int playedVoice;
    private int numOfSfx;

    private SfxUtil(String[] sfxIds, boolean consecutive, float initProb, float deltaProb, float minProb) {
        this.sfxIds = sfxIds;
        this.consecutive = consecutive;
        this.initProb = initProb;
        this.deltaProb = deltaProb;
        this.minProb = minProb;
        this.prob = initProb;
        this.playedVoice = SFX_NONE;
        this.numOfSfx = sfxIds.length;
    }

    public void playSFX() {
        playSFX(1.0f);
    }

    public void playSFX(float volumeMod) {
        boolean canPlay = false;
        if (consecutive || playedVoice == SFX_NONE) {
            canPlay = MathUtils.random(1.0f) < prob;
        }
        playedVoice = canPlay ? playedVoice : SFX_NONE;

        if (canPlay) {
            int random = MathUtils.random(0, numOfSfx - 1);
            playedVoice = ((random == playedVoice) ? ((random + 1) % numOfSfx) : random);
            CardCrawlGame.sound.playV(sfxIds[playedVoice], volumeMod);
            prob = Math.max(minProb, prob - deltaProb);
        }
    }

    public void playLayoffSFX(int layoffSize) {
        boolean canPlay = false;
        if (consecutive || playedVoice == SFX_NONE) {
            canPlay = MathUtils.random(1.0f) < prob;
        }
        playedVoice = canPlay ? playedVoice : SFX_NONE;

        if (canPlay) {
            int random = MathUtils.random(0, numOfSfx - 1);
            playedVoice = ((random == playedVoice || (random == numOfSfx - 1 && layoffSize != 2)) ? ((random + 1) % numOfSfx) : random);
            CardCrawlGame.sound.play(sfxIds[playedVoice], 0f);
            prob = Math.max(minProb, prob - deltaProb);
        }
    }

    public void reset() {
        playedVoice = 0;
        prob = 1.0f;
    }
}
