package br.com.rdac;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

@Stateless
@Remote(StatelessSession.class)
public class StatelessSessionBean implements StatelessSession {

    private static final Logger LOGGER = Logger.getLogger(StatelessSessionBean.class);

    @Resource
    SessionContext context;

    @Override
    public void invokeWithClientContext() {
        LOGGER.info("ClientContext is here = " + context.getContextData());
    }

    @Override
    public String getGreeting() {
        return "Hello from StatelessSessionBean@" + System.getProperty("jboss.node.name");
    }

}