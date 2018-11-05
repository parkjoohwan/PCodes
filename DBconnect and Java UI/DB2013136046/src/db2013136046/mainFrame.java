
package db2013136046;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author JOO
 */
public class mainFrame extends javax.swing.JFrame {
    
    Connection con = MyConnection.makeCon();
    JTextField[] tf = new JTextField[9];
    JLabel[] lf = new JLabel[9];

    public mainFrame() {
        initComponents();
        addNode();
        tf[0] = jTextField1;
        tf[1] = jTextField2;
        tf[2] = jTextField3;
        tf[3] = jTextField4;
        tf[4] = jTextField5;
        tf[5] = jTextField6;
        tf[6] = jTextField7;
        tf[7] = jTextField8;
        tf[8] = jTextField9;
        
        lf[0] = jLabel1;
        lf[1] = jLabel2;
        lf[2] = jLabel3;
        lf[3] = jLabel4;
        lf[4] = jLabel5;
        lf[5] = jLabel6;
        lf[6] = jLabel7;
        lf[7] = jLabel8;
        lf[8] = jLabel9;
        
        clearField();
    }
    
    private void clearField(){
        for ( int i = 0 ; i < tf.length ; i ++ ){
            tf[i].setText("");
            tf[i].setVisible(false);
            lf[i].setText("");
            lf[i].setVisible(false);
        }
    }
    
    
    private void clearTextFileds(){
        clearField();
        
        int selectedRow = jTable1.getSelectedRow();
        int noc = jTable1.getColumnCount();
        
        for ( int i = 0 ; i < noc ; i ++ ){
            tf[i].setText("");
            tf[i].setVisible(true);
            lf[i].setText(jTable1.getColumnName(i));
            lf[i].setVisible(true);
        }
    }
    
