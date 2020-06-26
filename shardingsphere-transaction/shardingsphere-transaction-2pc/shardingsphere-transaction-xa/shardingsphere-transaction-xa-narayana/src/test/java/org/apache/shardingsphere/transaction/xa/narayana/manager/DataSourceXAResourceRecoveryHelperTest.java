package org.apache.shardingsphere.transaction.xa.narayana.manager;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import lombok.SneakyThrows;
import org.apache.shardingsphere.transaction.xa.narayana.manager.fixture.ReflectiveUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataSourceXAResourceRecoveryHelperTest {
    
    @Mock
    private XADataSource xaDataSource;
    
    @Mock
    private XAResource xaResource;
    
    @Mock
    private XAConnection xaConnection;
    
    private DataSourceXAResourceRecoveryHelper recoveryHelper;
    
    @SneakyThrows
    @Before
    public void setUp() {
        when(xaConnection.getXAResource()).thenReturn(xaResource);
        when(xaDataSource.getXAConnection()).thenReturn(xaConnection);
        recoveryHelper = new DataSourceXAResourceRecoveryHelper(xaDataSource);
    }
    
    @SneakyThrows
    @Test
    public void assertGetXAResourcesCreatingConnecting() {
        recoveryHelper.getXAResources();
        XAResource[] xaResources = recoveryHelper.getXAResources();
        assertEquals(1, xaResources.length);
        Assert.assertThat(xaResources[0], sameInstance(recoveryHelper));
        verify(xaConnection, times(1)).getXAResource();
        verify(xaDataSource, times(1)).getXAConnection();
    }
    
    @SneakyThrows
    @Test
    public void assertGetXAResourcesWithoutConnecting() {
        ReflectiveUtil.setProperty(recoveryHelper, "delegate", xaResource);
        recoveryHelper.getXAResources();
        XAResource[] xaResources = recoveryHelper.getXAResources();
        assertEquals(1, xaResources.length);
        Assert.assertThat(xaResources[0], sameInstance(recoveryHelper));
        verify(xaConnection, times(0)).getXAResource();
        verify(xaDataSource, times(0)).getXAConnection();
    }
    
    @SneakyThrows
    @Test
    public void assertDelegateRecover() {
        recoveryHelper.getXAResources();
        recoveryHelper.recover(XAResource.TMSTARTRSCAN);
        verify(xaResource, times(1)).recover(XAResource.TMSTARTRSCAN);
        verify(xaConnection, times(0)).close();
    }
    
    @SneakyThrows
    @Test
    public void assertDelegateRecoverAndCloseConnection() {
        recoveryHelper.getXAResources();
        recoveryHelper.recover(XAResource.TMENDRSCAN);
        verify(xaResource).recover(XAResource.TMENDRSCAN);
        verify(xaConnection).close();
    }
    
    @Test
    public void assertDelegateStart() throws XAException {
        recoveryHelper.getXAResources();
        recoveryHelper.start(null, 0);
        verify(xaResource, times(1)).start(null, 0);
    }
    
    @Test
    public void assertDelegateEnd() throws XAException {
        recoveryHelper.getXAResources();
        recoveryHelper.end(null, 0);
        verify(xaResource, times(1)).end(null, 0);
    }
    
    @Test
    public void assertDelegatePrepare() throws XAException {
        recoveryHelper.getXAResources();
        recoveryHelper.prepare(null);
        verify(xaResource, times(1)).prepare(null);
    }
    
    @Test
    public void assertDelegateCommit() throws XAException {
        recoveryHelper.getXAResources();
        recoveryHelper.commit(null, true);
        verify(xaResource, times(1)).commit(null, true);
    }
    
    @Test
    public void assertDelegateRollback() throws XAException {
        recoveryHelper.getXAResources();
        recoveryHelper.rollback(null);
        verify(xaResource, times(1)).rollback(null);
    }
    
    @Test
    public void assertDelegateIsSameRM() throws XAException {
        recoveryHelper.getXAResources();
        recoveryHelper.isSameRM(null);
        verify(xaResource, times(1)).isSameRM(null);
    }
    
    @Test
    public void assertDelegateForget() throws XAException {
        recoveryHelper.getXAResources();
        recoveryHelper.forget(null);
        verify(xaResource, times(1)).forget(null);
    }
    
    @Test
    public void assertDelegateGetTransactionTimeout() throws XAException {
        recoveryHelper.getXAResources();
        recoveryHelper.getTransactionTimeout();
        verify(xaResource, times(1)).getTransactionTimeout();
    }
    
    @Test
    public void assertDelegateSetTransactionTimeout() throws XAException {
        recoveryHelper.getXAResources();
        recoveryHelper.setTransactionTimeout(0);
        verify(xaResource, times(1)).setTransactionTimeout(0);
    }
}
