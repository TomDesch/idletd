package io.github.stealingdapenta.idletd.custommob;

import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomMobLiveDataHandle {

    private MobWrapper mobWrapper;
    private CustomMob customMob;
    private long timeSinceLastAttack;

    private long aliveSince;

    public CustomMobLiveDataHandle(MobWrapper mobWrapper, CustomMob customMob) {
        this.mobWrapper = mobWrapper;
        this.customMob = customMob;
        this.timeSinceLastAttack = System.currentTimeMillis();
        this.aliveSince = System.currentTimeMillis();
    }
}