    private void showField(){
        clearField();
        int selectedRow = jTable1.getSelectedRow();
        int noc = jTable1.getColumnCount();
        for( int i = 0 ; i < noc ; i++ ){
            if( jTable1.getValueAt(selectedRow, i) == null){
                tf[i].setText(" ");
            }
            else{
                tf[i].setText(jTable1.getValueAt(selectedRow, i).toString());
                tf[i].setVisible(true);
            }
            lf[i].setText(jTable1.getColumnName(i));
            lf[i].setVisible(true);
        }
    }
    
    
    private TableModel rsToTableModel(ResultSet rs){
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            //data for columns
            int noc = rsmd.getColumnCount();
            Vector cnames = new Vector();
            for(int i=1; i<= noc; i++)
                cnames.addElement(rsmd.getColumnLabel(i));
            //data for table rows
            Vector rows = new Vector();
            while(rs.next()){
                Vector newRow = new Vector();
                for(int i = 1; i<=noc; i++)
                    newRow.addElement(rs.getObject(i));
                rows.addElement(newRow);
                
            }
            return new DefaultTableModel(rows, cnames);
        }
        catch(Exception e){
            return null;
        }
    }
    
    
    private void showTableData(String st){
        String query = "select * from "+st+"";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            jTable1.setModel(rsToTableModel(rs));
            jTable1.setRowSelectionInterval(0, 0);
        }
        catch(Exception e){
            jTextArea1.append("Error in showTableData()" + e.getMessage() +"\n");
        }
        
    }
    
    private String mySelectedNode(){
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
        String ns = node.getUserObject().toString();
        return ns;
    }
    
    
    void addNode(){
        DefaultTreeModel treeModel=(DefaultTreeModel)jTree1.getModel();
 
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mydatabase");
        DefaultMutableTreeNode tables = new DefaultMutableTreeNode("Tables");
        DefaultMutableTreeNode reports = new DefaultMutableTreeNode("Reports");
        DefaultMutableTreeNode utl = new DefaultMutableTreeNode("Utilities");
        DefaultMutableTreeNode about = new DefaultMutableTreeNode("About");
        DefaultMutableTreeNode ext = new DefaultMutableTreeNode("Exit");
        
        treeModel.setRoot(root);
        root.add(tables);
        root.add(reports);
        root.add(utl);
        root.add(about);
        root.add(ext);
        
        DefaultMutableTreeNode t1 = new DefaultMutableTreeNode(Tables.Recipe);
        DefaultMutableTreeNode t2 = new DefaultMutableTreeNode(Tables.MainMaterial);
        DefaultMutableTreeNode t3 = new DefaultMutableTreeNode(Tables.Submaterial);
        DefaultMutableTreeNode t4 = new DefaultMutableTreeNode(Tables.Condiment);
        DefaultMutableTreeNode t5 = new DefaultMutableTreeNode(Tables.Tool);
        tables.add(t1);
        tables.add(t2);
        tables.add(t3);
        tables.add(t4);
        tables.add(t5);
        
        treeModel.reload();
    }
    
    private void temp(){
        try{
            
        }catch(Exception e ){
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    //add to database when button clicked
    
    private void addRecipeSP(){
        String sql = "{call uspAddRecipe(?,?,?,?,?,?,?,?,?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
            cst.setString(2, jTextField2.getText());
            cst.setString(3, jTextField3.getText());
            int val = Integer.parseInt(jTextField4.getText());
            cst.setInt(4, val);
            val = Integer.parseInt(jTextField5.getText());
            cst.setInt(5, val);
            cst.setString(6, jTextField6.getText());
            cst.setString(7, jTextField7.getText());
            cst.setString(8, jTextField8.getText());
            cst.setString(9, jTextField9.getText());
                        
            cst.execute();
            
            jTextArea1.append("Recipe Record is a added ..." + "\n");
        }catch( SQLException e ){
            jTextArea1.append("Error in addReceipeSP() ..."  + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
     
    private void addToolSP(){
        String sql = "{call uspAddTool(?,?,?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
            cst.setString(2, jTextField2.getText());
            cst.setString(3, jTextField3.getText());
                        
            cst.execute();
            
            jTextArea1.append("Tool Record is a added ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in addToolSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
     
    private void addSubmaterialSP(){
        String sql = "{call uspAddSubMaterial(?,?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
            cst.setString(2, jTextField2.getText());
                        
            cst.execute();
            
            jTextArea1.append("Submaterial Record is a added ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in addSubmaterialSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
          
    private void addMainmaterialSP(){
        String sql = "{call uspAddMainMaterial(?,?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
            cst.setString(2, jTextField2.getText());
                        
            cst.execute();
            
            jTextArea1.append("Mainmaterial Record is a added ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in addMainmaterialSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    private void addCondimentSP(){
        String sql = "{call uspAddCondiment(?,?,?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
            cst.setString(2, jTextField2.getText());
            cst.setString(3, jTextField3.getText());
                        
            cst.execute();
            
            jTextArea1.append("Condiment Record is a added ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in addCondimentSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }

    private void updateRecipeSP(){
        String sql = "{call uspUpdateRecipe(?,?,?,?,?,?,?,?,?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
            cst.setString(2, jTextField2.getText());
            cst.setString(3, jTextField3.getText());
            int val = Integer.parseInt(jTextField4.getText());
            cst.setInt(4, val);
            val = Integer.parseInt(jTextField5.getText());
            cst.setInt(5, val);
            cst.setString(6, jTextField6.getText());
            cst.setString(7, jTextField7.getText());
            cst.setString(8, jTextField8.getText());
            cst.setString(9, jTextField9.getText());
                        
            cst.execute();
            
            jTextArea1.append("Recipe Record is updated ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in updateCRecipeSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
     
    private void updateToolSP(){
        String sql = "{call uspUpdateTool(?,?,?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
            cst.setString(2, jTextField2.getText());
            cst.setString(3, jTextField3.getText());
                        
            cst.execute();
            
            jTextArea1.append("Tool Record is updated ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in updateToolSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
     
    private void updateSubmaterialSP(){
        String sql = "{call uspUpdateSubMaterial(?,?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
            cst.setString(2, jTextField2.getText());
                        
            cst.execute();
            
            jTextArea1.append("Submaterial Record is updated ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in updateSubmaterialSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
          
    private void updateMainmaterialSP(){
        String sql = "{call uspUpdateMainMaterial(?,?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
            cst.setString(2, jTextField2.getText());
                        
            cst.execute();
            
            jTextArea1.append("Mainmaterial Record is updated ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in updateMainmaterialSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    
    private void updateCondimentSP(){
        String sql = "{call uspUpdateCondiment(?,?,?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
            cst.setString(2, jTextField2.getText());
            cst.setString(3, jTextField3.getText());
                        
            cst.execute();
            
            jTextArea1.append("Condiment Record is updated ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in updateCondimentSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    private void deleteRecipeSP(){
        String sql = "{call uspdeleteRecipe(?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
                        
            cst.execute();
            
            jTextArea1.append("Recipe Record is deleted ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in deleteRecipeSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    private void deleteToolSP(){
        String sql = "{call uspdeleteTool(?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
                        
            cst.execute();
            
            jTextArea1.append("Tool Record is deleted ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in deleteToolSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    private void deleteMainmaterialSP(){
        String sql = "{call uspdeleteMainmaterial(?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
                        
            cst.execute();
            
            jTextArea1.append("Mainmaterial Record is deleted ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in deleteMainmaterialSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
     private void deleteSubmaterialSP(){
        String sql = "{call uspdeleteSubmaterial(?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
                        
            cst.execute();
            
            jTextArea1.append("Submaterial Record is deleted ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in deleteSubmaterialSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    private void deleteCondimentSP(){
        String sql = "{call uspDeleteCondiment(?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setString(1, jTextField1.getText());
                        
            cst.execute();
            
            jTextArea1.append("Condiment Record is deleted ..." + "\n");
        }catch( Exception e ){
            jTextArea1.append("Error in deleteCondimentSP() ..." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    private void showfunctionSP(){
        String query = "select * from Assignment2T1_4()";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            jTable1.setModel(rsToTableModel(rs));
            jTable1.setRowSelectionInterval(0, 0);
            
            jTextArea1.append("show functions and procedures ..." + "\n");
        }
        catch(Exception e){
            jTextArea1.append("error int show functionSP ..." + "\n");
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jTextField9 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jButton1.setText("new");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("update");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("delete");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("show F/P");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("refresh");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(291, 291, 291))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jTextField9.setText("jTextField9");

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jTextField1.setText("jTextField1");

        jLabel3.setText("jLabel3");

        jTextField2.setText("jTextField2");

        jLabel4.setText("jLabel4");

        jTextField3.setText("jTextField3");

        jLabel5.setText("jLabel5");

        jTextField4.setText("jTextField4");

        jLabel6.setText("jLabel6");

        jTextField5.setText("jTextField5");

        jLabel7.setText("jLabel7");

        jTextField6.setText("jTextField6");

        jLabel8.setText("jLabel8");

        jTextField7.setText("jTextField7");

        jLabel9.setText("jLabel9");

        jTextField8.setText("jTextField8");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(78, 78, 78))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField8)
                    .addComponent(jTextField6)
                    .addComponent(jTextField5)
                    .addComponent(jTextField4)
                    .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .addComponent(jTextField2)
                    .addComponent(jTextField3)
                    .addComponent(jTextField9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(60, 60, 60))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 30, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
  
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        clearTextFileds();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        String st = mySelectedNode();
        showTableData(st);
    }//GEN-LAST:event_jTree1ValueChanged

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        showField();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         String st = mySelectedNode();
        if( st == Tables.Recipe.toString() )
            addRecipeSP();
        if( st == Tables.MainMaterial.toString() )
            addMainmaterialSP();
        if( st == Tables.Submaterial.toString() )
            addSubmaterialSP();
        if( st == Tables.Tool.toString() )
            addToolSP();
        if( st == Tables.Condiment.toString() )
            addCondimentSP();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String st = mySelectedNode();
        if( st == Tables.Recipe.toString() )
            updateRecipeSP();
        if( st == Tables.MainMaterial.toString() )
            updateMainmaterialSP();
        if( st == Tables.Submaterial.toString() )
            updateSubmaterialSP();
        if( st == Tables.Tool.toString() )
            updateToolSP();
        if( st == Tables.Condiment.toString() )
            updateCondimentSP();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String st = mySelectedNode();
        if( st == Tables.Recipe.toString() )
            deleteRecipeSP();
        if( st == Tables.MainMaterial.toString() )
            deleteMainmaterialSP();
        if( st == Tables.Submaterial.toString() )
            deleteSubmaterialSP();
        if( st == Tables.Tool.toString() )
            deleteToolSP();
        if( st == Tables.Condiment.toString() )
            deleteCondimentSP();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        showfunctionSP();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
         String st = mySelectedNode();
        showTableData(st);
         jTextArea1.append("\n\n\n\n\n");
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
