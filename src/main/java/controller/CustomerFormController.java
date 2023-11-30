package controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.tm.CustomerTm;

import java.sql.*;

public class CustomerFormController {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtSalary;

    @FXML
    private TableView<CustomerTm> tblCustomer;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colSalary;

    @FXML
    private TableColumn colOption;

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadCustomerTable();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue)->{
            setData(newValue);
        });
    }

    private void setData(CustomerTm newValue) {
        if(newValue !=null){
            txtId.setEditable(false);
            txtId.setText(newValue.getId());
            txtName.setText(newValue.getName());
            txtAddress.setText(newValue.getAddress());
            txtSalary.setText(String.valueOf(newValue.getSalary()));
        }

    }

    private void loadCustomerTable() {

        ObservableList<CustomerTm> tmList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customer";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade","root","1234");
            Statement stm = connection.createStatement();
            ResultSet result = stm.executeQuery(sql);
            while (result.next()){
                Button btn = new Button("DELETE");
                CustomerTm c = new CustomerTm(
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getDouble(4),
                        btn
                );
                btn.setOnAction(actionEvent -> {
                    deleteCustomer(c.getId());
                });
                tmList.add(c);
            }
            connection.close();
            tblCustomer.setItems(tmList);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteCustomer(String id) {
        String sql = "DELETE FROM customer WHERE id='"+id+"'";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade","root","1234");
            Statement stm = connection.createStatement();
            int result = stm.executeUpdate(sql);
            if (result>0){
                new Alert(Alert.AlertType.INFORMATION,"Customer deleted successfully").show();
                loadCustomerTable();
                clearFields();
            }
            else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
            }
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void reloadButtonOnAction(ActionEvent event) {
        loadCustomerTable();
        tblCustomer.refresh();
    }

    @FXML
    void saveButtonOnAction(ActionEvent event) {
//        try{
//            Double.parseDouble(txtSalary.getText());
//        }catch(NumberFormatException e){
//            new Alert(Alert.AlertType.ERROR,"Fill ").show();
//        }

        try {
            Customer c = new Customer(txtId.getText()
                    ,txtName.getText()
                    ,txtAddress.getText()
                    ,Double.parseDouble(txtSalary.getText())
            );

            String sql = "INSERT INTO customer VALUES('"+c.getId()+"','"+c.getName()+"','"+c.getAddress()+"',"+c.getSalary()+")";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade","root","1234");
            Statement stm = connection.createStatement();
            int result = stm.executeUpdate(sql);
            if (result>0){
                new Alert(Alert.AlertType.INFORMATION,"Customer saved successfully").show();
                loadCustomerTable();
                clearFields();
            }
            connection.close();
        }catch(SQLIntegrityConstraintViolationException ex){
            new Alert(Alert.AlertType.ERROR,"Customer already added").show();
        }catch (NumberFormatException | ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Error").show();
            //throw new RuntimeException(e);
        }

    }

    private void clearFields() {
        tblCustomer.refresh();
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
    }

    @FXML
    void updateButtonOnAction(ActionEvent event) {
        Customer c = new Customer(txtId.getText()
                ,txtName.getText()
                ,txtAddress.getText()
                ,Double.parseDouble(txtSalary.getText())
        );

        String sql = "UPDATE customer SET name='"+c.getName()+"',address='"+c.getAddress()+"',salary="+c.getSalary()+"WHERE id='"+c.getId()+"'";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade","root","1234");
            Statement stm = connection.createStatement();
            int result = stm.executeUpdate(sql);
            if (result>0){
                new Alert(Alert.AlertType.INFORMATION,"Customer" + c.getId()+" updated").show();
                loadCustomerTable();
                clearFields();
            }
            connection.close();
        }catch(SQLIntegrityConstraintViolationException ex){
            new Alert(Alert.AlertType.ERROR,"Customer already added").show();
        }catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
