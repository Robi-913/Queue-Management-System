package org.example.pt2024_30225_ardelean_robert_assignment_2;

import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server shortestTimeServer = servers.getFirst();
        int minWaitingTime = shortestTimeServer.getWaitingPeriod().get();

        for (Server server : servers) {
            int serverTime = server.getWaitingPeriod().get();
            if (serverTime < minWaitingTime) {
                shortestTimeServer = server;
                minWaitingTime = serverTime;
            }
        }
        shortestTimeServer.addTask(task);
    }
}

