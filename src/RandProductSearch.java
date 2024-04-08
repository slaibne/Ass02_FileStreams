
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;
import java.util.jar.JarEntry;

import static java.nio.file.StandardOpenOption.CREATE;

public class RandProductSearch extends JFrame {
    Vector<String> lineOfFile = new Vector<>();

    JFrame frame;

    JPanel main;
    JTextField productSearch;
    JButton search;
    JButton quit;
    JTextArea productsFound;

    public RandProductSearch() {
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

        panel();
        main.setBounds(frame.getWidth() / 4, frame.getHeight() / 3, 350, 300);
        productsFound.setBounds(frame.getWidth() / 4, (frame.getHeight() / 3) + 40, 200, 200);
        frame.add(main);

        JFileChooser chooser = new JFileChooser();
        File selectedFile;

        try {
            //Creates and sets a directory
            File workingDirectory= new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);

            //If they selected a file, it will run the code
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                //Gets the file path
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                //Weird code for reading files
                InputStream in =
                        new BufferedInputStream(Files.newInputStream(file, CREATE));
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(in));

                //While there is still a line to read
                while (reader.ready()) {
                    String rec = reader.readLine();
                    lineOfFile.add(rec);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void panel() {
        main = new JPanel();
        main.setPreferredSize(new Dimension(300, 200));

        productSearch = new JTextField("Enter a name to search for", 15);
        main.add(productSearch);

        search = new JButton("Search");
        search.setFocusable(false);
        search.addActionListener((ActionEvent ae) -> search(lineOfFile, productSearch, productsFound));
        main.add(search);

        quit = new JButton("Quit");
        quit.setFocusable(false);
        quit.addActionListener((ActionEvent ae) -> System.exit(0));
        main.add(quit);

        productsFound = new JTextArea(10, 20);
        productsFound.setEditable(false);
        productsFound.setBorder(new LineBorder(Color.BLACK));
        main.add(productsFound);
    }

    public void search(Vector<String> lineOfFile, JTextField productSearch, JTextArea productsFound) {
        String product = "";
        for (String line : lineOfFile) {
            if (line.contains(productSearch.getText())) {
                product = line.substring(8, 43).strip() + "\n";
            }
        }

        productsFound.setText("This is what Products were found:\n" + product);
    }
}
