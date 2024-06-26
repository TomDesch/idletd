package io.github.stealingdapenta.idletd.agent;

import io.github.stealingdapenta.idletd.agent.mainagent.MainAgent;
import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStats;
import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStatsService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AgentStatsService {

    private final MainAgentStatsService mainAgentStatsService;


    public void saveAgentStats(AgentStats agentStats) {
        if (agentStats instanceof MainAgentStats mainAgentStats) {
            mainAgentStatsService.saveMainAgentStats(mainAgentStats);
        }
        // todo extend for all agent types
    }

    public AgentStats getAgentStats(Agent agent) {
        if (agent instanceof MainAgent mainAgent) {
            return mainAgentStatsService.getMainAgentStats(mainAgent);
        }
        // todo extend for all agent types
        return null;
    }

    public void updateAgentStats(AgentStats agentStats) {
        if (agentStats instanceof MainAgentStats mainAgentStats) {
            mainAgentStatsService.updateMainAgentStats(mainAgentStats);
        }
        // todo extend for all agent types

    }
}
