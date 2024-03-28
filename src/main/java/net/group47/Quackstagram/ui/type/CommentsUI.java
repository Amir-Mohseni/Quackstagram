package net.group47.Quackstagram.ui.type;

import net.group47.Quackstagram.Handler;
import net.group47.Quackstagram.data.picture.Picture;
import net.group47.Quackstagram.data.user.User;
import net.group47.Quackstagram.ui.UIUtil;

import javax.swing.*;
import java.awt.*;



public class CommentsUI extends JFrame {
    Picture picture;
    JPanel contentPanel; // Declare contentPanel as a class-level variable to access it later

    public CommentsUI(Picture picture) {
        this.picture = picture;

        setTitle("Comments");
        setSize(UIUtil.WIDTH, UIUtil.HEIGHT);
        setMinimumSize(new Dimension(UIUtil.WIDTH, UIUtil.HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        JPanel navigationPanel = UIUtil.createNavigationPanel();

        // Content Panel for comments
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add comments to the panel
        updateCommentsPanel();

        // Add a text field and a button to add new comments
        JPanel addCommentPanel = getAddCommentPanel();

        // Add the content panel and the add comment panel to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(addCommentPanel, BorderLayout.SOUTH);

        // Add the header and navigation panel to the frame
        add(navigationPanel, BorderLayout.NORTH);


        revalidate();
        repaint();
    }

    private void updateCommentsPanel() {
        contentPanel.removeAll(); // Clear existing comments
        //Iterate over the comments and add them to the panel key and value
        for (User user : picture.getComments().keySet()) {
            String comment = picture.getComments().get(user);
            String commentText = user.getUsername() + ": " + comment;
            JLabel commentLabel = new JLabel(commentText);
            commentLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            contentPanel.add(commentLabel);
        }
    }

    private JPanel getAddCommentPanel() {
        // Panel to add a comment
        JPanel addCommentPanel = new JPanel(new BorderLayout());
        JTextField commentField = new JTextField();
        JButton addCommentButton = new JButton("Add Comment");
        addCommentButton.addActionListener(e -> {
            String comment = commentField.getText();
            if (!comment.isEmpty()) {
                User currentUser = Handler.getDataManager().forUsers().getCurrentUser();
                picture.addComment(currentUser, comment);
                // Update the content panel with the new comment
                String commentText = currentUser.getUsername() + ": " + comment;
                JLabel newCommentLabel = new JLabel(commentText);
                newCommentLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                contentPanel.add(newCommentLabel);

                // Repaint the UI to reflect the changes
                revalidate();
                repaint();
            }
        });
        addCommentPanel.add(commentField, BorderLayout.CENTER);
        addCommentPanel.add(addCommentButton, BorderLayout.EAST);
        return addCommentPanel;
    }

}