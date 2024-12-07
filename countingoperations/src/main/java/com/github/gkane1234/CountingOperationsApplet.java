package com.github.gkane1234;

import java.util.ArrayList;
import java.util.List;

public class CountingOperationsApplet implements SolverUpdateListener {
    private static final String SAVE_FILE_PATH = "counting_operations/outputs/saved_solutions/";
    private Solver solver;
    private SolutionList currentSolutions;
    
    private List<SolutionList> savedSolutionLists;
    private SolutionWriter savedSolutionWriter;

    private javax.swing.JTextField goalField;
    private javax.swing.JTextField[] valueFields;
    private javax.swing.JTextArea solutionsArea;
    private javax.swing.JTextArea debugArea;
    private javax.swing.JComboBox<Integer> numValuesCombo;
    private javax.swing.JPanel valuesPanel;
    private javax.swing.JComboBox<String> modeCombo;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JTextField minValueField;
    private javax.swing.JTextField maxValueField;
    private javax.swing.JTextField minSolutionsField;
    private javax.swing.JTextField maxSolutionsField;
    private javax.swing.JButton findButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton addToSavedButton;
    private javax.swing.JButton clearSavedButton;
    public CountingOperationsApplet() {
        System.out.println("Max Memory: " + Runtime.getRuntime().maxMemory());
        System.out.println("Total Memory: " + Runtime.getRuntime().totalMemory());
        System.out.println("Free Memory: " + Runtime.getRuntime().freeMemory());
        createJFrame();

        savedSolutionLists = new ArrayList<>();
        savedSolutionWriter = null;
    }

