import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    static List<List<Edge>> paths = new LinkedList<>();
    static Vertex start;
    static Vertex end;

    static int mem = 0;

    public static void main(String[] args) {
        new Main();
    }

    List<Edge> h(Edge... edges) {
        List<Edge> h = new LinkedList<>();
        h.addAll(Arrays.asList(edges));
        return h;
    }

    Edge e(int i, Vertex v) {
        return new Edge(i, v);
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public Main() {
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        try {
            System.setIn(new FileInputStream("resources/100 2100"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        int source = scanner.nextInt();
        int target = scanner.nextInt();
        List<Vertex> nodes = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            nodes.add(new Vertex(i));
        }
        for (int i = 0; i < m; i++) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            nodes.get(to).addEdge(new Edge(i, nodes.get(from)));
        }


        start = nodes.get(source);
        end = nodes.get(target);


        long startTime = System.currentTimeMillis();
        search(end, null, new LinkedList<>());
        //System.out.println(paths);

        paths.sort(Comparator.comparing(List::size));

        while(paths.size()>0) {// && mem < 3) {
            List<Edge> path = paths.get(0);
            ++mem;
            paths.remove(path);
            System.out.println(path);
            paths.removeIf(edges -> !Collections.disjoint(edges, path));
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long actualMemUsed = afterUsedMem - beforeUsedMem;
        System.out.println("Time of execution: " + elapsedTime);
        System.out.println("Memory usage: " + humanReadableByteCount(actualMemUsed, false));
    }

    class Vertex {
        String s;
        List<Edge> in;

        public Vertex(int i) {
            this.in = new ArrayList<>();
            this.s = Integer.toString(i);
        }
        public void addEdge(Edge edge){
            this.in.add(edge);
        }

        @Override
        public String toString() {
            return s;
        }
    }

    class Edge {
        int s;
        Vertex from;

        public Edge(Vertex from) {
            this.from = from;
        }

        Edge(int name, Vertex from) {
            this.s = name;
            this.from = from;
        }

        @Override
        public String toString() {
            return Integer.toString(s);
        }
    }

    static void search(Vertex v, Edge e, List<Edge> history) {
        if (history.contains(e)) {
            return;
        }
        if (e != null) {
            history.add(e);
        }
        if (v == start) {
            paths.add(new LinkedList<>(history));
        } else {
            for (Edge inE : v.in) {
                search(inE.from, inE, history);
            }
        }
    }
}
