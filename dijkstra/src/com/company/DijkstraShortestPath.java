package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

class DirectedEdge {
    private final int from;
    private final int to;
    private final long weight;

    public DirectedEdge(int from, int to) {
        this.from = from;
        this.to = to;
        this.weight = 1;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }

    public long getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%d->%d ", from, to);
    }
}

class DirectedGraph {
    private final int v;
    private List<DirectedEdge>[] neighborhoodLists;

    public DirectedGraph(int v) {
        this.v = v;
        this.neighborhoodLists = (List<DirectedEdge>[]) new List[v];
        for (int i = 0; i < v; i++) {
            neighborhoodLists[i] = new ArrayList<>();
        }
    }

    public int getNumberOfVertices() {
        return v;
    }

    public void addEdge(DirectedEdge edge) {
        neighborhoodLists[edge.from()].add(edge);
    }

    public void removeEdge(DirectedEdge edge) {
        neighborhoodLists[edge.from()].remove(edge);
    }

    public Iterable<DirectedEdge> getNeighborhoodList(int v) {
        return neighborhoodLists[v];
    }
}

public class DijkstraShortestPath {

    private DirectedEdge[] edgeTo;
    private Long[] distanceTo;
    private Queue<DistanceToEdge> priorityQueue;
    public DijkstraShortestPath(DirectedGraph graph, int source) {
        edgeTo = new DirectedEdge[graph.getNumberOfVertices()];
        distanceTo = new Long[graph.getNumberOfVertices()];
        priorityQueue = new PriorityQueue<>(
                graph.getNumberOfVertices());

        for (int v = 0; v < graph.getNumberOfVertices(); v++) {
            distanceTo[v] = Long.MAX_VALUE;
        }
        distanceTo[source] = 0L;

        priorityQueue.offer(new DistanceToEdge(source, 0L));

        while (!priorityQueue.isEmpty()) {
            relax(graph, priorityQueue.poll().getEdge());
        }

    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static void main(String[] args) {
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        try {
            System.setIn(new FileInputStream("resources/10 50"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        int source = scanner.nextInt();
        int target = scanner.nextInt();

        DirectedGraph graph = new DirectedGraph(n);
        for (int i = 0; i < m; i++) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            graph.addEdge(new DirectedEdge(from, to));
        }

        long startTime = System.currentTimeMillis();
        DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph,
                source);
        int k = 0;
        final int K = 3;
        while (shortestPath.hasPathTo(target) && k < K) {
            ++k;
            for (DirectedEdge edge : shortestPath.getPathTo(target)) {
                System.out.print(edge);
                graph.removeEdge(edge);
            }
            System.out.println();
            shortestPath = new DijkstraShortestPath(graph,
                    source);
        }
        System.out.println("Znaleziono k: " + k + " rozwiazan");
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long actualMemUsed = afterUsedMem - beforeUsedMem;
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time of execution: " + elapsedTime);
        System.out.println("Memory usage: " + humanReadableByteCount(actualMemUsed, false));

    }

    private void relax(DirectedGraph graph, int v) {
        for (DirectedEdge edge : graph.getNeighborhoodList(v)) {
            int w = edge.to();

            if (distanceTo[w] > distanceTo[v] + edge.getWeight()) {
                distanceTo[w] = distanceTo[v] + edge.getWeight();
                edgeTo[w] = edge;
                DistanceToEdge dte = new DistanceToEdge(w, distanceTo[w]);

                priorityQueue.remove(dte);
                priorityQueue.offer(dte);
            }
        }

    }

    public boolean hasPathTo(int v) {
        return distanceTo[v] < Long.MAX_VALUE;
    }

    public Iterable<DirectedEdge> getPathTo(int v) {
        Deque<DirectedEdge> path = new ArrayDeque<>();
        if (!hasPathTo(v)) {
            return path;
        }
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    class DistanceToEdge implements Comparable<DistanceToEdge> {
        private final int edge;
        private long distance;

        public DistanceToEdge(int edge, long distance) {
            this.edge = edge;
            this.distance = distance;
        }

        public long getDistance() {
            return distance;
        }

        public int getEdge() {
            return edge;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + (int) (distance ^ (distance >>> 32));
            result = prime * result + edge;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DistanceToEdge other = (DistanceToEdge) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (distance != other.distance)
                return false;
            if (edge != other.edge)
                return false;
            return true;
        }

        @Override
        public int compareTo(DistanceToEdge param) {
            int cmp = new Long(distance).compareTo(param.getDistance());

            if (cmp == 0) {
                return new Integer(edge).compareTo(param.getEdge());
            }
            return 0;
        }

        private DijkstraShortestPath getOuterType() {
            return DijkstraShortestPath.this;
        }
    }
}