package algorithms;

import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author Eftim
 */
public class ShortestPath {

	class Edge{
            String endNode;
            int dist;

            public Edge(String endNode, int dist) {
                this.endNode = endNode;
                this.dist = dist;
            }

            public void setDist(int dist) {
                this.dist = dist;
            }
                  
        }	
        ArrayList<String> nodes = new ArrayList<>();
        HashMap<String, ArrayList<Edge>> edges = new HashMap<>();
        Edge[] shortPaths;
        

	public boolean addNode(String s) {
            if(nodes.contains(s))
                return false;
            else{
                nodes.add(s);
                return true;
            }
	}
	
	public ArrayList<String> getNodes() {
		return nodes;
	}
	
	public void addEdge(String a, String b, int w) {
            if(edges.containsKey(a)){
                boolean found = false;
                for (Edge dest : edges.get(a)) {
                    if(dest.endNode.equals(b)){
                        dest.setDist(w);
                        found = true;
                        break;
                    }
                }
                if(found == false)
                    edges.get(a).add(new Edge(b, w));
            }else{
                edges.put(a, new ArrayList<>());
                edges.get(a).add(new Edge(b, w));
            }
	}

	public void computeShortestPaths(String start) {
            shortPaths = new Edge[nodes.size()];
            int startPos = nodes.indexOf(start);
            shortPaths[startPos] = new Edge("", 0);
            for (int i = 0; i < shortPaths.length; i++) {
                if (i != startPos)
                    shortPaths[i] = new Edge("", Integer.MAX_VALUE);
            }
            
            for (int i = 0; i < nodes.size()-1; i++) {
                boolean change = false;
                for (String node : edges.keySet()) {
                    for (Edge e : edges.get(node)) {
                        if(shortPaths[nodes.indexOf(node)].dist != Integer.MAX_VALUE){
                            if(shortPaths[nodes.indexOf(node)].dist + e.dist < shortPaths[nodes.indexOf(e.endNode)].dist){
                                shortPaths[nodes.indexOf(e.endNode)] = new Edge(node, shortPaths[nodes.indexOf(node)].dist + e.dist);
                                change = true;
                            }
                        }
                        
                    }
                }
                if(change == false)
                    break;
            }
	}
	
	/**
	 * Returns a list of nodes on the shortest path from the given origin to
	 * destination node. Returns null, if a path does not exist or there is a
	 * negative cycle in the graph.
	 * 
	 * @param start Origin node
	 * @param dest Destination node
	 * @return List of nodes on the shortest path from start to dest or null, if the nodes are not connected or there is a negative cycle in the graph.
	 */
	public ArrayList<String> getShortestPath(String start, String dest) {   
            ArrayList<String> path = new ArrayList<>();
            int dist = getShortestDist(start, dest);
            if(dist == Integer.MIN_VALUE || dist == Integer.MAX_VALUE)
                return null;
            else{
                String current = dest;
                while(true){
                    path.add(current);
                    if(shortPaths[nodes.indexOf(current)].endNode.equals(start)){
                        path.add(start);
                        break;
                    }else{
                        current = shortPaths[nodes.indexOf(current)].endNode;
                    }
                }
            }
            ArrayList<String> rPath = new ArrayList<>();
            for (int i = path.size()-1; i >=0; i--) {
                rPath.add(path.get(i));
            }
            return rPath;
              
	}
	
	/**
	 * Returns the distance of the shortest path from the given origin to
	 * destination node. Returns Integer.MAX_VALUE if a path does not exist
	 * or Integer.MIN_VALUE if there is a negative cycle in the graph.
	 * 
	 * @param start Origin node
	 * @param dest Destination node
	 * @return Distance of the shortest path from start to dest, Integer.MIN_VALUE, if there is a negative cycle in the graph, or Integer.MAX_VALUE, if the nodes are not connected.
	 */
	public int getShortestDist(String start, String dest) {
            for (String node : edges.keySet()) {
                for (Edge e : edges.get(node)) {
                    if(shortPaths[nodes.indexOf(node)].dist + e.dist < shortPaths[nodes.indexOf(e.endNode)].dist && shortPaths[nodes.indexOf(node)].dist != Integer.MAX_VALUE){
                        return Integer.MIN_VALUE;
                    }
                }
            }
            return shortPaths[nodes.indexOf(dest)].dist;
        }
        	
}

