package io.github.stealingdapenta.idletd.skin;

import java.util.ArrayList;
import java.util.List;

// Code based on original Design from XTen's UltraPrison server
public class SkinBuilder {

    private final List<String> description = new ArrayList<>();
    private final List<String> bonuses = new ArrayList<>();
    private String data;
    private String signature;
    private boolean obtainable = false;
    private String itemName = "None";
//    private BiConsumer<PrisonPlayer, AutoMinerReward> consumer;

    public SkinBuilder of(String data, String signature) {
        this.data = data;
        this.signature = signature;
        return this;
    }

    public SkinBuilder withObtainable(boolean obtainable) {
        this.obtainable = obtainable;
        return this;
    }

    public SkinBuilder withItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public SkinBuilder addDesc(String desc) {
        description.add(desc);
        return this;
    }

    public SkinBuilder addBonus(String bonus) {
        bonuses.add(bonus);
        return this;
    }

//    public SkinBuilder withEffect(BiConsumer<Player, AutoMinerReward> consumer){
//        this.consumer = consumer;
//        return this;
//    }

    public SkinProfile build() {
//        return new SkinProfile(data, signature, obtainable, itemName, description, bonuses, consumer);
        return new SkinProfile(data, signature, obtainable, itemName, description, bonuses);
    }

}
