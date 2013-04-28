package br.com.tbp.algorithm;


import br.com.tbp.model.Edge;
import br.com.tbp.model.Graph;
import br.com.tbp.model.User;

import java.util.*;

public class GraphDistance {

    public void execute(Graph graph) {
        int N = graph.getNodeSet().size();
        double[] betweenness = new double[N];
        double[] eccentricity = new double[N];
        double[] closeness = new double[N];
        int diameter = 0;
        double avgDist = 0;
        int shortestPaths = 0;
        int radius = Integer.MAX_VALUE;
        HashMap<User, Integer> indicies = new HashMap<User, Integer>();
        int index = 0;
        for (User s : graph.getNodeSet()) {
            indicies.put(s, index);
            index++;
        }
        for (User s : graph.getNodeSet()) {
            Stack<User> S = new Stack<User>();
            LinkedList<User>[] P = new LinkedList[N];
            double[] theta = new double[N];
            int[] d = new int[N];
            for (int j = 0; j < N; j++) {
                P[j] = new LinkedList<User>();
                theta[j] = 0;
                d[j] = -1;
            }
            int s_index = indicies.get(s);
            theta[s_index] = 1;
            d[s_index] = 0;
            LinkedList<User> Q = new LinkedList<User>();
            Q.addLast(s);
            while (!Q.isEmpty()) {
                User v = Q.removeFirst();
                S.push(v);
                int v_index = indicies.get(v);
                for (Edge edge : getEdges(v, graph)) {
                    User reachable = getOpposite(v, edge);
                    int r_index = indicies.get(reachable);
                    if (d[r_index] < 0) {
                        Q.addLast(reachable);
                        d[r_index] = d[v_index] + 1;
                    }
                    if (d[r_index] == (d[v_index] + 1)) {
                        theta[r_index] = theta[r_index] + theta[v_index];
                        P[r_index].addLast(v);
                    }
                }
            }
            double reachable = 0;
            for (int i = 0; i < N; i++) {
                if (d[i] > 0) {
                    avgDist += d[i];
                    eccentricity[s_index] = (int) Math.max(eccentricity[s_index], d[i]);
                    closeness[s_index] += d[i];
                    diameter = Math.max(diameter, d[i]);
                    reachable++;
                }
            }
            radius = (int) Math.min(eccentricity[s_index], radius);
            if (reachable != 0) {
                closeness[s_index] /= reachable;
            }
            shortestPaths += reachable;
            double[] delta = new double[N];
            while (!S.empty()) {
                User w = S.pop();
                int w_index = indicies.get(w);
                ListIterator<User> iter1 = P[w_index].listIterator();
                while (iter1.hasNext()) {
                    User u = iter1.next();
                    int u_index = indicies.get(u);
                    delta[u_index] += (theta[u_index] / theta[w_index]) * (1 + delta[w_index]);
                }
                if (w != s) {
                    betweenness[w_index] += delta[w_index];
                }
            }
        }
        avgDist /= shortestPaths;//mN * (mN - 1.0f);
        for (User s : graph.getNodeSet()) {
            int s_index = indicies.get(s);
            betweenness[s_index] /= 2;
            s.setBetweenness(betweenness[s_index]);
            s.setCloseness(closeness[s_index]);
            s.setEccentricity(eccentricity[s_index]);
        }
        graph.setAvgDist(avgDist);
        graph.setDiameter(diameter);
        graph.setRadius(radius);
        graph.setShortestPaths(shortestPaths);

    }

    private Set<Edge> getEdges(User source, Graph graph) {
        Set<Edge> edges = new HashSet<Edge>();
        for (Edge edge : graph.getEdgeSet()) {
            if (edge.getUser1().equals(source) || edge.getUser2().equals(source)) {
                edges.add(edge);
            }
        }
        return edges;
    }

    private User getOpposite(User user, Edge edge) {
        if(edge.getUser1().equals(user)) {
            return edge.getUser2();
        }
        if(edge.getUser2().equals(user)) {
            return edge.getUser1();
        }
        return null;
    }


}
