package com.github.paopaoyue.wljmod.card;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.github.paopaoyue.wljmod.effect.SparkEffect;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;

@NoPools
public class Koro extends AbstractWorkerCard {
    public static final String ID = "Wlj:Koro";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Koro() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 4, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseDamage = 0;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard) {
            this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, this.damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        } else {
            this.addToBot(new AbstractGameAction() {

                private float duration = 0.5f;
                private float prevDuration = duration;

                @Override
                public void update() {

                    if (duration == 0.5f) {
                        CardCrawlGame.sound.playA("ATTACK_WHIFF_1", -0.6f);
                        CardCrawlGame.sound.playA("ORB_LIGHTNING_CHANNEL", 0.6f);
                        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.BLUE.cpy(), true));
                        for (int i = 0; i < 6; i++) {
                            AbstractDungeon.effectsQueue.add(new SparkEffect(-100.0f * Settings.scale - 100.0f * (i + 1), Settings.HEIGHT / 2.0f + 50.0f * (i + 1), 1000.0f, 5000.0f + 500.0f * (5 - i)));
                        }
                        AbstractDungeon.effectsQueue.add(new SparkEffect(-100.0f * Settings.scale, Settings.HEIGHT / 2.0f, 1000.0f, 8000.0f));
                        for (int i = 0; i < 6; i++) {
                            AbstractDungeon.effectsQueue.add(new SparkEffect(-100.0f * Settings.scale - 100.0f * (i + 1), Settings.HEIGHT / 2.0f - 50.0f * (i + 1), 1000.0f, 5000.0f + 500.0f * (5 - i)));
                        }
                    }

                    duration -= Gdx.graphics.getDeltaTime();
                    if (duration < 0.0F) {
                        this.isDone = true;
                    }
                }
            });
            this.addToBot(new ShakeScreenAction(0.0f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
            this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    public int onLoseHP(int damageAmount) {
        this.baseDamage += damageAmount * this.magicNumber;
        if (AbstractDungeon.player.hand.contains(this)) {
            this.applyPowers();
        }
        return 0;
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(3);
        }
    }

    public AbstractCard makeCopy() {
        return new Koro();
    }

    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.baseDamage = 0;
        return card;
    }

}
