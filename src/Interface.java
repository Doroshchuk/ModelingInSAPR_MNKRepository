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
    private JCheckBox checkBoxPieceLinearInterpolation;
    private JCheckBox checkBoxInterpolationByLagrangePolynomial;

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
                frame.setSize(1200, 660);
                setUpInterface();
                JScrollPane scrollPane = new JScrollPane(drawingPanel);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setBounds(0, 0, 1000, 660);
                JPanel contentPane = new JPanel(null);
                contentPane.setPreferredSize(new Dimension(1000, 660));
                contentPane.add(scrollPane);
                frame.add(contentPane, BorderLayout.WEST);
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

        JLabel lbl = createLabel("<html><div style='text-align: center;'>" + "Укажите координаты новой точки или выбора существующей точки:" + "</div></html>", new int[]{30, 20, 190, 50}, inputPanel);

        createLabel("x = ", new int[]{80, 65, 20, 20}, inputPanel);
        JTextField coordinateOfXTF = createTextField(new int[]{100, 65, 60, 20}, "", inputPanel);

        createLabel("y = ", new int[]{80, 95, 20, 20}, inputPanel);
        JTextField coordinateOfYTF = createTextField(new int[]{100, 95, 60, 20}, "", inputPanel);

        JLabel messageLbl = createLabel("", new int[]{28, 165, 200, 40}, inputPanel);

        JButton addNewPointBtn = createButton(new int[]{40, 125, 40, 40}, "Images/add.png", inputPanel, (ActionEvent event) -> {
            double x = Double.parseDouble(coordinateOfXTF.getText());
            double y = Double.parseDouble(coordinateOfYTF.getText());

            Point newPoint = new Point(x, y);
            if(plot.getRealPoints().contains(newPoint)) {
                messageLbl.setText("Данная точка уже есть.");
                repaintSelectedPoint(newPoint);
            } else {
                addNewPointAndRepaint(newPoint);
            }
        });

        JButton removeSelectedPointBtn = createButton(new int[]{100, 125, 40, 40}, "Images/delete.png", inputPanel, (ActionEvent event) -> {
            if(!(plot.getSelectedPoint() == null)){
                drawingPanel.remove(plot);
                plot.removeSelectedPoint();
                checkBoxPieceLinearInterpolation.setSelected(false);
                plot.setExecutingPieceLinearInterpolation(false);
                checkBoxInterpolationByLagrangePolynomial.setSelected(false);
                plot.setExecutingInterpolationByLagrangePolynomial(false);
                repaintDrawingPanel(plot);
                messageLbl.setText("");
            } else {
                String messageText = "Вы не выбрали точку для удаления.";
                messageLbl.setText("<html><div style='text-align: center;'>" + messageText + "</div></html>");
            }
        });

        JButton removeAllPointsBtn = createButton(new int[]{160, 125, 40, 40}, "Images/clear.png", inputPanel, (ActionEvent event) -> {
            drawingPanel.remove(plot);
            plot.getPoints().clear();
            checkBoxPieceLinearInterpolation.setSelected(false);
            plot.setExecutingPieceLinearInterpolation(false);
            checkBoxInterpolationByLagrangePolynomial.setSelected(false);
            plot.setExecutingInterpolationByLagrangePolynomial(false);
            repaintDrawingPanel(plot);
        });

        checkBoxPieceLinearInterpolation = new JCheckBox();
        checkBoxPieceLinearInterpolation.setText("PieceLinearInterpolation");

        checkBoxInterpolationByLagrangePolynomial = new JCheckBox();
        checkBoxInterpolationByLagrangePolynomial.setText("InterpolationByLagrangePolynomial");

