package org.example.pt2024_30225_ardelean_robert_assignment_2;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class SimulationManager implements Runnable {
    public int timeLimit;
    public int numberOfClients;
    public int numberOfServers;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minServiceTime;
    public int maxServiceTime;
    private final Scheduler scheduler;
    private final SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    private final ClientGenerator clientGenerator;
    private final List<Task> generatedTasks = Collections.synchronizedList(new ArrayList<>());
    private final SimulationListener listener;
    private final JTextPane textPane;

    public SimulationManager(int timeLimit, int numberOfClients, int numberOfServers, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime, SimulationListener listener) {
        this.timeLimit = timeLimit;
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        this.listener = listener;
        this.clientGenerator = new ClientGenerator(minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);

        scheduler = new Scheduler(numberOfServers, maxServiceTime);

        for (int i = 0; i < numberOfServers; i++) {
            Thread serverThred = new Thread();
            serverThred.start();
        }

        scheduler.changeStrategy(selectionPolicy);

        generateNRandomTasks();

        JFrame frame = new JFrame("Simulation Log");
        textPane = new JTextPane();
        textPane.setEditable(false);

        textPane.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        Border lineBorder = BorderFactory.createLineBorder(new Color(50, 36, 178), 6);
        scrollPane.setBorder(lineBorder);

        frame.add(scrollPane);
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);

        // Setting text alignment
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);
        StyledDocument doc = textPane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), attrs, false);
    }

    private void appendToPane(String text) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }



    private void generateNRandomTasks() {
        synchronized (generatedTasks) {
            for (int i = 0; i < numberOfClients; i++) {
                Task generatedTask = clientGenerator.generateClient(i + 1);
                generatedTasks.add(generatedTask);
            }
            sortGeneratedTasks();
        }
    }

    private void sortGeneratedTasks() {
        synchronized (generatedTasks) {
            if (selectionPolicy == SelectionPolicy.SHORTEST_TIME) {
                generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
            } else if (selectionPolicy == SelectionPolicy.SHORTEST_QUEUE) {
                generatedTasks.sort(Comparator.comparingInt(Task::getServiceTime));
            }
        }
    }


    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("simulation_log.txt"))) {
            int currentTime = 0;
            double averageServiceTime = 0;
            int peakHour = -1;
            int maxPeakHourNbClients = 0;
            ArrayList<Integer> averageWaitingTimeValues = new ArrayList<>(Collections.nCopies(numberOfClients, 0));
            double averageWaitingTime = 0;


            while (currentTime <= timeLimit) {

                int peakHourNbClients = 0;
                writer.write("Time " + currentTime + "\n");
                String logMessage = "Time " + currentTime + "\n";
                SwingUtilities.invokeLater(() -> appendToPane(logMessage));

                synchronized (generatedTasks) {
                    Iterator<Task> iterator = generatedTasks.iterator();
                    while (iterator.hasNext()) {
                        Task task = iterator.next();
                        if (task.getArrivalTime() == currentTime) {
                            averageServiceTime = averageServiceTime + task.getServiceTime();
                            scheduler.dispatchTask(task);
                            iterator.remove();
                        }
                    }
                }

                for (Server server : scheduler.getServers()) {
                    peakHourNbClients += server.getTaskSize();
                }

                // Check for peak hour
                if (peakHourNbClients >= maxPeakHourNbClients) {
                    maxPeakHourNbClients = peakHourNbClients;
                    peakHour = currentTime;
                }

                if (generatedTasks.isEmpty()) {
                    writer.write("Waiting clients: none\n");
                    String logMessage1 = "Waiting clients: none\n";
                    SwingUtilities.invokeLater(() ->  appendToPane(logMessage1));

                } else {
                    writer.write("Waiting clients: ");
                    String logMessage1 = "Waiting clients: \n";
                    SwingUtilities.invokeLater(() ->  appendToPane(logMessage1));

                    for (Task t : generatedTasks) {
                        String logMessage2 = t.toString();
                        SwingUtilities.invokeLater(() ->  appendToPane(logMessage2));
                        writer.write(t.toString());
                    }
                    String logMessage3 = "\n";
                    SwingUtilities.invokeLater(() ->  appendToPane(logMessage3));
                    writer.write("\n");

                }

                for (int i = 0; i < scheduler.getServers().size(); i++) {
                    Server server = scheduler.getServers().get(i);
                    writer.write("Queue " + (i + 1) + ": ");
                    String logMessage4 = "Queue " + (i + 1) + ": ";
                    SwingUtilities.invokeLater(() ->  appendToPane(logMessage4));
                    List<Task> queue = Arrays.asList(server.getTasks());
                    if (queue.isEmpty()) {
                        writer.write("closed\n");
                        String logMessage5 = "closed\n";
                        SwingUtilities.invokeLater(() ->  appendToPane(logMessage5));
                    } else {
                        boolean decreaseFirst = true;
                        for (Task t : queue) {
                            writer.write(t.toString());
                            String logMessage5 = t.toString();
                            SwingUtilities.invokeLater(() ->  appendToPane(logMessage5));

                            averageWaitingTimeValues.set(t.getID()-1,averageWaitingTimeValues.get(t.getID()-1)+1);

                            if (decreaseFirst) {
                                t.decreaseServiceTime();
                                decreaseFirst = false;
                            }

                            if (t.getServiceTime() == 0) {
                                server.removeTask(t);
                            }
                        }
                        writer.write("\n");
                        String logMessage3 = "\n";
                        SwingUtilities.invokeLater(() ->  appendToPane(logMessage3));
                    }
                }

                writer.write("\n");
                String logMessage3 = "\n";
                SwingUtilities.invokeLater(() ->  appendToPane(logMessage3));

                writer.flush();
                currentTime++;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            int size=0;
            for (Integer averageWaitingTimeValue : averageWaitingTimeValues) {

                if (averageWaitingTimeValue > 0) {
                    averageWaitingTime += averageWaitingTimeValue;
                    size++;
                }

            }

            averageWaitingTime = averageWaitingTime/size;

            writer.write("Average Waiting Time: " + averageWaitingTime + "\n");
            String logMessage6 = "Average Waiting Time: " + averageWaitingTime + "\n";
            SwingUtilities.invokeLater(() ->  appendToPane(logMessage6));
            writer.write("Average Service Time: " + averageServiceTime / numberOfClients + "\n");
            String logMessage7 = "Average Service Time: " + averageServiceTime / numberOfClients + "\n";
            SwingUtilities.invokeLater(() ->  appendToPane(logMessage7));
            writer.write("Peak Hour: " + peakHour + "\n");
            String logMessage8 = "Peak Hour: " + peakHour + "\n";
            SwingUtilities.invokeLater(() ->  appendToPane(logMessage8));

            if (listener != null) {
                listener.onSimulationComplete("Simulation completed successfully!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
