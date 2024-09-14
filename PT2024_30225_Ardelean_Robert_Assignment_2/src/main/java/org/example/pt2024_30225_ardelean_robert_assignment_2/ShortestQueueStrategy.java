package org.example.pt2024_30225_ardelean_robert_assignment_2;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server shortestQueueServer = servers.getFirst();
        for (Server server : servers) {
            if (server.getTaskSize() < shortestQueueServer.getTaskSize()) {
                shortestQueueServer = server;
            }
        }
        shortestQueueServer.addTask(task);
    }
}
