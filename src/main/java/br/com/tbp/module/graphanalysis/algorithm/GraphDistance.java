package br.com.tbp.module.graphanalysis.algorithm;


import br.com.tbp.model.Edge;
import br.com.tbp.model.Graph;
import br.com.tbp.model.Node;
import br.com.tbp.module.graphanalysis.support.GraphUtils;

import java.util.*;

/**
 * Algoritmo adaptado do projeto gephi.
 * Creditos para: Patick J. McSweeney, Sebastien Heymann
 */
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
        HashMap<Node, Integer> indicies = new HashMap<Node, Integer>();
        int index = 0;
        for (Node s : graph.getNodeSet()) {
            indicies.put(s, index);
            index++;
        }
        for (Node s : graph.getNodeSet()) {
            Stack<Node> S = new Stack<Node>();
            LinkedList<Node>[] P = new LinkedList[N];
            double[] theta = new double[N];
            int[] d = new int[N];
            for (int j = 0; j < N; j++) {
                P[j] = new LinkedList<Node>();
                theta[j] = 0;
                d[j] = -1;
            }
            int s_index = indicies.get(s);
            theta[s_index] = 1;
            d[s_index] = 0;
            LinkedList<Node> Q = new LinkedList<Node>();
            Q.addLast(s);
            while (!Q.isEmpty()) {
                Node v = Q.removeFirst();
                S.push(v);
                int v_index = indicies.get(v);
                for (Edge edge : GraphUtils.getEdges(v, graph)) {
                    Node reachable = GraphUtils.getOpposite(v, edge);
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
                Node w = S.pop();
                int w_index = indicies.get(w);
                ListIterator<Node> iter1 = P[w_index].listIterator();
                while (iter1.hasNext()) {
                    Node u = iter1.next();
                    int u_index = indicies.get(u);
                    delta[u_index] += (theta[u_index] / theta[w_index]) * (1 + delta[w_index]);
                }
                if (w != s) {
                    betweenness[w_index] += delta[w_index];
                }
            }
        }
        avgDist /= shortestPaths;//mN * (mN - 1.0f);
        for (Node s : graph.getNodeSet()) {
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
}
