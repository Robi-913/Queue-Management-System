package org.example.pt2024_30225_ardelean_robert_assignment_2;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<Server> servers;
    private Strategy strategy;


    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        servers = new ArrayList<>(maxTasksPerServer);
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            servers.add(server);
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ShortestQueueStrategy();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new TimeStrategy();
        }
    }

    public void dispatchTask(Task task) {
        strategy.addTask(servers, task);
    }

    public List<Server> getServers() {
        return servers;
    }

}
