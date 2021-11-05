/**
 * The second part of the Quiz Card Player app written
 * by Head First Java 2nd Edition.
 */

package com.quizcardgame.app;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class QuizCardPlayer {

    private JTextArea display;
    private ArrayList<QuizCard> cardList;
    private QuizCard currentCard;
    private int currentCardIndex;
    private JFrame frame;
    private JButton nextButton;
    private boolean isShowAnswer;

    public static void main(String[] args) {
        QuizCardPlayer reader = new QuizCardPlayer();
        reader.go();
    }

    /**
     * Building GUI for our app.
     */
    private void go() {
        frame = new JFrame("Quiz Card PLayer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif",
                Font.BOLD,
                24);
        display = new JTextArea(10, 20);
        display.setFont(bigFont);
        display.setLineWrap(true);
        display.setEditable(false);

        JScrollPane qScroller = new JScrollPane(display);
        qScroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        nextButton = new JButton("Show Question");
        mainPanel.add(qScroller);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new NextCardListener());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load card set");
        loadMenuItem.addActionListener(new OpenMenuListener());
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(640, 500);
        frame.setVisible(true);
    }

    /**
     * We check the value of the flag "isShowAnswer" to find out what is
     * currently displayed - a question or an answer
     * and depending on the result, we perform the appropriate actions.
     */
    public class NextCardListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (isShowAnswer) {
                //Show the answer, since the question was already shown
                display.setText(currentCard.getAnswer());
                nextButton.setText("Next Card");
                isShowAnswer = false;
            } else {
                //next question
                if (currentCardIndex < cardList.size()) {
                    showNextCard();
                } else {
                    //no more cards
                    display.setText("That was last card!");
                    nextButton.setEnabled(false);
                }
            }
        }
    }

    /**
     * we call the file window, which allows to select a file to open.
     */
    public class OpenMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());
        }
    }

    /**
     * Create a BufferedReader associated with the new fileReader.
     * Provide the FileReader object with the File object selected
     * by the user in the file open window.
     *
     * We read one line at a time, passing the result to a method "makeCard"
     * that split and converts to a real object, and then adds it to ArrayList
     * @param file
     */
    private void loadFile(File file) {
        cardList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                makeCard(line);
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println("couldn`t read the card file");
            ex.printStackTrace();
        }
        showNextCard();
    }

    /**
     * Each line of text corresponds to one flashcard, we split
     * by a method split() from the class String that splits the line into
     * two parts (one for the question and one for the answer).
     * @param lineToParse
     */
    private void makeCard(String lineToParse) {
        String[] result = lineToParse.split("/");
        QuizCard card = new QuizCard(result[0], result[1]);
        cardList.add(card);
        System.out.println("made a card");
    }
    private void showNextCard() {
        currentCard = cardList.get(currentCardIndex);
        currentCardIndex++;
        display.setText(currentCard.getQuestion());
        nextButton.setText("Show Answer");
        isShowAnswer = true;
    }
    final class QuizCard {
        private String question;
        private String answer;
        private QuizCard(final String q, final String a) {
            question = q;
            answer = a;
        }
        public String getQuestion() {
            return question;
        }
        public String getAnswer() {
            return  answer;
        }
    }
}