    private void createJFrame() {
        javax.swing.JFrame frame = new javax.swing.JFrame("Expression Solver");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new java.awt.BorderLayout());
        
        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout());
        
        javax.swing.JPanel topPanel = new javax.swing.JPanel();
        topPanel.setLayout(new java.awt.GridLayout(4, 2));
        
        topPanel.add(new javax.swing.JLabel("Mode:"));
        String[] modes = {"Specific Values", "Find Values"};
        modeCombo = new javax.swing.JComboBox<>(modes);
        modeCombo.addActionListener(e -> updateMode());
        topPanel.add(modeCombo);
        
        topPanel.add(new javax.swing.JLabel("Number of Values:"));
        Integer[] values = {2, 3, 4, 5, 6, 7};
        numValuesCombo = new javax.swing.JComboBox<>(values);
        numValuesCombo.setSelectedItem(7);
        numValuesCombo.addActionListener(e -> updateValueFields());
        topPanel.add(numValuesCombo);
        
        javax.swing.JButton loadButton = new javax.swing.JButton("Load/Create Solver");
        loadButton.addActionListener(e -> loadSolver());
        topPanel.add(loadButton);
        topPanel.add(new javax.swing.JLabel());
        
        topPanel.add(new javax.swing.JLabel("Goal:"));
        goalField = new javax.swing.JTextField();
        topPanel.add(goalField);
        
        mainPanel.add(topPanel, java.awt.BorderLayout.NORTH);
        
        inputPanel = new javax.swing.JPanel(new java.awt.CardLayout());
        
        // Specific values panel
        valuesPanel = new javax.swing.JPanel();
        valuesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Values"));
        updateValueFields();
        
        // Find values panel
        javax.swing.JPanel findValuesPanel = new javax.swing.JPanel();
        findValuesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Value Constraints"));
        findValuesPanel.setLayout(new java.awt.GridLayout(4, 2));
        
        findValuesPanel.add(new javax.swing.JLabel("Min Value:"));
        minValueField = new javax.swing.JTextField("1");
        findValuesPanel.add(minValueField);
        
        findValuesPanel.add(new javax.swing.JLabel("Max Value:"));
        maxValueField = new javax.swing.JTextField("100");
        findValuesPanel.add(maxValueField);
        
        findValuesPanel.add(new javax.swing.JLabel("Min Solutions:"));
        minSolutionsField = new javax.swing.JTextField("1");
        findValuesPanel.add(minSolutionsField);
        
        findValuesPanel.add(new javax.swing.JLabel("Max Solutions:"));
        maxSolutionsField = new javax.swing.JTextField("10");
        findValuesPanel.add(maxSolutionsField);
        
        inputPanel.add(valuesPanel, "Specific Values");
        inputPanel.add(findValuesPanel, "Find Values");
        mainPanel.add(inputPanel, java.awt.BorderLayout.CENTER);

        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        findButton = new javax.swing.JButton("Find Solutions");
        findButton.addActionListener(e -> findSolutions());
        buttonPanel.add(findButton);
        
        javax.swing.JButton showButton = new javax.swing.JButton("Show Solutions");
        showButton.addActionListener(e -> showSolutions());
        buttonPanel.add(showButton);
        
        addToSavedButton = new javax.swing.JButton("Add List of Solutions to Saved");
        addToSavedButton.addActionListener(e -> addToSaved());
        buttonPanel.add(addToSavedButton);

        clearSavedButton = new javax.swing.JButton("Clear Saved");
        clearSavedButton.addActionListener(e -> clearSaved());
        buttonPanel.add(clearSavedButton);

        saveButton = new javax.swing.JButton("Save Solutions to File");
        saveButton.addActionListener(e -> saveSolutions());
        buttonPanel.add(saveButton);


        mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        
        frame.add(mainPanel, java.awt.BorderLayout.NORTH);
        
        // Create split pane for solutions and debug areas
        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT);
        
        solutionsArea = new javax.swing.JTextArea();
        solutionsArea.setEditable(false);
        splitPane.setTopComponent(new javax.swing.JScrollPane(solutionsArea));
        
        debugArea = new javax.swing.JTextArea();
        debugArea.setEditable(false);
        splitPane.setBottomComponent(new javax.swing.JScrollPane(debugArea));
        
        splitPane.setResizeWeight(0.7); // Give 70% space to solutions area by default
        
        frame.add(splitPane, java.awt.BorderLayout.CENTER);
        
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void updateMode() {
        java.awt.CardLayout cl = (java.awt.CardLayout)(inputPanel.getLayout());
        cl.show(inputPanel, (String)modeCombo.getSelectedItem());
        
        // Update button text based on mode
        if (modeCombo.getSelectedItem().equals("Find Values")) {
            findButton.setText("Find Values");
        } else {
            findButton.setText("Find Solutions");
        }
    }
    
    private void updateValueFields() {
        int numValues = (Integer)numValuesCombo.getSelectedItem();
        valueFields = new javax.swing.JTextField[numValues];
        valuesPanel.removeAll();
        valuesPanel.setLayout(new java.awt.FlowLayout());
        
        for (int i = 0; i < numValues; i++) {
            valueFields[i] = new javax.swing.JTextField(5);
            valuesPanel.add(valueFields[i]);
        }
        
        valuesPanel.revalidate();
        valuesPanel.repaint();
    }
    
    private void loadSolver() {
        int numValues = (Integer)numValuesCombo.getSelectedItem();
        broadcast("Loading solver with " + numValues + " values...");
        
        new Thread(() -> {
            try {
                solver = new Solver(numValues, true, true, this,true);

                javax.swing.SwingUtilities.invokeLater(() -> 
                    broadcast("\nSolver loaded successfully!")
                );
            } catch (Exception ex) {
                javax.swing.SwingUtilities.invokeLater(() ->
                    broadcast("\nError loading solver: " + ex.getMessage())
                );
            }
        }).start();
    }
    
    private void findSolutions() {
        if (solver == null||solver.getNumValues()!=(Integer)numValuesCombo.getSelectedItem()) {
            broadcast("Solver for "+numValuesCombo.getSelectedItem()+" values not loaded! Loading solver...");
            loadSolver();
        }
        
        try {
            double goal = Double.parseDouble(goalField.getText());
            
            if (modeCombo.getSelectedItem().equals("Specific Values")) {
                double[] values = new double[valueFields.length];
                for (int i = 0; i < valueFields.length; i++) {
                    values[i] = Double.parseDouble(valueFields[i].getText().trim());
                }
                currentSolutions = solver.findAllSolutions(values, goal,200);
                solutionsArea.setText("Found " + currentSolutions.getNumSolutions() + " solutions!\nClick 'Show Solutions' to display them.");
            } else {
                int minValue = Integer.parseInt(minValueField.getText().trim());
                int maxValue = Integer.parseInt(maxValueField.getText().trim());
                int minSolutions = Integer.parseInt(minSolutionsField.getText().trim());
                int maxSolutions = Integer.parseInt(maxSolutionsField.getText().trim());
                
                int valueRange[] = {minValue, maxValue};
                int solutionRange[] = {minSolutions, maxSolutions};
                try {
                    currentSolutions = solver.findSolvableValues(goal, valueRange, solutionRange);
                } catch (Exception ex) {
                    solutionsArea.setText("Failed to find any solutions: " + ex.getMessage());
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Found values: ");
                double[] foundValues = currentSolutions.getEvaluatedExpressionList().get(0).getValues();
                for (int i = 0; i < foundValues.length; i++) {
                    sb.append(foundValues[i]);
                    if (i < foundValues.length - 1) {
                        sb.append(", ");
                    }
                }
                sb.append("\nClick 'Show Solutions' to display the solutions.");
                solutionsArea.setText(sb.toString());
            }
        } catch (NumberFormatException ex) {
            solutionsArea.setText("Invalid input. Please enter valid numbers in all fields.");
        }
    }
    
    private void showSolutions() {
        if (currentSolutions == null) {
            solutionsArea.setText("Please find solutions first!");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(currentSolutions.getNumSolutions()).append(" solutions:\n\n");
        
        for (EvaluatedExpression solution : currentSolutions.getEvaluatedExpressionList()) {
            sb.append(solution.display()).append(" = ").append(currentSolutions.getGoal()).append("\n");
        }
        
        solutionsArea.setText(sb.toString());
    }

    private void saveSolutions() {
        if (savedSolutionLists.isEmpty()) {
            solutionsArea.setText("Please find solutions first!");
            return;
        }
        broadcast("Saving solutions to file...");
        savedSolutionWriter = new SolutionWriter(SAVE_FILE_PATH, savedSolutionLists, true, true);
        savedSolutionWriter.createFile();

        broadcast("Saved solutions to file!");
    }

    private void addToSaved() {
        if (currentSolutions == null) {
            solutionsArea.setText("Please find solutions first!");
            return;
        }
        broadcast("Adding solutions to saved list...");
        savedSolutionLists.add(currentSolutions);
        broadcast("Added solutions to saved list!");
    }

    private void clearSaved() {
        broadcast("Clearing saved list...");
        savedSolutionLists.clear();
        broadcast("Saved list cleared!");
    }

    @Override
    public void onSolverUpdate(String update) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            debugArea.append("\n" + update);
        });
    }

    private void broadcast(String message) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            debugArea.append("\n" + message);
        });
    }
    
    public static void main(String[] args) {
        //TODO: make a way to view saved solutions in applet, and make the gui better.
        javax.swing.SwingUtilities.invokeLater(() -> {
            new CountingOperationsApplet();
        });
    }
}
