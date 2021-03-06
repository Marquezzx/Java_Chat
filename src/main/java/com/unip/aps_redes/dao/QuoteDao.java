package com.unip.aps_redes.dao;

import com.unip.aps_redes.connection.ConnectionFactory;
import com.unip.aps_redes.model.Quote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class QuoteDao {

    private Connection con = null;

    public QuoteDao(String ip) {
        con = ConnectionFactory.getConnection(ip);
    }

  
    public Quote getQuoteBySubject(String subject){
        PreparedStatement statement = null;
        ResultSet result = null;
        Quote quote = null;
        try {
            statement = con.prepareStatement("SELECT * FROM tb_quote WHERE subject like '%"+subject+"%'");
            
            result = statement.executeQuery();
            
            while (result.next()){
                quote = new Quote(result.getString("username"), result.getString("subject"), result.getString("text"));
            }
        } catch(SQLException ex){
            ex.printStackTrace();
            System.out.println("Não foi possivel localizar quote");
        } finally {
            ConnectionFactory.closeConnection(con, statement);
        }
        
        return quote;
    }
    
    public List<Quote> getQuotes() {
        List<Quote> quotes = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            statement = con.prepareStatement("SELECT * FROM tb_quote");

            result = statement.executeQuery();

            while (result.next()) {
                quotes.add(new Quote(result.getString("username"), result.getString("subject"), result.getString("text")));
            }
            
        } catch (SQLException ex) {
            System.out.println("Não foi possivel recuperar a list");
            ex.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con, statement);
        }

        return quotes;
    }

    public void saveQuote(Quote quote) {
        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement("INSERT INTO tb_quote(username, subject, text) values (?, ?, ?)");

            statement.setString(1, quote.getUserName());
            statement.setString(2, quote.getSubject());
            statement.setString(3, quote.getText());
            statement.executeUpdate();
            System.out.println("Salvo com sucessso");
        } catch (SQLException ex) {
            System.err.println("Não foi possivel salvar usuario");
        } finally {
            ConnectionFactory.closeConnection(con, statement);
        }
    }
    


}
