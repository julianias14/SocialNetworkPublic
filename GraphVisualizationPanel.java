package src.graph;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GraphVisualizationPanel extends JPanel {
    private List<Profile> profiles;
    private NetworkGraph network;
    private Map<Profile, Point2D.Double> nodePositions;
    private Map<Profile, BufferedImage> profileImages;
    private static final int NODE_RADIUS = 25;
    private static final int PADDING = 50;
    
    public GraphVisualizationPanel() {
        this.profiles = new ArrayList<>();
        this.network = null;
        this.nodePositions = new HashMap<>();
        this.profileImages = new HashMap<>();
        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.WHITE);
    }
    
    public void updateGraph(List<Profile> profiles, NetworkGraph network) {
        this.profiles = new ArrayList<>(profiles);
        this.network = network;
        loadProfileImages();
        calculateNodePositions();
    }
    
    private void loadProfileImages() {
        profileImages.clear();
        for (Profile profile : profiles) {
            String imageUrl = profile.getProfileImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    URL url = new URL(imageUrl);
                    BufferedImage originalImage = ImageIO.read(url);
                    if (originalImage != null) {
                        // Resize image to fit in node
                        BufferedImage resizedImage = resizeImage(originalImage, NODE_RADIUS * 2, NODE_RADIUS * 2);
                        profileImages.put(profile, resizedImage);
                    }
                } catch (IOException | IllegalArgumentException e) {
                    // Image failed to load, will use default circle
                    System.out.println("Failed to load image for " + profile.getUsername() + ": " + e.getMessage());
                }
            }
        }
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        // Use higher resolution for better quality
        int highResWidth = targetWidth * 2;
        int highResHeight = targetHeight * 2;
        
        BufferedImage resizedImage = new BufferedImage(highResWidth, highResHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        
        // Use high-quality rendering hints
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        // Create circular clip
        g2d.setClip(new Ellipse2D.Double(0, 0, highResWidth, highResHeight));
        g2d.drawImage(originalImage, 0, 0, highResWidth, highResHeight, null);
        g2d.dispose();
        
        // Scale down to target size for final result
        BufferedImage finalImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D finalG2d = finalImage.createGraphics();
        finalG2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        finalG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        finalG2d.drawImage(resizedImage, 0, 0, targetWidth, targetHeight, null);
        finalG2d.dispose();
        
        return finalImage;
    }
    
    private void calculateNodePositions() {
        nodePositions.clear();
        if (profiles.isEmpty()) return;
        
        int numProfiles = profiles.size();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 3 - PADDING;
        
        for (int i = 0; i < numProfiles; i++) {
            double angle = 2 * Math.PI * i / numProfiles;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            nodePositions.put(profiles.get(i), new Point2D.Double(x, y));
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (profiles.isEmpty()) {
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String message = "No profiles to display";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g2d.drawString(message, x, y);
            return;
        }
        
        // Recalculate positions if needed
        if (nodePositions.isEmpty()) {
            calculateNodePositions();
        }
        
        // Draw edges (friendships)
        g2d.setColor(new Color(200, 200, 200));
        g2d.setStroke(new BasicStroke(2));
        
        for (int i = 0; i < profiles.size(); i++) {
            for (int j = i + 1; j < profiles.size(); j++) {
                Profile p1 = profiles.get(i);
                Profile p2 = profiles.get(j);
                
                if (network != null && network.searchFriend(p1, p2)) {
                    Point2D.Double pos1 = nodePositions.get(p1);
                    Point2D.Double pos2 = nodePositions.get(p2);
                    
                    if (pos1 != null && pos2 != null) {
                        Line2D.Double line = new Line2D.Double(pos1.x, pos1.y, pos2.x, pos2.y);
                        g2d.draw(line);
                    }
                }
            }
        }
        
        // Draw nodes (profiles)
        for (Profile profile : profiles) {
            Point2D.Double pos = nodePositions.get(profile);
            if (pos != null) {
                BufferedImage image = profileImages.getOrDefault(profile, null);
                
                if (image != null) {
                    // Draw profile image
                    g2d.drawImage(image, (int) (pos.x - NODE_RADIUS), (int) (pos.y - NODE_RADIUS), NODE_RADIUS * 2, NODE_RADIUS * 2, null);
                    
                    // Draw border around image
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2));
                    Ellipse2D.Double border = new Ellipse2D.Double(
                        pos.x - NODE_RADIUS, pos.y - NODE_RADIUS, 
                        NODE_RADIUS * 2, NODE_RADIUS * 2
                    );
                    g2d.draw(border);
                } else {
                    // Draw node circle
                    Ellipse2D.Double circle = new Ellipse2D.Double(
                        pos.x - NODE_RADIUS, pos.y - NODE_RADIUS, 
                        NODE_RADIUS * 2, NODE_RADIUS * 2
                    );
                    
                    // Color based on number of friends
                    int friendCount = 0;
                    if (network != null) {
                        friendCount = network.allFriends(profile).size();
                    }
                    
                    Color nodeColor;
                    if (friendCount == 0) {
                        nodeColor = new Color(255, 200, 200); // Light red for isolated nodes
                    } else if (friendCount <= 2) {
                        nodeColor = new Color(200, 255, 200); // Light green for few friends
                    } else {
                        nodeColor = new Color(200, 200, 255); // Light blue for many friends
                    }
                    
                    g2d.setColor(nodeColor);
                    g2d.fill(circle);
                    
                    // Draw border
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(circle);
                }
                
                // Draw username
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                String username = profile.getUsername();
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (int) (pos.x - fm.stringWidth(username) / 2);
                int textY = (int) (pos.y + NODE_RADIUS + fm.getAscent() + 5);
                g2d.drawString(username, textX, textY);
            }
        }
    }
    
    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        calculateNodePositions();
    }
} 