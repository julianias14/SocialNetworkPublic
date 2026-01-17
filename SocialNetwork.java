
package src.graph;
import java.util.ArrayList;

/*
 * CS112 Graph Lab
 * 
 * Implement the addProfile(), searchFriend(), allFriends() addFriend() using an adjacency matrix
 * based representation of an undirected graph
 * 
 * @author Juliania Shyprykevych, Pooja Kedia, Matthew Specht
 */

public class SocialNetwork{
    public ArrayList<Profile> profiles;
    public int[][] SocialNetwork;

    /**
     * Constructor to initialize the adjacency matrix using an initialized list of Profiles
     * 
     * The constructor will be provided. This initializes an n-dimensional matrix, populated with 0s.
     * Representing a social network of profiles with no Friends/connections.
     * 
     * @param profiles arraylist of Profile objects that represent the nodes the social network is comprised of
     */
    public NetworkGraph(ArrayList<Profile> profiles){
        this.profiles = new ArrayList<>(profiles);
        int n = profiles.size();
        
        int[][] NetworkGraph = new int[n][n];
        for(int i = 0; i< n; i++){
            for(int j=0; j<n; j++){
                NetworkGraph[i][j] =0;
            }
        }
        this.SocialNetwork = NetworkGraph;
    }
    
    /*
     * Add a new profile to the network while preserving existing friendships
     * 
     * Ensure the profile does not exist already and perform resizing.
     * 
     * @param newProfile Profile of new user to be added.
     */
    public void addProfile(Profile newProfile) {
      // WRITE YOUR CODE HERE //
        
    }

    /**
     * Searches if edge exists between two profiles.
     * 
     * Ensure both profiles exists and perform lookup.
     * 
     * @param primaryUser Profile of primary user
     * @param friendLookup Profile of the "friend" who you are looking up
     */
    public boolean searchFriend(Profile primaryUser, Profile friendLookup){
        // WRITE YOUR CODE HERE //
        return false;
    }

    /**
     * Return an arraylist of Profiles that represents the friend list of a user.
     * 
     * Ensure profile exists in the network.
     * 
     * @param primaryUser Profile who's friendlist you are trying to populate
     */
    public ArrayList<Profile> allFriends(Profile primaryUser){
        // WRITE YOUR CODE HERE //
        return new ArrayList<>();
    }
    /*
     * Add friend if they already exist in the network and add the friendship to the adjacency matrix.
     * 
     * Ensure both profiles exist, users aren't already friends, and both users exist.
     * 
     * @param primaryUser Profile who is initiating the friendship
     * @param friend User that primary user is adding
     */
    public void addFriend(Profile primaryUser, Profile friend) {
        // WRITE YOUR CODE HERE //
    } 
}
