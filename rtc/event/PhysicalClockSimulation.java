import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

class Message implements Serializable {
    String content;
    long physicalTime;

    public Message(String content) {
        this.content = content;
    }
}

class Process extends Thread {
    private final int id;
    private final int port;
    private final List<Integer> otherPorts;
    private final Random random = new Random();
    
    private long physicalClock = System.currentTimeMillis();
    private ServerSocket serverSocket;
    private final BlockingQueue<String> eventLog = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    public Process(int id, int port, List<Integer> otherPorts) {
        this.id = id;
        this.port = port;
        this.otherPorts = otherPorts;
    }

    @Override
    public void run() {
        System.out.println("Processo P" + id + " iniciado");
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
                    
                    physicalClock = System.currentTimeMillis();
                    eventLog.add("INTERNO | TS Físico: " + physicalClock);
                    
                    int targetPort = otherPorts.get(random.nextInt(otherPorts.size()));
                    sendMessage(targetPort);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private void sendMessage(int targetPort) {
        try {
            // Delay artificial para simular latência
            Thread.sleep(random.nextInt(10));
            
            try (Socket socket = new Socket("localhost", targetPort);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                
                Message msg = new Message("Msg de P" + id);
                physicalClock = System.currentTimeMillis();
                msg.physicalTime = physicalClock;
                
                oos.writeObject(msg);
                eventLog.add("ENVIO | TS Físico: " + physicalClock + " | Para: P" + (otherPorts.indexOf(targetPort) + 1));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleReceivedMessage(Message msg) {
        long receivedTime = msg.physicalTime;
        physicalClock = System.currentTimeMillis();
        eventLog.add("RECEBIDO | TS Físico Recebido: " + receivedTime + " | Local: " + physicalClock);
    }

    private void logEvents() {
        try {
            while (running) {
                String logEntry = eventLog.take(); // Bloqueia até ter um evento
                System.out.println("=== LOG DO PROCESSO P" + id + " ===");
                System.out.println(logEntry);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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

public class PhysicalClockSimulation {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Iniciando PhysicalClockSimulation - Relógios Físicos");

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
        System.out.println("Simulação de Relógios Físicos encerrada.");
    }
}