package org.example.pt2024_30225_ardelean_robert_assignment_2;

public class ClientGenerator {
    private final int minArrivalTime;
    private final int maxArrivalTime;
    private final int minServiceTime;
    private final int maxServiceTime;

    public ClientGenerator(int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime) {
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
    }

    public Task generateClient(int id) {
        int arrivalTime = getRandomNumber(minArrivalTime, maxArrivalTime);
        int serviceTime = getRandomNumber(minServiceTime, maxServiceTime);
        return new Task(id, arrivalTime, serviceTime);
    }

    private int getRandomNumber(int min, int max) {
        return ((int) (Math.random() * (max - min + 1) + min));
    }

}
