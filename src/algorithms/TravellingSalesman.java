package algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Eftim
 */
public class TravellingSalesman {
	
    class Node{
        
        int x;
        int y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    ArrayList<Node> nodes = new ArrayList<>();
    
        /**
	 * In this program we will consider complete undirected graphs only with
	 * Euclidean distances between the nodes.
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 */
	public void addNode(int x, int y){
            nodes.add(new Node(x, y));
	}
	
	/**
	 * Returns the distance between nodes v1 and v2.
	 * 
	 * @param v1 Identifier of the first node
	 * @param v2 Identifier of the second node
	 * @return Euclidean distance between the nodes
	 */
	public double getDistance(int v1, int v2) {
            return Math.sqrt(Math.pow(nodes.get(v2).x - nodes.get(v1).x, 2) + Math.pow(nodes.get(v2).y - nodes.get(v1).y, 2));
	}
	
	/**
	 * Finds and returns an optimal shortest tour from the origin node and
	 * returns the order of nodes to visit.
	 * 
	 * @param start Index of the origin node
	 * @return List of nodes to visit in specific order
	 */
        
        int[] tempArr;
        int[] lastPerm;
        double shortDist;
        
	public int[] calculateExactShortestTour(int start){
            
            int[] route = new int[nodes.size()];
            for (int i = 0; i < route.length; i++) {
                route[i] = i;
            }
            
            swap(route, 0, start);
            double dist = this.calculateDistanceTravelled(route);
            shortDist = dist;
            lastPerm = new int[route.length];
            tempArr = new int[1];
            permute(route, 1);
            return lastPerm;
        }
        

        
        private void permute(int[] route, int k){
            for(int i = k; i < route.length; i++){ 
                swap(route, i, k);
                if(k<route.length-1){
                    tempArr = new int[k+2]; 
                    System.arraycopy(route, 0, tempArr, 0, k+2);
                }else{
                    tempArr = new int[k+1]; 
                    System.arraycopy(route, 0, tempArr, 0, k+1);
                }
                if(this.calculateDistanceTravelled(tempArr)< shortDist)
                    permute(route, k+1); 
                swap(route, k, i); 
            } 
            
            if (k == route.length -1){
                double newDist = this.calculateDistanceTravelled(route);
                if(newDist < shortDist){
                    shortDist = newDist;
                    System.arraycopy(route, 0, lastPerm, 0, route.length);
                }
            }
        }
                
        private void swap(int[] arr, int i, int k) {

            int temp = arr[i];
            arr[i] = arr[k];
            arr[k] = temp;
        }
        
	/**
	 * Returns an optimal shortest tour and returns its distance given the
	 * origin node.
	 * 
	 * @param start Index of the origin node
	 * @return Distance of the optimal shortest TSP tour
	 */
	public double calculateExactShortestTourDistance(int start){
            return this.calculateDistanceTravelled(this.calculateExactShortestTour(start));
	}	
	
	/**
	 * Returns an approximate shortest tour and returns the order of nodes to
	 * visit given the origin node.
	 * 
	 * Uses a greedy nearest neighbor approach to construct
	 * an initial tour. Then it uses iterative 2-opt method to improve the
	 * solution.
	 * 
	 * @param start Index of the origin node
	 * @return List of nodes to visit in specific order
	 */
	public int[] calculateApproximateShortestTour(int start){
            return this.twoOptExchange(this.nearestNeighborGreedy(start));
	}
	
	/**
	 * Returns an approximate shortest tour and returns its distance given the
	 * origin node.
	 * 
	 * @param start Index of the origin node
	 * @return Distance of the approximate shortest TSP tour
	 */
	public double calculateApproximateShortestTourDistance(int start){
            return this.calculateDistanceTravelled(this.calculateApproximateShortestTour(start));
	}
	
	/**
	 * Constructs a Hamiltonian cycle by starting at the origin node and each
	 * time adding the closest neighbor to the path.
	 * 
	 * @param start Origin node
	 * @return List of nodes to visit in specific order
	 */
	public int[] nearestNeighborGreedy(int start){
            
            int currentNode = start;
            int count = 0;
            int tempNode = start;
            int[] retRoute = new int[nodes.size()];
            retRoute[count++] = start;
            Set<Integer> set = new HashSet();
            for (int i = 0; i < retRoute.length; i++) {
                set.add(i);
            }
            set.remove(currentNode);
            while(set.isEmpty()==false){
                double smallestLength = Double.MAX_VALUE;
                for (Integer node : set) {
                    double newLength = this.getDistance(currentNode, node);
                    if(newLength < smallestLength){
                        smallestLength = newLength;
                        tempNode = node;
                    }
                }
                currentNode = tempNode;
                retRoute[count++] = currentNode;
                set.remove(currentNode);
            }
            return retRoute;
	}
	
	/**
	 * Improves the given route using 2-opt exchange.
	 * 
	 * @param route Original route
	 * @return Improved route using 2-opt exchange
	 */
	private int[] twoOptExchange(int[] route) {
            
            int[] retRoute = route;
            double currentDist;
            boolean swap = true;
            int[] newRoute;
            double newDist;
            int start = 1;
            int end = route.length-1;
            boolean mem = false;
            while (swap){
                swap = false;
                currentDist = this.calculateDistanceTravelled(retRoute);
                for (int i = start; i < end; i++) {
                    for (int j = i; j < route.length; j++) {
                        newRoute = this.twoOptSwap(retRoute, i, j);
                        newDist = this.calculateDistanceTravelled(newRoute);
                        if(newDist < currentDist){
                            System.arraycopy(newRoute, 0, retRoute, 0, newRoute.length);
                            swap = true;
                            start = i;
                            mem = true;
                            break;
                        }
                    }
                    if(swap)
                        break;
                }
                if(swap==false){
                    if(mem){
                        swap = true;
                        end = start;
                        start = 1;
                    }  
                    mem = false;
                }
            }
            return retRoute;
	}
	
	/**
	 * Swaps the nodes i and k of the tour and adjusts the tour accordingly.
	 * 
	 * @param route Original tour
	 * @param i Name of the first node
	 * @param k Name of the second node
	 * @return The newly adjusted tour
	 */
	public int[] twoOptSwap(int[] route, int i, int k) {
            
            int[] newRoute = new int[route.length];
            System.arraycopy(route, 0, newRoute, 0, i);
            for (int j = 0; j <= k-i; j++) {
                newRoute[j+i] = route[k-j];
            }
            System.arraycopy(route, k+1, newRoute, k+1, route.length - (k+1));
            
            return newRoute;
	}

	/**
	 * Returns the total distance of the given tour i.e. the sum of distances
	 * of the given path add the distance between the final and initial node.
	 * 
	 * @param tour List of nodes in the given order
	 * @return Travelled distance of the tour
	 */
	public double calculateDistanceTravelled(int[] tour){
            double length = 0;
            for (int i = 0; i < tour.length; i++) {
                if(i == tour.length-1)
                    length += this.getDistance(tour[i], tour[0]);
                else
                    length += this.getDistance(tour[i], tour[i+1]);
            }
            return length;
	}
	
}