//        JLabel lblForCoefMNKByLine = createLabel("", new int[]{10, 225, 160, 20}, inputPanel);
//        JLabel lblForCoefMNKByParabola = createLabel("", new int[]{10, 265, 170, 20}, inputPanel);
//        lblForCoefMNKByLine.setVisible(false);
//        lblForCoefMNKByParabola.setVisible(false);
        checkBoxPieceLinearInterpolation.addActionListener(e -> {
            JCheckBox choice = (JCheckBox) e.getSource();
            if (choice.isSelected()) {
                if(plot.getPoints().isEmpty())
                    messageLbl.setText("Укажите точки для интерполяции.");
                else {
                    PieceLinearInterpolation pieceLinearInterpolation = new PieceLinearInterpolation(plot.getRealPoints());
                    setUpUsingInterpolation(pieceLinearInterpolation);
                    plot.setExecutingPieceLinearInterpolation(true);
                    messageLbl.setText("");
//                    lblForCoefMNKByLine.setVisible(true);
//                    lblForCoefMNKByLine.setText("a: " + String.format("%.4f", mnk_class.getaLine()) + ", b: " + String.format("%.4f", mnk_class.getbLine()));
                    repaintDrawingPanel(plot);
                }
            } else {
                if (!plot.getPoints().isEmpty() && checkBoxInterpolationByLagrangePolynomial.isSelected())
                    messageLbl.setText("");
//                lblForCoefMNKByLine.setVisible(false);
                plot.setExecutingPieceLinearInterpolation(false);
                repaintDrawingPanel(plot);
            }
        });

        checkBoxInterpolationByLagrangePolynomial.addActionListener(e -> {
            JCheckBox choice = (JCheckBox) e.getSource();
            if (choice.isSelected()) {
                if(plot.getPoints().isEmpty())
                    messageLbl.setText("Укажите точки для гнтерполяции.");
                else {
                    LagrangePolynomial lagrangePolynomial = new LagrangePolynomial(plot.getRealPoints());
                    setUpUsingInterpolation(lagrangePolynomial);
                    plot.setExecutingInterpolationByLagrangePolynomial(true);
                    messageLbl.setText("");
//                    lblForCoefMNKByParabola.setVisible(true);
//                    lblForCoefMNKByParabola.setText("a: " + String.format("%.4f", mnk_class.getaParabola()) + ", b: " + String.format("%.4f", mnk_class.getbParabola()) + ", c: " + String.format("%.4f", mnk_class.getcParabola()));
                    repaintDrawingPanel(plot);
                }
            } else {
                if (!plot.getPoints().isEmpty() && checkBoxPieceLinearInterpolation.isSelected())
                    messageLbl.setText("");
//                lblForCoefMNKByParabola.setVisible(false);
                plot.setExecutingInterpolationByLagrangePolynomial(false);
                repaintDrawingPanel(plot);
            }
        });

        checkBoxPieceLinearInterpolation.setVisible(true);
        checkBoxInterpolationByLagrangePolynomial.setVisible(true);
        checkBoxPieceLinearInterpolation.setBounds(10, 205, 200, 20);
        inputPanel.add(checkBoxPieceLinearInterpolation);
        checkBoxInterpolationByLagrangePolynomial.setBounds(10, 245, 200, 20);
        inputPanel.add(checkBoxInterpolationByLagrangePolynomial);
        setUpInterfaceOfcalculatingY(messageLbl);
    }

    private void setUpInterfaceOfcalculatingY(JLabel messageLbl){
        JLabel lblForDescription = createLabel("<html><div style='text-align: center;'>Введите x для подсчёта у:</div></html>", new int[]{10, 280, 300, 20}, inputPanel);
        createLabel("x = ", new int[]{20, 317, 20, 20}, inputPanel);
        JTextField coordinateOfXTF = createTextField(new int[]{40, 317, 60, 20}, "", inputPanel);
        JLabel yByPieceLinearInterpolation = createLabel("", new int[]{20, 345, 200, 20}, inputPanel);
        JLabel yByLagrangeInterpolation = createLabel("", new int[]{20, 365, 200, 20}, inputPanel);
        JButton calculateYUsingInterpolation = createButton(new int[]{150, 305, 40, 40}, "Images/calculate.png", inputPanel, (ActionEvent event) -> {
            double x = Double.parseDouble(coordinateOfXTF.getText());
            PieceLinearInterpolation pieceLinearInterpolation = new PieceLinearInterpolation(plot.getRealPoints());
            LagrangePolynomial lagrangePolynomial = new LagrangePolynomial(plot.getRealPoints());
            setUpUsingInterpolation(pieceLinearInterpolation);
            setUpUsingInterpolation(lagrangePolynomial);
            yByLagrangeInterpolation.setText("");
            yByPieceLinearInterpolation.setText("");
            if (checkBoxPieceLinearInterpolation.isSelected() && checkBoxInterpolationByLagrangePolynomial.isSelected()){
                yByPieceLinearInterpolation.setText("(pieceLinear) y = " + String.format("%.8f", pieceLinearInterpolation.executeInterpolation(x, pieceLinearInterpolation.getListX(), pieceLinearInterpolation.getListY())));
                yByLagrangeInterpolation.setText("(lagrange) y = " + String.format("%.8f", lagrangePolynomial.executeInterpolation(x, lagrangePolynomial.getListX(), lagrangePolynomial.getListY())));
                yByPieceLinearInterpolation.setVisible(true);
                yByLagrangeInterpolation.setVisible(true);
            } else if (checkBoxPieceLinearInterpolation.isSelected() && (!checkBoxInterpolationByLagrangePolynomial.isSelected())){
                yByPieceLinearInterpolation.setText("(pieceLinear) y = " + String.format("%.8f", pieceLinearInterpolation.executeInterpolation(x, pieceLinearInterpolation.getListX(), pieceLinearInterpolation.getListY())));
                yByPieceLinearInterpolation.setVisible(true);
                yByLagrangeInterpolation.setVisible(false);
            } else if ((!checkBoxPieceLinearInterpolation.isSelected()) && checkBoxInterpolationByLagrangePolynomial.isSelected()){
                yByLagrangeInterpolation.setText("(lagrange) y = " + String.format("%.8f", lagrangePolynomial.executeInterpolation(x, lagrangePolynomial.getListX(), lagrangePolynomial.getListY())));
                yByLagrangeInterpolation.setVisible(true);
                yByLagrangeInterpolation.setVisible(false);
            } else if ((!checkBoxPieceLinearInterpolation.isSelected()) && (!checkBoxInterpolationByLagrangePolynomial.isSelected())){
                messageLbl.setText("Выберите тип интерполяции.");
            }
        });

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point position = new Point(e.getX() - drawingPanel.getWidth() / 2, drawingPanel.getHeight() / 2 - e.getY());
//                if (plot.isZoom()) {
                    System.out.println("selectedPoint " + position.getX() + ", " + position.getY());
                    Point rx = new Point(1 / plot.getUnitVectorSizeForZoom(), plot.getCenter().getY());
                    Point ry = new Point(plot.getCenter().getX(), 1 / plot.getUnitVectorSizeForZoom());
                    position = plot.performAffineTransformation(plot.getCenter(), rx, ry, position);
                    System.out.println("selectedPointZoom " + position.getX() + ", " + position.getY());
//                }
                selectPoint(position);
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
        checkBoxPieceLinearInterpolation.setSelected(false);
        plot.setExecutingPieceLinearInterpolation(false);
        checkBoxInterpolationByLagrangePolynomial.setSelected(false);
        plot.setExecutingInterpolationByLagrangePolynomial(false);
        plot.setZoom(true);
        plot.setRealPoint(point);
        plot.setSelectedPoint(null);
        drawingPanel.remove(plot);
        repaintDrawingPanel(plot);
    }

    private void selectPoint(Point position){
        double distance = Double.MAX_VALUE;
        Point nearestPoint = null;
        for (Point point : plot.getPointsAfterZoom()) {
            double newDistance = plot.getDistance(point, position);
            System.out.println(newDistance);
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
        if (checkBoxPieceLinearInterpolation.isSelected()){
            plot.setExecutingPieceLinearInterpolation(true);
        }
        if (checkBoxInterpolationByLagrangePolynomial.isSelected()){
            plot.setExecutingInterpolationByLagrangePolynomial(true);
        }
        drawingPanel.remove(plot);
        plot.setSelectedPoint(selectedPoint);
        repaintDrawingPanel(plot);
    }

    private void repaintDrawingPanel(Plot plot){
        drawingPanel.add(plot);
        drawingPanel.revalidate();
        drawingPanel.repaint();
    }

    private void setUpUsingInterpolation(PieceLinearInterpolation pieceLinearInterpolation){
        plot.setPointsUsingPieceLinearInterpolation(pieceLinearInterpolation.calculatePoints(plot.getMinValue().getX(), plot.getMaxValue().getX()));
    }

    private void setUpUsingInterpolation(LagrangePolynomial lagrangePolynomial){
        plot.setPointsUsingInterpolationByLagrangePolynomial(lagrangePolynomial.calculatePoints(plot.getMinValue().getX(), plot.getMaxValue().getX()));
    }
}
