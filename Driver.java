package src.graph;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Swing-based driver for the Social Network lab (CS112).
 * Uses existing NetworkGraph methods: constructor, addFriend, searchFriend, allFriends.
 * Manages its own profile list and rebuilds the graph when profiles are added.
 */
public class Driver extends JFrame {
    private ArrayList<Profile> profiles;
    private NetworkGraph network;
    private DefaultComboBoxModel<String> profileModel;
    private Runnable updateAddModels;
    private Runnable updateSearchModels;
    private GraphVisualizationPanel graphViz;

    public Driver() {
        super("Social Network Driver");
        // Driver-managed profile list
        profiles = new ArrayList<>();
        network = new NetworkGraph(profiles);
        profileModel = new DefaultComboBoxModel<>();

        initUI();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();

        // --- Create Profile Tab ---
        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        createPanel.add(new JLabel("Username:"));
        JTextField createUserField = new JTextField(10);
        createPanel.add(createUserField);
        createPanel.add(new JLabel("Full Name:"));
        JTextField createNameField = new JTextField(10);
        createPanel.add(createNameField);
        createPanel.add(new JLabel("Profile Image URL:"));
        JTextField createImageField = new JTextField(20);
        createPanel.add(createImageField);
        JButton createBtn = new JButton("Create Profile");
        createBtn.addActionListener(e -> {
            String u = createUserField.getText().trim();
            String n = createNameField.getText().trim();
            String imgUrl = createImageField.getText().trim();
            if (u.isEmpty() || n.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and Full Name are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Profile p = new Profile(u, n, imgUrl.isEmpty() ? null : imgUrl);
            profiles.add(p);
            profileModel.addElement(u);
            // Add profile to network (preserves existing friendships)
            network.addProfile(p);
            // Update all dropdown models
            if (updateAddModels != null) updateAddModels.run();
            if (updateSearchModels != null) updateSearchModels.run();
            // Update graph visualization
            if (graphViz != null) {
                graphViz.updateGraph(profiles, network);
                graphViz.repaint();
            }
            JOptionPane.showMessageDialog(this, "Profile '" + u + "' created.");
            createUserField.setText("");
            createNameField.setText("");
            createImageField.setText("");
        });
        createPanel.add(createBtn);
        tabs.addTab("Create Profile", createPanel);

        // --- Add Friend Tab ---
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.add(new JLabel("User 1:"));
        DefaultComboBoxModel<String> addModel1 = new DefaultComboBoxModel<>();
        JComboBox<String> addPrimary = new JComboBox<>(addModel1);
        addPanel.add(addPrimary);
        addPanel.add(new JLabel("User 2:"));
        DefaultComboBoxModel<String> addModel2 = new DefaultComboBoxModel<>();
        JComboBox<String> addSecondary = new JComboBox<>(addModel2);
        addPanel.add(addSecondary);
        
        // Update both models when profiles change
        Runnable updateAddModels = () -> {
            addModel1.removeAllElements();
            addModel2.removeAllElements();
            for (Profile p : profiles) {
                addModel1.addElement(p.getUsername());
                addModel2.addElement(p.getUsername());
            }
        };
        
        JButton addBtn = new JButton("Add Friend");
        addBtn.addActionListener(e -> {
            String u1 = (String) addPrimary.getSelectedItem();
            String u2 = (String) addSecondary.getSelectedItem();
            if (u1 == null || u2 == null || u1.equals(u2)) {
                JOptionPane.showMessageDialog(this, "Select two different existing users.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Profile p1 = findProfile(u1);
            Profile p2 = findProfile(u2);
            network.addFriend(p1, p2);
            // Update graph visualization
            if (graphViz != null) {
                graphViz.updateGraph(profiles, network);
                graphViz.repaint();
            }
            JOptionPane.showMessageDialog(this, u1 + " and " + u2 + " are now friends.");
        });
        addPanel.add(addBtn);
        tabs.addTab("Add Friend", addPanel);

        // --- Search Friendship Tab ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("User 1:"));
        DefaultComboBoxModel<String> searchModel1 = new DefaultComboBoxModel<>();
        JComboBox<String> searchPrimary = new JComboBox<>(searchModel1);
        searchPanel.add(searchPrimary);
        searchPanel.add(new JLabel("User 2:"));
        DefaultComboBoxModel<String> searchModel2 = new DefaultComboBoxModel<>();
        JComboBox<String> searchSecondary = new JComboBox<>(searchModel2);
        searchPanel.add(searchSecondary);
        
        // Update both models when profiles change
        Runnable updateSearchModels = () -> {
            searchModel1.removeAllElements();
            searchModel2.removeAllElements();
            for (Profile p : profiles) {
                searchModel1.addElement(p.getUsername());
                searchModel2.addElement(p.getUsername());
            }
        };
        
        JButton searchBtn = new JButton("Check Friendship");
        searchBtn.addActionListener(e -> {
            String u1 = (String) searchPrimary.getSelectedItem();
            String u2 = (String) searchSecondary.getSelectedItem();
            if (u1 == null || u2 == null) {
                JOptionPane.showMessageDialog(this, "Select both users.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Profile p1 = findProfile(u1);
            Profile p2 = findProfile(u2);
            boolean friends = network.searchFriend(p1, p2);
            String msg = friends ? "They are friends." : "They are not friends.";
            JOptionPane.showMessageDialog(this, msg);
        });
        searchPanel.add(searchBtn);
        tabs.addTab("Search Friend", searchPanel);

        // --- List Friends Tab ---
        JPanel listPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        listPanel.add(new JLabel("Username:"));
        JComboBox<String> listCombo = new JComboBox<>(profileModel);
        listPanel.add(listCombo);
        JButton listBtn = new JButton("List Friends");
        listBtn.addActionListener(e -> {
            String u = (String) listCombo.getSelectedItem();
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Select a user.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Profile p = findProfile(u);
            java.util.List<Profile> friends = network.allFriends(p);
            StringBuilder sb = new StringBuilder("Friends of " + u + ":\n\n");
            for (Profile f : friends) {
                sb.append("• ").append(f.getUsername()).append(" (").append(f.getName()).append(")");
                if (f.getProfileImage() != null && !f.getProfileImage().isEmpty()) {
                    sb.append(" [Has profile image]");
                }
                sb.append("\n");
            }
            if (friends.isEmpty()) {
                sb.append("No friends found.");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });
        listPanel.add(listBtn);
        tabs.addTab("List Friends", listPanel);

        // --- Edit Profile Image Tab ---
        JPanel editImagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        editImagePanel.add(new JLabel("Select User:"));
        JComboBox<String> editImageCombo = new JComboBox<>(profileModel);
        editImagePanel.add(editImageCombo);
        editImagePanel.add(new JLabel("New Image URL:"));
        JTextField editImageField = new JTextField(25);
        editImagePanel.add(editImageField);
        JButton editImageBtn = new JButton("Update Image");
        editImageBtn.addActionListener(e -> {
            String username = (String) editImageCombo.getSelectedItem();
            String newImageUrl = editImageField.getText().trim();
            if (username == null) {
                JOptionPane.showMessageDialog(this, "Select a user.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Profile p = findProfile(username);
            if (p != null) {
                p.setProfileImage(newImageUrl.isEmpty() ? null : newImageUrl);
                String message = newImageUrl.isEmpty() ? 
                    "Profile image removed for " + username : 
                    "Profile image updated for " + username;
                JOptionPane.showMessageDialog(this, message);
                editImageField.setText("");
            }
        });
        editImagePanel.add(editImageBtn);
        tabs.addTab("Edit Profile Image", editImagePanel);

        // --- Profile Viewer Tab ---
        JPanel viewerPanel = new JPanel(new BorderLayout());
        JComboBox<String> viewerCombo = new JComboBox<>(profileModel);
        JPanel viewerTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        viewerTopPanel.add(new JLabel("Select User:"));
        viewerTopPanel.add(viewerCombo);
        viewerPanel.add(viewerTopPanel, BorderLayout.NORTH);
        
        JLabel imageLabel = new JLabel("Select a user to view their profile", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 300));
        imageLabel.setBorder(BorderFactory.createEtchedBorder());
        viewerPanel.add(imageLabel, BorderLayout.CENTER);
        
        JTextArea infoArea = new JTextArea(5, 40);
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(infoArea);
        viewerPanel.add(scrollPane, BorderLayout.SOUTH);
        
        viewerCombo.addActionListener(e -> {
            String username = (String) viewerCombo.getSelectedItem();
            if (username != null) {
                Profile p = findProfile(username);
                if (p != null) {
                    // Update info area
                    StringBuilder info = new StringBuilder();
                    info.append("Username: ").append(p.getUsername()).append("\n");
                    info.append("Full Name: ").append(p.getName()).append("\n");
                    info.append("Profile Image: ").append(p.getProfileImage() != null ? p.getProfileImage() : "None").append("\n");
                    
                    // Safely get friends list
                    java.util.List<Profile> friends = new ArrayList<>();
                    try {
                        friends = network.allFriends(p);
                    } catch (Exception ex) {
                        info.append("Friends: Error loading friends\n");
                    }
                    
                    info.append("Friends: ").append(friends.size()).append("\n");
                    if (!friends.isEmpty()) {
                        info.append("Friend List: ");
                        for (int i = 0; i < friends.size(); i++) {
                            if (i > 0) info.append(", ");
                            info.append(friends.get(i).getUsername());
                        }
                    }
                    
                    infoArea.setText(info.toString());
                    
                    // Load and display image
                    String imageUrl = p.getProfileImage();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        try {
                            java.net.URL url = new java.net.URL(imageUrl);
                            java.awt.image.BufferedImage originalImage = javax.imageio.ImageIO.read(url);
                            if (originalImage != null) {
                                int maxSize = 300;
                                int width = originalImage.getWidth();
                                int height = originalImage.getHeight();
                                double scale = Math.min((double) maxSize / width, (double) maxSize / height);
                                int newWidth = (int) (width * scale);
                                int newHeight = (int) (height * scale);
                                
                                int highResWidth = newWidth * 2;
                                int highResHeight = newHeight * 2;
                                
                                java.awt.image.BufferedImage highResImage = new java.awt.image.BufferedImage(highResWidth, highResHeight, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                                java.awt.Graphics2D highResG2d = highResImage.createGraphics();
                                highResG2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                                highResG2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
                                highResG2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                                highResG2d.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                                highResG2d.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                                highResG2d.drawImage(originalImage, 0, 0, highResWidth, highResHeight, null);
                                highResG2d.dispose();
                                
                                // Scale down to final size
                                java.awt.image.BufferedImage resizedImage = new java.awt.image.BufferedImage(newWidth, newHeight, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                                java.awt.Graphics2D g2d = resizedImage.createGraphics();
                                g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                                g2d.drawImage(highResImage, 0, 0, newWidth, newHeight, null);
                                g2d.dispose();
                                
                                imageLabel.setIcon(new ImageIcon(resizedImage));
                                imageLabel.setText("");
                            } else {
                                imageLabel.setIcon(null);
                                imageLabel.setText("Failed to load image");
                            }
                        } catch (Exception ex) {
                            imageLabel.setIcon(null);
                            imageLabel.setText("Failed to load image: " + ex.getMessage());
                        }
                    } else {
                        imageLabel.setIcon(null);
                        imageLabel.setText("No profile image");
                    }
                }
            }
        });
        
        tabs.addTab("Profile Viewer", viewerPanel);

        // --- Graph Visualization Tab ---
        JPanel graphPanel = new JPanel(new BorderLayout());
        graphViz = new GraphVisualizationPanel();
        graphPanel.add(graphViz, BorderLayout.CENTER);
        
        JButton refreshBtn = new JButton("Refresh Graph");
        refreshBtn.addActionListener(e -> {
            graphViz.updateGraph(profiles, network);
            graphViz.repaint();
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        graphPanel.add(buttonPanel, BorderLayout.SOUTH);
        tabs.addTab("Graph View", graphPanel);

        add(tabs, BorderLayout.CENTER);
        
        // Store update functions to call when profiles are added
        this.updateAddModels = updateAddModels;
        this.updateSearchModels = updateSearchModels;
    }

    /** Helper: find Profile object by username from driver-managed list */
    private Profile findProfile(String username) {
        for (Profile p : profiles) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Driver::new);
    }
}
