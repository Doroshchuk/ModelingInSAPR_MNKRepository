import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Objects;

public class Interface {
    private JPanel inputPanel, drawingPanel;
    private JFrame frame;
    private Plot plot;
    private Font font = new Font("TimesRoman", Font.PLAIN, 10);

    public static void main(String[] args) {
        new Interface();
    }

    private Interface() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                frame = new JFrame("This plot is created by Daria Doroshchuk");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.setResizable(false);
                frame.setSize(1200, 720);
                setUpInterface();
                frame.add(drawingPanel, BorderLayout.WEST);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setUpInterface(){
        inputPanel = new JPanel();
        frame.add(inputPanel, BorderLayout.EAST);
        drawingPanel = new JPanel();
        setJPanels(inputPanel, drawingPanel);
        plot = new Plot();
        drawingPanel.add(plot);

        JLabel lbl = createLabel("Укажите координаты новой точки: ", new int[]{30, 20, 190, 20}, inputPanel);

        createLabel("x = : ", new int[]{80, 50, 40, 20}, inputPanel);
        JTextField coordinateOfXTF = createTextField(new int[]{100, 50, 60, 20}, "", inputPanel);

        createLabel("y = : ", new int[]{80, 80, 40, 20}, inputPanel);
        JTextField coordinateOfYTF = createTextField(new int[]{100, 80, 60, 20}, "", inputPanel);

        JLabel messageLbl = createLabel("", new int[]{28, 150, 200, 40}, inputPanel);

        JButton addNewPointBtn = createButton(new int[]{40, 110, 40, 40}, "Images/executing.png", inputPanel, (ActionEvent event) -> {
            double x = Double.parseDouble(coordinateOfXTF.getText());
            double y = Double.parseDouble(coordinateOfYTF.getText());

            Point newPoint = new Point(x, y);
            if(plot.getPoints().contains(newPoint)) {
                repaintSelectedPoint(newPoint);
            } else addNewPointAndRepaint(newPoint);
        });

        JButton removeSelectedPointBtn = createButton(new int[]{100, 110, 40, 40}, "Images/executing.png", inputPanel, (ActionEvent event) -> {
            if(!(plot.getSelectedPoint() == null)){
                drawingPanel.remove(plot);
                plot.removeSelectedPoint();
                repaintDrawingPanel(plot);
            } else {
                String messageText = "Вы не выбрали точку для удаления.";
                messageLbl.setText("<html><div style='text-align: center;'>" + messageText + "</div></html>");
            }
        });

        JButton removeAllPointsBtn = createButton(new int[]{160, 110, 40, 40}, "Images/executing.png", inputPanel, (ActionEvent event) -> {
            drawingPanel.remove(plot);
            plot.getPoints().clear();
            plot.setChoice(false);
            repaintDrawingPanel(plot);
        });

        JCheckBox checkBox = new JCheckBox();
        checkBox.setText("Execute MNK by line");
        checkBox.addActionListener(e -> {
            JCheckBox choice = (JCheckBox) e.getSource();
            if (choice.isSelected()) {

            } else {

            }
        });

        checkBox.setBounds(40, 10, 120, 20);
        inputPanel.add(checkBox);
    }

    private JTextField createTextField(int bounds[], String value, JPanel panel) {
        JTextField textField = new JTextField(5);
        textField.setFont(font);
        textField.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        if (!Objects.equals(value, "")) textField.setText(value);
        panel.add(textField);
        return textField;
    }

    private JButton createButton(int bounds[], String iconName, JPanel panel, ActionListener handler) {
        JButton button = new JButton();
        button.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        try {
            Image img = ImageIO.read(getClass().getResource(iconName));
            button.setIcon(new ImageIcon(img));
        } catch (IOException ex) {
        }
        button.setBackground(Color.WHITE);
        button.addActionListener(handler);
        panel.add(button);
        return button;
    }

    private JLabel createLabel(String title, int bounds[], JPanel panel) {
        JLabel label = new JLabel(title);
        label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        panel.add(label);
        return label;
    }

    private void setJPanels(JPanel inputPanel, JPanel drawingPanel) {
        inputPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
        inputPanel.setFont(font);
        inputPanel.setPreferredSize(new Dimension(frame.getWidth() / 5, frame.getHeight()));
        inputPanel.setVisible(true);
        inputPanel.setLayout(null);
        drawingPanel.setVisible(true);

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectPoint(e);
            }
        });
    }

    private void addNewPointAndRepaint(Point point){
        plot.addPoint(point);
        plot.setSelectedPoint(null);
        drawingPanel.remove(plot);
        plot.setChoice(true);
    }

    private void selectPoint(MouseEvent e){
        double distance = Double.MAX_VALUE;
        Point nearestPoint = null;
        Point position = new Point(e.getX() - drawingPanel.getWidth() / 2, drawingPanel.getHeight() / 2 - e.getY());
        for (Point point : plot.getPoints()) {
            double newDistance = plot.getDistance(point, position);
            if (newDistance < 5 && newDistance < distance) {
                nearestPoint = point;
                distance = newDistance;
            }
        }
        if (distance != Double.MAX_VALUE) {
            repaintSelectedPoint(nearestPoint);
        } else addNewPointAndRepaint(position);
    }

    private void repaintSelectedPoint(Point selectedPoint){
        drawingPanel.remove(plot);
        plot.setSelectedPoint(selectedPoint);
        repaintDrawingPanel(plot);
    }

    private void repaintDrawingPanel(Plot plot){
        drawingPanel.add(plot);
        drawingPanel.validate();
        drawingPanel.repaint();
    }
}
