import java.util.ArrayList;

public class JobSchedule
{
    private ArrayList<Job> schedule;
    private ArrayList<Job> topSortedList;
    private ArrayList<Job> endJobs;
    private int minCompletionTime;
    private boolean minCompletionTimeChanged;
    private boolean change;
    
    JobSchedule()
    {
        schedule = new ArrayList<Job>();
        topSortedList = new ArrayList<Job>();
        endJobs = new ArrayList<Job>();
        minCompletionTimeChanged = false;
        change = false;
    }
    
    public Job addJob(int time)
    {
        Job newJob = new Job();
        newJob.timeToComplete = time;
        schedule.add(newJob);
        minCompletionTimeChanged = true;
        endJobs.add(newJob);
        topSortedList.add(newJob);
        return newJob;
    }
    
    public Job getJob(int index)
    {
        return schedule.get(index);
    }
    
    public int minCompletionTime()
    {
        if(!minCompletionTimeChanged)
            return minCompletionTime;
        if(change)
            topSort();
        if(topSortedList.size() < schedule.size())
            return -1;
        minCompletionTime = 0;

        for(int i = endJobs.size(); i > 0; i--)
            if(minCompletionTime < endJobs.get(i - 1).startTime + endJobs.get(i - 1).timeToComplete)
                    minCompletionTime = endJobs.get(i - 1).startTime + endJobs.get(i - 1).timeToComplete; 
        minCompletionTimeChanged = false;
        return minCompletionTime;
    }
    
    public void initializeTopSortedList()
    {
        topSortedList = new ArrayList<Job>();
        endJobs = new ArrayList<Job>();
        for(int i = 0; i < schedule.size(); i++)
        {
            schedule.get(i).inCycle = true;
            schedule.get(i).inDegree = schedule.get(i).incomingEdges.size();
            if(schedule.get(i).inDegree == 0)
                topSortedList.add(schedule.get(i));
            schedule.get(i).startTime = 0;
            if(schedule.get(i).outgoingEdges.size() == 0)
                endJobs.add(schedule.get(i));
        }
    }
    
    public ArrayList<Job> topSort()
    {
        initializeTopSortedList();
        int counter = 0;
        while(counter < topSortedList.size())
        {
            Job u = topSortedList.get(counter);
            u.inCycle = false;
            for(int i = 0; i < u.outgoingEdges.size(); i++)
            {
                if(u.outgoingEdges.get(i).startTime < u.startTime + u.timeToComplete)
                    u.outgoingEdges.get(i).startTime = u.startTime + u.timeToComplete;
                u.outgoingEdges.get(i).inDegree--;
                if(u.outgoingEdges.get(i).inDegree == 0)
                    topSortedList.add(u.outgoingEdges.get(i));
            }
            counter++;
        }
        change = false;
        return topSortedList;
    }
    
    public void changed()
    {
        minCompletionTimeChanged = true;
        change = true;
    }
    
    class Job
    {
        private ArrayList<Job> outgoingEdges;
        private ArrayList<Job> incomingEdges;
        private int timeToComplete;
        private int inDegree;
        private int startTime;
        private boolean inCycle;
        
        private Job()
        {
            outgoingEdges = new ArrayList<Job>();
            incomingEdges = new ArrayList<Job>();
            startTime = 0;
        }

        public void requires(Job j)
        {
            j.outgoingEdges.add(this);
            incomingEdges.add(j);
            if((j.startTime + j.timeToComplete) > this.startTime)
                changed();
        }
        
        public int getStartTime()
        {
            if(change)
                topSort();
            if(inCycle)
                return -1;
            return startTime;            
        }
    }
}