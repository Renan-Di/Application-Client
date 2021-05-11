package br.com.rdac;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.wildfly.naming.client.WildFlyInitialContextFactory;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        
        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
        jndiProperties.setProperty(Context.PROVIDER_URL, "remote+http://serverremote:8080");
        /*jndiProperties.put("jboss.naming.client.ejb.context", "true");
        jndiProperties.put("remote.connection.main.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "true"); */

        try {
            // Para lookup local não é preciso fornecer o contexto inicial
            Context localContext = new InitialContext();
            // jndi para lookup local do datasource
            String localDatasource = "jboss/datasources/ExampleDS";

            doLookup(localContext, localDatasource);

            // Localmente ambos os nomes jndi sãosuprtados para lookup
            // do EJB, nesse caso específico o ejb resido no mesmo módulo
            // que o application client
            doEjbLookup(localContext, "ejb:ear-1.0-SNAPSHOT/ejb/");
            // Creio que no caso do application client estar em outro
            // módulo este contexto não iria retornar o ejb
            doEjbLookup(localContext, "java:app/ejb/");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        
        try {
            Context remoteContext = new InitialContext(jndiProperties);
            //URL para lookup do datasource, não é suportado lookup
            // remoto de datasource pelo JBoss nas versões atuais
            String remoteDatasource = "datasources/ExampleDS";

            doLookup(remoteContext, remoteDatasource);
            // Nome jndi para lookup no servidor remoto
            doEjbLookup(remoteContext, "ejb:ear-1.0-SNAPSHOT/ejb/");
            // Este não funciona remotamente
            doEjbLookup(remoteContext, "java:app/ejb/");
        } catch (NamingException e) {
            e.printStackTrace();
        }

    }

    public static void doLookup(Context initialContext, String datasource) {
        
        System.out.println("\n\n\n\nInício do lookup do datasource " + datasource);

        try {
            Object datasourceObject = initialContext.lookup(datasource);
            if (datasourceObject instanceof DataSource) {
                DataSource ds = (DataSource) datasourceObject;
                try (Connection connection = ds.getConnection()) {
                    System.out.println(connection.getMetaData());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (NamingException e) {
            System.err.println(e);
        }

        System.out.println("Fim do lookup do datasource " + datasource + "\n\n\n\n");
    }
    
    public static void doEjbLookup(Context initialContext, String nameSpace) {
        System.out.println("\n\n\n\nInício do lookup do EJB no contexto " + nameSpace);
    
        try {
            Object sessionBeanObject = initialContext.lookup(nameSpace + "StatelessSessionBean!br.com.rdac.StatelessSession");

            if (sessionBeanObject instanceof StatelessSession) {
                StatelessSession sessionBean = (StatelessSession) sessionBeanObject;
                System.out.println(sessionBean.getGreeting());
                sessionBean.invokeWithClientContext();
            }
            
        } catch (NamingException e) {
            System.err.println(e);
        }
    
        System.out.println("Fim do lookup do EJB no contexto " + nameSpace + "\n\n\n\n");
    }
}