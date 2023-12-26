package io.github.stealingdapenta.idletd.agent.mainagent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainAgentStatsService {

    private final MainAgentStatsRepository mainAgentStatsRepository;

    public void saveMainAgentStats(MainAgentStats mainAgentStats) {
        mainAgentStatsRepository.saveMainAgentStats(mainAgentStats);
    }

    public MainAgentStats getMainAgentStats(long agentId) {
        return mainAgentStatsRepository.getMainAgentStats(agentId);
    }

    public void updateMainAgentStats(MainAgentStats mainAgentStats) {
        mainAgentStatsRepository.updateMainAgentStats(mainAgentStats);
    }

    public void deleteMainAgentStats(long agentId) {
        mainAgentStatsRepository.deleteMainAgentStats(agentId);
    }
}
