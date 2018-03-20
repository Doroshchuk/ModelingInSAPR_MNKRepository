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
    JCheckBox checkBoxMNKByLine;
    JCheckBox checkBoxMNKByParabola;

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

        JButton addNewPointBtn = createButton(new int[]{40, 110, 40, 40}, "Images/add.png", inputPanel, (ActionEvent event) -> {
            double x = Double.parseDouble(coordinateOfXTF.getText());
            double y = Double.parseDouble(coordinateOfYTF.getText());

            Point newPoint = new Point(x, y);
            if(plot.getPoints().contains(newPoint)) {
                repaintSelectedPoint(newPoint);
            } else {
                addNewPointAndRepaint(newPoint);
            }
        });

        JButton removeSelectedPointBtn = createButton(new int[]{100, 110, 40, 40}, "Images/delete.png", inputPanel, (ActionEvent event) -> {
            if(!(plot.getSelectedPoint() == null)){
                drawingPanel.remove(plot);
                plot.removeSelectedPoint();
                checkBoxMNKByLine.setSelected(false);
                checkBoxMNKByParabola.setSelected(false);
                repaintDrawingPanel(plot);
            } else {
                String messageText = "Вы не выбрали точку для удаления.";
                messageLbl.setText("<html><div style='text-align: center;'>" + messageText + "</div></html>");
            }
        });

        JButton removeAllPointsBtn = createButton(new int[]{160, 110, 40, 40}, "Images/clear.png", inputPanel, (ActionEvent event) -> {
            drawingPanel.remove(plot);
            plot.getPoints().clear();
            plot.setChoice(false);
            checkBoxMNKByLine.setSelected(false);
            checkBoxMNKByParabola.setSelected(false);
            repaintDrawingPanel(plot);
        });

        checkBoxMNKByLine = new JCheckBox();
        checkBoxMNKByLine.setText("MNK by Line");

        checkBoxMNKByParabola = new JCheckBox();
        checkBoxMNKByParabola.setText("MNK by Parabola");

        JLabel lblForCoefMNKByLine = createLabel("", new int[]{10, 220, 160, 20}, inputPanel);
        JLabel lblForCoefMNKByParabola = createLabel("", new int[]{10, 260, 170, 20}, inputPanel);
        lblForCoefMNKByLine.setVisible(false);
        lblForCoefMNKByParabola.setVisible(false);
        checkBoxMNKByLine.addActionListener(e -> {
            JCheckBox choice = (JCheckBox) e.getSource();
            if (choice.isSelected()) {
                if(plot.getPoints().isEmpty())
                    messageLbl.setText("Укажите точки для построения мнк.");
                else {
                    MNK_Class mnk_class = new MNK_Class(plot.getPoints());
                    setUpUsingOrdinaryLeastSquares(mnk_class);
                    plot.setExecutingMNKByLine(true);
                    messageLbl.setText("");
                    lblForCoefMNKByLine.setVisible(true);
                    lblForCoefMNKByLine.setText("a: " + String.format("%.4f", mnk_class.getaLine()) + ", b: " + String.format("%.4f", mnk_class.getbLine()));
                    repaintDrawingPanel(plot);
                }
            } else {
                if (!plot.getPoints().isEmpty() && checkBoxMNKByParabola.isSelected())
                    messageLbl.setText("");
                lblForCoefMNKByLine.setVisible(false);
                plot.setExecutingMNKByLine(false);
                repaintDrawingPanel(plot);
            }
        });

        checkBoxMNKByParabola.addActionListener(e -> {
            JCheckBox choice = (JCheckBox) e.getSource();
            if (choice.isSelected()) {
                if(plot.getPoints().isEmpty())
                    messageLbl.setText("Укажите точки для исполнения мнк.");
                else {
                    MNK_Class mnk_class = new MNK_Class(plot.getPoints());
                    setUpUsingOrdinaryLeastSquares(mnk_class);
                    plot.setExecutingMNKByParabola(true);
                    messageLbl.setText("");
                    lblForCoefMNKByParabola.setVisible(true);
                    lblForCoefMNKByParabola.setText("a: " + String.format("%.4f", mnk_class.getaParabola()) + ", b: " + String.format("%.4f", mnk_class.getbParabola()) + ", c: " + String.format("%.4f", mnk_class.getcParabola()));
                    repaintDrawingPanel(plot);
                }
            } else {
                if (!plot.getPoints().isEmpty() && checkBoxMNKByLine.isSelected())
                    messageLbl.setText("");
                lblForCoefMNKByParabola.setVisible(false);
                plot.setExecutingMNKByParabola(false);
                repaintDrawingPanel(plot);
            }
        });

        checkBoxMNKByLine.setVisible(true);
        checkBoxMNKByParabola.setVisible(true);
        checkBoxMNKByLine.setBounds(10, 200, 90, 20);
        inputPanel.add(checkBoxMNKByLine);
        checkBoxMNKByParabola.setBounds(10, 240, 110, 20);
        inputPanel.add(checkBoxMNKByParabola);
        setUpInterfaceOfcalculatingY(messageLbl);
    }

    private void setUpInterfaceOfcalculatingY(JLabel messageLbl){
        JLabel lblForDescription = createLabel("<html><div style='text-align: center;'>Введите x для подсчёта у:</div></html>", new int[]{10, 280, 300, 20}, inputPanel);
        createLabel("x = : ", new int[]{20, 312, 30, 20}, inputPanel);
        JTextField coordinateOfXTF = createTextField(new int[]{50, 312, 60, 20}, "", inputPanel);
        JButton calculateYUsingMNKByLineBtn = createButton(new int[]{150, 300, 40, 40}, "Images/delete.png", inputPanel, (ActionEvent event) -> {
            double x = Double.parseDouble(coordinateOfXTF.getText());
            MNK_Class mnk_class = new MNK_Class(plot.getPoints());
            setUpUsingOrdinaryLeastSquares(mnk_class);
            JLabel yByLine = createLabel("", new int[]{20, 340, 150, 20}, inputPanel);
            JLabel yByParabola = createLabel("", new int[]{20, 360, 150, 20}, inputPanel);
            if (checkBoxMNKByLine.isSelected() && checkBoxMNKByParabola.isSelected()){
                yByLine.setText("(by line) y = " + String.format("%.4f", -mnk_class.calculateYUsingOrdinaryLeastSquaresByLine(x)));
                yByParabola.setText("(by parabola) y = " + String.format("%.4f", -mnk_class.calculateYUsingOrdinaryLeastSquaresByParabola(x)));
                yByLine.setVisible(true);
                yByParabola.setVisible(true);
            } else if (checkBoxMNKByLine.isSelected() && (!checkBoxMNKByParabola.isSelected())){
                yByLine.setText("(by line) y = " + String.format("%.4f", -mnk_class.calculateYUsingOrdinaryLeastSquaresByLine(x)));
                yByLine.setVisible(true);
                yByParabola.setVisible(false);
            } else if ((!checkBoxMNKByLine.isSelected()) && checkBoxMNKByParabola.isSelected()){
                yByParabola.setText("(by parabola) y = " + String.format("%.4f", -mnk_class.calculateYUsingOrdinaryLeastSquaresByParabola(x)));
                yByParabola.setVisible(true);
                yByParabola.setVisible(false);
            } else if ((!checkBoxMNKByLine.isSelected()) && (!checkBoxMNKByParabola.isSelected())){
                messageLbl.setText("Выберите порядок мнк.");
            }
        });

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectPoint(e);
            }
        });
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
    }

    private void addNewPointAndRepaint(Point point){
        checkBoxMNKByLine.setSelected(false);
        checkBoxMNKByParabola.setSelected(false);
        plot.addPoint(point);
        plot.setSelectedPoint(null);
        plot.setChoice(true);
        repaintDrawingPanel(plot);
    }

    private void selectPoint(MouseEvent e){
        double distance = Double.MAX_VALUE;
        Point nearestPoint = null;
        Point position = new Point(e.getX() - drawingPanel.getWidth() / 2, drawingPanel.getHeight() / 2 - e.getY());
        for (Point point : plot.getPoints()) {
            double newDistance = plot.getDistance(point, position);
            if (newDistance < 3 && newDistance < distance) {
                nearestPoint = point;
                distance = newDistance;
            }
        }
        if (distance != Double.MAX_VALUE) {
            repaintSelectedPoint(nearestPoint);
        } else addNewPointAndRepaint(position);
    }

    private void repaintSelectedPoint(Point selectedPoint){
        if (checkBoxMNKByLine.isSelected()){
            plot.setExecutingMNKByLine(true);
        }
        if (checkBoxMNKByParabola.isSelected()){
            plot.setExecutingMNKByParabola(true);
        }
        drawingPanel.remove(plot);
        plot.setSelectedPoint(selectedPoint);
        repaintDrawingPanel(plot);
    }

    private void repaintDrawingPanel(Plot plot){
        drawingPanel.add(plot);
        drawingPanel.validate();
        drawingPanel.repaint();
    }

    private void setUpUsingOrdinaryLeastSquares(MNK_Class mnk_class){
        plot.setPointsUsingOrdinaryLeastSquaresByLine(mnk_class.calculatePointsUsingOrdinaryLeastSquaresByLine());
        plot.setPointsUsingOrdinaryLeastSquaresByParabola(mnk_class.calculatePointsUsingOrdinaryLeastSquaresByParabola());
    }
}
