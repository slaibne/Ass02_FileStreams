import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import static java.nio.file.StandardOpenOption.CREATE;

public class RandProductMaker extends JFrame {
    //Product product;
    Vector<JTextField> textBoxes = new Vector<>();

    JFrame frame;

    JPanel textPanel;
    JTextField recordCount;
    JTextField name;
    JTextField description;
    JTextField ID;
    JTextField cost;

    JPanel buttonPanel;
    JButton addRecord;
    JButton quit;

    public RandProductMaker() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        frame = new JFrame();
        frame.setLayout(null);

        //Finds the users screen height and width
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        //Centers the frame in the middle of the user's screen
        frame.setSize(screenWidth / 2, screenHeight / 2);
        frame.setLocation(screenWidth / 4, screenHeight / 4);

        textAreaCreation();
        textPanel.setBounds(frame.getWidth() / 4, 20, 400, 200);
        frame.add(textPanel);

        buttonCreation();
        buttonPanel.setBounds(frame.getWidth() / 3, 275, 250, 50);
        frame.add(buttonPanel);

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void textAreaCreation() {
        textPanel = new JPanel(new GridLayout(5,1,5,5));

        recordCount = new JTextField("Record Count: 0");
        recordCount.setBorder(null);
        recordCount.setEditable(false);
        textPanel.add(recordCount);

        name = new JTextField("Name:(Remove this)");
        textBoxes.add(name);
        textPanel.add(name);

        description = new JTextField("Description:(Remove this)");
        textBoxes.add(description);
        textPanel.add(description);

        ID = new JTextField("ID:(Remove this)");
        textBoxes.add(ID);
        textPanel.add(ID);

        cost = new JTextField("Cost:(Remove this)");
        textBoxes.add(cost);
        textPanel.add(cost);
    }

    private void buttonCreation() {
        buttonPanel = new JPanel(new BorderLayout());

        addRecord = new JButton("Add Record");
        addRecord.setFocusable(false);
        addRecord.addActionListener((ActionEvent ae) -> recordAddition(textBoxes, recordCount));
        buttonPanel.add(addRecord, BorderLayout.LINE_START);

        quit = new JButton("   Quit   ");
        quit.setFocusable(false);
        quit.addActionListener((ActionEvent ae) -> System.exit(0));
        buttonPanel.add(quit, BorderLayout.LINE_END);
    }

    private void recordAddition(Vector<JTextField> textBoxes, JTextField recordCount) {
        int i = 1;
        String name = "", description = "", ID = "", padding = "";
        double cost = 0;

        for (JTextField box : textBoxes) {
            switch (i) {
                case 1:
                    name = box.getText();

                    if (name.length() > 35 || name.length() == 0) {
                        JOptionPane.showMessageDialog(frame, "The name of choice is longer than 35 characters or doesn't exist.");
                        return;
                    } else {
                        for (int x = 0; x < 35 - name.length(); x++) {
                            padding += " ";
                        }
                        name += padding;
                        padding = "";
                    }

                    box.setText("");
                    break;
                case 2:
                    description = box.getText();

                    if (description.length() > 75 || description.length() == 0) {
                        JOptionPane.showMessageDialog(frame, "The description of choice is longer than 75 characters or doesn't exist.");
                        return;
                    } else {
                        for (int x = 0; x < 75 - description.length(); x++) {
                            padding += " ";
                        }
                        description += padding;
                        padding = "";
                    }

                    box.setText("");
                    break;
                case 3:
                    ID = box.getText();

                    if (ID.length() > 6 || ID.length() == 0) {
                        JOptionPane.showMessageDialog(frame, "Your ID of choice is longer than 6 characters or doesn't exist.");
                        return;
                    } else {
                        for (int x = 0; x < 6 - ID.length(); x++) {
                            padding += " ";
                        }
                        ID += padding;
                        padding = "";
                    }

                    box.setText("");
                    break;
                case 4:
                    try {
                        cost = Double.parseDouble(box.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "That isn't a price.");
                        return;
                    }

                    box.setText("");
                    break;
                case 5:
                    return;
            }

            i++;
        }

        Product p = new Product(name, description, ID, cost);

        JFileChooser chooser = new JFileChooser();
        File selectedFile;

        try {
            File workingDirectory= new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);

            //If they selected a file, it will run the code
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                OutputStream out =
                        new BufferedOutputStream(Files.newOutputStream(file, CREATE));
                BufferedWriter writer =
                        new BufferedWriter(new OutputStreamWriter(out));

                writer.write(p.toCSVDataRecord(name, description, ID, cost));
                writer.newLine();
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String record = recordCount.getText();
        int count = Integer.valueOf(record.substring(14)) + 1;
        recordCount.setText("Record Count: " + count);
    }
}