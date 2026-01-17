package src.graph;
public class Profile {

    private String username;
    private String name;
    private String profileImage;

    //Required information for Profile creation is username and name
    public Profile(String username, String name, String profileImage){
        this.username = username;
        this.name = name;
        this.profileImage = profileImage;
    }

    public String getUsername(){
        return this.username;
    }
    public String getName(){
        return this.name;
    }

    public String getProfileImage(){
        return this.profileImage;
    }

    public void changeUsername(String username){
        this.username = username;
    }

    public void setProfileImage(String url){
        this.profileImage = url;
    }



}
