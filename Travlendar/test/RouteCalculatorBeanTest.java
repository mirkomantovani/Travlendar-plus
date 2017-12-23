/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import entities.Preferences;
import entities.Travelmean;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import sessionbeans.RouteCalculatorBean;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import sessionbeans.PreferencesFacadeLocal;
import sessionbeans.TravelmeanFacadeLocal;

/**
 *
 * @author matteo
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteCalculatorBeanTest {
    
    @InjectMocks
    private RouteCalculatorBean nav;
    
    @Mock
    private PreferencesFacadeLocal pref;
    
    @Mock
    private TravelmeanFacadeLocal trans;
    
    private Preferences userPref;
    
    private Travelmean userTmeans;
    
    
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        userPref = new Preferences();
        userTmeans = new Travelmean();
        Mockito.when(pref.find(anyInt())).thenReturn(userPref);
        Mockito.when(trans.find(anyInt())).thenReturn(userTmeans);
        userPref.setAvoidmotorways(Boolean.TRUE);
        userPref.setAvoidtolls(Boolean.FALSE);
        userPref.setMaxcyclingdistance(5000);
        userPref.setMaxwalkingdistance(5000);
        userPref.setMinimizecarbonfootprint(Boolean.TRUE);
        userPref.setUid(1);
        userTmeans.setOwnedbike(Boolean.TRUE);
        userTmeans.setWalking(Boolean.FALSE);
        userTmeans.setUid(1);
        userTmeans.setOwnedcar(Boolean.TRUE);
        userTmeans.setPublictransport(Boolean.TRUE);
     
        
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void retrieveDurationTest(){
        try {
            assertEquals(this.nav.retrieveDuration("New York", "Washington", "1"),147779);
        } catch (IOException | ParseException | org.json.simple.parser.ParseException ex) {
            Logger.getLogger(RouteCalculatorBeanTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        userTmeans.setOwnedbike(Boolean.FALSE);
        userTmeans.setPublictransport(Boolean.FALSE);
        userTmeans.setWalking(Boolean.FALSE);
        userPref.setAvoidmotorways(Boolean.FALSE);
        
        try {
            assertEquals(this.nav.retrieveDuration("Washington,DC,USA", "New York,NY,USA", "1"),13852);
        } catch (IOException | ParseException | org.json.simple.parser.ParseException ex) {
            Logger.getLogger(RouteCalculatorBeanTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
