package net.group47.Quackstagram.ui.type;

import net.group47.Quackstagram.Handler;
import net.group47.Quackstagram.data.picture.Picture;
import net.group47.Quackstagram.data.user.User;
import net.group47.Quackstagram.ui.UI;
import net.group47.Quackstagram.ui.UIManager;
import net.group47.Quackstagram.ui.UIUtil;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class QuakstagramHomeUI extends JFrame {
    private static final int IMAGE_WIDTH = UIUtil.WIDTH - 100; // Width for the image posts
    private static final int IMAGE_HEIGHT = 150; // Height for the image posts
    private static final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel imageViewPanel;


    public QuakstagramHomeUI() {
        setTitle("Quakstagram Home");
        setSize(UIUtil.WIDTH, UIUtil.HEIGHT);
        setMinimumSize(new Dimension(UIUtil.WIDTH, UIUtil.HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePanel = new JPanel(new BorderLayout());
        imageViewPanel = new JPanel(new BorderLayout());

        initializeUI();

        cardPanel.add(homePanel, "Home");
        cardPanel.add(imageViewPanel, "ImageView");

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "Home"); // Start with the home view

        // Header Panel (reuse from InstagramProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);


        add(UIUtil.createNavigationPanel(), BorderLayout.SOUTH);
    }

    private void initializeUI() {

        // Content Scroll Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Never allow horizontal scrolling
        populateContentPanel(contentPanel);

        add(scrollPane, BorderLayout.CENTER);

        // Set up the home panel
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        homePanel.add(scrollPane, BorderLayout.CENTER);


    }

    private void populateContentPanel(JPanel panel) {
        User currentUser = Handler.getDataManager().forUsers().getCurrentUser();

        for(User user : currentUser.getFollowing())
            for(Picture picture : user.getPostedPictures()){

                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
                itemPanel.setBackground(Color.WHITE); // Set the background color for the item panel
                itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                itemPanel.setAlignmentX(CENTER_ALIGNMENT);
                JLabel nameLabel = new JLabel(user.getUsername());
                nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Crop the image to the fixed size
                JLabel imageLabel = new JLabel();
                imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to image label

                ImageIcon icon = picture.getImage(IMAGE_WIDTH, IMAGE_HEIGHT);
                if(icon == null){
                    // Handle exception: Image file not found or reading error
                    imageLabel.setText("Image not found");
                } else imageLabel.setIcon(icon);


                JLabel descriptionLabel = new JLabel(picture.getCaption());
                descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel likesLabel = new JLabel("Likes: " + picture.getLikes().size());
                likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JButton likeButton = getLikeButton(picture, likesLabel);

                itemPanel.add(nameLabel);
                itemPanel.add(imageLabel);
                itemPanel.add(descriptionLabel);
                itemPanel.add(likesLabel);
                itemPanel.add(likeButton);

                panel.add(itemPanel);

                // Make the image clickable
                imageLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        displayImage(picture); // Call a method to switch to the image view
                    }
                });


                // Grey spacing panel
                JPanel spacingPanel = new JPanel();
                spacingPanel.setPreferredSize(new Dimension(WIDTH - 10, 5)); // Set the height for spacing
                spacingPanel.setBackground(new Color(230, 230, 230)); // Grey color for spacing
                panel.add(spacingPanel);

            }
    }

    private JButton getLikeButton(Picture picture, JLabel likesLabel) {
        JButton likeButton = new JButton("❤");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setBackground(LIKE_BUTTON_COLOR); // Set the background color for the like button
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false); // Remove border
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLikeAction(picture, likesLabel);
            }
        });
        return likeButton;
    }

    private JPanel createHeaderPanel() {
        // Header Panel (reuse from main.ui.type.InstagramProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel("\u1F425 Quackstagram \u1F425");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;
    }

    private void handleLikeAction(Picture picture, JLabel likesLabel) {
        User currentUser = Handler.getDataManager().forUsers().getCurrentUser();
        picture.addLike(currentUser);

        likesLabel.setText("Likes: " + picture.getLikes().size());
    }

    private void displayImage(Picture picture) {
        imageViewPanel.removeAll(); // Clear previous content

        JLabel likesLabel = new JLabel("Likes: " + picture.getLikes().size()); // Update this line

        // Display the image
        JLabel fullSizeImageLabel = new JLabel();
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);

        ImageIcon icon = picture.getImage(IMAGE_WIDTH, IMAGE_HEIGHT);
        if(icon == null){
            fullSizeImageLabel.setText("Image not found");
        } else fullSizeImageLabel.setIcon(picture.getImage(IMAGE_WIDTH, IMAGE_HEIGHT));


        //User Info 
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(picture.getPostedBy().getUsername());
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        userPanel.add(userName);//User Name

        JButton likeButton = new JButton("❤");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setBackground(LIKE_BUTTON_COLOR); // Set the background color for the like button
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false); // Remove border
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLikeAction(picture, likesLabel); // Update this line
                displayImage(picture); // Refresh the view
            }
        });

//        //View Comments
//        JButton viewComments = new JButton("View Comments");
//        viewComments.addActionListener(new ActionListener() {
//                                           @Override
//                                           public void actionPerformed(ActionEvent e) {
//                                                  new CommentsUI(picture).setVisible(true);
//                                           }
//                                       });
//
//        userPanel.add(viewComments);


        // Information panel at the bottom
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(picture.getCaption())); // Description
        infoPanel.add(new JLabel("Likes:" + picture.getLikes().size())); // Likes
        infoPanel.add(likeButton);


        imageViewPanel.add(fullSizeImageLabel, BorderLayout.CENTER);
        imageViewPanel.add(infoPanel, BorderLayout.SOUTH);
        imageViewPanel.add(userPanel, BorderLayout.NORTH);

        imageViewPanel.revalidate();
        imageViewPanel.repaint();


        cardLayout.show(cardPanel, "ImageView"); // Switch to the image view
    }

}
