import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

class Message implements Serializable {
    String content;
    int[] vectorTime;

    public Message(String content) {
        this.content = content;
    }
}

class Process extends Thread {
    private final int id;
    private final int port;
    private final List<Integer> otherPorts;
    private final int numProcesses;
    private final Random random = new Random();
    
    private int[] vectorClock;
    private ServerSocket serverSocket;
    private final BlockingQueue<String> eventLog = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    public Process(int id, int port, List<Integer> otherPorts) {
        this.id = id;
        this.port = port;
        this.otherPorts = otherPorts;
        this.numProcesses = otherPorts.size() + 1;
        this.vectorClock = new int[numProcesses];
        Arrays.fill(vectorClock, 0);
    }

    @Override
    public void run() {
        startServer();
        startInternalEvents();
        logEvents();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            new Thread(() -> {
                while (running) {
                    try (Socket socket = serverSocket.accept();
                         ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                         
                        Message msg = (Message) ois.readObject();
                        handleReceivedMessage(msg);
                    } catch (Exception e) {
                        if (running) e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startInternalEvents() {
        new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(random.nextInt(3000) + 1000);
                    
                    synchronized (this) { vectorClock[id]++; }
                    eventLog.add("INTERNO | Vector: " + Arrays.toString(vectorClock));
                    
                    int targetPort = otherPorts.get(random.nextInt(otherPorts.size()));
                    sendMessage(targetPort);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private void sendMessage(int targetPort) {
        try (Socket socket = new Socket("localhost", targetPort);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
             
            Message msg = new Message("Msg de P" + id + " para P" + (otherPorts.indexOf(targetPort) + 1));
            
            synchronized (this) {
                vectorClock[id]++;
                msg.vectorTime = vectorClock.clone();
            }
            eventLog.add("ENVIO   | Vector: " + Arrays.toString(vectorClock) + " | Para: P" + (otherPorts.indexOf(targetPort) + 1));
            
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleReceivedMessage(Message msg) {
        synchronized (this) {
            for (int i = 0; i < numProcesses; i++) {
                vectorClock[i] = Math.max(vectorClock[i], msg.vectorTime[i]);
            }
            vectorClock[id]++;
        }
        eventLog.add("RECEBIDO | Vector Recebido: " + Arrays.toString(msg.vectorTime) + " | Atual: " + Arrays.toString(vectorClock));
    }

    private void logEvents() {
        System.out.println("=== LOG DO PROCESSO P" + id + " ===");
        while (running) {
            try {
                System.out.println(eventLog.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void shutdown() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class VectorClockSimulation {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> ports = Arrays.asList(5000, 5001, 5002);
        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < ports.size(); i++) {
            List<Integer> otherPorts = new ArrayList<>(ports);
            otherPorts.remove((Integer) ports.get(i));
            
            Process p = new Process(i, ports.get(i), otherPorts);
            processes.add(p);
            p.start();
        }

        Thread.sleep(30000);
        
        processes.forEach(Process::shutdown);
        System.out.println("Simulação de Relógios Vetoriais encerrada.");
    }
}