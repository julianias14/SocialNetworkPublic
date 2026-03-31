# Social Network (Data Structures Assignment)

## Overview
This project is a graph-based assignment developed for CS112: Data Structures at Rutgers University — New Brunswick. I made this project with two peers as part of Rutgers' Assignment Guru program.

Students are asked to implement a resizable adjacency matrix for an undirected graph representing a social network's friends list. Core operations include adding user profiles, managing friendships, and querying the graph structure:
* addProfile: Adds a new user profile as a vertex in the graph, dynamically resizing the adjacency matrix.
* setFriend: Creates or removes an undirected edge between two users in the adjacency matrix.
* searchFriend: Checks whether two users share a friendhsip edge in the graph.
* allFriends: Returns a list of all profiles connected to a user.

**Note**: This repository intentionally contains starter code only (with "write your code here" placeholders) to comply with academic integrity policies. The solution to the assignment is not posted in order to avoid academic integrity violations from students taking this course. However, images of the assignment are given in this documentation.

## Technologies
* Java
* Undirected Graphs
* Adjacency Matrix (2D Boolean Array)
* ArrayLists

## Features
* Dynamically resizes the adjacency matrix as new profiles are added.
* Parallel structure between a Profile ArrayList and the adjacency matrix indices.
* Undirected edge management: setting graph[i][j] always mirrors graph[j][i].
* GUI driver for visually testing profile creation, friend management, and graph traversal.

## Project Structure
* SocialNetwork.java: Core class, implements all adjacency matrix operations (for students to edit and submit)
* Profile.java: Stores uesr data: username, name, and status
* Driver.java: GUI interface for testing profile creation and friend relationships

## How it Works
1. Profile Storage: Profiles are stored in an ArrayList<Profile> parallel to the adjacency matrix. A profile at index s in the list corresponds to row and column x in the matrix.
2. Matrix Resizing: When a new profile is added, a new (n+1)*(n+1) boolean array is created, the existing n*n values are copied over, and the new row/column defaults to false.
3. Friendship Management: setFriend(profileA, profileB, boolean) looks up both profiles' indices and sets the corresponding entries in the adjacency matrix to the given boolean.
4. Edges: searchFriend checks for an edge between two profiles; allFriends iterates over the corresponding row of the adjacency matrix and returns all connected profiles.

## Running the project
* Compile: javac Driver.java
* Run: java Driver

### ⚠️ Academic Integrity
This repository is intended for professional reference only. Students are expected to complete the implementation based on the description from the official course website.
