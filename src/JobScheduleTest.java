import static org.junit.Assert.*;

import org.junit.Test;

public class JobScheduleTest
{
    @Test
    public void test()
    {
        JobSchedule schedule = new JobSchedule();
        schedule.addJob(8); //adds job 0 with time 8
        JobSchedule.Job j1 = schedule.addJob(3); //adds job 1 with time 3
        schedule.addJob(5); //adds job 2 with time 5
        assertEquals(8, schedule.minCompletionTime()); //should return 8, since job 0 takes time 8 to complete.
        /* Note it is not the min completion time of any job, but the earliest the entire set can complete. */
        schedule.getJob(0).requires(schedule.getJob(2)); //job 2 must precede job 0
        assertEquals(13, schedule.minCompletionTime()); //should return 13 (job 0 cannot start until time 5)
        schedule.getJob(0).requires(j1); //job 1 must precede job 0
        assertEquals(13, schedule.minCompletionTime()); //should return 13
        assertEquals(5, schedule.getJob(0).getStartTime()); //should return 5
        assertEquals(0, j1.getStartTime()); //should return 0
        assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0
        j1.requires(schedule.getJob(2)); //job 2 must precede job 1
        assertEquals(16, schedule.minCompletionTime()); //should return 16
        assertEquals(8, schedule.getJob(0).getStartTime()); //should return 8
        assertEquals(5, schedule.getJob(1).getStartTime()); //should return 5
        assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0
        schedule.getJob(1).requires(schedule.getJob(0)); //job 0 must precede job 1 (creates loop)
        assertEquals(-1, schedule.minCompletionTime()); //should return -1
        assertEquals(-1, schedule.getJob(0).getStartTime()); //should return -1
        assertEquals(-1, schedule.getJob(1).getStartTime()); //should return -1
        assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0 (no loops in prerequisites)
        
        //SECOND SET OF TESTS
        schedule = new JobSchedule();
        assertEquals(0, schedule.minCompletionTime());
        schedule.addJob(16);
        schedule.addJob(2);
        schedule.addJob(20);
        schedule.addJob(3);
        assertEquals(20, schedule.minCompletionTime());
        schedule.addJob(0);
        assertEquals(20, schedule.minCompletionTime());
        assertEquals(20, schedule.minCompletionTime());
        
        schedule.getJob(0).requires(schedule.getJob(4));
        schedule.getJob(4).requires(schedule.getJob(1));
        schedule.getJob(2).requires(schedule.getJob(3));
        schedule.getJob(0).requires(schedule.getJob(2));
        assertEquals(39, schedule.minCompletionTime());
        assertEquals(39, schedule.minCompletionTime());
        schedule.getJob(0).requires(schedule.getJob(1));
        schedule.getJob(1).requires(schedule.getJob(3));
        schedule.getJob(4).requires(schedule.getJob(3));
        assertEquals(39, schedule.minCompletionTime());
        assertEquals(23, schedule.getJob(0).getStartTime());
        assertEquals(3,schedule.getJob(1).getStartTime());
        assertEquals(3, schedule.getJob(2).getStartTime());
        assertEquals(0, schedule.getJob(3).getStartTime());
        assertEquals(5, schedule.getJob(4).getStartTime());
        schedule.addJob(100000);
        assertEquals(100000, schedule.minCompletionTime());
        schedule.getJob(5).requires(schedule.getJob(1));
        schedule.getJob(0).requires(schedule.getJob(5));
        assertEquals(100021, schedule.minCompletionTime());
        assertEquals(100005, schedule.getJob(0).getStartTime());
        assertEquals(3, schedule.getJob(1).getStartTime());
        assertEquals(3, schedule.getJob(2).getStartTime());
        assertEquals(0, schedule.getJob(3).getStartTime());
        assertEquals(5, schedule.getJob(4).getStartTime());
        assertEquals(5, schedule.getJob(5).getStartTime());
        schedule.getJob(5).requires(schedule.getJob(4));
        assertEquals(100021, schedule.minCompletionTime());
        schedule.getJob(4).requires(schedule.getJob(5));
        assertEquals(3, schedule.getJob(2).getStartTime());
        assertEquals(3, schedule.getJob(1).getStartTime());
        assertEquals(-1, schedule.getJob(4).getStartTime());
        assertEquals(-1, schedule.getJob(5).getStartTime());
        assertEquals(-1, schedule.getJob(0).getStartTime());
        
        //THIRD SET OF TESTS
        schedule = new JobSchedule();
        schedule.addJob(5);
        schedule.addJob(2);
        schedule.addJob(10);
        schedule.addJob(12);
        schedule.getJob(1).requires(schedule.getJob(0));
        schedule.getJob(2).requires(schedule.getJob(1));
        schedule.getJob(3).requires(schedule.getJob(0));
        schedule.getJob(1).requires(schedule.getJob(3));
        assertEquals(29, schedule.minCompletionTime());
        assertEquals(0, schedule.getJob(0).getStartTime());
        assertEquals(17, schedule.getJob(1).getStartTime());
        assertEquals(19, schedule.getJob(2).getStartTime());
        assertEquals(5, schedule.getJob(3).getStartTime());
        schedule.addJob(100);
        schedule.addJob(10);
        schedule.getJob(5).requires(schedule.getJob(4));
        assertEquals(110, schedule.minCompletionTime());
        assertEquals(0, schedule.getJob(4).getStartTime());
        assertEquals(100, schedule.getJob(5).getStartTime());
        schedule.getJob(2).requires(schedule.getJob(5));
        assertEquals(120, schedule.minCompletionTime());
        assertEquals(110, schedule.getJob(2).getStartTime());
        assertEquals(110, schedule.getJob(2).getStartTime());
        assertEquals(100, schedule.getJob(5).getStartTime());
        schedule.addJob(7);
        schedule.getJob(6).requires(schedule.getJob(3));
        assertEquals(17, schedule.getJob(6).getStartTime());
        schedule.getJob(3).requires(schedule.getJob(6));
        assertEquals(-1, schedule.minCompletionTime());
        assertEquals(100, schedule.getJob(5).getStartTime());
        assertEquals(-1, schedule.getJob(2).getStartTime());
        
        //Test 4
        schedule = new JobSchedule();
        schedule.addJob(10);
        schedule.addJob(5);
        schedule.addJob(3);
        schedule.addJob(2);
        assertEquals(10, schedule.minCompletionTime());
        schedule.addJob(4);
        schedule.getJob(1).requires(schedule.getJob(0));
        assertEquals(10, schedule.getJob(1).getStartTime());
        schedule.getJob(3).requires(schedule.getJob(0));
        schedule.getJob(3).requires(schedule.getJob(1));
        schedule.getJob(2).requires(schedule.getJob(0));
        schedule.getJob(4).requires(schedule.getJob(2));
        assertEquals(17, schedule.minCompletionTime());
        schedule.addJob(15);
        assertEquals(0, schedule.getJob(0).getStartTime());
        assertEquals(10, schedule.getJob(1).getStartTime());
        assertEquals(10, schedule.getJob(2).getStartTime());
        assertEquals(15, schedule.getJob(3).getStartTime());
        assertEquals(13, schedule.getJob(4).getStartTime());
        assertEquals(0, schedule.getJob(5).getStartTime());
        schedule.getJob(0).requires(schedule.getJob(5));
        assertEquals(32, schedule.minCompletionTime());
        assertEquals(15, schedule.getJob(0).getStartTime());
        assertEquals(25, schedule.getJob(1).getStartTime());
        assertEquals(25, schedule.getJob(2).getStartTime());
        assertEquals(30, schedule.getJob(3).getStartTime());
        assertEquals(28, schedule.getJob(4).getStartTime());
        assertEquals(0, schedule.getJob(5).getStartTime());
        
        //Test 5
        schedule = new JobSchedule();
        schedule.addJob(10);
        schedule.addJob(5);       
        schedule.getJob(1).requires(schedule.getJob(0));
        schedule.getJob(0).requires(schedule.getJob(1));
        assertEquals(-1, schedule.minCompletionTime()); 
        schedule.addJob(3);
        schedule.getJob(1).requires(schedule.getJob(2));
        assertEquals(-1, schedule.minCompletionTime()); 
        assertEquals(-1, schedule.getJob(1).getStartTime()); 
        assertEquals(0, schedule.getJob(2).getStartTime());
        
        //TEST 6
        schedule = new JobSchedule();
        schedule.addJob(3);
        assertEquals(3, schedule.minCompletionTime());
        schedule.addJob(4);
        schedule.addJob(2);
        assertEquals(0, schedule.getJob(2).getStartTime());
        schedule.addJob(5);
        assertEquals(5, schedule.minCompletionTime());
        schedule.addJob(5);
        schedule.addJob(3);
        schedule.addJob(7);
        schedule.addJob(6);
        assertEquals(0, schedule.getJob(6).getStartTime());
        schedule.addJob(8);
        assertEquals(8, schedule.minCompletionTime());
        schedule.addJob(2);
        schedule.addJob(1);
        assertEquals(8, schedule.minCompletionTime());
        schedule.addJob(4);
        schedule.addJob(3);
        schedule.addJob(2);
        schedule.addJob(11);
        schedule.addJob(7);
        schedule.addJob(1);
        assertEquals(0, schedule.getJob(4).getStartTime());
        schedule.addJob(2);
        schedule.addJob(10);
        assertEquals(11, schedule.minCompletionTime());
        schedule.addJob(8);
        schedule.addJob(4);
        assertEquals(0, schedule.getJob(15).getStartTime());
        assertEquals(11, schedule.minCompletionTime());
        schedule.addJob(3);
        schedule.addJob(4);
        assertEquals(11, schedule.minCompletionTime());
        
        schedule.getJob(1).requires(schedule.getJob(0));
        schedule.getJob(2).requires(schedule.getJob(0));
        schedule.getJob(3).requires(schedule.getJob(0));
        assertEquals(3, schedule.getJob(2).getStartTime());
        schedule.getJob(5).requires(schedule.getJob(0));
        schedule.getJob(8).requires(schedule.getJob(1));
        schedule.getJob(6).requires(schedule.getJob(2));
        assertEquals(15, schedule.minCompletionTime());
        schedule.getJob(6).requires(schedule.getJob(3));
        schedule.getJob(6).requires(schedule.getJob(5));
        schedule.getJob(8).requires(schedule.getJob(6));
        assertEquals(15, schedule.getJob(8).getStartTime());
        assertEquals(3, schedule.getJob(5).getStartTime());
        schedule.getJob(4).requires(schedule.getJob(6));
        schedule.getJob(7).requires(schedule.getJob(5));
        schedule.getJob(11).requires(schedule.getJob(7));
        schedule.getJob(12).requires(schedule.getJob(11));
        schedule.getJob(9).requires(schedule.getJob(4));
        schedule.getJob(10).requires(schedule.getJob(8));
        assertEquals(20, schedule.getJob(9).getStartTime());
        schedule.getJob(9).requires(schedule.getJob(12));
        schedule.getJob(9).requires(schedule.getJob(10));
        assertEquals(24, schedule.getJob(9).getStartTime());
        assertEquals(26, schedule.minCompletionTime());
        schedule.getJob(22).requires(schedule.getJob(10));
        assertEquals(28, schedule.minCompletionTime());
        schedule.getJob(13).requires(schedule.getJob(9));
        schedule.getJob(20).requires(schedule.getJob(22));
        schedule.getJob(14).requires(schedule.getJob(13));
        schedule.getJob(18).requires(schedule.getJob(13));
        schedule.getJob(15).requires(schedule.getJob(14));
        assertEquals(16, schedule.getJob(12).getStartTime());
        schedule.getJob(16).requires(schedule.getJob(15));
        schedule.getJob(19).requires(schedule.getJob(18));
        assertEquals(38, schedule.getJob(19).getStartTime());
        schedule.getJob(17).requires(schedule.getJob(19));
        schedule.getJob(17).requires(schedule.getJob(16));
        schedule.getJob(21).requires(schedule.getJob(20));
        schedule.getJob(20).requires(schedule.getJob(17));
        assertEquals(12, schedule.getJob(11).getStartTime());
        assertEquals(56, schedule.minCompletionTime());
        assertEquals(53, schedule.getJob(21).getStartTime());

        //Test 7
        schedule = new JobSchedule();
        schedule.addJob(3);
        assertEquals(3, schedule.minCompletionTime());
        schedule.addJob(4);
        assertEquals(0, schedule.getJob(1).getStartTime());
        schedule.getJob(0).requires(schedule.getJob(1));
        schedule.getJob(1).requires(schedule.getJob(0));
        assertEquals(-1, schedule.getJob(0).getStartTime());
        schedule.addJob(5);
        assertEquals(0, schedule.getJob(2).getStartTime());
        schedule.addJob(6);
        assertEquals(0, schedule.getJob(3).getStartTime());
        assertEquals(-1, schedule.minCompletionTime());
        schedule.addJob(7);
        schedule.getJob(4).requires(schedule.getJob(3));
        assertEquals(-1, schedule.minCompletionTime());
        assertEquals(6, schedule.getJob(4).getStartTime());
        schedule.addJob(8);
        schedule.addJob(9);
        schedule.getJob(5).requires(schedule.getJob(6));
        assertEquals(9, schedule.getJob(5).getStartTime());
        assertEquals(-1, schedule.minCompletionTime());
        
        //TEST 8
        schedule = new JobSchedule();
        schedule.addJob(3);
        schedule.addJob(8);
        schedule.addJob(10);
        assertEquals(10, schedule.minCompletionTime());
        assertEquals(0, schedule.getJob(2).getStartTime());
        schedule.getJob(1).requires(schedule.getJob(0));
        assertEquals(11, schedule.minCompletionTime());
        assertEquals(3, schedule.getJob(1).getStartTime());
        schedule.getJob(2).requires(schedule.getJob(1));
        assertEquals(11, schedule.getJob(2).getStartTime());
        assertEquals(21, schedule.minCompletionTime());
        schedule.addJob(3);
        assertEquals(0, schedule.getJob(3).getStartTime());
        assertEquals(21, schedule.minCompletionTime());
        schedule.getJob(1).requires(schedule.getJob(3));
        assertEquals(3, schedule.getJob(1).getStartTime());
        assertEquals(21, schedule.minCompletionTime());
        schedule.getJob(2).requires(schedule.getJob(3));
        assertEquals(3, schedule.getJob(1).getStartTime());
        assertEquals(21, schedule.minCompletionTime());
        schedule.getJob(0).requires(schedule.getJob(3));
        assertEquals(24, schedule.minCompletionTime());
        assertEquals(6, schedule.getJob(1).getStartTime());
        schedule.addJob(100000);
        assertEquals(100000, schedule.minCompletionTime());
        assertEquals(6, schedule.getJob(1).getStartTime());
        
        //TEST
        schedule = new JobSchedule();
        schedule.addJob(0);
        schedule.addJob(1);
        schedule.addJob(2);
        schedule.addJob(3);
        schedule.getJob(1).requires(schedule.getJob(0));
        schedule.getJob(2).requires(schedule.getJob(1));
        assertEquals(1, schedule.getJob(2).getStartTime());
        schedule.getJob(0).requires(schedule.getJob(2));
        assertEquals(0, schedule.getJob(3).getStartTime());
        assertEquals(-1, schedule.getJob(2).getStartTime());
        schedule.getJob(1).requires(schedule.getJob(3));
        assertEquals(0, schedule.getJob(3).getStartTime());
        assertEquals(-1, schedule.getJob(2).getStartTime());
        schedule.addJob(4);
        schedule.addJob(5);
        assertEquals(0, schedule.getJob(3).getStartTime());
        assertEquals(-1, schedule.getJob(2).getStartTime());
    }
}