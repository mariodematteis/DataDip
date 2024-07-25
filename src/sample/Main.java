package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

public class Main extends Application {

    Button estabilishConnection = null;
    Button closeConnection = null;
    Button addCondition = null;
    Button deleteCondition = null;

    TextField ipaddress_textfield = null;
    TextField port_textfield = null;
    TextField username_textfield = null;
    PasswordField password_field = null;
    TextField startdatetime_textfield = null;
    TextField enddatetime_textfield = null;
    TextField tp_textfield = null;
    TextField sl_textfield = null;
    TextField time_textfield = null;
    Label cues_label = null;
    ComboBox financialinstruments_combobox = null;
    ComboBox timeunits_combobox = null;

    ComboBox firstKindOfValue_combobox = null;
    ComboBox secondKindOfValue_combobox = null;
    ComboBox relation_combobox = null;
    TextField firstValueCondition = null;
    TextField secondValueCondition = null;

    ComboBox operation_combobox = null;

    TableView conditions_tableview = null;

    Label success_label = null;
    Label failure_label = null;
    Label operations_label = null;

    ImageView result_imageview = null;

    public Thread connection;
    public boolean simulated = false;

    public Object extractValue(int row, int column) {
        TableColumn col = (TableColumn) conditions_tableview.getColumns().get(column);
        Object data = col.getCellObservableValue(conditions_tableview.getItems().get(row)).getValue();
        return data;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setTitle("DataDip");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        financialinstruments_combobox = (ComboBox) root.lookup("#financialinstruments_combobox");
        timeunits_combobox = (ComboBox) root.lookup("#timeunits_combobox");
        time_textfield = (TextField) root.lookup("#time_textfield");
        estabilishConnection = (Button) root.lookup("#connection_button");
        closeConnection = (Button) root.lookup("#close_button");
        ipaddress_textfield = (TextField) root.lookup("#ip_textfield");
        port_textfield = (TextField) root.lookup("#port_textfield");
        username_textfield = (TextField) root.lookup("#username_textfield");
        password_field = (PasswordField) root.lookup("#password_field");
        startdatetime_textfield = (TextField) root.lookup("#startdatetime_field");
        enddatetime_textfield = (TextField) root.lookup("#enddatetime_field");
        tp_textfield = (TextField) root.lookup("#tp_field");
        sl_textfield = (TextField) root.lookup("#sl_field");
        cues_label = (Label) root.lookup("#cues_label");

        addCondition = (Button) root.lookup("#add_button");
        deleteCondition = (Button) root.lookup("#remove_button");
        firstKindOfValue_combobox = (ComboBox) root.lookup("#firstvalue_combobox");
        secondKindOfValue_combobox = (ComboBox) root.lookup("#secondvalue_combobox");
        firstValueCondition = (TextField) root.lookup("#firstvalue_textfield");
        secondValueCondition = (TextField) root.lookup("#secondvalue_textfield");
        relation_combobox = (ComboBox) root.lookup("#relationships_combobox");
        conditions_tableview = (TableView) root.lookup("#conditions_tableview");

        operation_combobox = (ComboBox) root.lookup("#operation_combobox");

        success_label = (Label) root.lookup("#success_label");
        failure_label = (Label) root.lookup("#failure_label");
        operations_label = (Label) root.lookup("#operations_label");

        result_imageview = (ImageView) root.lookup("#outcomes_imageview");

        TableColumn<Conditions, String> first_tov_column = new TableColumn<>("Type of value");
        first_tov_column.setCellValueFactory(new PropertyValueFactory<>("firstCondition"));

        TableColumn<Conditions, String> first_value_column = new TableColumn<>("Value");
        first_value_column.setCellValueFactory(new PropertyValueFactory<>("firstConditionValue"));

        TableColumn<Conditions, String> relation_column = new TableColumn<>("Relation");
        relation_column.setCellValueFactory(new PropertyValueFactory<>("relation"));

        TableColumn<Conditions, String> second_tov_column = new TableColumn<>("Type of value");
        second_tov_column.setCellValueFactory(new PropertyValueFactory<>("secondCondition"));

        TableColumn<Conditions, String> second_value_column = new TableColumn<>("Value");
        second_value_column.setCellValueFactory(new PropertyValueFactory<>("secondConditionValue"));

        conditions_tableview.getColumns().set(0, first_tov_column);
        conditions_tableview.getColumns().set(1, first_value_column);
        conditions_tableview.getColumns().add(2, relation_column);
        conditions_tableview.getColumns().add(3, second_tov_column);
        conditions_tableview.getColumns().add(4, second_value_column);

        financialinstruments_combobox.setItems(FXCollections.observableArrayList(
                "EUR/USD",
                "EUR/GBP",
                "USD/JPY",
                "USD/CHF",
                "USD/CAD",
                "GBP/USD",
                "GBP/JPY",
                "AUD/USD",
                "NZD/USD"
        ));

        timeunits_combobox.setItems(FXCollections.observableArrayList(
                "Ticks",
                "Milliseconds",
                "Seconds",
                "Minutes",
                "Hours",
                "Days",
                "Weeks",
                "Years"
        ));

        firstKindOfValue_combobox.setItems(FXCollections.observableArrayList(
                "GENERIC VALUE",
                "OPEN",
                "CLOSE",
                "HIGH",
                "LOW",
                "BB_TOP",
                "BB_MID",
                "BB_BOTTOM",
                "RSI",
                "STOCHASTIC_SLOW_K",
                "STOCHASTIC_SLOW_D",
                "STOCHASTIC_FAST_K",
                "STOCHASTIC_FAST_D",
                "STDDEV",
                "SMA",
                "EMA",
                "WMA",
                "MACD",
                "SIGNAL",
                "ADX",
                "CCI",
                "PSAR",
                "MOMENTUM_STOCHASTIC_SLOW_K",
                "MOMENTUM_STOCHASTIC_SLOW_D",
                "MOMENTUM_PSAR"
        ));

        secondKindOfValue_combobox.setItems(FXCollections.observableArrayList(
                "GENERIC VALUE",
                "OPEN",
                "CLOSE",
                "HIGH",
                "LOW",
                "BB_TOP",
                "BB_MID",
                "BB_BOTTOM",
                "RSI",
                "STOCHASTIC_SLOW_K",
                "STOCHASTIC_SLOW_D",
                "STOCHASTIC_FAST_K",
                "STOCHASTIC_FAST_D",
                "STDDEV",
                "SMA",
                "EMA",
                "WMA",
                "MACD",
                "SIGNAL",
                "ADX",
                "CCI",
                "PSAR",
                "MOMENTUM_STOCHASTIC_SLOW_K",
                "MOMENTUM_STOCHASTIC_SLOW_D",
                "MOMENTUM_PSAR"
        ));

        relation_combobox.setItems(FXCollections.observableArrayList(
                ">",
                "<",
                "="
        ));

        operation_combobox.setItems(FXCollections.observableArrayList(
                "LONG",
                "SHORT"
        ));

        estabilishConnection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                JSONObject conditions = new JSONObject();

                for (int i = 0; i < conditions_tableview.getItems().size(); i++) {
                    System.out.println("Processing row number: " + (i+1));
                    JSONArray jsonArray = new JSONArray();
                    for (int k = 0; k < 5; k++) {
                        jsonArray.put(extractValue(i, k));
                    }
                    conditions.put(String.valueOf(i), jsonArray);
                }

                /*if (ipaddress_textfield.getText().trim().isEmpty() || username_textfield.getText().trim().isEmpty() || password_field.getText().trim().isEmpty() || startdatetime_textfield.getText().trim().isEmpty() || enddatetime_textfield.getText().trim().isEmpty() || tp_textfield.getText().trim().isEmpty() || sl_textfield.getText().trim().isEmpty() || financialinstruments_combobox.getSelectionModel().isEmpty()) {
                    cues_label.setText("Fill the empty fields");
                } else {
                    cues_label.setText("Trying to estabilish a connection...");
                    Connection connection = new Connection(ipaddress_textfield.getText(), 40957, username_textfield.getText(), password_field.getText(), financialinstruments_combobox.getValue().toString(), startdatetime_textfield.getText(), enddatetime_textfield.getText(), tp_textfield.getText(), sl_textfield.getText());
                    connection.start();
                }*/

                cues_label.setText("Trying to estabilish a connection...");
                connection = new Thread(new Connection(ipaddress_textfield.getText(), Integer.parseInt(port_textfield.getText()), username_textfield.getText(), password_field.getText(), financialinstruments_combobox.getValue().toString(), time_textfield.getText(), timeunits_combobox.getValue().toString(), startdatetime_textfield.getText(), enddatetime_textfield.getText(), tp_textfield.getText(), sl_textfield.getText(), conditions, operation_combobox.getValue().toString()));
                connection.start();
                simulated = true;
            }
        });

        closeConnection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (simulated) {
                    connection.interrupt();
                    cues_label.setText("Connection interrupted");
                } else {
                    cues_label.setText("No connection currently running");
                }
            }
        });

        addCondition.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String firstCondition = firstKindOfValue_combobox.getValue().toString();
                String firstConditionValue = firstValueCondition.getText();
                String secondCondition = secondKindOfValue_combobox.getValue().toString();
                String secondConditionValue = secondValueCondition.getText();

                if (relation_combobox.getValue().equals(">")) {
                    conditions_tableview.getItems().add(new Conditions(firstCondition, firstConditionValue, secondCondition, secondConditionValue, 0));
                } else if (relation_combobox.getValue().equals("<")) {
                    conditions_tableview.getItems().add(new Conditions(firstCondition, firstConditionValue, secondCondition, secondConditionValue, 1));
                } else if (relation_combobox.getValue().equals("=")) {
                    conditions_tableview.getItems().add(new Conditions(firstCondition, firstConditionValue, secondCondition, secondConditionValue, 2));
                }
            }
        });

        deleteCondition.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                conditions_tableview.getItems().removeAll(
                        conditions_tableview.getSelectionModel().getSelectedItems()
                );
            }
        });

    }

    public Label getCues_label() {
        return cues_label;
    }


    public static void main(String[] args) {
        launch(args);
    }

    public class Connection implements Runnable {

        public String IP_ADDRESS = null;
        public int PORT_ADDRESS = 40956;
        public String USERNAME = null;
        public String PASSWORD = null;
        public String ASSET = null;
        public String TIME = null;
        public String UNITTIME = null;
        public String STARTDATETIME = null;
        public String ENDDATETIME = null;
        public String TP = null;
        public String SL = null;
        public JSONObject CONDITIONS = null;
        public String OPERATIONTYPE = null;

        public Socket socket = null;
        public BufferedReader bufferedReader = null;
        public PrintWriter printWriter = null;

        public Connection(String ip, int port, String username, String password, String asset, String time, String unit_time, String startdatetime, String enddatetime, String tp, String sl, JSONObject conditions, String operation_type) {
            IP_ADDRESS = ip;
            PORT_ADDRESS = port;
            USERNAME = username;
            PASSWORD = password;
            ASSET = asset;
            TIME = time;
            UNITTIME = unit_time;
            STARTDATETIME = startdatetime;
            ENDDATETIME = enddatetime;
            TP = tp;
            SL = sl;
            CONDITIONS = conditions;
            OPERATIONTYPE = operation_type;
        }

        public String response(String message) {
            return new JSONObject().put("MESSAGE", message).toString();
        }

        @Override
        public void run() {

            while (Thread.currentThread().isInterrupted()) {
                try {
                    bufferedReader.close();
                    printWriter.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Connection...");
            try {
                socket = new Socket(IP_ADDRESS, PORT_ADDRESS);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                String message;
                while ((message = bufferedReader.readLine()) != null) {
                    JSONObject json_data = new JSONObject(message);
                    String json_message = json_data.getString("MESSAGE");
                    if (json_message.equals("LOGIN_DEMAND")) {
                        System.out.println("SENDING DATA...");
                        JSONObject response = new JSONObject();
                        response.put("MESSAGE", "LOGIN_CREDENTIALS");
                        response.put("USERNAME", USERNAME);
                        response.put("PASSWORD", PASSWORD);
                        printWriter.println(response.toString());
                        System.out.println("CREDENTIALS SENT");
                    } else if (json_message.equals("LOGIN_SUCCESS")) {

                        System.out.println("CORRECTLY LOGGED IN");
                        printWriter.println(response("SIMULATION"));
                    } else if (json_message.equals("LOGIN_FAILURE")) {
                        System.out.println("FAILED LOGGED IN");
                    } else if (json_message.equals("LOGIN_NOTEXIST")) {
                        System.out.println("CREDENTIALS NOT EXISTING");
                    } else if (json_message.equals("OK")) {
                        if (json_data.getString("REFERENCE").equals("SIMULATION")) {
                            Platform.runLater(()->{
                                getCues_label().setText("Simulation started... Waiting for the outcomes...");
                            });
                            System.out.println("SIMULATION REQUEST ACCEPTED");
                            JSONObject json_simulationdata = new JSONObject();
                            json_simulationdata.put("MESSAGE", "SIMULATIONDATA");
                            json_simulationdata.put("ASSET", ASSET);
                            json_simulationdata.put("TIME", TIME);
                            json_simulationdata.put("UNITTIME", UNITTIME);
                            json_simulationdata.put("STARTDATETIME", STARTDATETIME);
                            json_simulationdata.put("ENDDATETIME", ENDDATETIME);
                            json_simulationdata.put("TP", TP);
                            json_simulationdata.put("SL", SL);
                            json_simulationdata.put("CONDITIONS", CONDITIONS);
                            json_simulationdata.put("OPERATIONTYPE", OPERATIONTYPE);
                            printWriter.println(json_simulationdata.toString());
                        } else if (json_data.getString("REFERENCE").equals("SIMULATIONDATA")) {
                            System.out.println("Simulation started...");
                            System.out.println(json_data.get("RESULT").toString());
                            Platform.runLater(()->{
                                getCues_label().setText("Simulation carried out");
                                String data = json_data.getString("IMAGE");
                                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data.split(",")[1].getBytes(StandardCharsets.UTF_8)));
                                Image image = new Image(inputStream);
                                result_imageview.setImage(image);
                                success_label.setText(json_data.getString("SUCCESS") + "%" + " - " + json_data.getString("SUCCESSFUL_OPERATIONS"));
                                failure_label.setText(json_data.getString("FAILURE") + "%" + " - " + json_data.getString("FAILED_OPERATIONS"));
                                operations_label.setText(json_data.getString("OPERATIONS"));
                            });
                        }
                    } else if (json_message.equals("SIMULATION_OUTCOMES")) {
                        //result_imageview.setImage(new Image());
                    } else {
                        System.out.println(json_message);
                    }
                }

            } catch (Exception e) {

            }
        }


    }

    protected String getSaltString(int lenght) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < lenght) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
