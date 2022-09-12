/*
 * Shortest-Path Problem 
 */

import java.util.*;
import java.io.*; 
import java.io.File; 
import java.io.FileNotFoundException;
import java.util.Scanner; 

/**************************************** class Edge ****************************************/

// Data structure to store graph edges
class Edge
{
    // (source, dest, weight) triplet represent edge from source to dest having weight 
	int source, dest, weight;

	// constructor 
	public Edge(int source, int dest, int weight) 
	{
		this.source = source;
		this.dest = dest;
		this.weight = weight;
	}

	public int getEdgeSource() 
	{
        return source;
    }

    public int getEdgeDest() 
	{
        return dest;
    }

    public int getEdgeWeight() 
	{
        return weight;
    }
} // end Edge class

/**************************************** Floyd–Warshall Algorithm + The Rectangular algorithm ****************************************/
// Refernce to Journal Paper: Speeding up the Floyd–Warshall algorithm for the cycled shortest path problem (2012)

class CW2
{
    // total_dist: output the summation of the distance for all nodes (cost[])
	static int total_dist = 0; 

    public static void floydWarshall_rectangular(int[][] adjMatrix, int N)
    {
        // cost[] stores the shortest path cost
        int[][] cost = new int[N][N];

        // initialize cost[]; cost would be the same as the weight of the edge
        for (int v = 0; v < N; v++)
        {
            for (int u = 0; u < N; u++)
            {               
                cost[v][u] = adjMatrix[v][u];
            } // end inner for
        } // end outer for
 
        
        int temp1,temp2;  

        // outer for 
        for (int k = 0; k < N; ++k)
        {   
            // inner for1 -> v for rows
            for (int v = 0; v < N; ++v)
            {
                temp1 = cost[v][k];

                if (temp1 == Integer.MAX_VALUE) // if temp1 is MAX_VALUE, skip the row 
                {
                    continue;
                }

                 // inner for2 -> u for columns
                for (int u = 0; u < N; ++u)
                {    
                    temp2 = cost[k][u];
                    
                    if (temp2 == Integer.MAX_VALUE || v == k || u == k) // if temp2 is MAX_VALUE, or row/column of index K, skip the colume  
                    {
                        continue;
                    }
                    
                    if (cost[v][u] > cost[v][k] + cost[k][u])
                    {
                        cost[v][u] = cost[v][k] + cost[k][u]; 
                    }
                } // end inner for2 
            } // end inner for1
        } // end outer for 


        // Calculate and print the total distance in cost[] 
        int element;

        // Loop through the cost[]; update node with Integer.MAX_VALUE to 0
        // Output the summation of the distance for all nodes  
        for (int i = 0; i < N; i++) 
        {
			for (int j = 0; j < N; j++) 
            {
				element = cost[i][j];

				if (element == Integer.MAX_VALUE)
				{
					element = 0; 
				}
				total_dist += element;
			} // end inner for
		} // end outer for 
		System.out.println(total_dist);
    }
 

/**************************************** Main Method ****************************************/

    public static void main(String[] args) throws IOException
    {
    	long start_time = System.currentTimeMillis(); 
        
        // ArrayList to store the nodes (ignore dulication)
		ArrayList<String> nodes_collect = new ArrayList<>();
		
		// ArrayList to store Edge 
		ArrayList<Edge> edges = new ArrayList<>();
	   
		try 
		{			
            Scanner input = new Scanner(System.in);

			while(input.hasNextLine()) 
			{
				// Regex expression splits line at spaces and tabs
				String [] values = input.nextLine().split("\\s+|\\t+");		 	

				nodes_collect.add(values[1]);
				nodes_collect.add(values[2]);

				// if value is "u" - undirected 
				if (values[0].equalsIgnoreCase("u")) 
				{
					int u = Integer.parseInt(values[1]);  
					int v = Integer.parseInt(values[2]);  
					int w = Integer.parseInt(values[3]);  
					edges.add(new Edge(u, v, w));
					edges.add(new Edge(v, u, w));
				} 
				// if value is "d" - directed 
				else if (values[0].equalsIgnoreCase("d")) 
				{
                    // (u, v, w) triplet represent edge from vertex u to vertex v having weight w
					int u = Integer.parseInt(values[1]);  
					int v = Integer.parseInt(values[2]);  
					int w = Integer.parseInt(values[3]);  
					edges.add(new Edge(u, v, w));
				}
			} // end while 		
		} // end try

		catch (Exception e)
		{
			e.printStackTrace(); 
		} // end catch

		// Set to store unique nodes
        Set<String> unique_nodes = new LinkedHashSet<String>(nodes_collect);  

		// Set total number of vertices in the adjMatrix; refer to size of set
		final int N = unique_nodes.size(); 
		int I = Integer.MAX_VALUE;

        // given adjacency representation of the matrix
        int[][] adjMatrix = new int[N][N];

        // initialize adjMatrix[]
        for (int v = 0; v < N; v++)
        {
            for (int u = 0; u < N; u++)
            {
            	if (v == u) 
            	{                   
                    adjMatrix[v][u] = 0; // set diagonal elements to 0
                } // end if
                else
                { 
                    adjMatrix[v][u] = Integer.MAX_VALUE; // set remaining elements to MAX_VALUE
                } // end else
            } // end inner for 
        } // end outer for 

        // Update Edge in the adjMatrix
        for(Edge edge : edges) 
        {   
    		int source = edge.getEdgeSource();
    		int dest = edge.getEdgeDest();
    		int weight = edge.getEdgeWeight();

    		int v = new ArrayList<>(unique_nodes).indexOf(String.valueOf(source));
    		int u = new ArrayList<>(unique_nodes).indexOf(String.valueOf(dest));
    		
            // check if an existing edge with the same source and dest exist
            if (adjMatrix[v][u] != I) 
            {
                if (weight < adjMatrix[v][u]) // if the current weight is less than prev
                {
                    adjMatrix[v][u] = weight; // update weight 
                }
                else // the current weight is more than prev; do not update
                {
                    continue;
                }
            }
            adjMatrix[v][u] = weight;
		} // end for   
 
        // Run Floyd–Warshall Algorithm + The Rectangular algorithm
        floydWarshall_rectangular(adjMatrix, N);
   		long end_time = System.currentTimeMillis(); 
		//System.out.println("Elapsed time: " + (float)(end_time - start_time)/1000 + "s");
    } // end main    
}

/**************************************** End Program ****************************************/
