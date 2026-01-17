# SocialNetworkPublic
## Official assignment description:
The purpose of this assignment is to implement a resizing adjacency matrix for an undirected graph, which represents a friends list of a social network.

SocialNetwork.java – This class contains the basic operations to be performed on the adjacency matrix. You will edit this class to implement the methods and submit to AutoLab. 
Profile.java – This class file stores data about a user’s profile and contains the user’s username, name, and status. 
Driver.java – The driver provides an interface for creating profiles, adding/removing friends, and viewing the graph structure. You can use this to test your solution and check if it behaves as expected.

### addProfile
This method adds a new Profile to the ArrayList of Profiles. If the profile is already present in the ArrayList then just do nothing (use the ArrayList contains() method).
Since adding a new Profile adds a corresponding vertex to our graph, we will then have to resize the adjacency matrix. Create a new 2d array with an extra row and column, copy over the values from the old adjacency matrix, then update the “graph” instance variable to the new updated adjacency matrix. 
Remember, the Profiles ArrayList and the adjacency matrix are parallel, meaning that if something is at index “x” in the Profiles ArrayList, then it will also correspond to index “x” in the adjacency matrix (both row and column). When we resize our adjacency matrix from size n to n+1, the first n by n values will be exactly the same (and need to be copied over), and the extra row and column would be new, since this is a boolean matrix they will have default values of “false”.
Here is the Driver after adding the profiles (“username1”, “name1”, “status1”), (“username2”, “name2”, “status2”), (“username3”, “name3”, “status3”)
Note: The driver will display the most recently added profile (the third one shown above). You should use the Next Profile and Previous Profile buttons to cycle through the Profile list, to check that they all appear and are in the correct order.

### setFriend
Create/remove a link between the two user profiles by retrieving the corresponding index and adding/removing edges to connect/disconnect both users.
Check if both profiles exist in the network, and find the index of both user profiles. If you have the profile object, you can use the indexOf method to find the correct index (this method, along with some other ArrayList methods are explained above). If one or both of the users do not exist, then simply do nothing. If the users do exist, then we will set the entries in the adjacency matrix at the corresponding indices to the “areFriends” boolean. This boolean could either be true or false, if it is true then we are adding a friendship, and if it is false that represent removing a friendship.
Since this is an adjacency matrix, if the value at graph[i][j] is true, then that corresponds to a single directed edge from vertex i to vertex j. Thus, to set an undirected edge between x and y, set the values at both graph[x][y] AND graph[y][x].

### searchFriend
This method searches for an edge between two Profiles, to determine if they are friends.
Return false if either user does not exist in the graph. Then, return true if there is an undirected edge between those two profiles in the adjacency matrix, otherwise return false.
To test this method, simply verify that the functionality of the “Add Friends” column properly works. It should ONLY show the users which the currently selected profile is NOT yet friends with, and it should exclude all of users current friends.

### allFriends
Populate an arraylist of all the friends a user has in an arraylist of profiles.
Check if the user we are searching for exists, otherwise return an empty arraylist. If the profile exists, search through the corresponding row/column of the user’s index and populate an arraylist by iterating through this row/column and adding every Profile which is a friend of this user.
To test this method, simply verify that the functionality of the “All Friends” column properly works. It should ONLY show the users which the currently selected profile is already friends with, and it should exclude all other non-friends.

By Pooja Kedia, Juliania Shyprykevych, and Matthew Specht
