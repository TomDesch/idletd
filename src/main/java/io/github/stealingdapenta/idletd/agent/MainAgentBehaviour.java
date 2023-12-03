package io.github.stealingdapenta.idletd.agent;

import net.citizensnpcs.api.ai.tree.Behavior;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;

public class MainAgentBehaviour implements Behavior {

    @Override
    public void reset() {
//        state = null;
        // this method can be called at any time if another goal is selected
    }

    @Override
    public BehaviorStatus run() {
        return BehaviorStatus.FAILURE;
//        if(!npcIsCool()) {
//            return BehaviorStatus.FAILURE;
//        } else if (npcIsAwesome()) {
//            return BehaviorStatus.SUCCESS;
//        } else if (npcNeedsCool()) {
//            new AccumulateCoolBehavior().run(); // easily run other behavior inline
//            return BehaviorStatus.RUNNING;
//        }
    }

    @Override
    public boolean shouldExecute() {
//        if (npcIsCool()) {
//            return true;
//        }
        return false;
    }
}
