package chatroomCase;

import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ThreadPoolManager {
	private int maximumCapasity;
	private static ThreadPoolManager instance; 
	private Set<ThreadPool>  pool;
	
	private ThreadPoolManager(){
		maximumCapasity = 4;		
		pool = new HashSet<ThreadPool>(maximumCapasity);
	}
	
	public static ThreadPoolManager getInstance(){
		if(instance == null){
			instance = new ThreadPoolManager();
		}		
		return instance;
	}
	
	public int getMaximumPoolSize(){
		return maximumCapasity;
	}
	
	public int getCurrentPoolSize(){
		return pool.size();
	}
	
	public int getAvailableThreads(){
		int counter = 0;
		for (Iterator<ThreadPool> iterator = pool.iterator(); iterator.hasNext();) {
			ThreadPool thread = (ThreadPool) iterator.next();
			if(thread.getRunnbingStatus().equals(ThreadPool.STATUS_AVAILABLE)){
				++counter;
			}			
		}
		return counter;
	}
	
	public int getBusyThreads(){
		int counter = 0;
		for (Iterator<ThreadPool> iterator = pool.iterator(); iterator.hasNext();) {
			ThreadPool thread = (ThreadPool) iterator.next();
			if(thread.getRunnbingStatus().equals(ThreadPool.STATUS_BUSY)){
				++counter;
			}			
		}
		return counter;
	}

	public void assignTask(Socket clientSocket){	

		ThreadPool requestThread = getThreadFromPool();
		requestThread.setTask(new RequestTask()); 
		requestThread.setClientSocket(clientSocket);
    	System.out.println("Setting socket and update requestThread status from "+requestThread.getRunnbingStatus()+" of thread "+requestThread.getName()+" to "+ThreadPool.STATUS_BUSY);
    	requestThread.setRunningStatus(ThreadPool.STATUS_BUSY);
   	}
	
	public void releaseTask(){
		ThreadPool requestThread = (ThreadPool)Thread.currentThread();
		System.out.println("Changing thread "+requestThread.getName()+" status from "+requestThread.getRunnbingStatus()+" to "+ThreadPool.STATUS_AVAILABLE);
		requestThread.setRunningStatus(ThreadPool.STATUS_AVAILABLE);
	}
	
	
	public ThreadPool getThreadFromPool(){
		System.out.println("Current size of pool "+pool.size());
		// ask client to wait for some time and try again
		if(pool.size() == maximumCapasity){
			System.out.println("Running out of resource. Can not create new thread please wait for some time.");
			return null;
		}
		
		// if threads exist in pool then check which one is free and return that
		for (Iterator<ThreadPool> iterator = pool.iterator(); iterator.hasNext();) {
			ThreadPool thread = (ThreadPool) iterator.next();
			String threadStatus = thread.getRunnbingStatus();
			if(threadStatus.equals(ThreadPool.STATUS_AVAILABLE)){
				System.out.println("Thread name "+thread.getName());
				System.out.println("Thread is available, returning "+thread.getName()+ "with status "+thread.getRunnbingStatus());
				System.out.println("This thread will wait for status change from "+thread.getRunnbingStatus()+" to "+ThreadPool.STATUS_BUSY);
				return thread;
			}
		}
		
		// if no thread exist in pool then create one add to pool and return back
		ThreadPool newThread = new ThreadPool();
		newThread.setRunningStatus(ThreadPool.STATUS_AVAILABLE);
		newThread.start();
		pool.add(newThread);
		System.out.println("Thread name "+newThread.getName());
		System.out.println("Created, set to busy and started new thread "+newThread.getName()); 
		System.out.println("This thread will wait for status change from "+newThread.getRunnbingStatus()+" to "+ThreadPool.STATUS_BUSY);
		
		return newThread;
	}
	
}
