/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import entities.Break;
import entities.BreakPK;
import entities.Meeting;
import entities.MeetingPK;
import entities.Warning;
import entities.WarningPK;
import static entities.Warning_.meetings;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
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
import sessionbeans.BreakFacadeLocal;
import sessionbeans.ConflictCheckerBean;
import sessionbeans.MeetingFacadeLocal;
import sessionbeans.WarningFacadeLocal;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import sessionbeans.RouteCalculatorBean;


/**
 *
 * @author matteo
 */

@RunWith(MockitoJUnitRunner.class)
public class ConflictCheckerBeanTest {
    
    @InjectMocks
    private final ConflictCheckerBean checker = new ConflictCheckerBean();
    
    
    @Mock
    private MeetingFacadeLocal meetingFacade;
    @Mock
    private WarningFacadeLocal warningFacade;
    @Mock
    private BreakFacadeLocal breakFacade;
    @Mock
    private RouteCalculatorBean nav;
    
    private Meeting m;
    
    private Break b;
    
    private Warning w;
    
    private ArrayList<Meeting> meetings;
    private ArrayList<Warning> warnings;
    private ArrayList<Break> breaks;
    
  
    
    @Before
    public void setUp() {
     
        m = new Meeting(1,1);
        m.setLocation("Peschiera Borromeo");
        m.setDuration(60);
        m.setStartingdate(new Date(Date.UTC(2018-1900, 0, 1, 12, 30, 0)));
        m.setMeetingPK(new MeetingPK(1,1));
      
        warnings = new ArrayList<>();
        meetings = new ArrayList<>();
        breaks = new ArrayList<>(); 
        meetings.add(0, m);
        Mockito.when(warningFacade.getWarningsFromUID(anyInt())).thenReturn(warnings);
        Mockito.when(breakFacade.getBreaksFromUID(anyInt())).thenReturn(breaks);
        Mockito.when(meetingFacade.getMeetingsFromUID(anyInt())).thenReturn(meetings);
        try {
            Mockito.when(nav.retrieveDuration(anyString(), anyString(), anyString())).thenReturn((long)0);
        } catch (IOException | ParseException | org.json.simple.parser.ParseException ex) {
            Logger.getLogger(ConflictCheckerBeanTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        b = new Break(1,1);
        b.setBreakPK(new BreakPK(1,1));
        b.setStartingtime(new Date(Date.UTC(2018-1900, 0, 1, 12, 40, 0)));
        b.setDayofweek("monday");
        b.setRecurrent(true);
        b.setMinduration(new Date(Date.UTC(2918-1900, 0, 1, 13, 30, 0)));
        b.setEndingtime(new Date(Date.UTC(2018-1900, 0, 1, 13, 40, 0)));
        breaks.add(b);
        w = new Warning();
         w.setBreaks("1");
         w.setMeetings("1");
         w.setWarningPK(new WarningPK(1,1));
         warnings.add(w);
        
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
    public void checkMeetingOverlapTest() throws IOException, MalformedURLException, ParseException, org.json.simple.parser.ParseException{
       
        Meeting m1 = new Meeting(3,1);
        m1.setLocation("Bussero");
        m1.setDuration(300);
        m1.setStartingdate(new Date(Date.UTC(2018-1900, 0, 1, 12, 45, 0)));
        
      
        
            ArrayList<Meeting> meets = this.checker.CheckMeetingOverlaps(m1);

        
        assertTrue(meets.contains(m));
        
      
    }
    
    @Test
    public void breakConflictCheckerTest(){
    
        ArrayList<Break> conflictuals = this.checker.BreakConflictChecker(m);
        
        assertTrue(conflictuals.contains(b));
            
    }
    
    @Test
    public void checkRescheduleTest(){
        assertTrue(this.checker.checkReschedule(1).contains(b));
    }
    
    @Test
    public void checkWarningExistenceTest(){
        Warning w = new Warning(1,1);
        w.setBreaks("");
        w.setMeetings("");
        
        assertEquals(checker.checkWarningExistence("2%3%", "2%", "1"),"not exists");
        assertEquals(checker.checkWarningExistence("1%", "1%", "1"),"1");
    }

   

    
    
    

}
